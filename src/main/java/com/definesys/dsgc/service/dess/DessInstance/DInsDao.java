package com.definesys.dsgc.service.dess.DessInstance;

import com.definesys.dsgc.service.dess.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstBean;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLog;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @ClassName DInsDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-8-3 13:29
 * @Version 1.0
 **/
@Repository
public class DInsDao {

    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;


    public PageQueryResult queryJobInstaceList(CommonReqBean param, int pageSize, int pageIndex) {
        StringBuffer sqlStr = new StringBuffer("select di.*,db.BUSINESS_TYPE,db.BUSINESS_NAME from DESS_INSTANCE di left join DESS_BUSINESS db on di.BUSINESS_ID = db.BUSINESS_ID where 1=1 ");
        MpaasQuery mq = sw.buildQuery();
        // 检索搜索框条件
        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }
        // 检索下拉列表类型
        if (param.getQueryType().equals("ALL")) {
        } else {
            String[] conArray = param.getQueryType().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append("and db.BUSINESS_TYPE like '%" + s + "%'");
                }
            }
        }
        mq.sql(sqlStr.toString() + " order by di.creation_date desc");
        return mq.doPageQuery(pageIndex, pageSize, DinstBean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(di.job_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(di.job_name) like '%" + conUpper + "%' )";
        return conAnd;
    }


    public void saveJobBaseInfo(DinstBean dinstBean){
        sw.buildQuery()
                .doInsert(dinstBean);
    }

    public boolean checkJobNoIsExist(String jobNo){
        DinstBean job_no = sw.buildQuery().eq("job_no", jobNo).doQueryFirst(DinstBean.class);
        if(job_no==null){
            return false;
        }else{
            return true;
        }
    }

    public void delJobInstance (String jobNo){
        sw.buildQuery()
                .eq("job_no",jobNo)
                .doDelete(DinstBean.class);
    }


    public void updateJobInstanceStatus(DinstBean dinstBean){
        sw.buildQuery()
                .eq("job_no",dinstBean.getJobNo())
                .update("job_status",dinstBean.getJobStatus())
                .doUpdate(dinstBean);
    }


    public DinstBean getJobInstance(String jobNo){
        return sw.buildQuery()
                .sql("select di.*,db.BUSINESS_TYPE from DESS_INSTANCE di left join DESS_BUSINESS db on di.BUSINESS_ID = db.BUSINESS_ID where di.JOB_NO = #jobNo ")
                .setVar("jobNo",jobNo)
                .doQueryFirst(DinstBean.class);
    }

    public void saveJobInsDeatail(DinstBean dinstBean){
        sw.buildQuery()
                .eq("job_no",dinstBean.getJobNo())
                .update("job_name",dinstBean.getJobName())
                .update("job_description",dinstBean.getJobDescription())
                .doUpdate(dinstBean);
    }

    public void saveScheduling(DinstBean dinstBean){
        sw.buildQuery()
                .eq("job_no",dinstBean.getJobNo())
                .update("alive_start",dinstBean.getAliveStart())
                .update("alive_end",dinstBean.getAliveEnd())
                .update("job_frequency",dinstBean.getJobFrequency())
                .update("job_rate",dinstBean.getJobRate())
                .doUpdate(dinstBean);
    }



}
