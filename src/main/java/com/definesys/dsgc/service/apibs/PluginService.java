package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.DagPlugUsingBean;
import com.definesys.dsgc.service.apibs.bean.pluginBean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ApiBsService apiBsService;

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
    public PlBasicAuthBean queryPlBasicAuthBeanById(String id){
        DagPlugUsingBean consumerValue=apiBsService.queryPluginUsingByid(id);
        PlBasicAuthBean result=pluginDao.queryPlBasicAuthBeanById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
    }
    public void delPlBasicAuthBeanById(String id){ pluginDao.delPlBasicAuthBeanById(id); }
    @Transactional
    public void updatePlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){
        apiBsService.updatePluginUsingConsumer(plBasicAuthBean.getConsumer(),plBasicAuthBean.getDpuId());
        pluginDao.updatePlBasicAuthBean(plBasicAuthBean);
    }

    //PlKeyAuthBeann
    public PlKeyAuthBean queryPlKeyAuthBeannById(String id){
        DagPlugUsingBean consumerValue=apiBsService.queryPluginUsingByid(id);
        PlKeyAuthBean result=  pluginDao.queryPlKeyAuthBeannById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
    }
    public void addPlKeyAuthBean(PlKeyAuthBean plKeyAuthBean){pluginDao.addPlKeyAuthBean(plKeyAuthBean);}
    public void delPlKeyAuthBeanById(String id){ pluginDao.delPlKeyAuthBeanById(id); }
    @Transactional
    public void updatePlKeyAuthBeanBean(PlKeyAuthBean plKeyAuthBean){
        apiBsService.updatePluginUsingConsumer(plKeyAuthBean.getConsumer(),plKeyAuthBean.getDpuId());
        pluginDao.updatePlKeyAuthBeanBean(plKeyAuthBean); }
    //    PlOauth2;  3
    public PlOauth2 queryPlOauth2nById(String id) {
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlOauth2 result = pluginDao.queryPlOauth2nById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
    }
    public void addPlOauth2(PlOauth2 plOauth2){pluginDao.addPlOauth2(plOauth2); }
    public void delPlOauth2ById(String id){
        pluginDao.delPlOauth2ById(id);
    }
    @Transactional
    public void updatePlOauth2(PlOauth2 plOauth2){
        apiBsService.updatePluginUsingConsumer(plOauth2.getConsumer(),plOauth2.getDpuId());
        pluginDao.updatePlOauth2( plOauth2); }

    //    PlAddAcl;  4
    public PlAddAcl queryPlAddAclById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlAddAcl result = pluginDao.queryPlAddAclById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
        }
    public void addPlAddAcl(PlAddAcl plAddAcl){pluginDao.addPlAddAcl( plAddAcl); }
    public void delPlAddAclById(String id){
        pluginDao.delPlAddAclById(id);
    }
    @Transactional
    public void updatePlAddAcl(PlAddAcl plAddAcl){
        apiBsService.updatePluginUsingConsumer(plAddAcl.getConsumer(),plAddAcl.getDpuId());
        pluginDao.updatePlAddAcl( plAddAcl); }

    //    PlIpRestriction;  5
    public PlIpRestriction queryPlIpRestrictionById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlIpRestriction result = pluginDao.queryPlIpRestrictionById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
         }
    public void addPlIpRestriction(PlIpRestriction plIpRestriction){pluginDao.addPlIpRestriction(plIpRestriction); }
    public void delPlIpRestrictionById(String id){ pluginDao.delPlIpRestrictionById(id); }
    @Transactional
    public void updatePlIpRestriction(PlIpRestriction plIpRestriction){
        apiBsService.updatePluginUsingConsumer(plIpRestriction.getConsumer(),plIpRestriction.getDpuId());
        pluginDao.updatePlIpRestriction(plIpRestriction); }
    //   PlRateLimiting; 6

    public PlRateLimiting queryPlRateLimitingById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlRateLimiting result = pluginDao.queryPlRateLimitingById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
         }
    public void addPlRateLimiting(PlRateLimiting plRateLimiting){pluginDao.addPlRateLimiting( plRateLimiting); }
    public void delPlRateLimitingById(String id){ pluginDao.delPlRateLimitingById(id); }
    @Transactional
    public void updatePlRateLimiting(PlRateLimiting plRateLimiting){
        apiBsService.updatePluginUsingConsumer(plRateLimiting.getConsumer(),plRateLimiting.getDpuId());
        pluginDao.updatePlRateLimiting( plRateLimiting); }
    //   PlReqSizeLimiting; 7

    public PlReqSizeLimiting queryPlReqSizeLimitingById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlReqSizeLimiting result = pluginDao.queryPlReqSizeLimitingById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
         }
    public void addPlReqSizeLimiting(PlReqSizeLimiting plReqSizeLimiting){pluginDao.addPlReqSizeLimiting( plReqSizeLimiting); }
    public void delPlReqSizeLimitingById(String id){ pluginDao.delPlReqSizeLimitingById(id); }
    @Transactional
    public void updatePlReqSizeLimitingl(PlReqSizeLimiting plReqSizeLimiting){
        apiBsService.updatePluginUsingConsumer(plReqSizeLimiting.getConsumer(),plReqSizeLimiting.getDpuId());
        pluginDao.updatePlReqSizeLimitingl( plReqSizeLimiting); }

    //    PlReqTrans;  8
    public PlReqTrans queryPlReqTransById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlReqTrans result = pluginDao.queryPlReqTransById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
       }
    public void addPlReqTrans(PlReqTrans plReqTrans){pluginDao.addPlReqTrans( plReqTrans); }
    public void delPlReqTransById(String id){ pluginDao.delPlReqTransById(id); }
    @Transactional
    public void updatePlReqTrans(PlReqTrans plReqTrans){
        apiBsService.updatePluginUsingConsumer(plReqTrans.getConsumer(),plReqTrans.getDpuId());
        pluginDao.updatePlReqTrans( plReqTrans); }

    //     PlResTrans;  9
    public PlResTrans queryPlResTransById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlResTrans result = pluginDao.queryPlResTransById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
         }
    public void addPlResTrans(PlResTrans plResTrans){pluginDao.addPlResTrans( plResTrans); }
    public void delPlResTransById(String id){ pluginDao.delPlResTransById(id); }
    @Transactional
    public void updatePlResTrans(PlResTrans plResTrans){
        apiBsService.updatePluginUsingConsumer(plResTrans.getConsumer(),plResTrans.getDpuId());
        pluginDao.updatePlResTrans( plResTrans); }

    //     PlCorrelationId;  10
    public PlCorrelationId queryPlCorrelationIdById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlCorrelationId result = pluginDao.queryPlCorrelationIdById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
         }
    public void addPlCorrelationId(PlCorrelationId plCorrelationId){pluginDao.addPlCorrelationId( plCorrelationId); }
    public void delPlCorrelationIdById(String id){ pluginDao.delPlCorrelationIdById(id); }
    @Transactional
    public void updatePlCorrelationId(PlCorrelationId plCorrelationId){
        apiBsService.updatePluginUsingConsumer(plCorrelationId.getConsumer(),plCorrelationId.getDpuId());
        pluginDao.updatePlCorrelationId( plCorrelationId); }

