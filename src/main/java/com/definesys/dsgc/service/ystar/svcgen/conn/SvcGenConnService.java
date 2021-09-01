package com.definesys.dsgc.service.ystar.svcgen.conn;

import com.definesys.dsgc.common.utils.DSUtils;
import com.definesys.dsgc.service.svcgen.bean.SABean;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.ystar.mg.conn.bean.DBconnVO;
import com.definesys.dsgc.service.ystar.mg.conn.bean.MuleSvcConnBean;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnInfoJsonBean;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnValidBean;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.util.FtpUtil;
import com.definesys.dsgc.service.ystar.svcgen.util.ServiceGenerateProxy;
import com.definesys.dsgc.service.ystar.svcgen.util.ShellUtil;
import com.definesys.dsgc.service.ystar.utils.JdbcConnection;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: SvcGenConnService
 * @Description: SvcGenConnService
 * @Author：ystar
 * @Date : 2021/4/15 17:00
 */
@Service("SvcGenConnService")
public class SvcGenConnService {

    @Autowired
    private SvcGenConnDao connDao;

    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();

    public Response pageQuerySvcGenConn(SvcgenConnBean svcgenConnBean, int pageIndex, int pageSize) {
        return Response.ok().data(connDao.pageQuerySvcGenConn(svcgenConnBean, pageIndex, pageSize));
    }

    public Response querySvcGenConnList(SvcgenConnBean svcgenConnBean) {
        return Response.ok().data(connDao.querySvcGenConnList(svcgenConnBean));
    }

