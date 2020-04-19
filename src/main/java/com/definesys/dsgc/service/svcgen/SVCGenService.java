package com.definesys.dsgc.service.svcgen;

import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.svcgen.utils.ServiceGenerateProxy;
import com.definesys.dsgc.service.svcinfo.bean.SVCUriBean;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.lkv.FndLookupTypeDao;
import com.definesys.dsgc.service.users.DSGCUserService;
import com.definesys.dsgc.service.utils.FileCopyUtil;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SVCGenService {

    public Map<String,String> fileTypeMap = new HashMap<String,String>();

    public Map<String,String> fileTypeMeaningMap = new HashMap<String,String>();

    public Map<String,DSGCUser> userMap = new HashMap<String,DSGCUser>();


    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();

    @Autowired
    private DSGCUserService us;

    @Autowired
    private FndLookupTypeDao lkvDao;


    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private SVCGenDao svcGenDao;

    /**
     * 获取服务快速配置的基础信息
     *
     * @param servNo
     * @return
     */
    public BasicInfoJsonBean getSvcGenBasicInfo(String uid,String servNo) {
        BasicInfoJsonBean res = null;
        try {
            res = this.sgProxy.getSvcGenBasicInfo(servNo);
            if (res != null) {
                res.setLastUpdateBy(this.getUserName(res.getLastUpdateBy()));
                res.setReadonly(true);
                UserHelper uh = this.userHelper.user(uid);
                boolean isCanEdit = uh.isSvcGenEditorByServNo(servNo);
                if (isCanEdit) {
                    res.setReadonly(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取服务当前使用的代码文件（基于快速配置生成的代码文件）
     *
     * @param servNo
     * @return
     */
    public List<CodeFileJsonBean> getCurrentUsingSvcGenFileList(String servNo) {
        this.initFileTypeMeaningMap();
        List<CodeFileJsonBean> res = new ArrayList<CodeFileJsonBean>();
        try {
            res = this.sgProxy.getCurrentUsingSvcGenFileList(servNo);
            if (res != null) {
                Iterator<CodeFileJsonBean> iters = res.iterator();
                while (iters.hasNext()) {
                    CodeFileJsonBean cfjb = iters.next();
                    this.formatCodeFileJsonBean(cfjb);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    private void formatCodeFileJsonBean(CodeFileJsonBean cfjb) {
        //设置fileName
        if (cfjb.getFilePath() != null) {
            int i = cfjb.getFilePath().lastIndexOf("/");
            if (i != -1) {
                if (cfjb.getFileSuffix() != null) {
                    cfjb.setFileName(cfjb.getFilePath().substring(i + 1,cfjb.getFilePath().indexOf(cfjb.getFileSuffix())));
                } else {
                    cfjb.setFileName(cfjb.getFilePath().substring(i + 1));
                }
            } else {
                cfjb.setFileName(cfjb.getFilePath());
            }
        }
        //更新用户名显示
        cfjb.setLastUpdateBy(this.getUserName(cfjb.getLastUpdateBy()));

        cfjb.setFileType(this.getFileTypeBySuffix(cfjb.getFileSuffix()));

        cfjb.setFileTypeMeaning(this.getFileTypeMeaning(cfjb.getFileType()));
    }

    /**
     * 获取部署配置项信息
     *
     * @param servNo
     * @return
     */
    public List<DeployProfileJsonBean> getServDeployProfileList(String uid,String servNo) {
        List<DeployProfileJsonBean> res = new ArrayList<DeployProfileJsonBean>();
        try {

            res = this.sgProxy.getServDeployProfileList(servNo);
            if (res != null) {
                Iterator<DeployProfileJsonBean> dplIter = res.iterator();
                Map<String,String> dplEnableStatMap = lkvDao.getlookupValues("SVCGEN_DEPLOY_PROFILE_ENABLE");
                UserHelper uh = this.userHelper.user(uid);
                boolean isEditDeployProfile = uh.isSvcGenEditorByServNo(servNo);

                while (dplIter.hasNext()) {
                    DeployProfileJsonBean djb = dplIter.next();
                    djb.setIsEnableMeaning(dplEnableStatMap.get(djb.getIsEnable()));
                    djb.setLastDplUser(this.getUserName(djb.getLastDplUser()));
                    djb.setLastUpdatedBy(this.getUserName(djb.getLastUpdatedBy()));
                    djb.setReadonly(true);
                    if ("Y".equals(djb.getIsEnable()) && isEditDeployProfile) {
                        //如果没有废弃，且具备当前服务的管理权限，则可以编辑
                        djb.setReadonly(false);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * disable 服务部署配置项
     *
     * @param uid
     * @param dpId
     * @return
     */
    public int disableDeployProfileByDPId(String uid,String dpId) {
        int res = 0;
        //先判断是否有权限更新
        boolean isAuth = this.isSvcGenEditorByDPId(dpId,uid);
        if (isAuth) {
            try {
                res = this.sgProxy.disableDeployProfileByDPId(uid,dpId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //没有权限
            res = -1;
        }
        return res;
    }


    /**
     * 生成服务代码
     *
     * @param loginUser
     * @param tcb
     * @return
     * @throws Exception
     */
    public Response generateServiceCode(String loginUser,TmplConfigBean tcb) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("TO_RESOLVE_LIST",tcb.getToResolveFileList());
        paramMap.put("resolveDenpendencies",tcb.getResolveDenpendencies());
        if ("0".equals(tcb.getTmplFlag())) {
            if (tcb.getSaForWsdl() != null && tcb.getSaForWsdl().trim().length() > 0) {
                SABean sa = this.svcGenDao.getSaInfoBySaCode(tcb.getSaForWsdl());
                tcb.setWsdlUN(sa.getUn());
                tcb.setWsdlPD(sa.getPd());
            }
        }
        return this.sgProxy.generateServiceCode(loginUser,tcb,paramMap);
    }

    /**
     * 生成服务代码并保存资产信息
     *
     * @param loginUser
     * @param tcb
     * @return
     * @throws Exception
     */
    public Response generateServiceCodeAsset(String loginUser,TmplConfigBean tcb) throws Exception {
        Response genRes = this.generateServiceCode(loginUser,tcb);
//        if(Response.CODE_OK.equals(genRes.getCode())){
//            //绑定服务资产信息
//            SVCCreateBean scc = new SVCCreateBean();
//            scc.setServNo(tcb.getServAssetNo());
//            scc.setServName(tcb.getServName());
//            scc.setServShareType(tcb.getServShareType());
//            scc.setServSystem(tcb.getServSystem());
//            scc.setSgObjCode(tcb.getServNo());
//            this.bindingSgObjToNewServ(scc);
//        }
        return genRes;
    }


    /**
     * 生成服务代码
     *
     * @param loginUser
     * @param tcb
     * @return
     * @throws Exception
     */
    public List<CodeFileJsonBean> generateIDEServiceCode(String loginUser,TmplConfigBean tcb) throws Exception {
        this.initFileTypeMeaningMap();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("TO_RESOLVE_LIST",tcb.getToResolveFileList());
        paramMap.put("resolveDenpendencies",tcb.getResolveDenpendencies());
        List<CodeFileJsonBean> res = this.sgProxy.generateIDETmplCodeFiles(loginUser,tcb,paramMap);
        if (res != null) {
            Iterator<CodeFileJsonBean> iters = res.iterator();
            while (iters.hasNext()) {
                CodeFileJsonBean cfj = iters.next();
                this.formatCodeFileJsonBean(cfj);
                cfj.setLastUpdateBy(this.getUserName(loginUser));
            }
        }
        return res;
    }


    /**
     * 部署服务
     *
     * @param loginUser
     * @param req
     * @return
     * @throws Exception
     */
    public Response deployServcie(String loginUser,DeployServiceJsonBean req) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        return this.sgProxy.deployServcie(loginUser,req.getServNo(),req.getDpId(),req.getDeployDesc());

    }


    /**
     * 判断当前用户是否有权限更新当前配置项
     *
     * @param dpId
     * @param uid
     * @return
     */
    private boolean isSvcGenEditorByDPId(String dpId,String uid) {
        boolean isCanToEdit = false;
        //获取服务所属系统，判断当前用户是否有权限修改，需要服务所属系统进行判断；
        UserHelper uh = this.userHelper.user(uid);
        boolean isAdmin = (uh.isAdmin() || uh.isSuperAdministrator());
        if (isAdmin) {
            //如果是管理员，则可以编辑
            isCanToEdit = true;
        } else {
            if (uh.isSystemMaintainer()) {
                //根据dpId查询服务所属系统
//                Map<String,Object> servSysRes = this.sw.buildQuery().sql("select s.subordinate_system SYS_CODE from dsgc_svcgen_tmpl t, dsgc_svcgen_deploy_profiles d ,dsgc_services s\n" +
//                        "where d.deve_id = t.deve_id\n" +
//                        " and t.is_profile = 'Y'\n" +
//                        " and t.serv_no = s.serv_no\n" +
//                        " and d.dp_id = #dpId").setVar("dpId",dpId).doQueryFirst();

                Map<String,Object> servSysRes = this.sw.buildQuery().sql("select s.sys_code\n" +
                        "  from dsgc_svcgen_tmpl t, dsgc_svcgen_deploy_profiles d, dsgc_svcgen_obj s\n" +
                        " where d.deve_id = t.deve_id\n" +
                        "   and t.is_profile = 'Y'\n" +
                        "   and t.serv_no = s.obj_code\n" +
                        "   and d.dp_id = #dpId").setVar("dpId",dpId).doQueryFirst();
                if (servSysRes != null) {
                    String sysCode = (String)servSysRes.get("SYS_CODE");
                    if (uh.isSpecifySystemMaintainer(sysCode)) {
                        isCanToEdit = true;
                    }
                }
            }
        }
        return isCanToEdit;
    }


    private String getUserName(String uid) {
//        if (this.userMap.isEmpty()) {
//
//        }
        this.initUserMap();
        DSGCUser userInfo = this.userMap.get(uid);
        if (userInfo == null) {
            return uid;
        } else {
            return userInfo.getUserName();
        }
    }

    /**
     * 获取用户的信息Map
     *
     * @return
     */
    private void initUserMap() {
        List<DSGCUser> allUsers = this.us.findAll();
        Map<String,DSGCUser> userNameMap = new HashMap<String,DSGCUser>();

        if (allUsers != null) {
            Iterator<DSGCUser> userIter = allUsers.iterator();
            while (userIter.hasNext()) {
                DSGCUser user = userIter.next();
                userNameMap.put(user.getUserId(),user);
            }
        }
        this.userMap = userNameMap;
    }

    private String getFileTypeBySuffix(String suffix) {
        if (this.fileTypeMap.isEmpty()) {
            this.initFileTypeMap();
        }
        if (suffix == null || suffix.trim().length() == 0) {
            return "dft";
        }
        String res = this.fileTypeMap.get(suffix);
        if (res == null || res.trim().length() == 0) {
            res = "dft";
        }
        return res;
    }

    private void initFileTypeMap() {
        Map<String,String> tmpMap = new HashMap<String,String>();
        tmpMap.put(".proxy","ps");
        tmpMap.put(".bix","bs");
        tmpMap.put(".pipeline","pp");
        tmpMap.put(".wsdl","wsdl");
        tmpMap.put(".xsd","xsd");
        tmpMap.put(".xsl","xsl");
        tmpMap.put(".xqy","xqy");
        tmpMap.put(".sa","sa");
        tmpMap.put(".wadl","wadl");
        this.fileTypeMap = tmpMap;
    }

    private String getFileTypeMeaning(String fileType) {
        if (this.fileTypeMeaningMap.isEmpty()) {
            this.initFileTypeMeaningMap();
        }

        String res = this.fileTypeMeaningMap.get(fileType);
        if (res == null || res.trim().length() == 0) {
            res = this.fileTypeMeaningMap.get("dft");
        }
        return res;
    }

    private void initFileTypeMeaningMap() {
        this.fileTypeMeaningMap = this.lkvDao.getlookupValues("SVCGEN_FILE_TYPE_LKV");
    }

    public List<SvcgenProjInfoBean> queryProjectDir(String uId) {
        UserHelper uh = this.userHelper.user(uId);
        List<SvcgenProjInfoBean> list = new ArrayList<>();
        try {
            boolean isAdmin = (uh.isAdmin() || uh.isSuperAdministrator());
            if (isAdmin) {
                list = svcGenDao.queryAllProjectDir();
            } else {
                list = svcGenDao.queryProjectDirByUserId(uId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 获取项目目录下接口目录
     *
     * @param projDir
     * @return
     * @throws Exception
     */
    public List<ServDirJsonBean> queryServDirByProjDir(String projDir) throws Exception {
        return this.sgProxy.getServDirByProjDir(projDir);
//        List<ServDirJsonBean> tmp = new ArrayList<>();
//        ServDirJsonBean t = new ServDirJsonBean();
//        t.setServDirName("11111");
//        tmp.add(t);
//        ServDirJsonBean t1 = new ServDirJsonBean();
//        t1.setServDirName("22222");
//        tmp.add(t1);
//        ServDirJsonBean t2 = new ServDirJsonBean();
//        t2.setServDirName("33333");
//        tmp.add(t2);
//        return tmp;
    }

    /**
     * 获取静态静态账号密码文件
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public List<ServcieAccountBean> getServcieAccountList(String uid) throws Exception {
       return this.svcGenDao.getServcieAccountList(uid,this.userHelper.user(uid));
    }

    /**
     * 迁移过来的代码，将被废弃20191121
     *
     * @param parantFilePath
     * @return
     */
    public Map<String,Object> queryInterDirByProj(String parantFilePath) {
        List<String> List = null;
        Map<String,Object> fileMap = null;
        try {
            List<String> fileList = FileCopyUtil.getChildDir(parantFilePath);
            if (fileList.size() > 0) {
                fileMap = new HashMap<String,Object>();
                List = new ArrayList<String>();
                String strkey = null;
                String strValue = null;
                for (String ss : fileList) {
                    //截取路径，路径 : 目录名称 键值对格式
                    if (ss.contains("/")) {
                        //linux  unix java 环境下 路径
                        strkey = ss.substring(0,ss.lastIndexOf("/") + 1);
                        strValue = ss.substring(ss.lastIndexOf("/") + 1);
                    } else if (ss.contains("\\")) {
                        //win环境下
                        strkey = ss.substring(0,ss.lastIndexOf("\\") + 1);
                        strValue = ss.substring(ss.lastIndexOf("\\") + 1);
                    }
                    List.add(strValue);
                }
                fileMap.put("projPath",strkey);
                fileMap.put("projName",List);
                return fileMap;
            } else {
                return fileMap;
            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("索引的文件路径不存在，或路径格式不正确，检索文件失败！");
        }

    }

    /**
     * 获取用户有权限部署的列表
     *
     * @return
     */
    public List<DeployProfileSltBean> getUserCanDeployList(String uid,String servNo) {
        List<DeployProfileSltBean> res = new ArrayList<DeployProfileSltBean>();
        UserHelper uh = this.userHelper.user(uid);
        if (uh.isSvcGenEditorByServNo(servNo)) {
            return svcGenDao.getUserCanDeployProfiles(uid,servNo);
        }
        return res;
    }

    /**
     * 列出目录下文件和子目录
     *
     * @param dirRelativePath
     * @return
     * @throws Exception
     */
    public List<VCObjectJsonBean> listTeamRepoFiles(String dirRelativePath) throws Exception {

        Set<String> filter = this.getFileListFilter();
        return this.sgProxy.listTeamRepositoryDir(dirRelativePath,filter,true);
    }

    private Set<String> getFileListFilter() {
        Set<String> filter = new HashSet<String>();
        filter.add(".sboverview");
        filter.add(".jws");
        filter.add("pom.xml");
        filter.add(".jpr");
        filter.add(".sso");
        filter.add(".lck");
        filter.add(".sso.lck");
        filter.add("jps-config.xml");
        return filter;
    }

    /**
     * 列出目录下文件和子目录
     *
     * @param dirRelativePath
     * @return
     * @throws Exception
     */
    public List<VCObjectTreeJsonBean> listTeamRepoTreeList(String dirRelativePath,String fileFilter) throws Exception {
        boolean filterType = true;
        Set<String> filter = new HashSet<>();
        if("0".equals(fileFilter)) {
            filter = this.getFileListFilter();
        } else if("1".equals(fileFilter)){
            filter =  new HashSet<>();
            filter.add(".ptx");
            filterType = false;
        }

        List<VCObjectJsonBean> lst = this.sgProxy.listTeamRepositoryDir(dirRelativePath,filter,filterType);
        List<VCObjectTreeJsonBean> res = new ArrayList<VCObjectTreeJsonBean>();
        if (lst != null) {
            Iterator<VCObjectJsonBean> iters = lst.iterator();
            while (iters.hasNext()) {
                VCObjectJsonBean voj = iters.next();
                VCObjectTreeJsonBean tmp = new VCObjectTreeJsonBean();
                tmp.setTitle(voj.getObjName());
                tmp.setKey(voj.getRelativePath());
                if ("Y".equals(voj.getIsDir())) {
                    tmp.setIsLeaf(false);
                } else {
                    tmp.setIsLeaf(true);
                }
                int dotIdx = voj.getObjName().lastIndexOf(".");
                String suffix = null;
                if (dotIdx != -1) {
                    suffix = voj.getObjName().substring(dotIdx);
                }
                tmp.setFileType(this.getFileTypeBySuffix(suffix));
                res.add(tmp);
            }
        }
        return res;
    }

    public List<DSGCSystemEntities> queryAllSystem() {
        return svcGenDao.queryAllSystem();
    }


    /**
     * 根据dpid获取deployprofile配置信息
     *
     * @param dpId
     * @return
     */
    public IdeDeployProfileBean getIdeDeployProfileByDpId(String dpId) throws Exception {
        return this.svcGenDao.getIdeDeployProfileByDpId(dpId);
    }

    public TmplConfigBean getCommonDeployProfileByDpId(String dpId) throws Exception {
        return this.svcGenDao.getDeployProfileByDPId(dpId);
    }

    public TmplConfigBean getCommonTmplConfigByServNo(String servNo) throws Exception {
        return this.svcGenDao.getTmplConfigByServNo(servNo);
    }


    public String newOsbRestDeployProfile(String uid,RestDeployProfileBean dpl) throws Exception {
        return this.sgProxy.newOsbRestDeployProfile(
                uid,dpl.getServNo(),dpl.getDplName(),dpl.getEnvCode(),dpl.getBsUri(),dpl.getSaCode(),
                this.sgProxy.covertListToMap(dpl.getObHeaders()));
    }

    public String newOsbSoapDeployProfile(String uid,SoapDeployProfileBean dpl) throws Exception {
        return this.sgProxy.newOsbSoapDeployProfile(
                uid,dpl.getServNo(),dpl.getDplName(),dpl.getEnvCode(),dpl.getBsUri(),dpl.getSaCode(),
                this.sgProxy.covertListToMap(dpl.getObHeaders()));
    }

    public String newOsbSaCmptDeployProfile(String uid,SaCmptDeployProfileBean dpl) throws Exception {
        return this.sgProxy.newOsbSaCmptDeployProfile(
                uid,dpl.getServNo(),dpl.getDplName(),dpl.getEnvCode(),dpl.getSaUN(),dpl.getSaUN());
    }


    public Response resolveWsdl(WSDLResolveBean wsdl) throws Exception {
        String saCode = wsdl.getSaCode();
        if (saCode != null && saCode.trim().length() > 0) {
            SABean sa = this.svcGenDao.getSaInfoBySaCode(saCode);
            //根据系统编号，默认设置下一步的提供方系统
            System.out.println(sa.getUn()+"=======>"+sa.getPd());
            Response res = this.sgProxy.parseSpyWSDL(wsdl.getWsdlUri(),sa.getUn(),sa.getPd());
            if(Response.CODE_OK.equals(res.getCode())) {
                return res.setMessage(sa.getSystemCode());
            } else {
                return res;
            }
        }
        return this.sgProxy.parseSpyWSDL(wsdl.getWsdlUri(),null,null);
    }

    /**
     * 获取svc类型的sg对象
     * @param q
     * @param uid
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public PageQueryResult<SvcGenObjJsonBean> getSgSvcObjList(SvcGenObjReqBean q,String uid,int pageSize,int pageIndex){
        return this.getSgObjList(true,q,uid,pageSize,pageIndex);
    }

    /**
     * 获取非svc类型对象，包括sa tmpl misc
     * @param q
     * @param uid
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public PageQueryResult<SvcGenObjJsonBean> getSgCmptObjList(SvcGenObjReqBean q,String uid,int pageSize,int pageIndex){
        return this.getSgObjList(false,q,uid,pageSize,pageIndex);
    }

    private PageQueryResult<SvcGenObjJsonBean> getSgObjList(boolean isSvcType,SvcGenObjReqBean q,String uid,int pageSize,int pageIndex){
        UserHelper uh = this.userHelper.user(uid);
        PageQueryResult<SvcGenObjJsonBean> resPageQ = this.svcGenDao.getSgObjList(isSvcType,q,uid,uh,pageSize,pageIndex);
        List<SvcGenObjJsonBean> resLst = resPageQ.getResult();
        if(resLst != null){
            Iterator<SvcGenObjJsonBean> resIter = resLst.iterator();
            while (resIter.hasNext()){
                SvcGenObjJsonBean ob = resIter.next();
                this.initReadOnlyForSvcGenObjBean(ob,uid);
            }
        }
        return resPageQ;
    }

    public SvcGenObjJsonBean getSvcGenObjInfo(String sgObjCode,String uid) {
        UserHelper uh = this.userHelper.user(uid);
        return this.getSvcGenObjInfo(sgObjCode,uid,uh);
    }

    public SvcGenObjJsonBean getSvcGenObjInfo(String sgObjCode,String uid,UserHelper uh) {
        SvcGenObjJsonBean res = this.svcGenDao.getSvcGenObjInfo(sgObjCode,uid,uh);
        if(res != null){
            this.initReadOnlyForSvcGenObjBean(res,uid);
        }
        return res;
    }

    public Response deployServMetaDataByServNo(String loginUser,String servNo) throws Exception{
       return  this.sgProxy.deployServMetaDataByServNo(loginUser,servNo);
    }

    private void initReadOnlyForSvcGenObjBean(SvcGenObjJsonBean ob,String uid){
        UserHelper uh = this.userHelper.user(uid);
        ob.setReadonly(true);
        if(uid != null &&  ob !=null){
            if(uh.isAdmin() || uh.isSuperAdministrator()){
                ob.setReadonly(false);
            }
            else {
                if ("Y".equals(ob.getEnabled())) {
                    if (ob.getServNo() == null) {
                        //如果还没有服务资产化，则应该为系统负责人且是资源创建人，或者更新人时可以编辑
                        if (uh.isSystemMaintainer() && (uid.equals(ob.getLastUpdatedBy())
                                || uid.equals(ob.getCreatedBy()) || uh.isSpecifySystemMaintainer(ob.getSysCode()))) {
                            ob.setReadonly(false);
                        }

                    } else {
                        if (uh.isSpecifySystemMaintainer(ob.getServSystem())) {
                            ob.setReadonly(false);
                        }
                    }
                } else{
                    //已经被废弃了，无法操作权限
                    ob.setReadonly(true);
                }
            }
        }
    }

    /**
     * 将生成的服务资源绑定到对应的服务资产上
     * @param scc
     * @return
     * @throws Exception
     */
    public int bindingSgObjToNewServ(SVCCreateBean scc) throws Exception {
        List<SVCUriBean> uriList = this.sgProxy.resolveServIBUriList(scc.getSgObjCode());
        return this.svcGenDao.bindingSgObjToNewServ(scc,uriList);
    }

    public int deleteSvcGenObj(String uid,String sgObjCode) throws Exception{
        UserHelper uh = this.userHelper.user(uid);
        SvcGenObjJsonBean objJsonBean = this.getSvcGenObjInfo(sgObjCode,uid,uh);
        return this.svcGenDao.deleteSvcGenObj(objJsonBean,uid,uh);
    }


    public List<SVCUriBean> resolveServIBUriList(String sgObjCode) throws Exception{
        return this.sgProxy.resolveServIBUriList(sgObjCode);
    }

}
