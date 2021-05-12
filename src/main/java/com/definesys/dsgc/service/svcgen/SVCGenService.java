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
import com.definesys.dsgc.service.ystar.svcgen.bean.WSDLObjectBean;
import com.definesys.dsgc.service.ystar.utils.WSDLUtils;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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


    private String getUserName(String uid) {
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

    public int deleteSvcGenObj(String uid,String sgObjCode) throws Exception{
        UserHelper uh = this.userHelper.user(uid);
        SvcGenObjJsonBean objJsonBean = this.getSvcGenObjInfo(sgObjCode,uid,uh);
        return this.svcGenDao.deleteSvcGenObj(objJsonBean,uid,uh);
    }

}
