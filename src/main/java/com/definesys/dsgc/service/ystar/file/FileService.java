package com.definesys.dsgc.service.ystar.file;

import com.definesys.dsgc.service.ystar.file.bean.ResultEntity;
import com.definesys.dsgc.service.ystar.file.bean.ScpConnectEntity;
import com.definesys.dsgc.service.ystar.utils.FileUtils;
import com.jcraft.jsch.JSchException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service("FileService")
public class FileService {

    @Async
    public ResultEntity uploadFile(ScpConnectEntity scpConnectEntity, String remoteFileName) {
        File file = new File(scpConnectEntity.getFilePath());//本地文件
        String code = null;
        String message = null;
        try {
            if (!file.exists()) {
                throw new IllegalArgumentException("请确保上传文件不为空且存在！");
            }
            if (remoteFileName == null || "".equals(remoteFileName.trim())) {
                throw new IllegalArgumentException("远程服务器新建文件名不能为空!");
            }
            FileUtils.remoteUploadFile(scpConnectEntity, file, remoteFileName);
            code = "ok";
            message = remoteFileName;
        } catch (IllegalArgumentException | IOException | JSchException e) {
            code = "Exception";
            message = e.getMessage();
        } catch (Exception e) {
            throw e;
        } catch (Error e) {
            code = "Error";
            message = e.getMessage();
        }
        return new ResultEntity(code, message, null);
    }

}
