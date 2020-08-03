package com.definesys.dsgc.service.dess;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.bean.DessBusiness;
import com.definesys.dsgc.service.dess.bean.DessLog;
import com.definesys.dsgc.service.dess.bean.DinstBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName DessDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 14:35
 * @Version 1.0
 **/
@Repository
public class DessDao {

    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;


    public PageQueryResult queryJobInstaceList(CommonReqBean param, int pageSize, int pageIndex) {
        StringBuffer sqlStr = new StringBuffer("select * from dess_instance where 1=1 ");
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
                    sqlStr.append("and  job_type like '%" + s + "%'");
                }
            }
        }
        mq.sql(sqlStr.toString() + " order by creation_date desc");
        return mq.doPageQuery(pageIndex, pageSize, DinstBean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(job_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(job_name) like '%" + conUpper + "%' )";
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
                .update("status",dinstBean.getStatus())
                .doUpdate(dinstBean);
    }


    public PageQueryResult queryJobLogList(CommonReqBean param, int pageSize, int pageIndex) {
        StringBuffer sqlStr = null;
        if("oracle".equals(dbType)){
            sqlStr = new StringBuffer("select dl.LOG_ID,dl.JOB_NO,dl.STATUS,dl.DO_TIME,dl.CREATION_DATE,dl.RETRY_TIMES,di.JOB_NAME,di.JOB_TYPE from dess_log dl LEFT JOIN DESS_INSTANCE DI on dl.JOB_NO = di.JOB_NO where 1=1");
        }
        if ("mysql".equals(dbType)){
            sqlStr = new StringBuffer("select dl.LOG_ID,dl.JOB_NO,dl.STATUS,dl.DO_TIME,dl.CREATION_DATE,dl.RETRY_TIMES,di.JOB_NAME,di.JOB_TYPE from dess_log dl LEFT JOIN DESS_INSTANCE DI on dl.JOB_NO = di.JOB_NO where 1=1");
        }
        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluseLog(s));
                }
            }
        }
        if(param.getQueryType().equals("ALL")){
        } else{
            String[] conArray = param.getQueryType().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append("and  di.job_type like '%" + s + "%'");
                }
            }
        }
        if("oracle".equals(dbType)){
            mq.sql(sqlStr.toString()+" order by dl.creation_date desc");
        }
        if ("mysql".equals(dbType)){
            mq.sql(sqlStr.toString()+" GROUP BY dl.log_id HAVING COUNT(1)>1  order by dl.creation_date desc");
        }

        return mq.doPageQuery(pageIndex, pageSize, DessLog.class);
    }


    private String generateLikeAndCluseLog(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(dl.log_id) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dl.job_no) like '%" + conUpper + "%' ";
        conAnd += " or UPPER(di.job_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public DinstBean getJobInstance(String jobNo){
        return sw.buildQuery()
                .eq("job_no",jobNo)
                .doQueryFirst(DinstBean.class);
    }

    public void saveJobInsDeatail(DinstBean dinstBean){
        sw.buildQuery()
                .eq("job_no",dinstBean.getJobNo())
                .update("job_name",dinstBean.getJobName())
                .update("version",dinstBean.getVersion())
                .update("description",dinstBean.getDescription())
                .doUpdate(dinstBean);
    }

    public void saveScheduling(DinstBean dinstBean){
        sw.buildQuery()
                .eq("job_no",dinstBean.getJobNo())
                .update("alive_start",dinstBean.getAliveStart())
                .update("alive_end",dinstBean.getAliveEnd())
                .update("FREQUENCY",dinstBean.getFrequency())
                .doUpdate(dinstBean);
    }

    public void saveJobDefinition(DessBusiness dessBusiness){
        sw.buildQuery()
                .doInsert(dessBusiness);
    }


    public DessBusiness getJobDefinition(String jobNo){
        return sw.buildQuery()
                .eq("job_no",jobNo)
                .doQueryFirst(DessBusiness.class);
    }
}
