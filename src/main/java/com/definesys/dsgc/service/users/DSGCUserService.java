package com.definesys.dsgc.service.users;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apilr.ApiLrDao;
import com.definesys.dsgc.service.apilr.bean.DagLrbean;
import com.definesys.dsgc.service.apiroute.ApiRouteDao;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.dsgc.service.apps.AppsDao;
import com.definesys.dsgc.service.market.MarketDao;
import com.definesys.dsgc.service.market.bean.DSGCApisBean;
import com.definesys.dsgc.service.svcmng.SVCMngDao;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.system.DSGCSystemDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.users.bean.*;
//import com.definesys.dsgc.app.svcmng.DSGCServiceDao;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("userService")
public class DSGCUserService {

    @Autowired
    private SWordLogger logger;

    @Autowired
    DSGCUserDao userDao;

    @Autowired
    private SVCMngDao svcMngDao;

    @Autowired
    private DSGCSystemDao systemDao;

    @Autowired
    private AppsDao appsDao;

    @Autowired
    private ApiRouteDao apiRouteDao;

    @Autowired
    private MarketDao marketDao;

    @Autowired
    private ApiLrDao apiLrDao;


//    @Autowired
//    DSGCServiceDao dsgc_service;

    public DSGCUser login(DSGCUser user) {
        DSGCUser result=this.userDao.login(user);
        if(result.getUserRole().equals("SystemLeader")){
            List<Map<String,Object>> appList=this.userDao.findAppCodeByUser(user.getUserName());
            List<String> appCodeList=new ArrayList<String>();
            for(Map<String,Object> item :appList){
                appCodeList.add((String) item.get("SYSCODE"));
            }
            result.setAppCode(appCodeList);
        }
        return result;
    }



    @Transactional(rollbackFor = Exception.class)
    public String createUser(DSGCUser user, List<DSGCServiceUser> dsgcServiceUsers) {
        //创建新用户时，如果用户已存在，则终止新增操作，并返回错误日志
        boolean ckUser = this.userDao.checkUserNameIsExistByName(user);
        if(ckUser){
            logger.error("%s","用户已存在，不能重复创建！");
            throw new MpaasBusinessException("用户已存在，不能重复创建！");
        }

        String id = this.userDao.createUser(user);
        user = this.userDao.findUserByUserName(user.getUserName());
        String userId = user.getUserId();
        if (dsgcServiceUsers != null && dsgcServiceUsers.size() > 0) {
            for (int i = 0; i < dsgcServiceUsers.size(); i++) {
                DSGCServiceUser serviceUser = dsgcServiceUsers.get(i);
                serviceUser.setUserId(userId);
//                this.dsgc_service.saveServiceUser(serviceUser);
            }
        }
        return userId;
    }

    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return
     */
    public DSGCUser findUserByUserName(String userName){
        return this.userDao.findUserByUserName(userName);
    }

