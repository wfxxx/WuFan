package com.definesys.dsgc.service.apibs;


import com.definesys.dsgc.service.apibs.bean.pluginBean.*;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName PluginDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:05
 * @Version 1.0
 **/
@Repository
public class PluginDao {

    @Autowired
    private MpaasQueryFactory sw;


//PlBasicAuthBean 1
    public PlBasicAuthBean queryPlBasicAuthBeanById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlBasicAuthBean.class); }
    public void addPlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){sw.buildQuery().doInsert(plBasicAuthBean); }
    public void delPlBasicAuthBeanById(String id){
     sw.buildQuery().eq("dpuId",id).doDelete(PlBasicAuthBean.class);
    }
    public void updatePlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){ sw.buildQuery().eq("baId",plBasicAuthBean.getBaId()).doUpdate(plBasicAuthBean); }

//PlBasicAuthBean 2
    public PlKeyAuthBean queryPlKeyAuthBeannById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlKeyAuthBean.class); }
    public void addPlKeyAuthBean(PlKeyAuthBean plKeyAuthBean){sw.buildQuery().doInsert(plKeyAuthBean); }
    public void delPlKeyAuthBeanById(String id){
        sw.buildQuery().eq("dpuId",id).doDelete(PlKeyAuthBean.class);
    }
    public void updatePlKeyAuthBeanBean(PlKeyAuthBean plKeyAuthBean){ sw.buildQuery().eq("kaId",plKeyAuthBean.getKaId()).doUpdate(plKeyAuthBean); }

 //    PlOauth2;  3
    public PlOauth2 queryPlOauth2nById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlOauth2.class); }
    public void addPlOauth2(PlOauth2 plOauth2){sw.buildQuery().doInsert(plOauth2); }
    public void delPlOauth2ById(String id){
        sw.buildQuery().eq("dpuId",id).doDelete(PlOauth2.class);
    }
    public void updatePlOauth2(PlOauth2 plOauth2){ sw.buildQuery().eq("poId",plOauth2.getPoId()).doUpdate(plOauth2); }

    //    PlAddAcl;  4
    public PlAddAcl queryPlAddAclById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlAddAcl.class); }
    public void addPlAddAcl(PlAddAcl plAddAcl){sw.buildQuery().doInsert(plAddAcl); }
    public void delPlAddAclById(String id){
        sw.buildQuery().eq("dpuId",id).doDelete(PlAddAcl.class);
    }
    public void updatePlAddAcl(PlAddAcl plAddAcl){ sw.buildQuery().eq("aclId",plAddAcl.getAclId()).doUpdate(plAddAcl); }

 //    PlIpRestriction;  5
    public PlIpRestriction queryPlIpRestrictionById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlIpRestriction.class); }
    public void addPlIpRestriction(PlIpRestriction plIpRestriction){sw.buildQuery().doInsert(plIpRestriction); }
    public void delPlIpRestrictionById(String id){
        sw.buildQuery().eq("dpuId",id).doDelete(PlIpRestriction.class);
    }
    public void updatePlIpRestriction(PlIpRestriction plIpRestriction){ sw.buildQuery().eq("irId",plIpRestriction.getIrId()).doUpdate(plIpRestriction); }
  //   PlRateLimiting; 6

    public PlRateLimiting queryPlRateLimitingById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlRateLimiting.class); }
    public void addPlRateLimiting(PlRateLimiting plRateLimiting){sw.buildQuery().doInsert(plRateLimiting); }
    public void delPlRateLimitingById(String id){
        sw.buildQuery().eq("dpuId",id).doDelete(PlRateLimiting.class);
    }
    public void updatePlRateLimiting(PlRateLimiting plRateLimiting){ sw.buildQuery().eq("prlId",plRateLimiting.getPrlId()).doUpdate(plRateLimiting); }
  //   PlReqSizeLimiting; 7

    public PlReqSizeLimiting queryPlReqSizeLimitingById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlReqSizeLimiting.class); }
    public void addPlReqSizeLimiting(PlReqSizeLimiting plReqSizeLimiting){sw.buildQuery().doInsert(plReqSizeLimiting); }
    public void delPlReqSizeLimitingById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlReqSizeLimiting.class); }
    public void updatePlReqSizeLimitingl(PlReqSizeLimiting plReqSizeLimiting){ sw.buildQuery().eq("rslId",plReqSizeLimiting.getRslId()).doUpdate(plReqSizeLimiting); }

 //    PlReqTrans;  8
    public PlReqTrans queryPlReqTransById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlReqTrans.class); }
    public void addPlReqTrans(PlReqTrans plReqTrans){sw.buildQuery().doInsert(plReqTrans); }
    public void delPlReqTransById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlReqTrans.class); }
    public void updatePlReqTrans(PlReqTrans plReqTrans){ sw.buildQuery().eq("prtId",plReqTrans.getPrtId()).doUpdate(plReqTrans); }

