package com.definesys.dsgc.service.flow.mng;

import com.definesys.dsgc.service.flow.bean.DsgcSvcgenProjInfo;
import com.definesys.dsgc.service.flow.bean.FlowServices;
import com.definesys.dsgc.service.flow.dto.FlowReqCommonDTO;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.definesys.mpaas.query.session.MpaasSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class FlowSvcService {
    @Autowired
    private FlowSvcDao flowSvcDao;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private SVCLogDao sldao;

    /**
     * 分页查询flowServices
     *
     * @param param
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageQueryResult<FlowServices> pageQueryFlowServices(FlowReqCommonDTO param,String uid,int pageIndex,int pageSize) {
        UserHelper uh = this.userHelper.user(uid);
        //如果是管理员，则不做任何过滤，查询所有数据
        if (uh.isAdmin() || uh.isSuperAdministrator()) {
            return this.flowSvcDao.pageQueryFlowServices(param,pageIndex,pageSize,null);
        } else if (uh.isSystemMaintainer()) {
            //如果是系统管理员，则只能查看自己所属应用的list
            List<String> sysCodeList = new ArrayList<String>();
            List<DSGCSystemUser> dsgcSystemUsers = sldao.findUserSystemByUserId(uid);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser)iter.next();
                sysCodeList.add(s.getSysCode());
            }
            return this.flowSvcDao.pageQueryFlowServices(param,pageIndex,pageSize,sysCodeList);
        } else {
            //其它角色不允许查看
            PageQueryResult noData =  new PageQueryResult<FlowServices>();
            noData.setCount(0L);
            noData.setResult(new ArrayList());

            return noData;
        }
    }


    public String megreFlowServices(FlowServices flow,String uid){
        UserHelper uh = this.userHelper.user(uid);

        //校验
        if(!(uh.isSuperAdministrator() || uh.isAdmin() || uh.isSystemMaintainer())) {
            return "非法的操作权限！";
        } else {
            if(flow.getProjectCode() == null ||  flow.getProjectCode().trim().length() == 0){
                return "集成流必须指定一个项目";
            }

            DsgcSvcgenProjInfo projInfo = this.flowSvcDao.queryProjectDirInfo(flow.getProjectCode());
            if(projInfo == null){
                return "非法的项目";
            }

            if(uh.isSystemMaintainer()) {
                if(!uh.isSpecifySystemMaintainer(projInfo.getSysCode())){
                    return "非法的操作权限：当前项目无访问权限！";
                }
            }
        }

        if(flow.getFlowId() == null || flow.getFlowId().trim().length() == 0){
            //添加集成流
            flow.setFlowId(null);
            int result = this.flowSvcDao.addFlowServcie(flow);
            if(result == 0){
                return "集成流名称不能为空！";
            } else if(result == -1){
                return "已经存在重复的集成流名称！";
            } else if(result != 1) {
                return "操作失败！";
            }
        } else {
            int result = this.flowSvcDao.updateFlowService(flow);
            if(result == 0){
                return "集成流名称不能为空！";
            } else if(result == -1){
                return "已经存在重复的集成流名称！";
            } else if(result == -2){
                return "集成流名称不允许更改！";
            } else if(result != 1) {
                return "操作失败！";
            }
        }

        return "Y";
    }


    /**
     * 删除flowServices
     * @param flowId
     * @param uid
     * @return
     */
    public String deleteFlowServcie(String flowId,String uid){
        UserHelper uh = this.userHelper.user(uid);
        //校验
        if(!(uh.isSuperAdministrator() || uh.isAdmin() || uh.isSystemMaintainer())) {
            return "非法的操作权限！";
        } else {
            FlowServices flow = this.flowSvcDao.getFlowServcieByFlowId(flowId);
            if(flow == null){
                return "对象不存在，无法删除！";
            } else{
                if(uh.isSystemMaintainer() && ! uh.isSpecifySystemMaintainer(flow.getAppCode())){
                    return "非法的操作权限！";
                }
            }
        }

        this.flowSvcDao.deleteFlowServcie(flowId);
        return "Y";
    }


    /**
     * 判断当前用户是否对flow有编辑权限
     * @param flowId
     * @return
     */
    public boolean isAuthToEditFlow(String flowId){
        String uid = MpaasSession.getCurrentUser();

        UserHelper uh = this.userHelper.user(uid);
        if(uh.isSuperAdministrator() || uh.isAdmin()) {
            return true;
        } else if(uh.isSystemMaintainer()) {
            FlowServices fs = this.flowSvcDao.getFlowServcieByFlowId(flowId);
            if(fs != null){
                if(uh.isSpecifySystemMaintainer(fs.getAppCode())){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }



}
