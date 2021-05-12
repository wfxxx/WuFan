package com.definesys.dsgc.service.ystar.excel;

import com.definesys.dsgc.service.svcinfo.SVCInfoDao;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoBean;
import com.definesys.dsgc.service.svcinfo.bean.SVCUriBean;
import com.definesys.dsgc.service.svcmng.SVCMngDao;
import com.definesys.dsgc.service.svcmng.bean.DSGCPayloadSampleBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.excel.bean.SvcPayloadParam;
import com.definesys.dsgc.service.ystar.excel.util.PayloadUtil;
import com.definesys.mpaas.common.http.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service("ImportExcelService")
public class ImportExcelService extends ImportExcelBaseService {

    @Autowired
    private PayloadParamDao paramDao;
    @Autowired
    private SVCInfoDao svcInfoDao;

    private static final String REQ_PARAM_FLAG = "请求参数";
    private static final String RES_PARAM_FLAG = "返回参数";
    private static final String PARAM_NODE_NAME = "参数编码";
    private static final String PARAM_NODE_VALUE = "参数名称";
    private static final String PARAM_NODE_TYPE = "字符类型";
    private static final String PARAM_NODE_REQUIRED = "是否必填";
    private static final String PARAM_NODE_PARENT_NAME = "父节点名称";
    private static final String PARAM_NODE_DESC = "备注";

    public Response upLoadFile(List<MultipartFile> files) {
        int rowNum = 0;//已取值的行数

        return Response.ok().data(files.size()).setMessage("上传文件数：" + files.size());
    }

