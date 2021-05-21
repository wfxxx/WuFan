package com.definesys.dsgc.service.ystar.file;

import com.definesys.dsgc.service.ystar.file.bean.ScpConnectEntity;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName: FileController
 * @Description: TODO
 * @Author：ystar
 * @Date : 2021/5/21 12:00
 */
@RestController("FileController")
@Api(value = "文件传输", tags = {"文件传输"})
@RequestMapping("/dsgc/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /*** 导入Excel文件  */
    @ApiOperation("上传文件至服务器")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Response uploadFile(@RequestBody ScpConnectEntity scpConnectEntity,
                               @RequestParam String trgFileName) {
        System.out.println("------导入------");
        //System.out.println(files.size());
        return Response.ok().data(fileService.uploadFile(scpConnectEntity, trgFileName));
    }
}