    public Response checkConnectNameIsExist(SvcgenConnBean svcgenConnBean) {
        return Response.ok().data(connDao.queryFirstSvcGenConnect(svcgenConnBean) != null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response checkConnInfoValid(SvcgenConnBean svcgenConnBean) {
        String connType = svcgenConnBean.getConnType();
        String attr1 = svcgenConnBean.getAttr1();//数据库类型
        String attr2 = svcgenConnBean.getAttr2(); //IP
        String attr3 = svcgenConnBean.getAttr3(); //Port
        String attr4 = svcgenConnBean.getAttr4(); //用户名
        String attr5 = svcgenConnBean.getAttr5(); //密码
        String attr6 = svcgenConnBean.getAttr6();//RFC系统编号或数据库SID/服务名
        String attr7 = svcgenConnBean.getAttr7();//数据库实例名
        boolean connSuccess = false;
        try {
            if ("DB".equals(connType)) {
                connSuccess = this.checkDBConnInfoValid(attr1, attr2, attr3, attr4, attr5, attr6, attr7);
                System.out.println("connSuccess-》" + connSuccess);
            } else if ("RFC".equals(connType)) {
                SapConnValidBean sapConnValidBean = this.checkSapConnInfoValid(attr1, attr2, attr3, attr4, attr5, attr6);
                connSuccess = sapConnValidBean.isSapConnValid();
            } else if ("FTP".equals(connType)) {
                connSuccess = this.checkFtpConnInfoValid(attr1, attr2, attr3, attr4, attr5);
            } else if ("REST".equals(connType) || "SOAP".equals(connType) || "GIT".equals(connType)) {
                connSuccess = this.checkIpPortConnInfoValid(attr2, attr3);
            } else if ("Shell".equals(connType)) {
                connSuccess = this.checkShellConnInfoValid(attr2, attr4, attr5);
            }
//            else if ("GIT".equals(connType)) {
//                connSuccess = this.checkGitConnInfoValid(attr2, attr3, attr4, attr5);
//            }
            if (connSuccess) {
                return Response.ok().setMessage("连接成功！").data(true);
            } else {
                return Response.error("连接失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("测试DB连接失败，请检查连接信息，稍后再试！");
        }
    }


    public boolean checkDBConnInfoValid(String dbType, String ip, String port, String username, String password, String sidOrServNameValue, String dbName) {
        String url = "";
        if ("oracle".equals(dbType)) {
            url = "jdbc:oracle:thin:@//" + ip + ":" + port + "/" + dbName;
        } else if ("mysql".equals(dbType)) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useSSL=false";
        } else if ("sqlserver".equals(dbType)) {
            url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName + ";user=" + username + ";password=" + password ;
        } else if ("hive".equals(dbType)) {
            url = "";
        }
        Connection connection = null;
        try {
            System.out.println(dbType + " " + url + " " + username + " " + password);
            connection = JdbcConnection.getDSGCDBConnectTest(dbType, url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("数据库驱动加载失败，无法连接数据库");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("getDBConnect异常，无法连接数据库");
        } finally {
            DSUtils.close(connection);
        }
        return true;
    }

    public SapConnValidBean checkSapConnInfoValid(String lang, String sapIp, String sapClient, String connUN, String connPD, String sn) {
        SapConnInfoJsonBean connInfo = new SapConnInfoJsonBean();
        connInfo.setLang(lang);
        connInfo.setSapIp(sapIp);
        connInfo.setSapClient(sapClient);
        connInfo.setConnUN(connUN);
        connInfo.setConnPD(connPD);
        connInfo.setSn(sn);
        SapConnValidBean res = new SapConnValidBean(false);
        try {
            String rtn = this.sgProxy.validSapConnInfo(connInfo);
            System.out.println("rtn->" + rtn);
            if ("S".equals(rtn)) {
                res.setSapConnValid(true);
            } else {
                res.setSapConnValid(false);
                res.setSapConnValidMsg(rtn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setSapConnValid(false);
            String errorMsg = CommonUtils.getExceptionTrackDetailInfo(e);
            errorMsg = CommonUtils.splitSpecifyLengthStr(errorMsg, 4000);
            res.setSapConnValidMsg(Base64.getEncoder().encodeToString(errorMsg.getBytes(Charset.forName("UTF-8"))));
        }
        return res;
    }

    public boolean checkFtpConnInfoValid(String ftpType, String ip, String port, String username, String password) {
        int ftpPort = Integer.parseInt(port);
        return FtpUtil.checkFtpConnect(ip, ftpPort, username, password);
    }

    public boolean checkIpPortConnInfoValid(String ip, String port) {
        int ftpPort = Integer.parseInt(port);
        return ShellUtil.checkIpPortValid(ip, ftpPort);
    }

    public boolean checkGitConnInfoValid(String ip, String port, String username, String password) {
        return ShellUtil.checkGitRepoValid("http://" + ip + ":" + port, username, password);
    }

    public boolean checkShellConnInfoValid(String ip, String username, String password) {
        return ShellUtil.checkShellConnValid(ip, username, password);
    }


    public Response saveSvcGenConnectInfo(SvcgenConnBean svcgenConnBean) {
        String connId = svcgenConnBean.getConnId();
        if (StringUtil.isNotBlank(connId)) {
            this.connDao.removeSvcGenConnById(connId);
        }
        return Response.ok().data(this.connDao.addSvcGenDBConnectInfo(svcgenConnBean));
    }

    public Response removeSvcGenConnById(String connId) {
        this.connDao.removeSvcGenConnById(connId);
        return Response.ok().setMessage("删除成功！");
    }


    public Response listQuerySvcGenConnByType(String dbType) {
        return Response.ok().data(connDao.listQuerySvcGenConnByType(dbType));
    }

    public DBconnVO sigQueryDBConnByName(String connName) {
        if (StringUtil.isNotBlank(connName)) {
            SvcgenConnBean connBean = connDao.querySvcGenConnectByName(connName);
            DBconnVO dBconnVO = new DBconnVO();
            dBconnVO.setConnId(connBean.getConnId());
            dBconnVO.setConnName(connBean.getConnName());
            dBconnVO.setDbType(connBean.getAttr1());
            dBconnVO.setDbIp(connBean.getAttr2());
            dBconnVO.setPort(connBean.getAttr3());
            dBconnVO.setConnUN(connBean.getAttr4());
            dBconnVO.setConnPD(connBean.getAttr5());
            if ("oracle".equals(connBean.getAttr1())) {
                dBconnVO.setSidOrServNameLabel(connBean.getAttr6());
                dBconnVO.setSidOrServNameValue(connBean.getAttr7());
            } else if ("mysql".equals(connBean.getAttr1())) {
                dBconnVO.setDbName(connBean.getAttr7());
            }
            return dBconnVO;
        } else {
            throw new MpaasBusinessException("请求参数错误，请检查参数！");
        }
    }

}

