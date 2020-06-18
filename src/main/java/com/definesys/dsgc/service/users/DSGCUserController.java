package com.definesys.dsgc.service.users;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.users.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.adapter.UserProfile;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.definesys.mpaas.query.session.MpaasSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/dsgc/user")
@RestController
@Api(description = "用户模块接口", tags = "系统模块")
//@AuthAspect(menuCode = "user",menuName = "用户管理")
public class DSGCUserController {

    @Autowired
    DSGCUserService userService;

    @Autowired
    private SWordLogger logger;

    @ApiOperation(value = "测试接口", notes = "用于刚创建环境，验证环境信息")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Response test(String code) {
        return Response.ok().data(code);
    }

    @ApiOperation(value = "用户登陆接口", notes = "用户登陆接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestBody DSGCUser u) {
        u = this.userService.login(u);
        return Response.ok().data(u);
    }

    /**
     * 新登录接口
     * @param user
     * @return
     */
    @ApiOperation(value = "登陆", notes = "登陆")
    @RequestMapping(value = "/login2" ,method = RequestMethod.POST)
    public Response login2(@RequestBody DSGCUser user) {
        if(null == user
                || StringUtil.isBlank(user.getUserName())
                || StringUtil.isBlank(user.getUserPassword())){
            logger.info("%s","账号或密码不能为空！");
            return Response.error("账号或密码不能为空！").setMessage("账号或密码不能为空！");
        }

        DSGCUser userForBase = userService.findByUserName(user);

        if(null == userForBase){
            return Response.error("账号输入错误！").setMessage("账号输入错误！");
        }else{
            if (!userForBase.getUserPassword().equals(user.getUserPassword())) {
                return Response.error("密码输入错误！").setMessage("密码输入错误！").setCode("userError");
            }else if("Y".equals(userForBase.getIsLocked())){
                return Response.error("账号已被锁定！").setMessage("账号已被锁定，请联系管理员！");
            }else {
                String token = userService.getToken(userForBase);
                userForBase.setUserPassword(null);
                Map<String,Object> retMap = new HashMap<>();
                retMap.put("token",token);
                retMap.put("userInfo",userForBase);
//                Cookie cookie = new Cookie("token", token);
//                response.addCookie(cookie);

                return Response.ok().setMessage("登录成功！").data(retMap);

            }
        }

    }


    @ApiOperation(value = "修改密码", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
    })
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public Response changePwd(@RequestBody String updJson,HttpServletRequest req) {
        //json 包含参数：账号，旧密码，新密码
        JSONObject reqJson = JSONObject.parseObject(updJson);
        //获取当前登录用户id
        String uid = req.getHeader("uid");
        //判断当前用户是否存在
        String res = null;
        try{
            res = this.userService.changePwd(reqJson,uid);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("修改密码出错！").setMessage(ex.getMessage());
        }

        return Response.ok().data(res).setMessage("密码修改成功！");
    }

//    @ApiOperation(value = "创建用户", notes = "创建用户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
//    })
//    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
//    public Response createUser(@RequestBody DSGCUser u) {
//        String uid = this.userService.createUser(u);
//        this.logger.debug("uid " + uid);
//        return Response.ok().data(uid);
//    }

//    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
//    })
//    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
//    public Response updateUser(@RequestBody DSGCUser u) {
//        String uid = this.userService.updateUser(u);
//        this.logger.debug("uid " + uid);
//        return Response.ok().data(uid);
//    }

//    @OperationAspect(value = "用户管理--删除用户信息")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
    })
    @RequestMapping(value = "/deleteUserById", method = RequestMethod.POST)
    public Response deleteUserById(@RequestBody DSGCUser u) {
        String uid = this.userService.deleteUserById(u);
        this.logger.debug("uid " + uid);
        return Response.ok().data(uid);
    }

