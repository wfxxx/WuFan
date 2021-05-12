package com.definesys.dsgc.service.svcgen;

import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.svcgen.utils.ServiceGenerateProxy;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnInfoJsonBean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RFCStepsService {

    @Autowired
    private RFCStepsDao rfcStepsDao;

    @Autowired
    private UserHelper userHelper;

    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();

    /**
     * 获取连接信息
     *
     * @param connName
     * @return
     * @throws Exception
     */
    public SapConnInfoJsonBean getSapConnInfoByName(String connName) throws Exception {
        return this.sgProxy.getSapConnInfoByName(connName);
    }

    /**
     * 获取所有sap连接信息
     *
     * @return
     * @throws Exception
     */
    public List<SapConnSltJsonBean> getSapConnList() throws Exception {
        return this.sgProxy.getSapConnList();
    }




//    public SapConnVaildBean vaildSapConnInfo(String loginUser,SapConnInfoJsonBean connInfo) {
//        SapConnVaildBean res = new SapConnVaildBean(false);
//        if (loginUser != null && connInfo != null && connInfo.getConnName() != null) {
//            SapConfiguration sapCfg = new SapConfiguration();
//            sapCfg.setClient(connInfo.getSapCient());
//            sapCfg.setLang(connInfo.getLang());
//            sapCfg.setUser(connInfo.getConnUN());
//            sapCfg.setPassword(connInfo.getConnPD());
//            sapCfg.setHost(connInfo.getSapIp());
//            sapCfg.setSysnr(connInfo.getSn());
//            sapCfg.setConnName(connInfo.getConnName());
//            if (connInfo.getConnId() == null || connInfo.getConnId().trim().length() == 0) {
//                sapCfg.setConnId(UUID.randomUUID().toString());
//            } else {
//                sapCfg.setConnId(connInfo.getConnId());
//            }
//
//            SapClient sapClient = null;
//            try {
//                sapClient = SapClient.connect(sapCfg);
//                //判断连接信息是否有效
//                if (sapClient != null && sapClient.isAlive()) {
//                    res.setSapConnVaild(true);
//                    //有效的连接信息，则保存到数据库
//                    try {
//                        sapCfg.storeInDB(loginUser);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                res.setSapConnVaild(false);
//                String errorMsg = CommonUtils.getExceptionTrackDetailInfo(e);
//                errorMsg = CommonUtils.splitSpecifyLengthStr(errorMsg,1000);
//                res.setSapConnVaildMsg(Base64.getEncoder().encodeToString(errorMsg.getBytes(Charset.forName("UTF-8"))));
//            }
//        }
//
//        return res;
//    }


    public RFCSyncJsonBean syncRfcFromSap(String uid,String connName) throws Exception {
        RFCSyncJsonBean res = new RFCSyncJsonBean();
        res.setRfcCount(this.sgProxy.syncRfcFromSap(uid,connName));
        return res;
    }

    public PageQueryResult<RFCInfoBean> queryRfc(RFCInfoQueryBean queryInfo,int pageSize,int pageIndex) {
        return rfcStepsDao.queryRfc(queryInfo,pageSize,pageIndex);
    }


    public RfcDeployProfileBean getRfcDeployProfileDetail(String dpId) {
        return rfcStepsDao.getRfcDeployProfileDetail(dpId);
    }


}