    public boolean checkUserName(String userName) {
        return this.userDao.findUserByUserName(userName) == null;
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateUser(DSGCUser user, List<DSGCServiceUser> dsgcServiceUsers) {
        //创建新用户时，如果用户已存在，则终止新增操作，并返回错误日志
        boolean ckUser = this.userDao.checkUserNameIsExistByName(user);
        if(ckUser){
            logger.error("%s","用户已存在，不能重复创建！");
            throw new MpaasBusinessException("用户已存在，不能重复创建！");
        }

//        this.dsgc_service.deleteServiceUser(user.getUserId());
        if (dsgcServiceUsers != null && dsgcServiceUsers.size() > 0) {
            for (int i = 0; i < dsgcServiceUsers.size(); i++) {
//                this.dsgc_service.saveServiceUser(dsgcServiceUsers.get(i));
            }
        }
        return this.userDao.updateUser(user);
    }


    @Transactional
    public String changePwd(JSONObject reqJson,String uid) {
        String account = reqJson.getString("userName");
        String oldPwd = reqJson.getString("oldPwd");
        String newPwd = reqJson.getString("newPwd");

        //校验修改的账号是否存在
        DSGCUser dsgcUser = userDao.checkUserName(uid,account);
        if(null == dsgcUser){
            throw new MpaasBusinessException("账号不存在！");
        }

        if("Y".equals(dsgcUser.getIsLocked())){
            throw new MpaasBusinessException("账号已被锁定，无法修改密码！");
        }

        //校验修改的原密码是否与数据库存储密码相同
        if(! oldPwd.equals(dsgcUser.getUserPassword())){
            throw new MpaasBusinessException("原密码输入不正确，请重试！");
        }
        //执行密码更新操作
        dsgcUser.setUserPassword(newPwd);
        return this.userDao.changePwd(dsgcUser);
    }

    @Transactional
    public String deleteUserById(DSGCUser user) {
        return this.userDao.deleteUserById(user);
    }

    public PageQueryResult<DSGCUser> query(DSGCUser user, int pageSize, int pageIndex) {
        return this.userDao.query(user, pageSize, pageIndex);
    }
//    public DSGCUser findUserByUserName(String userName) {
//        return this.user.findUserByUserName(userName);
//    }

    public JSONObject findUserById(DSGCUser user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo", this.userDao.findUserById(user));
        jsonObject.put("serviceInfo", this.userDao.findServiceUserByUserId(user.getUserId()));
        return jsonObject;
    }

    public DSGCUser findUserById(String userid) {
        return this.userDao.findUserById(userid);
    }

    public List<DSGCUser> findAll() {
        return this.userDao.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserIslock(String userName) {
//        DSGCUser user = dsgc_service.queryUserByUserName(userName);
//        if(null == user){
//            throw new MpaasBusinessException("该账户不存在，锁定账户失败！");
//        }
//        user.setIsLocked("Y");
//        dsgc_service.updateUserIslock(user);
    }

    /**
     * 根据用户名查询用户基本信息
     * @param dsgcUser
     * @return
     */
    public DSGCUser findByUserName(DSGCUser dsgcUser) {
        return this.userDao.findByUserName(dsgcUser);
    }

    /**
     * 根据用户信息，生成token
     * @param user
     * @return
     */
    public String getToken(DSGCUser user) {
        Date start = new Date();
        //一小时有效时间
        long currentTime = System.currentTimeMillis() + 12*60* 60 * 1000;
        Date end = new Date(currentTime);
        String token = "";

//        token = JWT.create()
//                .withAudience(user.getUserId())
//                .withIssuedAt(start)
//                .withExpiresAt(end)
//                .sign(Algorithm.HMAC256(user.getUserPassword()));
        return token;
    }


    public DSGCUser findUserByUserId(String userId) {
        return this.userDao.findUserByUserId(userId);
    }

    public List<DSGCUser> queryAllSysUser() {
        return userDao.queryAllSysUser();
    }

    public boolean insertPassword(DSGCUserKs dsgcUserKs){
        return userDao.insertPassword(dsgcUserKs);
    }

    public List<DSGCUserKs> getKsPassword(String userId){
        return userDao.getKsPassword(userId);
    }

    public boolean updateKsPassword(DSGCUserKs dsgcUserKs){
        return userDao.updateKsPassword(dsgcUserKs);
    }

    public void deleteKsPassword(DSGCUserKs dsgcUserKs){  userDao.deleteKsPassword(dsgcUserKs); }

    public List<Map<String, Object>> getEnvCode(){
        return userDao.getEnvCode();
    }

    public Map<String,List>  findAllService(DSGCServiceQueryVO service) {
//        PageQueryResult<DSGCService> pageQueryResult = this.dsgcServiceDao.findAllService(service);
//        List<DSGCService> list =pageQueryResult.getResult();
//        List<List> tempList = new ArrayList<List>();
//        for (int i = 0;i<list.size();i++) {
//            String servNo = list.get(i).getServNo();
//            List<Map<String, Object>> tabs = this.dsgcServiceDao.queryTab(servNo);
//            tempList.add(tabs);
//        }
//        return pageQueryResult;



        PageQueryResult<DSGCService> pageQueryResult = this.userDao.findAllService(service);
        Long count = pageQueryResult.getCount();
        List<Long> countList = new ArrayList<Long>();
        countList.add(count);
        List<DSGCService> list =pageQueryResult.getResult();
        List<List> tempList = new ArrayList<List>();
        for (int i = 0;i<list.size();i++) {
            String servNo = list.get(i).getServNo();
            List<Map<String, Object>> tabs = this.userDao.queryTab(servNo);
            tempList.add(tabs);
//            System.out.println(tabs);
//            String temp = tabs.toString();
//            temp = temp.replace("=",":");
//            list.get(i).setAttribue1(temp);
        }
        Map<String,List> map = new HashMap<String,List>();
        map.put("servers",list);
        map.put("tags",tempList);
        map.put("count",countList);
        return map;
    }

    public Boolean checkSystemLeaderAllowAccess(CheckSysLeaderRoleVO param){
        Boolean result = false;
        if(StringUtil.isBlank(param.getUserId())){
            return result;
        }
        if(StringUtil.isNotBlank(param.getServNo())){
            DSGCService service= svcMngDao.queryServByServNo(param.getServNo());
            if(service == null){
                return false;
            }
            List<DSGCSystemUser> systemUserList = systemDao.findSystemUserByUserId(param.getUserId());
            String appCode = service.getSubordinateSystem();
            for (int i = 0; i < systemUserList.size(); i++) {
                if (appCode.equals(systemUserList.get(i).getSysCode())){
                    result = true;
                    break;
                }
            }
        }else if(StringUtil.isNotBlank(param.getApiCode())){
            DSGCApisBean dsgcApis = marketDao.queryApiByApiCode(param.getApiCode());
            if(dsgcApis == null){
                return false;
            }
            List<DSGCSystemUser> systemUserList = systemDao.findSystemUserByUserId(param.getUserId());
            String appCode = dsgcApis.getAppCode();
            for (int i = 0; i < systemUserList.size(); i++) {
                if (appCode.equals(systemUserList.get(i).getSysCode())){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public Boolean checkAppdetailEdit(CheckAppDetailEditVO param){
        List<DSGCSystemUser> systemUserList = systemDao.findSystemUserByUserId(param.getUserId());
        DSGCSystemEntities dsgcSystemEntities = appsDao.querySystemEnt(param.getAppId());
        String appCode = dsgcSystemEntities.getSysCode();
        Boolean isEdit = false;
        if(StringUtil.isBlank(appCode)){
            return isEdit;
        }
        for (int i = 0; i <systemUserList.size() ; i++) {
            if (appCode.equals(systemUserList.get(i).getSysCode())){
                isEdit = true;
                break;
            }
        }
        return isEdit;
    }
    public Boolean checApikRouteDetailAllowAccess(CheckApiRouteDetailVO param){
        List<DSGCSystemUser> systemUserList = systemDao.findSystemUserByUserId(param.getUserId());
        DagRoutesBean dagRoutesBean = apiRouteDao.queryRouteDetail(param.getRouteCode());
        String appCode = dagRoutesBean.getAppCode();
        Boolean isEdit = false;
        if(StringUtil.isBlank(appCode)){
            return isEdit;
        }
        for (int i = 0; i <systemUserList.size() ; i++) {
            if (appCode.equals(systemUserList.get(i).getSysCode())){
                isEdit = true;
                break;
            }
        }
        return isEdit;
    }
    public Boolean checkApiLrDetailAllowAccess(CheckApiRouteDetailVO param){
        List<DSGCSystemUser> systemUserList = systemDao.findSystemUserByUserId(param.getUserId());
        DagLrbean dagLrbean = apiLrDao.queryLrDetail(param.getRouteCode());
        String appCode = dagLrbean.getAppCode();
        Boolean isEdit = false;
        if(StringUtil.isBlank(appCode)){
            return isEdit;
        }
        for (int i = 0; i <systemUserList.size() ; i++) {
            if (appCode.equals(systemUserList.get(i).getSysCode())){
                isEdit = true;
                break;
            }
        }
        return isEdit;
    }

}
