package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.pluginBean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PluginService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:06
 * @Version 1.0
 **/
@Service
public class PluginService {

    @Autowired
    private PluginDao pluginDao;

    public void deletePluginContext(String budId,String pluginCode) throws Exception {
        switch (pluginCode){
            case "basic-auth":
                pluginDao.delPlBasicAuthBeanById(budId);
                break;
            case"key-auth":
                pluginDao.delPlKeyAuthBeanById(budId);
                break;
            case"oauth2":
                pluginDao.delPlOauth2ById(budId);
                break;
            case"acl":
                pluginDao.delPlAddAclById(budId);
                break;
            case"ip-restriction":
                pluginDao.delPlIpRestrictionById(budId);
                break;
            case"rate-limiting":
                pluginDao.delPlRateLimitingById(budId);
                break;
            case"request-size-limiting":
                pluginDao.delPlReqSizeLimitingById(budId);
                break;
            case"request-transformer":
                pluginDao.delPlReqTransById(budId);
                break;
            case"response-transformer":
                pluginDao.delPlResTransById(budId);
                break;
            case"correlation-id":
                pluginDao.delPlCorrelationIdById(budId);
                break;
//            case"11":
//                pluginDao.delPluginTcpLog(id);
//                break;
//            case"12":
//                pluginDao.delplUdpLogById(id);
//                break;
//            case"13":
//                pluginDao.delPlHttpLogById(id);
//                break;
            default:
                throw new Exception("插件"+pluginCode+"删除失败，没有对应的插件表");

        }
    }
    //  PlBasicAuthBean
    public void addPlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){ pluginDao.addPlBasicAuthBean(plBasicAuthBean); }
    public PlBasicAuthBean queryPlBasicAuthBeanById(String id){ return pluginDao.queryPlBasicAuthBeanById(id); }
    public void delPlBasicAuthBeanById(String id){ pluginDao.delPlBasicAuthBeanById(id); }
    public void updatePlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){ pluginDao.updatePlBasicAuthBean(plBasicAuthBean); }

    //PlKeyAuthBeann
    public PlKeyAuthBean queryPlKeyAuthBeannById(String id){ return  pluginDao.queryPlKeyAuthBeannById(id); }
    public void addPlKeyAuthBean(PlKeyAuthBean plKeyAuthBean){pluginDao.addPlKeyAuthBean(plKeyAuthBean);}
    public void delPlKeyAuthBeanById(String id){ pluginDao.delPlKeyAuthBeanById(id); }
    public void updatePlKeyAuthBeanBean(PlKeyAuthBean plKeyAuthBean){ pluginDao.updatePlKeyAuthBeanBean(plKeyAuthBean); }
    //    PlOauth2;  3
    public PlOauth2 queryPlOauth2nById(String id){ return  pluginDao.queryPlOauth2nById(id); }
    public void addPlOauth2(PlOauth2 plOauth2){pluginDao.addPlOauth2(plOauth2); }
    public void delPlOauth2ById(String id){
        pluginDao.delPlOauth2ById(id);
    }
    public void updatePlOauth2(PlOauth2 plOauth2){ pluginDao.updatePlOauth2( plOauth2); }

    //    PlAddAcl;  4
    public PlAddAcl queryPlAddAclById(String id){ return  pluginDao.queryPlAddAclById(id); }
    public void addPlAddAcl(PlAddAcl plAddAcl){pluginDao.addPlAddAcl( plAddAcl); }
    public void delPlAddAclById(String id){
        pluginDao.delPlAddAclById(id);
    }
    public void updatePlAddAcl(PlAddAcl plAddAcl){ pluginDao.updatePlAddAcl( plAddAcl); }

    //    PlIpRestriction;  5
    public PlIpRestriction queryPlIpRestrictionById(String id){ return  pluginDao.queryPlIpRestrictionById(id); }
    public void addPlIpRestriction(PlIpRestriction plIpRestriction){pluginDao.addPlIpRestriction(plIpRestriction); }
    public void delPlIpRestrictionById(String id){ pluginDao.delPlIpRestrictionById(id); }
    public void updatePlIpRestriction(PlIpRestriction plIpRestriction){ pluginDao.updatePlIpRestriction(plIpRestriction); }
    //   PlRateLimiting; 6

    public PlRateLimiting queryPlRateLimitingById(String id){ return  pluginDao.queryPlRateLimitingById(id); }
    public void addPlRateLimiting(PlRateLimiting plRateLimiting){pluginDao.addPlRateLimiting( plRateLimiting); }
    public void delPlRateLimitingById(String id){ pluginDao.delPlRateLimitingById(id); }
    public void updatePlRateLimiting(PlRateLimiting plRateLimiting){ pluginDao.updatePlRateLimiting( plRateLimiting); }
    //   PlReqSizeLimiting; 7

    public PlReqSizeLimiting queryPlReqSizeLimitingById(String id){ return  pluginDao.queryPlReqSizeLimitingById(id); }
    public void addPlReqSizeLimiting(PlReqSizeLimiting plReqSizeLimiting){pluginDao.addPlReqSizeLimiting( plReqSizeLimiting); }
    public void delPlReqSizeLimitingById(String id){ pluginDao.delPlReqSizeLimitingById(id); }
    public void updatePlReqSizeLimitingl(PlReqSizeLimiting plReqSizeLimiting){ pluginDao.updatePlReqSizeLimitingl( plReqSizeLimiting); }

    //    PlReqTrans;  8
    public PlReqTrans queryPlReqTransById(String id){ return  pluginDao.queryPlReqTransById(id); }
    public void addPlReqTrans(PlReqTrans plReqTrans){pluginDao.addPlReqTrans( plReqTrans); }
    public void delPlReqTransById(String id){ pluginDao.delPlReqTransById(id); }
    public void updatePlReqTrans(PlReqTrans plReqTrans){ pluginDao.updatePlReqTrans( plReqTrans); }

    //     PlResTrans;  9
    public PlResTrans queryPlResTransById(String id){ return  pluginDao.queryPlResTransById(id); }
    public void addPlResTrans(PlResTrans plResTrans){pluginDao.addPlResTrans( plResTrans); }
    public void delPlResTransById(String id){ pluginDao.delPlResTransById(id); }
    public void updatePlResTrans(PlResTrans plResTrans){ pluginDao.updatePlResTrans( plResTrans); }

    //     PlCorrelationId;  10
    public PlCorrelationId queryPlCorrelationIdById(String id){ return  pluginDao.queryPlCorrelationIdById(id); }
    public void addPlCorrelationId(PlCorrelationId plCorrelationId){pluginDao.addPlCorrelationId( plCorrelationId); }
    public void delPlCorrelationIdById(String id){ pluginDao.delPlCorrelationIdById(id); }
    public void updatePlCorrelationId(PlCorrelationId plCorrelationId){ pluginDao.updatePlCorrelationId( plCorrelationId); }