//    @OperationAspect(value = "用户管理--新增或修改用户信息")
    @ApiOperation(value = "编辑用户信息", notes = "根据用户userId来判断，有就修改，没有就新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
    })
    @RequestMapping(value = "/modifyUser", method = RequestMethod.POST)
    public Response modifyUser(@RequestBody String body) {
        String uid = "";
        JSONObject jo = JSONObject.parseObject(body);
        DSGCUser u = new DSGCUser();
        u.setUserId(jo.getString("userId"));
        u.setUserName(jo.getString("userName"));
        u.setUserPassword(jo.getString("userPassword"));
        u.setIsAdmin(jo.getString("isAdmin"));
        u.setUserMail(jo.getString("userMail"));
        u.setUserDescription(jo.getString("userDescription"));
        u.setUserPhone(jo.getString("userPhone"));
        u.setUserDept(jo.getString("userDept"));
        u.setUserRole(jo.getString("userRole"));
        u.setIsLocked(jo.getString("isLocked"));
        u.setWorkNumber(jo.getString("workNumber"));
        List<DSGCServiceUser> dsgcServiceUsers = new ArrayList<>();
        JSONArray js = jo.getJSONArray("services");
        if(js!=null && js.size()>0){
            for (int i = 0; i <js.size() ; i++) {
                JSONObject jsonObject = js.getJSONObject(i);
                DSGCServiceUser serviceUser = new DSGCServiceUser();
                serviceUser.setUserId(jo.getString("userId"));
                serviceUser.setUserName(jo.getString("userName"));
                serviceUser.setServNo(jsonObject.getString("servNo"));
                serviceUser.setIsShow(jsonObject.getString("isShow"));
                serviceUser.setIsModify(jsonObject.getString("isModify"));
                dsgcServiceUsers.add(serviceUser);
            }

        }
        try{
            if (u.getUserId() == null || u.getUserId().trim().length() == 0) {
                uid = this.userService.createUser(u,dsgcServiceUsers);
            } else {
                uid = this.userService.updateUser(u,dsgcServiceUsers);
            }
            this.logger.debug("uid " + uid);
            return Response.ok().data(uid).setMessage("保存用户信息成功！");
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存用户信息失败！").setMessage(ex.getMessage());
        }

    }

    @ApiOperation(value = "查询用户信息", notes = "根据用户信息，查询符合条件的用户对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int")
    })
    @RequestMapping(value = "/query", method = {RequestMethod.POST,RequestMethod.GET})
    public Response query(HttpServletRequest request,
                          @RequestBody DSGCUser u,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageQueryResult<DSGCUser> users = this.userService.query(u, pageSize, pageIndex);
        this.logger.debug("getResult " + users.getResult());
//获取当前用户
        String currentUser = MpaasSession.getCurrentUser();
//获取当前用户信息
        UserProfile userProfile = MpaasSession.getUserProfile();

        this.logger.debug("currentUser " + currentUser);
        this.logger.debug("isAdmin " +request.getHeader("isAdmin"));
        return Response.ok().data(users);
    }

    @ApiOperation(value = "查找用户信息", notes = "根据用户userId查找用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "u", value = "用户对象", dataType = "DSGCUser")
    })
    @RequestMapping(value = "/findUserById", method = RequestMethod.POST)
    public Response findUserById(@RequestBody DSGCUser u) {
        return Response.ok().data(this.userService.findUserById(u));
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Response findAll() {
        List<DSGCUser> users = this.userService.findAll();
        return Response.ok().data(users);
    }

    @RequestMapping(value = "/checkUserName", method = RequestMethod.GET)
    public Response checkUserName(String userName) {
        return Response.ok().data( this.userService.checkUserName(userName));
    }

    @RequestMapping(value = "/findUserByName", method = RequestMethod.POST)
    public Response findUserByUserName(@RequestBody DSGCUser user){
        if (user.getUserName() != null) {
            return Response.ok().data(this.userService.findUserByUserName(user.getUserName()));
        }
        return Response.error("用户名为空");
    }

//    @OperationAspect(value = "用户管理--用户登录失败5次，执行账户锁定")
    @RequestMapping(value="/updateUserToLock",method = RequestMethod.POST)
    public Response updateUserToLock(@RequestBody String user){
        JSONObject object =  JSONObject.parseObject(user);
        String userName = object.getString("userName");
        try{
            userService.updateUserIslock(userName);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("锁定账户出错！").setMessage(ex.getMessage());
        }

        return Response.ok().setMessage("锁定账户成功！");
    }

    /**
     * 系统管理--添加系统负责人选人接口
     * @return
     */
    @RequestMapping(value="/queryAllSysUser",method = RequestMethod.GET)
    public Response queryAllSysUser(){
        List<DSGCUser> sysUsers = userService.queryAllSysUser();
        return Response.ok().data(sysUsers);
    }

    /**用户管理 插入用户口令
     *
     * @param dsgcUserKs
     * @return
     */
    @RequestMapping(value = "/insertPassword",method = RequestMethod.POST)
    public Response insertPassword(@RequestBody DSGCUserKs dsgcUserKs){
        this.logger.debug(" dsgcUserKs : " + dsgcUserKs);
        if(this.userService.insertPassword(dsgcUserKs)){
            return Response.ok();
        }else {
            return Response.error("该口令已存在");
        }
    }

    /**
     * 查询用户口令
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getKsPassword",method = RequestMethod.GET)
    public Response getKsPassword(@RequestParam("userId") String userId){
        this.logger.debug(" userId : " + userId);
        Map<String, Object> map = new HashMap<String, Object>();
        List<DSGCUserKs> dsgcUserKsList = userService.getKsPassword(userId);
        List<Map<String, Object>> envCodeList = userService.getEnvCode();
        map.put("KsList",dsgcUserKsList);
        map.put("envCodeList",envCodeList);
        return Response.ok().data(map);
    }

    /**
     * 修改用户口令
     * @param dsgcUserKs
     * @return
     */
    @RequestMapping(value = "/updateKsPassword",method = RequestMethod.POST)
    public Response updateKsPassword(@RequestBody DSGCUserKs dsgcUserKs){
        this.logger.debug("dsgcUserKs :" + dsgcUserKs);
        if(this.userService.updateKsPassword(dsgcUserKs)){
            return Response.ok();
        }else {
            return Response.error("该口令已存在");
        }
    }

    /**
     * 删除用户口令
     * @param dsgcUserKs
     * @return
     */
    @RequestMapping(value = "/deleteKsPassword",method = RequestMethod.POST)
    public Response deleteKsPassword(@RequestBody DSGCUserKs dsgcUserKs){
        this.logger.debug("dsgcUserKs :" + dsgcUserKs);
        this.userService.deleteKsPassword(dsgcUserKs);
        return Response.ok();
    }

    @RequestMapping(value = "/findAllService", method = RequestMethod.POST)
    public Response findAllService(@RequestBody DSGCServiceQueryVO service,HttpServletRequest request) {
//        PageQueryResult<DSGCService> pageRes = dsgcServiceService.findAllService(service);
//        Map<String,Object> resMap = new HashMap<>();
//        if(pageRes.getCount() < 1){
//            resMap.put("count",0);
//            resMap.put("servers",null);
//        }else {
//            resMap.put("count",pageRes.getCount());
//            resMap.put("servers",pageRes.getResult());
//        }
//        return Response.ok().data(resMap);
//        String uid = request.getHeader("uid");
//        String userRole = request.getHeader("userRole");
        Map<String,List> map = userService.findAllService(service);
        return Response.ok().data(map);
    }
    @RequestMapping(value = "/checkSystemLeaderAllowAccess", method = RequestMethod.POST)
    public Response checkSystemLeaderAllowAccess(@RequestBody CheckSysLeaderRoleVO param, HttpServletRequest request) {
        try {
            return Response.ok().setData( userService.checkSystemLeaderAllowAccess(param));
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok().setData(false);
        }

    }
    @RequestMapping(value = "/checkAppdetailEdit", method = RequestMethod.POST)
    public Response checkAppdetailEdit(@RequestBody CheckAppDetailEditVO param, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        if(!userId.equals(param.getUserId())){
            return Response.error("非法请求");
        }
        try {
            return Response.ok().setData( userService.checkAppdetailEdit(param));
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok().setData(false);
        }

    }
    @RequestMapping(value = "/checApikRouteDetailAllowAccess", method = RequestMethod.POST)
    public Response checApikRouteDetailAllowAccess(@RequestBody CheckApiRouteDetailVO param, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        if(!userId.equals(param.getUserId())){
            return Response.error("非法请求");
        }
        try {
            return Response.ok().setData( userService.checApikRouteDetailAllowAccess(param));
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok().setData(false);
        }

    }
    @RequestMapping(value = "/checkApiLrDetailAllowAccess", method = RequestMethod.POST)
    public Response checkApiLrDetailAllowAccess(@RequestBody CheckApiRouteDetailVO param, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        if(!userId.equals(param.getUserId())){
            return Response.error("非法请求");
        }
        try {
            return Response.ok().setData( userService.checkApiLrDetailAllowAccess(param));
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok().setData(false);
        }

    }

    @RequestMapping(value = "/findUserNtyCfg", method = RequestMethod.POST)
    public Response findUserNtyCfg(@RequestBody DSGCUserNtyCfgBean userNtyCfg) {
        try {
            return Response.ok().setData( userService.findUserNtyCfg(userNtyCfg));
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok().setData(false);
        }
    }

    @RequestMapping(value = "/modifyUserNtyCfg", method = RequestMethod.POST)
    public Response modifyUserNtyCfg(@RequestBody DSGCUserNtyCfgBean userNtyCfg) {
        try {
            return Response.ok().setData( userService.modifyUserNtyCfg(userNtyCfg));
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok().setData(false);
        }
    }
}