//     PluginTcpLog;   11

    public PluginTcpLog queryPluginTcpLogById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PluginTcpLog result = pluginDao.queryPluginTcpLogById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
         }
    public void addPluginTcpLog(PluginTcpLog pluginTcpLog){pluginDao.addPluginTcpLog( pluginTcpLog); }
    public void delPluginTcpLog(String id){ pluginDao.delPluginTcpLog(id); }
    @Transactional
    public void updatePluginTcpLog(PluginTcpLog pluginTcpLog){
        apiBsService.updatePluginUsingConsumer(pluginTcpLog.getConsumer(),pluginTcpLog.getDpuId());
        pluginDao. updatePluginTcpLog( pluginTcpLog); }

//     PlUdpLog;  12

    public PlUdpLog queryplUdpLogById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlUdpLog result = pluginDao.queryplUdpLogById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
        }
    public void addplUdpLog(PlUdpLog plUdpLog){pluginDao.addplUdpLog( plUdpLog); }
    public void delplUdpLogById(String id){ pluginDao.delplUdpLogById(id); }
    @Transactional
    public void updateplUdpLog(PlUdpLog plUdpLog){
        apiBsService.updatePluginUsingConsumer(plUdpLog.getConsumer(),plUdpLog.getDpuId());
        pluginDao.updateplUdpLog( plUdpLog); }

    //     PlHttpLog;  13
    public PlHttpLog queryPlHttpLogById(String id){
        DagPlugUsingBean consumerValue = apiBsService.queryPluginUsingByid(id);
        PlHttpLog result = pluginDao.queryPlHttpLogById(id);
        result.setConsumer(consumerValue.getConsumer());
        return result;
        }
    public void addPlHttpLog(PlHttpLog plHttpLog){pluginDao.addPlHttpLog( plHttpLog); }
    public void delPlHttpLogById(String id){ pluginDao.delPlHttpLogById(id); }
    @Transactional
    public void updatePlHttpLog(PlHttpLog plHttpLog){
        apiBsService.updatePluginUsingConsumer(plHttpLog.getConsumer(),plHttpLog.getDpuId());
        pluginDao.updatePlHttpLog( plHttpLog); }




}