//     PluginTcpLog;   11

    public PluginTcpLog queryPluginTcpLogById(String id){ return  pluginDao. queryPluginTcpLogById(id); }
    public void addPluginTcpLog(PluginTcpLog pluginTcpLog){pluginDao.addPluginTcpLog( pluginTcpLog); }
    public void delPluginTcpLog(String id){ pluginDao.delPluginTcpLog(id); }
    public void updatePluginTcpLog(PluginTcpLog pluginTcpLog){ pluginDao. updatePluginTcpLog( pluginTcpLog); }

//     PlUdpLog;  12

    public PlUdpLog queryplUdpLogById(String id){ return  pluginDao.queryplUdpLogById(id); }
    public void addplUdpLog(PlUdpLog plUdpLog){pluginDao.addplUdpLog( plUdpLog); }
    public void delplUdpLogById(String id){ pluginDao.delplUdpLogById(id); }
    public void updateplUdpLog(PlUdpLog plUdpLog){ pluginDao.updateplUdpLog( plUdpLog); }

    //     PlHttpLog;  13
    public PlHttpLog queryPlHttpLogById(String id){ return  pluginDao.queryPlHttpLogById(id); }
    public void addPlHttpLog(PlHttpLog plHttpLog){pluginDao.addPlHttpLog( plHttpLog); }
    public void delPlHttpLogById(String id){ pluginDao.delPlHttpLogById(id); }
    public void updatePlHttpLog(PlHttpLog plHttpLog){ pluginDao.updatePlHttpLog( plHttpLog); }


}
