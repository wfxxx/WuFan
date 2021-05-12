package com.definesys.dsgc.service.svcgen;

import com.definesys.dsgc.service.svcgen.bean.RFCInfoBean;
import com.definesys.dsgc.service.svcgen.bean.RFCInfoQueryBean;
import com.definesys.dsgc.service.svcgen.bean.RfcDeployProfileBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class RFCStepsDao {
    @Autowired
    private MpaasQueryFactory sw;


    public void saveRFCList(List<RFCInfoBean> rfcList){

    }

    public PageQueryResult<RFCInfoBean> queryRfc(RFCInfoQueryBean queryInfo,int pageSize,int pageIndex){
        return sw.buildQuery()
                .like("funcName",queryInfo.getFuncName())
                .like("funcGroup",queryInfo.getFuncGroup())
                .like("funcDesc",queryInfo.getFuncDesc())
                .doPageQuery(pageIndex, pageSize,RFCInfoBean.class);
    }


    public RfcDeployProfileBean getRfcDeployProfileDetail(String dpId){
        Map<String,Object> res =  sw.buildQuery().sql("select d.dp_name,d.env_code,(select e.env_name from dsgc_env_info_cfg e where e.env_code = d.env_code) env_name,t.busi_serv_uri from dsgc_svcgen_deploy_profiles d,dsgc_svcgen_tmpl t where d.deve_id = t.deve_id and d.dp_id = #dpId ")
                .setVar("dpId",dpId).doQueryFirst();
        RfcDeployProfileBean dpl = null;
        if(res != null && !res.isEmpty()){
            dpl = new RfcDeployProfileBean();
            dpl.setDplName((String)res.get("DP_NAME"));
            dpl.setEnvCode((String)res.get("ENV_CODE"));
            dpl.setJndiName((String)res.get("BUSI_SERV_URI"));
            dpl.setEnvCodeMeaning((String)res.get("ENV_NAME"));
        }
        return dpl;
    }

}