    public Response importSvcParams(List<MultipartFile> files) {
        int rowNum = 0;//已取值的行数
        String curSheetName = null;
        String curSvcCode = null;

        for (MultipartFile file : files) {
            int realRowCount = 0;//真正有数据的行数
            //得到工作空间
            Workbook workbook = null;
            try {
                workbook = super.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
                int sheetCount = super.getSheetCount(workbook);
                //获取当前文件得Sheet页
                if (sheetCount > 0) {
                    //遍历找到第一个Sheet页
                    for (int i = 0; i < sheetCount; i++) {
                        //得到工作表
                        Sheet sheet = super.getSheetByWorkbook(workbook, i);
                        curSheetName = sheet.getSheetName();
                        if (sheet.getRow(2000) != null) {
                            throw new RuntimeException("系统已限制单批次导入必须小于或等于2000");
                        }
                        //记录是否已经遍历完请求参数,以及响应参数是否已开始遍历
                        boolean findReq = false;
                        boolean findRes = false;
                        String svcType = null;//服务类型：REST/SOAP
                        realRowCount = sheet.getPhysicalNumberOfRows();
                        List<SvcPayloadParam> list = new ArrayList<>();
                        for (Row row : sheet) {
                            boolean isTitle = false;
                            int curRowNum = row.getRowNum();
                            //实际有值行数与已遍历行数
                            if (realRowCount == rowNum) {
                                break;
                            }
                            //遍历行
                            if (curRowNum == 0) {//第一行，拿服务资产编号
                                curSvcCode = row.getCell(1).getStringCellValue();
                                if (StringUtil.isBlank(curSvcCode)) {
                                    return Response.error("导入失败，在Sheet页[" + curSheetName + "]的服务接口编码为空！");
                                } else if ((svcType = this.paramDao.querySvcUriBySvcCode(curSvcCode).getUriType()) == null) {
                                    return Response.error("导入失败，在Sheet页[" + curSheetName + "]中，服务编码[" + curSvcCode + "]不存在！");
                                }
                                System.out.println(" 0 -> " + curSvcCode);
                                continue;
                            } else if (curRowNum >= 3) {//从第四行开始遍历实际数据
                                boolean isRowNull = true;
                                String nullValue = null;
                                for (int j = 0; j < 6; j++) {
                                    nullValue = null;
                                    Cell celObj = row.getCell(j);
                                    String celValue = (celObj != null && celObj.getStringCellValue() != null) ? celObj.getStringCellValue().trim() : null;
                                    if (!findReq && !findRes) {//请求参数开始
                                        findReq = true;
                                    }
                                    if (StringUtil.isBlank(celValue)) {//列为空
                                        if (nullValue == null) {//首次遇见为空列
                                            if (findReq || findRes) {
                                                if (j == 0) {
                                                    nullValue = PARAM_NODE_NAME;
                                                } else if (j == 1) {
                                                    nullValue = PARAM_NODE_VALUE;
                                                } else if (j == 2) {
                                                    nullValue = PARAM_NODE_TYPE;
                                                }
                                                if (findReq && j == 3) {
                                                    nullValue = PARAM_NODE_REQUIRED;
                                                }
                                            }
                                        }
                                    } else {
                                        isRowNull = false;
                                        if (j == 0) {
                                            if (celValue.contains(RES_PARAM_FLAG)) { //返回参数
                                                findReq = true;
                                                findRes = false;
                                                isTitle = true;
                                                break;
                                            }
                                            if (celValue.contains(PARAM_NODE_NAME)) {// 参数编码
                                                isTitle = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (isRowNull) {//判断是否为空行
                                    if (findReq) {
                                        findReq = true;
                                        findRes = false;
                                    } else if (findRes) {
                                        return Response.ok().data("已成功导入" + files.size() + "个文件，共" + rowNum + "条数据！");
                                    }
                                } else if (nullValue != null) { //不为空行，但出现必填字段为空现象，报错
                                    return Response.error("导入失败，请求参数字段[" + nullValue + "]不能为空！");
                                } else if (isTitle) {
                                    continue;
                                } else {//无空行，且无必填字段为空现象，获取实际数据写入
                                    String cel0 = row.getCell(0).getStringCellValue().trim();
                                    String cel1 = row.getCell(1).getStringCellValue().trim();
                                    String cel2 = row.getCell(2).getStringCellValue().trim();
                                    Cell cel3 = row.getCell(3);
                                    Cell cel4 = row.getCell(4);
                                    Cell cel5 = row.getCell(5);
                                    SvcPayloadParam paramsBean = null;
                                    String desc = null;
                                    if (findReq) {
                                        paramsBean = new SvcPayloadParam(curSvcCode, "REQ");
                                        paramsBean.setParamNeed("是".equalsIgnoreCase(row.getCell(3).getStringCellValue().trim()) ? "Y" : "N");
                                        if (cel5 != null) {
                                            desc = cel5.getStringCellValue().trim();
                                        }
                                    } else if (findRes) {
                                        paramsBean = new SvcPayloadParam(curSvcCode, "RES");
                                        if (cel4 != null) {
                                            desc = cel4.getStringCellValue().trim();
                                        }
                                    }
                                    paramsBean.setParamDesc(desc);
                                    paramsBean.setParamCode(cel0);
                                    paramsBean.setParamName(cel1);
                                    paramsBean.setParamType(cel2);
                                    paramsBean.setParamSample("example" + rowNum);
                                    System.out.println(paramsBean.toString());
                                    list.add(paramsBean);
                                    rowNum++;
                                }
                            } else { // 2、3行直接放行
                                continue;
                            }
                        }
                        this.saveSvcPayloadParam(list, curSvcCode, svcType);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                String errStr = "文件导入失败!";
                if (curSvcCode != null) {
                    errStr = "在Sheet页[" + curSheetName + "]中，服务接口[" + curSvcCode + "]导入失败！";
                }
                return Response.error(errStr);
            }
            //return Response.ok().data("已成功导入" + rowNum + "条数据！");
        }
        return Response.ok().data("已成功导入" + files.size() + "个文件，共" + rowNum + "条数据！");
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSvcPayloadParam(List<SvcPayloadParam> list, String svcCode, String svcType) {
        //先删除参数
        this.paramDao.removePayloadParam(svcCode);
        //添加参数
        for (SvcPayloadParam svcPayloadParam : list) {
            this.paramDao.savePayloadParam(svcPayloadParam);
        }
        //添加报文示例
        DSGCPayloadSampleBean[] payloadSamples = PayloadUtil.getPayloadSample(list, svcCode, svcType);
        //先删除示例
        this.paramDao.removePayloadSample(svcCode);
        //再添加示例
        this.paramDao.savePayloadSample(payloadSamples[0]);
        this.paramDao.savePayloadSample(payloadSamples[1]);
    }

}