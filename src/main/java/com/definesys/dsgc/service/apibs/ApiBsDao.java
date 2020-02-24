package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.*;
import com.definesys.dsgc.service.apiroute.bean.DagEnvInfoCfgBean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApiBsDao {
    @Autowired
    private MpaasQueryFactory sw;
    public PageQueryResult queryApiBsList(CommonReqBean param, int pageSize, int pageIndex, String userRole,List<String> sysCodeList){
        StringBuffer sqlStr = new StringBuffer("select db.*,dse.sys_name appName from dag_bs db,dsgc_system_entities dse where 1=1 and db.app_code = dse.sys_code ");
        MpaasQuery mq = sw.buildQuery();
        if ("SystemLeader".equals(userRole)) {
            sqlStr.append(" and db.app_code in ( ");
            for (int i = 0; i < sysCodeList.size(); i++) {
                if (i < sysCodeList.size() - 1) {
                    sqlStr.append("'" + sysCodeList.get(i) + "',");
                } else {
                    sqlStr.append("'" + sysCodeList.get(i) + "') ");
                }
            }
        }
        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex, pageSize, DagBsbean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(db.bs_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(db.bs_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%' )";

        return conAnd;
    }

    public List<DagBsbean> queryApiBsByCustomInput(CommonReqBean param){
      return sw.buildQuery().like("bs_code",param.getCon0()).doQuery(DagBsbean.class);
    }

    public DagBsbean queryApiBsByCode(String code){
        return sw.buildQuery().like("bs_code",code).doQueryFirst(DagBsbean.class);
    }

    public void updateDagBs(DagBsbean dagBsbean){
        sw.buildQuery().rowid("bs_id",dagBsbean.getBsId()).doUpdate(dagBsbean);

    }

    public void addApiBs(DagBsbean dagBsbean){
        sw.buildQuery().doInsert(dagBsbean);
    }
    public DagBsbean queryApiBsById(CommonReqBean param){
        return sw.buildQuery().eq("bs_id",param.getCon0()).doQueryFirst(DagBsbean.class);
    }
    public void delApiBs(CommonReqBean param){
         sw.buildQuery().eq("bs_id",param.getCon0()).doDelete(DagBsbean.class);
    }

    public DagBsDtiBean queryDagBsDtiByVid(String params){
        return sw.buildQuery().eq("vid",params).doQueryFirst(DagBsDtiBean.class);
    }

    public DagBsDtiBean queryDagBsDtiByid(String params){
        return sw.buildQuery().eq("dbd_id",params).doQueryFirst(DagBsDtiBean.class);
    }

    public DagBsDtiBean updateDagBsDti(DagBsDtiBean dagBsDtiBean){
        sw.buildQuery().eq("dbd_id",dagBsDtiBean.getDbdId()).doUpdate(dagBsDtiBean);
        return queryDagBsDtiByid(dagBsDtiBean.getDbdId());
    }

    public void delDagBsDtiByVid(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(DagBsDtiBean.class);
    }

    public DagBsDtiBean addDagBsDti(DagBsDtiBean dagBsDtiBean){
         sw.buildQuery().doInsert(dagBsDtiBean);
        return queryDagBsDtiByid(dagBsDtiBean.getDbdId());
    }


    public void updatePluginUsing(DagPlugUsingBean dagPlugUsingBean){
        sw.buildQuery().eq("dpu_Id",dagPlugUsingBean.getDpuId()).doUpdate(dagPlugUsingBean);
    }

    public void addPluginUsing(DagPlugUsingBean dagPlugUsingBean){
        sw.buildQuery().doInsert(dagPlugUsingBean);
    }

    public void delPluginUsing(String id){
        sw.buildQuery().rowid("dpu_id", id).doDelete(DagPlugUsingBean.class);
    }

    public void delPluginUsingByVid(String vid){
        sw.buildQuery().eq("vid", vid).doDelete(DagPlugUsingBean.class);
    }


    public List<DagPlugUsingBean> queryPluginUsing(String vid){
        return sw.buildQuery().eq("vid",vid).doQuery(DagPlugUsingBean.class);
    }

    public List<DagPlugStoreBean> queryPluginStoreByType(String type){
        return sw.buildQuery().eq("plugin_type",type).doQuery(DagPlugStoreBean.class);
    }

    public DagPlugStoreBean queryPluginStoreByCode(String code){
        return sw.buildQuery().eq("plugin_code",code).doQueryFirst(DagPlugStoreBean.class);
    }

    public List<Map<String,Object>> queryPluginStoreTypeTotal(){
        return sw.buildQuery().sql("select t.plugin_type from dag_plugin_store t group by t.plugin_type").doQuery();
    }

    public List<DagCodeVersionBean> queryDagCodeVersionBySource(String sourceId){
        return sw.buildQuery().eq("sour_code",sourceId).doQuery(DagCodeVersionBean.class);
    }

    public DagCodeVersionBean queryDagCodeVersionByid(String id){
        return sw.buildQuery().eq("vid",id).doQueryFirst(DagCodeVersionBean.class);
    }


    public void delDagCodeVersionByid(String id){
         sw.buildQuery().rowid("vid",id).doDelete(DagCodeVersionBean.class);
    }

    public DagCodeVersionBean updateDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().rowid("vid",dagCodeVersionBean.getVid()).doUpdate(dagCodeVersionBean);
        return queryDagCodeVersionByid(dagCodeVersionBean.getVid());
    }

    public void addDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().doInsert(dagCodeVersionBean);
    }



    public Map<String,Object> querySysNameByCode(String code){
        return  sw.buildQuery().sql("select e.sys_name from DSGC_SYSTEM_ENTITIES e where e.sys_code=#code")
                .setVar("code",code).doQueryFirst();
    }

    public List<DagEnvInfoCfgBean> queryEnv(List<String> envCode){
        return  sw.buildQuery().in("envCode",envCode).doQuery(DagEnvInfoCfgBean.class);
    }

}
