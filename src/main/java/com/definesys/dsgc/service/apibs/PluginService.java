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

    public void deletePluginContext(String vid,String pluginCode) throws Exception {
        switch (pluginCode){
            case "1":
                pluginDao.delPlBasicAuthBeanByVId(vid);
                break;
            case"2":
                pluginDao.delPlKeyAuthBeanByVId(vid);
                break;
            case"3":
                pluginDao.delPlOauth2ByVId(vid);
                break;
            case"4":
                pluginDao.delPlAddAclByVId(vid);
                break;
            case"5":
                pluginDao.delPlIpRestrictionByVId(vid);
                break;
            case"6":
                pluginDao.delPlRateLimitingByVId(vid);
                break;

            case"7":
                pluginDao.delPlReqSizeLimitingByVId(vid);
                break;
            case"8":
                pluginDao.delPlReqSizeLimitingByVId(vid);
                break;
            case"9":
                pluginDao.delPlResTransByVId(vid);
                break;
            case"10":
                pluginDao.delPlCorrelationIdByVId(vid);
                break;
            case"11":
                pluginDao.delPluginTcpLog(vid);
                break;
            case"12":
                pluginDao.delplUdpLogByVId(vid);
                break;
            case"13":
                pluginDao.delPlHttpLogByVId(vid);
                break;
            default:
                throw new Exception("该插件code没有对应的插件表");

        }
    }
    //  PlBasicAuthBean
    public void addPlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){ pluginDao.addPlBasicAuthBean(plBasicAuthBean); }
    public PlBasicAuthBean queryPlBasicAuthBeanByVid(String vid){ return pluginDao.queryPlBasicAuthBeanByVid(vid); }
    public void delPlBasicAuthBeanByVId(String vid){ pluginDao.delPlBasicAuthBeanByVId(vid); }
    public void updatePlBasicAuthBean(PlBasicAuthBean plBasicAuthBean){ pluginDao.updatePlBasicAuthBean(plBasicAuthBean); }

    //PlKeyAuthBeann
    public PlKeyAuthBean queryPlKeyAuthBeannByVid(String vid){ return  pluginDao.queryPlKeyAuthBeannByVid(vid); }
    public void addPlKeyAuthBean(PlKeyAuthBean plKeyAuthBean){pluginDao.addPlKeyAuthBean(plKeyAuthBean);}
    public void delPlKeyAuthBeanByVId(String vid){ pluginDao.delPlKeyAuthBeanByVId(vid); }
    public void updatePlKeyAuthBeanBean(PlKeyAuthBean plKeyAuthBean){ pluginDao.updatePlKeyAuthBeanBean(plKeyAuthBean); }
    //    PlOauth2;  3
    public PlOauth2 queryPlOauth2nByVid(String vid){ return  pluginDao.queryPlOauth2nByVid(vid); }
    public void addPlOauth2(PlOauth2 plOauth2){pluginDao.addPlOauth2(plOauth2); }
    public void delPlOauth2ByVId(String vid){
        pluginDao.delPlOauth2ByVId(vid);
    }
    public void updatePlOauth2(PlOauth2 plOauth2){ pluginDao.updatePlOauth2( plOauth2); }

    //    PlAddAcl;  4
    public PlAddAcl queryPlAddAclByVid(String vid){ return  pluginDao.queryPlAddAclByVid( vid); }
    public void addPlAddAcl(PlAddAcl plAddAcl){pluginDao.addPlAddAcl( plAddAcl); }
    public void delPlAddAclByVId(String vid){
        pluginDao.delPlAddAclByVId( vid);
    }
    public void updatePlAddAcl(PlAddAcl plAddAcl){ pluginDao.updatePlAddAcl( plAddAcl); }

    //    PlIpRestriction;  5
    public PlIpRestriction queryPlIpRestrictionByVid(String vid){ return  pluginDao.queryPlIpRestrictionByVid( vid); }
    public void addPlIpRestriction(PlIpRestriction plIpRestriction){pluginDao.addPlIpRestriction(plIpRestriction); }
    public void delPlIpRestrictionByVId(String vid){ pluginDao.delPlIpRestrictionByVId( vid); }
    public void updatePlIpRestriction(PlIpRestriction plIpRestriction){ pluginDao.updatePlIpRestriction(plIpRestriction); }
    //   PlRateLimiting; 6

    public PlRateLimiting queryPlRateLimitingByVid(String vid){ return  queryPlRateLimitingByVid( vid); }
    public void addPlRateLimiting(PlRateLimiting plRateLimiting){pluginDao.addPlRateLimiting( plRateLimiting); }
    public void delPlRateLimitingByVId(String vid){ pluginDao.delPlRateLimitingByVId( vid); }
    public void updatePlRateLimiting(PlRateLimiting plRateLimiting){ pluginDao.updatePlRateLimiting( plRateLimiting); }
    //   PlReqSizeLimiting; 7

    public PlReqSizeLimiting queryPlReqSizeLimitingByVid(String vid){ return  pluginDao.queryPlReqSizeLimitingByVid( vid); }
    public void addPlReqSizeLimiting(PlReqSizeLimiting plReqSizeLimiting){pluginDao.addPlReqSizeLimiting( plReqSizeLimiting); }
    public void delPlReqSizeLimitingByVId(String vid){ pluginDao.delPlReqSizeLimitingByVId( vid); }
    public void updatePlReqSizeLimitingl(PlReqSizeLimiting plReqSizeLimiting){ pluginDao.updatePlReqSizeLimitingl( plReqSizeLimiting); }

    //    PlReqTrans;  8
    public PlReqTrans queryPlReqTransByVid(String vid){ return  pluginDao.queryPlReqTransByVid( vid); }
    public void addPlReqTrans(PlReqTrans plReqTrans){pluginDao.addPlReqTrans( plReqTrans); }
    public void delPlReqTransByVId(String vid){ pluginDao.delPlReqTransByVId( vid); }
    public void updatePlReqTrans(PlReqTrans plReqTrans){ pluginDao.updatePlReqTrans( plReqTrans); }

    //     PlResTrans;  9
    public PlResTrans queryPlResTransByVid(String vid){ return  pluginDao.queryPlResTransByVid( vid); }
    public void addPlResTrans(PlResTrans plResTrans){pluginDao.addPlResTrans( plResTrans); }
    public void delPlResTransByVId(String vid){ pluginDao.delPlResTransByVId( vid); }
    public void updatePlResTrans(PlResTrans plResTrans){ pluginDao.updatePlResTrans( plResTrans); }

    //     PlCorrelationId;  10
    public PlCorrelationId queryPlCorrelationIdByVid(String vid){ return  pluginDao.queryPlCorrelationIdByVid( vid); }
    public void addPlCorrelationId(PlCorrelationId plCorrelationId){pluginDao.addPlCorrelationId( plCorrelationId); }
    public void delPlCorrelationIdByVId(String vid){ pluginDao.delPlCorrelationIdByVId( vid); }
    public void updatePlCorrelationId(PlCorrelationId plCorrelationId){ pluginDao.updatePlCorrelationId( plCorrelationId); }