//     PlResTrans;  9
    public PlResTrans queryPlResTransById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlResTrans.class); }
    public void addPlResTrans(PlResTrans plResTrans){sw.buildQuery().doInsert(plResTrans); }
    public void delPlResTransById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlResTrans.class); }
    public void updatePlResTrans(PlResTrans plResTrans){ sw.buildQuery().eq("prtId",plResTrans.getPrtId()).doUpdate(plResTrans); }

//     PlCorrelationId;  10
    public PlCorrelationId queryPlCorrelationIdById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlCorrelationId.class); }
    public void addPlCorrelationId(PlCorrelationId plCorrelationId){sw.buildQuery().doInsert(plCorrelationId); }
    public void delPlCorrelationIdById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlCorrelationId.class); }
    public void updatePlCorrelationId(PlCorrelationId plCorrelationId){ sw.buildQuery().eq("clId",plCorrelationId.getClId()).doUpdate(plCorrelationId); }

//     PluginTcpLog;   11

    public PluginTcpLog queryPluginTcpLogById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PluginTcpLog.class); }
    public void addPluginTcpLog(PluginTcpLog pluginTcpLog){sw.buildQuery().doInsert(pluginTcpLog); }
    public void delPluginTcpLog(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PluginTcpLog.class); }
    public void updatePluginTcpLog(PluginTcpLog pluginTcpLog){ sw.buildQuery().eq("tlId",pluginTcpLog.getTlId()).doUpdate(pluginTcpLog); }

//     PlUdpLog;  12

    public PlUdpLog queryplUdpLogById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlUdpLog.class); }
    public void addplUdpLog(PlUdpLog plUdpLog){sw.buildQuery().doInsert(plUdpLog); }
    public void delplUdpLogById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlUdpLog.class); }
    public void updateplUdpLog(PlUdpLog plUdpLog){ sw.buildQuery().eq("uiId",plUdpLog.getUlId()).doUpdate(plUdpLog); }

//     PlHttpLog;  13
public PlHttpLog queryPlHttpLogById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlHttpLog.class); }
    public void addPlHttpLog(PlHttpLog plHttpLog){sw.buildQuery().doInsert(plHttpLog); }
    public void delPlHttpLogById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlHttpLog.class); }
    public void updatePlHttpLog(PlHttpLog plHttpLog){ sw.buildQuery().eq("hlId",plHttpLog.getHlId()).doUpdate(plHttpLog); }

    //     PlJwt;  14
    public PlJwt queryPlJwtById(String id){ return  sw.buildQuery().eq("dpuId",id).doQueryFirst(PlJwt.class); }
    public void addPlJwt(PlJwt plJwt){sw.buildQuery().doInsert(plJwt); }
    public void delPlJwtById(String id){ sw.buildQuery().eq("dpuId",id).doDelete(PlJwt.class); }
    public void updatePlJwt(PlJwt plJwt){ sw.buildQuery().eq("jwtId",plJwt.getJwtId()).doUpdate(plJwt); }



}
