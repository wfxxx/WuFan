package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.apps.bean.UserResDTO;
import com.definesys.dsgc.service.consumers.bean.*;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svcAuth.SVCAuthDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConsumersService {

    @Autowired
    private ConsumersDao consumersDao;
    @Autowired
    private SVCAuthDao svcAuthDao;
    public PageQueryResult<ConsumerEntitieDTO> queryconsumersList(CommonReqBean commonReqBean, int pageSize, int pageIndex, String userName, String userRole){
        PageQueryResult<DSGCConsumerEntities> entitiesPageQueryResult = new PageQueryResult<>();
        PageQueryResult<ConsumerEntitieDTO> result = new PageQueryResult<>();
        List<ConsumerEntitieDTO> resultList = new ArrayList<>();
//        if("Tourist".equals(userRole)){
//            return new PageQueryResult<>();
//        }else {
            entitiesPageQueryResult = consumersDao.queryconsumersList(commonReqBean,pageSize,pageIndex,userName,userRole);
        List<DagEnvInfoCfgBean> envList = consumersDao.queryApiEnv();
            for (DSGCConsumerEntities item:entitiesPageQueryResult.getResult()) {
                ConsumerEntitieDTO consumerEntitieDTO = new ConsumerEntitieDTO();
                consumerEntitieDTO.setDceId(item.getDceId());
                consumerEntitieDTO.setCsmName(item.getCsmName());
                consumerEntitieDTO.setCsmCode(item.getCsmCode());
                consumerEntitieDTO.setCsmDesc(item.getCsmDesc());
                consumerEntitieDTO.setOwner(item.getOwner());
                String envStr = item.getDeployEnv();
                if(StringUtil.isNotBlank(envStr)){
                    String[] envArr = envStr.trim().split(",");
                    StringBuilder envNameStr = new StringBuilder();
                    for (int j =0;j<envArr.length;j++){
                        for (int i = 0; i < envList.size(); i++) {
                            if (envArr[j].equals(envList.get(i).getEnvCode()) && j ==0){
                                envNameStr.append(envList.get(i).getEnvName());
                                break;
                            }else if(envArr[j].equals(envList.get(i).getEnvCode())){
                                envNameStr.append("，"+envList.get(i).getEnvName());
                                break;
                            }
                        }
                    }
                    consumerEntitieDTO.setDeployEnv(envNameStr.toString());
                }


                resultList.add(consumerEntitieDTO);
            }
            result.setResult(resultList);
            result.setCount(entitiesPageQueryResult.getCount());
      //  }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addConsumer(DSGCConsumerEntities consumerEntities){
        consumerEntities.setIsEnable("Y");
       consumersDao.addConsumer(consumerEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delConsumer(String dceId){
            consumersDao.delConsumer(dceId);
    }
    public ConsumerEntitieDTO queryConsumerDataById(String id){
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(id);
        ConsumerEntitieDTO result = new ConsumerEntitieDTO();
        List<UserResDTO> userResDTOList = new ArrayList<>();
        List<DSGCUser> userList = consumersDao.queryConsumerUserByCmsCode(dsgcConsumerEntities.getCsmCode());
        List<FndLookupValue> lookupValues = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
        for (DSGCUser item:userList) {
            UserResDTO userResDTO = new UserResDTO();
            userResDTO.setUserId(item.getUserId());
            userResDTO.setUserName(item.getUserName());
            userResDTO.setUserRole(item.getUserRole());
            userResDTO.setUserDescription(item.getUserDescription());
            for (int i=0;i<lookupValues.size();i++){
                if (item.getUserRole().equals(lookupValues.get(i).getLookupCode())){
                    userResDTO.setUserRoleName(lookupValues.get(i).getMeaning());
                    break;
                }
            }
            userResDTOList.add(userResDTO);
        }
        result.setDceId(dsgcConsumerEntities.getDceId());
        result.setCsmCode(dsgcConsumerEntities.getCsmCode());
        result.setCsmName(dsgcConsumerEntities.getCsmName());
        result.setCsmDesc(dsgcConsumerEntities.getCsmDesc());
        result.setUserResDTOS(userResDTOList);
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveConsumerData( ConsumerEntitieDTO consumerEntitieDTO,String userId){
        DSGCConsumerEntities dsgcConsumerEntities = new DSGCConsumerEntities();
        dsgcConsumerEntities.setCsmName(consumerEntitieDTO.getCsmName());
        dsgcConsumerEntities.setCsmCode(consumerEntitieDTO.getCsmCode());
        dsgcConsumerEntities.setCsmDesc(consumerEntitieDTO.getCsmDesc());
        dsgcConsumerEntities.setLastUpdatedBy(userId);

        dsgcConsumerEntities.setDceId(consumerEntitieDTO.getDceId());
        consumersDao.updateConsumerData(dsgcConsumerEntities);

        List<UserResDTO> list =consumerEntitieDTO.getUserResDTOS();
        List<DSGCConsumerUsers> userList = new ArrayList<>();
        for (UserResDTO item:list) {
            DSGCConsumerUsers consumerUsers = new DSGCConsumerUsers();
            consumerUsers.setCsmCode(consumerEntitieDTO.getCsmCode());
            consumerUsers.setUserId(item.getUserId());
            consumerUsers.setCreatedBy(userId);
            consumerUsers.setLastUpdatedBy(userId);
            userList.add(consumerUsers);
        }
        consumersDao.delConsumerUser(consumerEntitieDTO.getCsmCode());
        consumersDao.addConsumerUser(userList);
    }
    public ConsumerBasicAuthUserDTO queryConsumerBasicAuthData(String dceId){
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(dceId);
        List<DSGCEnvInfoCfg> envList = consumersDao.queryApiEnv();
        List<DSGCConsumerAuth> dsgcConsumerAuthList = consumersDao.queryConsumerAuthByCsmCode(dsgcConsumerEntities.getCsmCode());
        Iterator<DSGCEnvInfoCfg> iterable = envList.iterator();
        List<Map<String,String>> pwdList = new ArrayList<>();
        while (iterable.hasNext()){
            DSGCEnvInfoCfg dsgcEnvInfoCfg = iterable.next();
            Map<String,String> map = new HashMap<>();
            map.put("envCode",dsgcEnvInfoCfg.getEnvCode());
            map.put("envName",dsgcEnvInfoCfg.getEnvName());
            map.put("pwd","");
            for (int i = 0; i <dsgcConsumerAuthList.size() ; i++) {
                if(dsgcEnvInfoCfg.getEnvCode().equals(dsgcConsumerAuthList.get(i).getEnvCode())){
                    map.put("pwd",dsgcConsumerAuthList.get(i).getCaAttr1());
                    break;
                }
            }
            pwdList.add(map);


        }
        ConsumerBasicAuthUserDTO result = new ConsumerBasicAuthUserDTO();
        result.setCsmCode(dsgcConsumerEntities.getCsmCode());
        result.setPwdList(pwdList);
//        if (dsgcConsumerAuth != null){
//            result.setPwd(dsgcConsumerAuth.getCaAttr1());
//        }


//        List<DSGCConsumerAuth> consumerAuth = consumersDao.queryConsumerBasicAuthData(dceId);
//        List<ConsumerBasicAuthUserDTO> result = new ArrayList<>();
//        if(consumerAuth.size() > 0){
//            ConsumerBasicAuthUserDTO dto = new ConsumerBasicAuthUserDTO();
//            dto.setPwd(consumerAuth.get(0).getCaAttr1());
//            result.add(dto);
//        }
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateConsumerBasicAuthPwd(UpdateBasicPwdVO updateBasicPwdVO){
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntByCsmCode(updateBasicPwdVO.getCsmCode());
      //  DSGCConsumerAuth auth = consumersDao.queryConsumerBasicDataByEnvCode(updateBasicPwdVO);

        DSGCConsumerAuth dsgcConsumerAuth = new DSGCConsumerAuth();
        dsgcConsumerAuth.setCaAttr1(updateBasicPwdVO.getPwd());
        dsgcConsumerAuth.setCsmCode(updateBasicPwdVO.getCsmCode());
        dsgcConsumerAuth.setEnvCode(updateBasicPwdVO.getEnvCode());
        dsgcConsumerAuth.setCaType("basic");
        consumersDao.updateConsumerBasicAuthPwd(dsgcConsumerAuth);
    }
    public List<ConsumerEntitieDTO> queryConsumersBaseInfoList(){
      List<DSGCConsumerEntities> dsgcConsumerEntities = consumersDao.queryConsumersBaseInfoList();
      List<ConsumerEntitieDTO> consumerEntitieDTOS = new ArrayList<>();
        for (DSGCConsumerEntities item:dsgcConsumerEntities) {
            ConsumerEntitieDTO consumerEntitieDTO = new ConsumerEntitieDTO();
            consumerEntitieDTO.setDceId(item.getDceId());
            consumerEntitieDTO.setCsmCode(item.getCsmCode());
            consumerEntitieDTO.setCsmName(item.getCsmName());
            consumerEntitieDTO.setCsmDesc(item.getCsmDesc());
            consumerEntitieDTOS.add(consumerEntitieDTO);
        }
        return consumerEntitieDTOS;
    }

    public List<Map<String,Object>> queryConsumersListByUserId(String id){

        return consumersDao.queryConsumersListByUserId(id);
    }
    public List<ConsumerDeployDTO> queryConsumerDeployData(CommonReqBean param){
        List<ConsumerDeployDTO> result = new ArrayList<>();
       List<DSGCEnvInfoCfg> envList = consumersDao.queryApiEnv();
       DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(param.getCon0());
       String envStr = dsgcConsumerEntities.getDeployEnv();
        List<String> env = new ArrayList<>();
       if(StringUtil.isNotBlank(envStr)){
           env = Arrays.asList(envStr.trim().split(","));
       }
        Iterator<DSGCEnvInfoCfg> iterable = envList.iterator();
        while (iterable.hasNext()){
            DSGCEnvInfoCfg dsgcEnvInfoCfg = iterable.next();
            ConsumerDeployDTO consumerDeployDTO = new ConsumerDeployDTO();
            consumerDeployDTO.setEnvName(dsgcEnvInfoCfg.getEnvName());
            consumerDeployDTO.setEnvCode(dsgcEnvInfoCfg.getEnvCode());
            consumerDeployDTO.setDeployment(false);
            for (int i = 0; i <env.size() ; i++) {
                if(dsgcEnvInfoCfg.getEnvCode().equals(env.get(i))){
                    consumerDeployDTO.setDeployment(true);
                    break;
                }
            }
            result.add(consumerDeployDTO);
        }
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    public void consumerDeploy(ConsumerDeployChangeVO param){
        /**
         * TODO  部署或者取消部署
         */
    }
}
