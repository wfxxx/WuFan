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
    public PlBasicAuthBean queryPlBasicAuthBeanByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlBasicAuthBean.class); }
    public void addPlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){sw.buildQuery().doInsert(plBasicAuthBean); }
    public void delPlBasicAuthBeanByVId(String vid){
     sw.buildQuery().eq("vid",vid).doDelete(PlBasicAuthBean.class);
    }
    public void updatePlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){ sw.buildQuery().eq("vid",plBasicAuthBean.getVid()).doUpdate(plBasicAuthBean); }

//PlBasicAuthBean 2
    public PlKeyAuthBean queryPlKeyAuthBeannByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlKeyAuthBean.class); }
    public void addPlKeyAuthBean(PlKeyAuthBean plKeyAuthBean){sw.buildQuery().doInsert(plKeyAuthBean); }
    public void delPlKeyAuthBeanByVId(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(PlKeyAuthBean.class);
    }
    public void updatePlKeyAuthBeanBean(PlKeyAuthBean plKeyAuthBean){ sw.buildQuery().eq("vid",plKeyAuthBean.getVid()).doUpdate(plKeyAuthBean); }

 //    PlOauth2;  3
    public PlOauth2 queryPlOauth2nByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlOauth2.class); }
    public void addPlOauth2(PlOauth2 plOauth2){sw.buildQuery().doInsert(plOauth2); }
    public void delPlOauth2ByVId(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(PlOauth2.class);
    }
    public void updatePlOauth2(PlOauth2 plOauth2){ sw.buildQuery().eq("vid",plOauth2.getVid()).doUpdate(plOauth2); }

    //    PlAddAcl;  4
    public PlAddAcl queryPlAddAclByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlAddAcl.class); }
    public void addPlAddAcl(PlAddAcl plAddAcl){sw.buildQuery().doInsert(plAddAcl); }
    public void delPlAddAclByVId(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(PlAddAcl.class);
    }
    public void updatePlAddAcl(PlAddAcl plAddAcl){ sw.buildQuery().eq("vid",plAddAcl.getVid()).doUpdate(plAddAcl); }

 //    PlIpRestriction;  5
    public PlIpRestriction queryPlIpRestrictionByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlIpRestriction.class); }
    public void addPlIpRestriction(PlIpRestriction plIpRestriction){sw.buildQuery().doInsert(plIpRestriction); }
    public void delPlIpRestrictionByVId(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(PlIpRestriction.class);
    }
    public void updatePlIpRestriction(PlIpRestriction plIpRestriction){ sw.buildQuery().eq("vid",plIpRestriction.getVid()).doUpdate(plIpRestriction); }
  //   PlRateLimiting; 6

    public PlRateLimiting queryPlRateLimitingByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlRateLimiting.class); }
    public void addPlRateLimiting(PlRateLimiting plRateLimiting){sw.buildQuery().doInsert(plRateLimiting); }
    public void delPlRateLimitingByVId(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(PlRateLimiting.class);
    }
    public void updatePlRateLimiting(PlRateLimiting plRateLimiting){ sw.buildQuery().eq("vid",plRateLimiting.getVid()).doUpdate(plRateLimiting); }
  //   PlReqSizeLimiting; 7

    public PlReqSizeLimiting queryPlReqSizeLimitingByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlReqSizeLimiting.class); }
    public void addPlReqSizeLimiting(PlReqSizeLimiting plReqSizeLimiting){sw.buildQuery().doInsert(plReqSizeLimiting); }
    public void delPlReqSizeLimitingByVId(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PlReqSizeLimiting.class); }
    public void updatePlReqSizeLimitingl(PlReqSizeLimiting plReqSizeLimiting){ sw.buildQuery().eq("vid",plReqSizeLimiting.getVid()).doUpdate(plReqSizeLimiting); }

 //    PlReqTrans;  8
    public PlReqTrans queryPlReqTransByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlReqTrans.class); }
    public void addPlReqTrans(PlReqTrans plReqTrans){sw.buildQuery().doInsert(plReqTrans); }
    public void delPlReqTransByVId(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PlReqTrans.class); }
    public void updatePlReqTrans(PlReqTrans plReqTrans){ sw.buildQuery().eq("vid",plReqTrans.getVid()).doUpdate(plReqTrans); }

//     PlResTrans;  9
    public PlResTrans queryPlResTransByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlResTrans.class); }
    public void addPlResTrans(PlResTrans plResTrans){sw.buildQuery().doInsert(plResTrans); }
    public void delPlResTransByVId(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PlResTrans.class); }
    public void updatePlResTrans(PlResTrans plResTrans){ sw.buildQuery().eq("vid",plResTrans.getVid()).doUpdate(plResTrans); }

//     PlCorrelationId;  10
    public PlCorrelationId queryPlCorrelationIdByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlCorrelationId.class); }
    public void addPlCorrelationId(PlCorrelationId plCorrelationId){sw.buildQuery().doInsert(plCorrelationId); }
    public void delPlCorrelationIdByVId(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PlCorrelationId.class); }
    public void updatePlCorrelationId(PlCorrelationId plCorrelationId){ sw.buildQuery().eq("vid",plCorrelationId.getVid()).doUpdate(plCorrelationId); }

//     PluginTcpLog;   11

    public PluginTcpLog queryPluginTcpLogByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PluginTcpLog.class); }
    public void addPluginTcpLog(PluginTcpLog pluginTcpLog){sw.buildQuery().doInsert(pluginTcpLog); }
    public void delPluginTcpLog(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PluginTcpLog.class); }
    public void updatePluginTcpLog(PluginTcpLog pluginTcpLog){ sw.buildQuery().eq("vid",pluginTcpLog.getVid()).doUpdate(pluginTcpLog); }

//     PlUdpLog;  12

    public PlUdpLog queryplUdpLogByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlUdpLog.class); }
    public void addplUdpLog(PlUdpLog plUdpLog){sw.buildQuery().doInsert(plUdpLog); }
    public void delplUdpLogByVId(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PlUdpLog.class); }
    public void updateplUdpLog(PlUdpLog plUdpLog){ sw.buildQuery().eq("vid",plUdpLog.getVid()).doUpdate(plUdpLog); }

//     PlHttpLog;  13
public PlHttpLog queryPlHttpLogByVid(String vid){ return  sw.buildQuery().eq("vid",vid).doQueryFirst(PlHttpLog.class); }
    public void addPlHttpLog(PlHttpLog plHttpLog){sw.buildQuery().doInsert(plHttpLog); }
    public void delPlHttpLogByVId(String vid){ sw.buildQuery().eq("vid",vid).doDelete(PlHttpLog.class); }
    public void updatePlHttpLog(PlHttpLog plHttpLog){ sw.buildQuery().eq("vid",plHttpLog.getVid()).doUpdate(plHttpLog); }


}
