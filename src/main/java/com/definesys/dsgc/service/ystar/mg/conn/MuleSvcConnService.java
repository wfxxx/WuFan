package com.definesys.dsgc.service.ystar.mg.conn;

import com.definesys.dsgc.service.ystar.mg.conn.bean.MuleSvcConnBean;
import com.definesys.dsgc.service.ystar.mg.util.FtpUtil;
import com.definesys.dsgc.service.ystar.mg.util.ShellUtil;
import com.definesys.dsgc.service.ystar.mg.conn.bean.DBconnVO;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.utils.JdbcConnection;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @ClassName: MuleSvcConnService
 * @Description: MuleSvcConnService
 * @Author：ystar
 * @Date : 2021/7/15 17:00
 */
@Service("MuleSvcConnService")
public class MuleSvcConnService {

    @Autowired
    private MuleSvcConnDao connDao;


    public Response pageQuerySvcGenConn(MuleSvcConnBean MuleSvcConnBean, int pageIndex, int pageSize) {
        return Response.ok().data(connDao.pageQuerySvcGenConn(MuleSvcConnBean, pageIndex, pageSize));
    }

    public Response listQuerySvcGenConn(MuleSvcConnBean MuleSvcConnBean) {
        return Response.ok().data(connDao.listQuerySvcGenConn(MuleSvcConnBean));
    }

    public Response listQuerySvcGenConnByType(String dbType) {
        return Response.ok().data(connDao.listQuerySvcGenConnByType(dbType));
    }

    public DBconnVO sigQueryDBConnByName(String connName) {
        if (StringUtil.isNotBlank(connName)) {
            MuleSvcConnBean connBean = connDao.querySvcGenConnectByName(connName);
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

    public Response checkConnectNameIsExist(MuleSvcConnBean MuleSvcConnBean) {
        return Response.ok().data(connDao.sigQuerySvcGenConn(MuleSvcConnBean) != null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response checkConnInfoValid(MuleSvcConnBean MuleSvcConnBean) {
        String connType = MuleSvcConnBean.getConnType();
        String attr1 = MuleSvcConnBean.getAttr1();//数据库类型
        String attr2 = MuleSvcConnBean.getAttr2(); //IP
        String attr3 = MuleSvcConnBean.getAttr3(); //Port
        String attr4 = MuleSvcConnBean.getAttr4(); //用户名
        String attr5 = MuleSvcConnBean.getAttr5(); //密码
        String attr6 = MuleSvcConnBean.getAttr6();//RFC系统编号或数据库SID/服务名
        String attr7 = MuleSvcConnBean.getAttr7();//数据库实例名
        boolean connSuccess = false;
        try {
            if ("DB".equals(connType)) {
                connSuccess = this.checkDBConnInfoValid(attr1, attr2, attr3, attr4, attr5, attr6, attr7);
                System.out.println("connSuccess-》" + connSuccess);
            }
//            else if ("RFC".equals(connType)) {
//                SapConnValidBean sapConnValidBean = this.checkSapConnInfoValid(attr1, attr2, attr3, attr4, attr5, attr6);
//                connSuccess = sapConnValidBean.isSapConnValid();
//            }
            else if ("FTP".equals(connType)) {
                connSuccess = this.checkFtpConnInfoValid(attr1, attr2, attr3, attr4, attr5);
            } else if ("IP".equals(connType) || "GIT".equals(connType)) {
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
            return Response.error("测试DB连接失败，请检查连接信息，稍后再试！");
        }
    }


    public boolean checkDBConnInfoValid(String dbType, String ip, String port, String username, String password, String sidOrServNameValue, String dbName) {
        Boolean connSuccess = false;
        String url = "";
        if ("oracle".equals(dbType)) {
            url = "jdbc:oracle:thin:@//" + ip + ":" + port + "/" + sidOrServNameValue;
        } else if ("mysql".equals(dbType)) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useSSL=false";
        } else if ("sqlserver".equals(dbType)) {
            url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName + ";user=" + username + ";password=" + password;
        } else if ("hive".equals(dbType)) {
            url = "jdbc:hive2://" + ip + ":" + port + ";database=" + dbName ;
        }
        Connection connection = null;
        try {
            connection = JdbcConnection.getDSGCDBConnectTest(dbType, url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("数据库驱动加载失败，无法连接数据库");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("getDBConnect异常，无法连接数据库");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        return true;
    }


    public boolean checkFtpConnInfoValid(String ftpType, String ip, String port, String username, String password) {
        int ftpPort = Integer.parseInt(port);
        return FtpUtil.checkFtpConnect(ip, ftpPort, username, password);
    }

    public boolean checkIpPortConnInfoValid(String ip, String port) {
        int ftpPort = Integer.parseInt(port);
        return ShellUtil.checkIpPortValid(ip, ftpPort);
    }


    public boolean checkShellConnInfoValid(String ip, String username, String password) {
        return ShellUtil.checkShellConnValid(ip, username, password);
    }


    public Response saveSvcGenConnectInfo(MuleSvcConnBean MuleSvcConnBean) {
        String connId = MuleSvcConnBean.getConnId();
        if (StringUtil.isNotBlank(connId)) {
            this.connDao.removeSvcGenDBConnectInfoById(connId);
        }
        return Response.ok().data(this.connDao.addSvcGenDBConnectInfo(MuleSvcConnBean));
    }

}

