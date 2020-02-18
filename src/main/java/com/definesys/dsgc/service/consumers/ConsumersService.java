package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.apps.bean.UserResDTO;
import com.definesys.dsgc.service.consumers.bean.*;
import com.definesys.dsgc.service.svcAuth.SVCAuthDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            for (DSGCConsumerEntities item:entitiesPageQueryResult.getResult()) {
                ConsumerEntitieDTO consumerEntitieDTO = new ConsumerEntitieDTO();
                consumerEntitieDTO.setDceId(item.getDceId());
                consumerEntitieDTO.setCsmName(item.getCsmName());
                consumerEntitieDTO.setCsmCode(item.getCsmCode());
                consumerEntitieDTO.setCsmDesc(item.getCsmDesc());
                consumerEntitieDTO.setOwner(item.getOwner());
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
        DSGCConsumerAuth dsgcConsumerAuth = consumersDao.queryConsumerAuthByCsmCode(dsgcConsumerEntities.getCsmCode());
        ConsumerBasicAuthUserDTO result = new ConsumerBasicAuthUserDTO();
        result.setCsmCode(dsgcConsumerEntities.getCsmCode());
        if (dsgcConsumerAuth != null){
            result.setPwd(dsgcConsumerAuth.getCaAttr1());
        }
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
    public void updateConsumerBasicAuthPwd(ConsumerBasicAuthUserDTO basicAuthUser){
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntByCsmCode(basicAuthUser.getCsmCode());
        DSGCConsumerAuth dsgcConsumerAuth = new DSGCConsumerAuth();
        dsgcConsumerAuth.setCaAttr1(basicAuthUser.getPwd());
        dsgcConsumerAuth.setCsmCode(dsgcConsumerEntities.getCsmCode());
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
}
