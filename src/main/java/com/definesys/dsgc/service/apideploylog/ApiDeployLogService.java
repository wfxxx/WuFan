package com.definesys.dsgc.service.apideploylog;

import com.definesys.dsgc.service.apideploylog.bean.*;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiDeployLogService {
    @Autowired
    private ApiDeployLogDao apiDeployLogDao;

    public List<QueryDeployStatDTO> queryDeploySurvey(CommonReqBean param){
        List<QueryDeployStatDTO> result = new ArrayList<>();
        List<DagDeployStatBean> list = apiDeployLogDao.queryDeploySurvey(param);
        Iterator<DagDeployStatBean> iterable = list.iterator();
        while (iterable.hasNext()){
            DagDeployStatBean dagDeployStatBean = iterable.next();
            QueryDeployStatDTO queryDeployStatDTO = new QueryDeployStatDTO();
            queryDeployStatDTO.setvName(dagDeployStatBean.getvName());
            queryDeployStatDTO.setEnvName(dagDeployStatBean.getEnvName());
            queryDeployStatDTO.setDeployTime(dagDeployStatBean.getDeployTime());
            result.add(queryDeployStatDTO);
        }
        return result;
    }
    public PageQueryResult<QueryDeployLogDTO> queryDeployHis(CommonReqBean param,int pageIndex,int pageSize){
        PageQueryResult<QueryDeployLogDTO> result = new PageQueryResult<>();
        List<QueryDeployLogDTO> logDTOS = new ArrayList<>();
        PageQueryResult<DagDeployLogBean> list = apiDeployLogDao.queryDeployHis(param,pageIndex,pageSize);
        Iterator<DagDeployLogBean> iterable = list.getResult().iterator();
        while (iterable.hasNext()){
            DagDeployLogBean dagDeployLogBean = iterable.next();
            QueryDeployLogDTO queryDeployLogDTO = new QueryDeployLogDTO();
            queryDeployLogDTO.setLogCnt(dagDeployLogBean.getLogCnt());
            queryDeployLogDTO.setEnvName(dagDeployLogBean.getEnvName());
            if(StringUtil.isNotBlank(dagDeployLogBean.getCreatedBy())){
                DSGCUser dsgcUser =apiDeployLogDao.queryUserNameById(dagDeployLogBean.getCreatedBy());
                queryDeployLogDTO.setDeployor(dsgcUser.getUserName());
            }
            logDTOS.add(queryDeployLogDTO);
        }
        result.setResult(logDTOS);
        result.setCount(list.getCount());
        return result;
    }

    public void addDagDeployStat(DagDeployStatBean dagDeployStatBean){
        apiDeployLogDao.addDagDeployStat(dagDeployStatBean);
    }
}
