package com.definesys.dsgc.service.ystar.excel;

import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.report.bean.ReportSvcDataBean;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @ClassName: ImportExcelController
 * @Description: TODO
 * @Author：ystar
 * @Date : 2020/12/16 12:00
 */
@RestController("ImportExcelController")
@Api(value = "Excel导入", tags = {"Excel导入"})
@RequestMapping("/dsgc/excel")
public class ImportExcelController {

    @Autowired
    private ImportExcelService importExcelService;

    /*** 导入Excel文件  */
    @ApiOperation("导入Excel文件")
    @RequestMapping(value = "/importSvcParams", method = RequestMethod.POST)
    public Response ImportSvcParams(@RequestParam(value = "file") List<MultipartFile> files) {
        System.out.println("------导入------");
        //System.out.println(files.size());
        return Response.ok().data(importExcelService.importSvcParams(files));
    }

    /*** 导入Excel文件  */
    @ApiOperation("导入Excel文件")
    @RequestMapping(value = "/upLoadFile", method = RequestMethod.POST)
    public Response upLoadFile(@RequestParam(value = "file") List<MultipartFile> files,
                               @RequestParam(value = "uid") String uid) throws UnsupportedEncodingException {
        byte[] str = uid.getBytes("UTF-8");

        System.out.println("-uid->" + uid+" \n->"+str.toString());
        System.out.println(files.size());
        return Response.ok().data(importExcelService.upLoadFile(files));
    }
}
