package com.definesys.dsgc.service.svclog;

import com.alibaba.fastjson.JSONArray;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svclog.bean.DSGCLogAudit;
import com.definesys.dsgc.service.svclog.bean.DSGCLogInstance;
import com.definesys.dsgc.service.svclog.bean.DSGCLogOutBound;
import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import com.definesys.mpaas.common.exception.MpaasRuntimeException;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("logService")
public class DSGCLogInstanceService {

    @Autowired
    DSGCLogInstanceDao dsgcLogInstanceDao;


    public PageQueryResult<DSGCLogInstance> query(List<Object> keyword,String userRole, String uid, DSGCLogInstance instance, int pageSize, int pageIndex) {
        try {
            PageQueryResult<DSGCLogInstance> dsgcLogInstance = new PageQueryResult<DSGCLogInstance>();
            // 根据用户名查询用户所管理的系统
            // 记录管理系统名称
            List<String> codeList = new ArrayList<>();
            // 记录接口名称
            List<String> servNoList = new ArrayList<>();
            if ("SuperAdministrators".equals(userRole)||"Administrators".equals(userRole)) { // 超管和管理员可以查看到所有的服务日志
            } else if ("SystemLeader".equals(userRole)) { // 系统负责人只能查看自己负责的系统
                // 根据uid可以查看到用户与系统的关系
                List<DSGCSystemUser> codes = dsgcLogInstanceDao.querySystemlistUserByUserId(uid);
                if(codes.size() < 1){
                   return dsgcLogInstance;
                }else{
                    for(DSGCSystemUser user : codes){
                        codeList.add(user.getSysCode());
                    }
                    // 根据系统查询接口名称
                    List<TopologyVO> servNoBySystem = dsgcLogInstanceDao.getServNoBySystem(codeList);
                    for (TopologyVO topologyVO: servNoBySystem) {
                        servNoList.add(topologyVO.getServNo());
                    }
                }
            } else if ("Tourist".equals(userRole)) {
                return dsgcLogInstance;
            }
            dsgcLogInstance = this.dsgcLogInstanceDao.query(keyword,userRole ,uid, instance, pageSize, pageIndex,servNoList);
            return dsgcLogInstance;
        } catch (Exception e) {
            throw new MpaasRuntimeException(e);
        }
    }

//    public PageQueryResult<DSGCLogInstance> query1(String userRole, DSGCLogInstance instance, int pageSize, int pageIndex,String uId) {
//        //当前用户为系统管理员时，获取系统编码，作为查询筛选条件
//        DSGCSystemUser systemUser = projDirManagerDao.querySystemUserByUserId(uId);
//        try {
//
//            return this.dsgcLogInstanceDao.query(userRole,instance, pageSize, pageIndex, systemUser);
//        } catch (Exception e) {
//            throw new MpaasRuntimeException(e);
//        }
//    }

    public DSGCLogInstance findLogById(DSGCLogInstance instance) {
        return this.dsgcLogInstanceDao.findLogById(instance);
    }

    public DSGCLogInstance findLogById(String trackId) {
        return this.dsgcLogInstanceDao.findLogById(trackId);
    }

    public List<String> getLogPartition() {
        return this.dsgcLogInstanceDao.getLogPartition();
    }

    public List<DSGCLogAudit> getAuditLog(String trackId) {
        return this.dsgcLogInstanceDao.getAuditLog(trackId);
    }

    public void doRetry(JSONArray jsonArray) {
        this.dsgcLogInstanceDao.doRetry(jsonArray);
    }

    public List<DSGCLogOutBound> getStackLog(String trackId) {
        return this.dsgcLogInstanceDao.getStackLog(trackId);
    }

    public String getHeaderPayload(String trackId, String ibLob) {
        return this.dsgcLogInstanceDao.getHeaderPayload(trackId, ibLob);
    }

    public String getBodyPayload(String ibLob) {
        return this.dsgcLogInstanceDao.getBodyPayload(ibLob);
    }

    public void noPayload(HttpServletResponse response) {
        String s = "该报文不存在，请联系管理员";
        showData(response, s);
    }

    public void showData(HttpServletResponse response,String str) {
        if (str.startsWith("<")) {
            response.setContentType("text/xml;charset=UTF-8");
        } else {
            response.setContentType("text/plain;charset=UTF-8");
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(str);
        out.flush();
        out.close();
    }

    public String dealPath(String startTime, String servNo, String ibLob) {
        String t =startTime.trim();// StringUtils.substringBefore(startTime, " ");
        String folderName = t.replace("-", "");
        String folderName1 = folderName.substring(0, 6);

        String path = "/u01/osbLog/" + folderName1 + "/" + folderName + "/" + servNo + "/" + ibLob;
        System.out.println(path);
        return path;
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String tempString = "";
        String fileContent = "";
        if (file.exists()) {
            try {
                reader = new BufferedReader(new FileReader(file));
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
                    fileContent += tempString;
                    line++;
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e1) {
                    }
                }
            }
        } else {
            return "error";
        }
        return fileContent;
    }

    public String getErrMsg(String errLob) {
        return this.dsgcLogInstanceDao.getErrMsg(errLob);
    }

    public List<DSGCSystemEntities> getAllSystemCodeAndName() {
        return dsgcLogInstanceDao.getAllSystemCodeAndName();
    }
}