//     PluginTcpLog;   11

    public PluginTcpLog queryPluginTcpLogByVid(String vid){ return  pluginDao. queryPluginTcpLogByVid( vid); }
    public void addPluginTcpLog(PluginTcpLog pluginTcpLog){pluginDao.addPluginTcpLog( pluginTcpLog); }
    public void delPluginTcpLog(String vid){ pluginDao.delPluginTcpLog( vid); }
    public void updatePluginTcpLog(PluginTcpLog pluginTcpLog){ pluginDao. updatePluginTcpLog( pluginTcpLog); }

//     PlUdpLog;  12

    public PlUdpLog queryplUdpLogByVid(String vid){ return  pluginDao.queryplUdpLogByVid( vid); }
    public void addplUdpLog(PlUdpLog plUdpLog){pluginDao.addplUdpLog( plUdpLog); }
    public void delplUdpLogByVId(String vid){ pluginDao.delplUdpLogByVId( vid); }
    public void updateplUdpLog(PlUdpLog plUdpLog){ pluginDao.updateplUdpLog( plUdpLog); }

    //     PlHttpLog;  13
    public PlHttpLog queryPlHttpLogByVid(String vid){ return  pluginDao.queryPlHttpLogByVid( vid); }
    public void addPlHttpLog(PlHttpLog plHttpLog){pluginDao.addPlHttpLog( plHttpLog); }
    public void delPlHttpLogByVId(String vid){ pluginDao.delPlHttpLogByVId( vid); }
    public void updatePlHttpLog(PlHttpLog plHttpLog){ pluginDao.updatePlHttpLog( plHttpLog); }


}
