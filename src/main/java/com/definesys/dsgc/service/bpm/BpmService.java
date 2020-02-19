package com.definesys.dsgc.service.bpm;

import com.definesys.dsgc.service.bpm.bean.*;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.users.DSGCUserDao;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BpmService {
    @Autowired
    private BpmDao bpmdao;

    @Autowired
    private DSGCUserDao dsgcUserDao;

    public String generateBpmInst(){
      BpmInstanceBean bpmInstanceBean = new BpmInstanceBean();
              bpmdao.generateBpmInst(bpmInstanceBean);
      return bpmInstanceBean.getInstId();
    }

    public PageQueryResult<BpmInstanceDTO> getTaskList(BpmCommonReqBean param,String userId,int pageSize,int pageIndex){
        PageQueryResult<BpmInstanceBean> beanPageQueryResult = bpmdao.getTaskList(param,userId,pageSize,pageIndex);
        List<BpmInstanceDTO> bpmInstanceDTOList = new ArrayList<>();
        long count = beanPageQueryResult.getCount();
        List<BpmInstanceBean> bpmInstanceBeanList = beanPageQueryResult.getResult();

        instanceBeanListMapping(bpmInstanceBeanList,bpmInstanceDTOList);

        PageQueryResult<BpmInstanceDTO> result = new PageQueryResult<>();
        result.setCount(count);
        result.setResult(bpmInstanceDTOList);
        return result;
    }

    public PageQueryResult<BpmInstanceDTO> myApply(BpmCommonReqBean param,String userId,int pageSize,int pageIndex){
        PageQueryResult<BpmInstanceBean> beanPageQueryResult = bpmdao.myApply(param,userId,pageSize,pageIndex);
        List<BpmInstanceDTO> bpmInstanceDTOList = new ArrayList<>();
        long count = beanPageQueryResult.getCount();
        List<BpmInstanceBean> bpmInstanceBeanList = beanPageQueryResult.getResult();

        instanceBeanListMapping(bpmInstanceBeanList,bpmInstanceDTOList);

        PageQueryResult<BpmInstanceDTO> result = new PageQueryResult<>();
        result.setCount(count);
        result.setResult(bpmInstanceDTOList);
        return result;
    }
    public void instanceBeanListMapping(List<BpmInstanceBean> bpmInstanceBeanList,List<BpmInstanceDTO> bpmInstanceDTOList){
        for (BpmInstanceBean item:bpmInstanceBeanList) {
        //    BpmInstanceDTO bpmInstanceDTO = new BpmInstanceDTO();
//            if(item.getCurNode() != null && item.getCurNode() != ""){
//                bpmInstanceDTO.setCurNodeName(bpmdao.getBpmNodeById(item.getCurNode()).get(0).getNodeName());
//            }
//            bpmInstanceDTO.setProcessName(bpmdao.findBpmProcessById(item.getProcessId()).get(0).getProcessName());
//            bpmInstanceDTO.setCreatedBy(dsgcUserDao.findUserById(item.getCreatedBy()).getUserName());
//            bpmInstanceDTO.setLastUpdatedBy(dsgcUserDao.findUserById(item.getLastUpdatedBy()).getUserName());
//            bpmInstanceDTO.setCreationDate(item.getCreationDate());
//            bpmInstanceDTO.setCurNode(item.getCurNode());
//            bpmInstanceDTO.setLastUpdateDate(item.getLastUpdateDate());
//            bpmInstanceDTO.setObjectVersionNumber(item.getObjectVersionNumber());
//            bpmInstanceDTO.setInstStat(item.getInstStat());
//            bpmInstanceDTO.setInstId(item.getInstId());
//            bpmInstanceDTO.setInstTitle(item.getInstTitle());
//            bpmInstanceDTOList.add(bpmInstanceDTO);

            bpmInstanceDTOList.add(instanceBeanMapping(item));
        }
    }
    public BpmInstanceDTO instanceBeanMapping(BpmInstanceBean bpmInstanceBean){
        BpmInstanceDTO bpmInstanceDTO = new BpmInstanceDTO();
        if(bpmInstanceBean.getCurNode() != null && bpmInstanceBean.getCurNode() != ""){
            List<BpmNodeBean> nodeBeans= bpmdao.getBpmNodeById(bpmInstanceBean.getCurNode());
            bpmInstanceDTO.setCurNodeName(nodeBeans.get(0).getNodeName());
            List<DSGCUser> users =bpmdao.queryTaskUserByNodeId(bpmInstanceBean.getInstId(),bpmInstanceBean.getCurNode());
            List<String> userList = new ArrayList<>();
            Iterator<DSGCUser> iterator = users.iterator();
            while (iterator.hasNext()){
                DSGCUser user = iterator.next();
                userList.add(user.getUserName());
            }
            bpmInstanceDTO.setApprover(userList);
        }
        bpmInstanceDTO.setProcessName(bpmdao.findBpmProcessById(bpmInstanceBean.getProcessId()).get(0).getProcessName());
        bpmInstanceDTO.setCreatedBy(dsgcUserDao.findUserById(bpmInstanceBean.getCreatedBy()).getUserName());
        bpmInstanceDTO.setLastUpdatedBy(dsgcUserDao.findUserById(bpmInstanceBean.getLastUpdatedBy()).getUserName());
        bpmInstanceDTO.setCreationDate(bpmInstanceBean.getCreationDate());
        bpmInstanceDTO.setCurNode(bpmInstanceBean.getCurNode());
        bpmInstanceDTO.setProcessId(bpmInstanceBean.getProcessId());
        bpmInstanceDTO.setLastUpdateDate(bpmInstanceBean.getLastUpdateDate());
        bpmInstanceDTO.setObjectVersionNumber(bpmInstanceBean.getObjectVersionNumber());
        bpmInstanceDTO.setInstStat(bpmInstanceBean.getInstStat());
        bpmInstanceDTO.setInstId(bpmInstanceBean.getInstId());
        bpmInstanceDTO.setInstTitle(bpmInstanceBean.getInstTitle());
        //bpmInstanceDTO.setApprover(bpmInstanceBean.getApprover());
        return bpmInstanceDTO;
    }
    public List<BpmHistoryDTO> getInstHistory(BpmCommonReqBean param){
        List<BpmHistoryBean> bpmHistoryBeanList = bpmdao.getInstHistory(param);
        List<BpmHistoryDTO> bpmHistoryDTOList = new ArrayList<>();
        for (BpmHistoryBean item:bpmHistoryBeanList) {
            BpmHistoryDTO bpmHistoryDTO = new BpmHistoryDTO();
            bpmHistoryDTO.setApprover(dsgcUserDao.findUserById(item.getApprover()).getUserName());
            bpmHistoryDTO.setCreatedBy(dsgcUserDao.findUserById(item.getCreatedBy()).getUserName());
            bpmHistoryDTO.setLastUpdatedBy(dsgcUserDao.findUserById(item.getLastUpdatedBy()).getUserName());
            bpmHistoryDTO.setInstTitle(bpmdao.findBpmInstanceById(item.getInstId()).get(0).getInstTitle());
            if(!"-1".equals(item.getNodeId())){
                bpmHistoryDTO.setNodeName(bpmdao.getBpmNodeById(item.getNodeId()).get(0).getNodeName());
            }else {
                bpmHistoryDTO.setNodeName("申请人发起节点");
            }
            bpmHistoryBeanMapping(item,bpmHistoryDTO);
            bpmHistoryDTOList.add(bpmHistoryDTO);
        }
        return bpmHistoryDTOList;
    }

    public void bpmHistoryBeanMapping(BpmHistoryBean bpmHistoryBean,BpmHistoryDTO bpmHistoryDTO){
    bpmHistoryDTO.setHisId(bpmHistoryBean.getHisId());
    bpmHistoryDTO.setInstId(bpmHistoryBean.getInstId());
//    bpmHistoryDTO.setNodeName(bpmHistoryBean.getNodeName());
    bpmHistoryDTO.setApproveOper(bpmHistoryBean.getApproveOper());
    bpmHistoryDTO.setApproveOpinion(bpmHistoryBean.getApproveOpinion());
    bpmHistoryDTO.setObjectVersionNumber(bpmHistoryBean.getObjectVersionNumber());
    bpmHistoryDTO.setCreationDate(bpmHistoryBean.getCreationDate());
    bpmHistoryDTO.setLastUpdateDate(bpmHistoryBean.getLastUpdateDate());
    }

    public List<Map<String, Object>> getTaskCount(String userId){
        return bpmdao.getTaskCount(userId);
    }
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(BpmApproveBean param,String userId) throws Exception{
        Boolean isAuthApply = false;
       List<BpmInstanceBean> instanceBeanList = bpmdao.findBpmInstanceById(param.getInstId());
        String nodeId = instanceBeanList.get(0).getCurNode();
        List<BpmNodeBean> nodeBeanList = bpmdao.findBpmNodeById(nodeId);    //当前所在节点
        BpmHistoryBean bpmHistoryBean = new BpmHistoryBean();
        bpmHistoryBean.setInstId(instanceBeanList.get(0).getInstId());
        bpmHistoryBean.setApproveOpinion(param.getApproveOpinion());
        bpmHistoryBean.setApprover(userId);
        bpmHistoryBean.setCreatedBy(userId);
        bpmHistoryBean.setLastUpdatedBy(userId);
        bpmHistoryBean.setObjectVersionNumber(1);
        bpmHistoryBean.setInstId(param.getInstId());
        bpmHistoryBean.setNodeName(nodeBeanList.get(0).getNodeName());
        bpmHistoryBean.setNodeId(nodeBeanList.get(0).getNodeId());
        BpmInstanceBean bpmInstanceBean = new BpmInstanceBean();
        bpmInstanceBean.setLastUpdatedBy(userId);
        bpmInstanceBean.setInstId(instanceBeanList.get(0).getInstId());
        BpmTaskBean bpmTaskBean = new BpmTaskBean();
        bpmTaskBean.setInstId(param.getInstId());
        List<String> userList = new ArrayList<>();                          //审批用户集合
        if(!"spy".equals(nodeBeanList.get(0).getApprType())){
            List<String> roleUserList = getApprUserList(nodeBeanList.get(0));
            for (int i = 0;i<roleUserList.size();i++){
                if (roleUserList.get(i).equals(userId)){
                    isAuthApply = true;
                    break;
                }else {
                    isAuthApply = false;
                }
            }
            if(!isAuthApply){
                throw new Exception("无权限审批");
            }

         }

        if("1".equals(param.getPassOrReject())){   //审批通过
            bpmHistoryBean.setApproveOper("agree");
            if (!"1".equals(nodeBeanList.get(0).getNodePos())) {
                bpmInstanceBean.setCurNode(nodeBeanList.get(0).getPassNode());
                bpmInstanceBean.setInstStat("appr");
                bpmTaskBean.setNodeId(nodeBeanList.get(0).getPassNode());
                List<BpmNodeBean> passNodeList = bpmdao.findBpmNodeById(nodeBeanList.get(0).getPassNode());
                if ("spy".equals(passNodeList.get(0).getApprType())) {
                    if(StringUtil.isBlank(param.getApprover())){
                        throw new Exception("当前流程需指定审批人");
                    }
                    String[] conArray = param.getApprover().trim().split(",");
                    userList = Arrays.asList(conArray);
                 //   userList.add(param.getApprover());
                }
                else if("creator".equals(passNodeList.get(0).getApprType())){
                    List<BpmInstanceBean> instanceBeans = bpmdao.findBpmInstanceById(param.getInstId());
                    userList.add(instanceBeans.get(0).getCreatedBy());
                }
                else {
                    userList = getApprUserList(passNodeList.get(0));
                }
            }else {
                bpmInstanceBean.setInstStat("ompleted");
            }
        }else if ("2".equals(param.getPassOrReject())){   //审批驳回
            bpmHistoryBean.setApproveOper("eject");
            bpmInstanceBean.setInstStat("ejected");
            if(nodeBeanList.get(0).getRejectNode() != null){
                bpmInstanceBean.setCurNode(nodeBeanList.get(0).getRejectNode());
                bpmTaskBean.setNodeId(nodeBeanList.get(0).getRejectNode());
                List<BpmNodeBean> rejectNodeList = bpmdao.findBpmNodeById(nodeBeanList.get(0).getRejectNode());
                if("spy".equals(rejectNodeList.get(0).getApprType())){
                    userList = new ArrayList<>();
                    userList.add(param.getApprover());

                }else {
                   userList = getApprUserList(rejectNodeList.get(0));
                }
            }else {
                bpmInstanceBean.setCurNode("");
            }

        }else {
            bpmHistoryBean.setApproveOper("ancel");
        }

        bpmdao.addHistory(bpmHistoryBean);
        bpmdao.updateBpmInstance(bpmInstanceBean);
        BpmTaskBean delTask = new BpmTaskBean();
        delTask.setInstId(param.getInstId());
        delTask.setNodeId(nodeId);
        bpmdao.delTask(delTask);
        if(userList != null && userList.size()>0){
            for (String item:userList) {
                BpmTaskBean taskBean = new BpmTaskBean();
                taskBean.setInstId(bpmTaskBean.getInstId());
                taskBean.setNodeId(bpmTaskBean.getNodeId());
                taskBean.setApprover(item);
                taskBean.setCreatedBy(userId);
                bpmdao.addTask(taskBean);
        }
        }
       // bpmdao.approveTask(param);
    }

    public List<String> getApprUserList(BpmNodeBean bpmNodeBean){
        String apprCode = bpmNodeBean.getApprCode();
        List<String> result = new ArrayList<>();
        switch (bpmNodeBean.getApprType()){
            case "role":          //角色编码
               List<DSGCUser> userList = dsgcUserDao.getUserListByRole(apprCode);
                for (DSGCUser item:userList) {
                    result.add(item.getUserId());
                }
                break;
            case "user":         //用户
                result.add(apprCode);
                break;
            case "userList":    //用户列表
               String[] users = apprCode.split(",");
               result = Arrays.asList(users);
                break;
            case "spy":          //前置审批人指定
                break;
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public String generateBpmInstance(BpmInstanceBean bpmInstanceBean,String approver,String userId) throws Exception{
     BpmNodeBean bpmNodeBean = new BpmNodeBean();
     bpmNodeBean.setProcessId(bpmInstanceBean.getProcessId());
     bpmNodeBean.setNodePos("0");
     List<BpmNodeBean> nodeBeanList = bpmdao.getBpmNodes(bpmNodeBean);
     if(nodeBeanList.size() == 0){
         return null;
     }
    bpmInstanceBean.setCurNode(nodeBeanList.get(0).getNodeId());
     bpmInstanceBean.setInstStat("appr");
     bpmInstanceBean.setCreatedBy(userId);
     bpmInstanceBean.setCreationDate(new Date());
     bpmInstanceBean.setLastUpdatedBy(userId);
     bpmInstanceBean.setObjectVersionNumber(1);
     bpmdao.createBpmInstance(bpmInstanceBean);
        List<String> userList = null;
        if("spy".equals(nodeBeanList.get(0).getApprType())){
            if(StringUtil.isBlank(approver)){
                throw new Exception("当前流程需指定审批人");
            }
            userList = new ArrayList<>();
            String[] conArray = approver.trim().split(",");
            userList = Arrays.asList(conArray);
           // userList.add(approver);
        }else {
            userList = getApprUserList(nodeBeanList.get(0));
        }
        for (String item:userList) {
            BpmTaskBean taskBean = new BpmTaskBean();
            taskBean.setInstId(bpmInstanceBean.getInstId());
            taskBean.setNodeId(nodeBeanList.get(0).getNodeId());
            taskBean.setApprover(item);
            taskBean.setCreatedBy(userId);
           addTask(taskBean);
        }
        BpmHistoryBean bpmHistoryBean = new BpmHistoryBean();
        bpmHistoryBean.setInstId(bpmInstanceBean.getInstId());
        bpmHistoryBean.setNodeName("流程发起");
        bpmHistoryBean.setApprover(userId);
        bpmHistoryBean.setApproveOper("agree");
        bpmHistoryBean.setNodeId("-1");
        bpmHistoryBean.setApproveOpinion("申请人发起流程");
        bpmHistoryBean.setCreatedBy(userId);
        bpmHistoryBean.setLastUpdatedBy(userId);
        bpmHistoryBean.setObjectVersionNumber(1);
        Date date = new Date();
        bpmHistoryBean.setCreationDate(date);
        bpmHistoryBean.setLastUpdateDate(date);
        bpmdao.addHistory(bpmHistoryBean);
        return bpmInstanceBean.getInstId();
    }

    public void addTask(BpmTaskBean bpmTaskBean){
        bpmdao.addTask(bpmTaskBean);
    }


    public BpmInstanceDTO queryBpmInstanceBaseInfo(BpmCommonReqBean param){
      BpmInstanceBean bpmInstanceBean =  bpmdao.queryBpmInstanceBaseInfo(param);
        return instanceBeanMapping(bpmInstanceBean);
    }

    public List<BpmProcessBean> queryProcessTypeList(){
       return bpmdao.queryProcessTypeList();
    }

    public List<BpmNodeBean> queryNodesByInstanceId(String id){
        return bpmdao.queryNodesByInstanceId(id);
    }

    public BpmNodeBean queryNodeById(String id){
        return bpmdao.queryNodeById(id);
    }


    public BpmHistoryBean addhistory(BpmHistoryBean bpmHistoryBean){
        return bpmdao.addhistory(bpmHistoryBean);

    }

    public List<BpmNodeBean> getBpmNodeByNodeId(String nodeId){
        return bpmdao.getBpmNodeById(nodeId);

    }





}
