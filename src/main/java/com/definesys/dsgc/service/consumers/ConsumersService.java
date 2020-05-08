package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.apps.bean.UserResDTO;
import com.definesys.dsgc.service.consumers.bean.*;
import com.definesys.dsgc.service.dagclient.ConsumerDeployService;
import com.definesys.dsgc.service.dagclient.proxy.bean.JWTAuthBean;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svcAuth.SVCAuthDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.utils.JwtTokenUtil;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ConsumersService {

    @Autowired
    private ConsumersDao consumersDao;
    @Autowired
    private SVCAuthDao svcAuthDao;

    @Value("${jwt.accessTokenExpireTime}")
    private String accessTokenExpireTime;

    @Autowired
    private ConsumerDeployService consumerDeployService;

    public PageQueryResult<ConsumerEntitieDTO> queryconsumersList(CommonReqBean commonReqBean, int pageSize, int pageIndex, String userName, String userRole) {
        PageQueryResult<DSGCConsumerEntities> entitiesPageQueryResult = new PageQueryResult<>();
        PageQueryResult<ConsumerEntitieDTO> result = new PageQueryResult<>();
        List<ConsumerEntitieDTO> resultList = new ArrayList<>();
//        if("Tourist".equals(userRole)){
//            return new PageQueryResult<>();
//        }else {
        entitiesPageQueryResult = consumersDao.queryconsumersList(commonReqBean, pageSize, pageIndex, userName, userRole);
        List<DSGCEnvInfoCfg> envList = consumersDao.queryApiEnv();
        for (DSGCConsumerEntities item : entitiesPageQueryResult.getResult()) {
            ConsumerEntitieDTO consumerEntitieDTO = new ConsumerEntitieDTO();
            consumerEntitieDTO.setDceId(item.getDceId());
            consumerEntitieDTO.setCsmName(item.getCsmName());
            consumerEntitieDTO.setCsmCode(item.getCsmCode());
            consumerEntitieDTO.setCsmDesc(item.getCsmDesc());
            consumerEntitieDTO.setOwner(item.getOwner());
            String envStr = item.getDeployEnv();
            if (StringUtil.isNotBlank(envStr)) {
                String[] envArr = envStr.trim().split(",");
                StringBuilder envNameStr = new StringBuilder();
                for (int j = 0; j < envArr.length; j++) {
                    for (int i = 0; i < envList.size(); i++) {
                        if (envArr[j].equals(envList.get(i).getEnvCode()) && j == 0) {
                            envNameStr.append(envList.get(i).getEnvName());
                            break;
                        } else if (envArr[j].equals(envList.get(i).getEnvCode())) {
                            envNameStr.append("，" + envList.get(i).getEnvName());
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
    public void addConsumer(DSGCConsumerEntities consumerEntities) {
        consumerEntities.setIsEnable("Y");
        consumersDao.addConsumer(consumerEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delConsumer(String dceId) {
        consumersDao.delConsumer(dceId);
    }

    public ConsumerEntitieDTO queryConsumerDataById(String id) {
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(id);
        ConsumerEntitieDTO result = new ConsumerEntitieDTO();
        List<UserResDTO> userResDTOList = new ArrayList<>();
        List<DSGCUser> userList = consumersDao.queryConsumerUserByCmsCode(dsgcConsumerEntities.getCsmCode());
        List<FndLookupValue> lookupValues = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
        for (DSGCUser item : userList) {
            UserResDTO userResDTO = new UserResDTO();
            userResDTO.setUserId(item.getUserId());
            userResDTO.setUserName(item.getUserName());
            userResDTO.setUserRole(item.getUserRole());
            userResDTO.setUserDescription(item.getUserDescription());
            for (int i = 0; i < lookupValues.size(); i++) {
                if (item.getUserRole().equals(lookupValues.get(i).getLookupCode())) {
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
    public void saveConsumerData(ConsumerEntitieDTO consumerEntitieDTO, String userId) {
        DSGCConsumerEntities dsgcConsumerEntities = new DSGCConsumerEntities();
        dsgcConsumerEntities.setCsmName(consumerEntitieDTO.getCsmName());
        dsgcConsumerEntities.setCsmCode(consumerEntitieDTO.getCsmCode());
        dsgcConsumerEntities.setCsmDesc(consumerEntitieDTO.getCsmDesc());
        dsgcConsumerEntities.setLastUpdatedBy(userId);

        dsgcConsumerEntities.setDceId(consumerEntitieDTO.getDceId());
        consumersDao.updateConsumerData(dsgcConsumerEntities);

        List<UserResDTO> list = consumerEntitieDTO.getUserResDTOS();
        List<DSGCConsumerUsers> userList = new ArrayList<>();
        for (UserResDTO item : list) {
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

    public ConsumerBasicAuthUserDTO queryConsumerBasicAuthData(String dceId) {
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(dceId);
        List<DSGCEnvInfoCfg> envList = consumersDao.queryApiEnv();
        List<DSGCConsumerAuth> dsgcConsumerAuthList = consumersDao.queryConsumerAuthByCsmCode(dsgcConsumerEntities.getCsmCode());
        Iterator<DSGCEnvInfoCfg> iterable = envList.iterator();
        List<Map<String, String>> pwdList = new ArrayList<>();
        while (iterable.hasNext()) {
            DSGCEnvInfoCfg dsgcEnvInfoCfg = iterable.next();
            Map<String, String> map = new HashMap<>();
            map.put("envCode", dsgcEnvInfoCfg.getEnvCode());
            map.put("envName", dsgcEnvInfoCfg.getEnvName());
            map.put("pwd", "");
            for (int i = 0; i < dsgcConsumerAuthList.size(); i++) {
                if (dsgcEnvInfoCfg.getEnvCode().equals(dsgcConsumerAuthList.get(i).getEnvCode())) {
                    map.put("pwd", dsgcConsumerAuthList.get(i).getCaAttr1());
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
    public void updateConsumerBasicAuthPwd(UpdateBasicPwdVO updateBasicPwdVO,String userId) throws Exception {
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntByCsmCode(updateBasicPwdVO.getCsmCode());
        //  DSGCConsumerAuth auth = consumersDao.queryConsumerBasicDataByEnvCode(updateBasicPwdVO);
        Boolean res = consumerDeployService.updateBasicAuth(dsgcConsumerEntities.getCsmCode(), updateBasicPwdVO.getPwd(), updateBasicPwdVO.getEnvCode(),userId);
        if (!res) {
            throw new Exception("新增或更新basic认证密码失败！");
        }
        DSGCConsumerAuth dsgcConsumerAuth = new DSGCConsumerAuth();
        dsgcConsumerAuth.setCaAttr1(updateBasicPwdVO.getPwd());
        dsgcConsumerAuth.setCsmCode(updateBasicPwdVO.getCsmCode());
        dsgcConsumerAuth.setEnvCode(updateBasicPwdVO.getEnvCode());
        dsgcConsumerAuth.setCaType("basic");
        consumersDao.updateConsumerBasicAuthPwd(dsgcConsumerAuth);
    }

    public List<ConsumerEntitieDTO> queryConsumersBaseInfoList() {
        List<DSGCConsumerEntities> dsgcConsumerEntities = consumersDao.queryConsumersBaseInfoList();
        List<ConsumerEntitieDTO> consumerEntitieDTOS = new ArrayList<>();
        for (DSGCConsumerEntities item : dsgcConsumerEntities) {
            ConsumerEntitieDTO consumerEntitieDTO = new ConsumerEntitieDTO();
            consumerEntitieDTO.setDceId(item.getDceId());
            consumerEntitieDTO.setCsmCode(item.getCsmCode());
            consumerEntitieDTO.setCsmName(item.getCsmName());
            consumerEntitieDTO.setCsmDesc(item.getCsmDesc());
            consumerEntitieDTOS.add(consumerEntitieDTO);
        }
        return consumerEntitieDTOS;
    }

    public List<Map<String, Object>> queryConsumersListByUserId(String id) {

        return consumersDao.queryConsumersListByUserId(id);
    }

    public List<ConsumerDeployDTO> queryConsumerDeployData(CommonReqBean param) {
        List<ConsumerDeployDTO> result = new ArrayList<>();
        List<DSGCEnvInfoCfg> envList = consumersDao.queryApiEnv();
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(param.getCon0());
        String envStr = dsgcConsumerEntities.getDeployEnv();
        List<String> env = new ArrayList<>();
        if (StringUtil.isNotBlank(envStr)) {
            env = Arrays.asList(envStr.trim().split(","));
        }
        Iterator<DSGCEnvInfoCfg> iterable = envList.iterator();
        while (iterable.hasNext()) {
            DSGCEnvInfoCfg dsgcEnvInfoCfg = iterable.next();
            ConsumerDeployDTO consumerDeployDTO = new ConsumerDeployDTO();
            consumerDeployDTO.setEnvName(dsgcEnvInfoCfg.getEnvName());
            consumerDeployDTO.setEnvCode(dsgcEnvInfoCfg.getEnvCode());
            consumerDeployDTO.setDeployment(false);
            for (int i = 0; i < env.size(); i++) {
                if (dsgcEnvInfoCfg.getEnvCode().equals(env.get(i))) {
                    consumerDeployDTO.setDeployment(true);
                    break;
                }
            }
            result.add(consumerDeployDTO);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void consumerDeploy(ConsumerDeployChangeVO param,String userId) throws Exception {
        if (StringUtil.isNotBlank(param.getDceId()) && StringUtil.isNotBlank(param.getEnvCode())) {
            DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(param.getDceId());
            String deployEnv = dsgcConsumerEntities.getDeployEnv();
            Boolean res;
            if (param.getDeployment()) {
                res = consumerDeployService.deployConsumer(dsgcConsumerEntities.getCsmCode(), param.getEnvCode(),userId);
                if (!res) {
                    throw new Exception("部署失败！");
                }
                if (StringUtil.isNotBlank(deployEnv)) {
                    deployEnv = deployEnv + "," + param.getEnvCode();
                } else {
                    deployEnv = param.getEnvCode();
                }
                dsgcConsumerEntities.setDeployEnv(deployEnv);
                consumersDao.updateConsumerDeployEnv(dsgcConsumerEntities);
            } else {
                res = consumerDeployService.undeployConsumer(dsgcConsumerEntities.getCsmCode(), param.getEnvCode(),userId);
                if (!res) {
                    throw new Exception("取消部署失败！");
                }
                if (StringUtil.isNotBlank(deployEnv)) {
                    List<String> env = new ArrayList<String>(Arrays.asList(deployEnv.trim().split(",")));
                    Iterator<String> iterator = env.iterator();
                    while (iterator.hasNext()) {
                        String item = iterator.next();
                        if (item.equals(param.getEnvCode())) {
                            iterator.remove();
                        }
                    }
                    String envStr = String.join(",", env);
                    dsgcConsumerEntities.setDeployEnv(envStr);
                    consumersDao.updateConsumerDeployEnv(dsgcConsumerEntities);
                }
            }
        }
    }

    public Boolean checkCsmCodeIsExsit(CommonReqBean param) {
        Boolean isExsit = consumersDao.checkCsmCodeIsExsit(param.getCon0());
        return isExsit;
    }

    public Boolean checkCsmNameIsExsit(CommonReqBean param) {
        Boolean isExsit = consumersDao.checkCsmNameIsExsit(param.getCon0());
        return isExsit;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createToken(CreateTokenVO param) {
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(param.getDceId());
        if (dsgcConsumerEntities != null) {
            String iss =UUID.randomUUID().toString();
            LocalDateTime dateTime = LocalDateTime.parse(param.getExp(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Long expSecond = dateTime.toEpochSecond(ZoneOffset.of("+8"));
            long currSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            String completeSecretKey = UUID.randomUUID().toString();
            //同步在网关中新建jwt auth信息
            JWTAuthBean jb = new JWTAuthBean();
            jb.setIssKey(iss);
            jb.setAlgorithm("HS256");
            jb.setCsmCode(dsgcConsumerEntities.getCsmCode());
            jb.setEnvCode(param.getEnvCode());
            jb.setSecretKey(completeSecretKey);
            boolean deployRes = false;
            try {
                deployRes = this.consumerDeployService.addJWTAuth(jb);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (deployRes) {
                String token = JwtTokenUtil.generateToken(iss,expSecond,completeSecretKey);
                if (StringUtils.isNotEmpty(token)) {
                    saveJwtInfo(iss,completeSecretKey,token,param.getEnvCode(),dsgcConsumerEntities.getCsmCode(),expSecond,currSecond);
                } else {
                    throw new RuntimeException("生成token失败");
                }
            } else {
                throw new RuntimeException("访问网关失败！");
            }
        } else {
            throw new RuntimeException("消费者不存在！");
        }
    }

    public void saveJwtInfo(String iss,String secretKey,String token,String envCode,String csmCode,Long expSecond,Long currSecond){
        DagConsumerJwt dagConsumerJwt = new DagConsumerJwt();
        dagConsumerJwt.setAlgorithm("HS256");
        dagConsumerJwt.setCsmCode(csmCode);
        dagConsumerJwt.setIssKey(iss);
        dagConsumerJwt.setEnvCode(envCode);
        dagConsumerJwt.setSecretKey(secretKey);
        consumersDao.saveConsumerJwtInfo(dagConsumerJwt);
        DagConsumerToken dagConsumerToken = new DagConsumerToken();
        dagConsumerToken.setCsmCode(csmCode);
        dagConsumerToken.setEnvCode(envCode);
        dagConsumerToken.setToken(token);
        dagConsumerToken.setDcjId(dagConsumerJwt.getDcjId());
        dagConsumerToken.setIatTime(String.valueOf(currSecond));
        dagConsumerToken.setExpTime(String.valueOf(expSecond));
        consumersDao.saveConsumerToken(dagConsumerToken);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createTokenByCode(CreateTokenVO param) {
        if (StringUtil.isNotBlank(param.getCsmCode()) && StringUtil.isNotBlank(param.getEnvCode())) {
            DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntByCsmCode(param.getCsmCode());
            if (dsgcConsumerEntities == null) {
                long currSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                Long expSecond = currSecond + Long.parseLong(accessTokenExpireTime);
                String iss = UUID.randomUUID().toString();
                String completeSecretKey = UUID.randomUUID().toString();
                //同步在网关中新建jwt auth信息
                JWTAuthBean jb = new JWTAuthBean();
                jb.setIssKey(iss);
                jb.setAlgorithm("HS256");
                jb.setCsmCode(dsgcConsumerEntities.getCsmCode());
                jb.setEnvCode(param.getEnvCode());
                jb.setSecretKey(completeSecretKey);
                boolean deployRes = false;
                try {
                    deployRes = this.consumerDeployService.addJWTAuth(jb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (deployRes) {
                    String token = JwtTokenUtil.generateToken(iss,expSecond,completeSecretKey);
                    if (StringUtils.isNotEmpty(token)) {
                        saveJwtInfo(iss,completeSecretKey,token,param.getEnvCode(),param.getCsmCode(),expSecond,currSecond);
                    } else {
                        throw new RuntimeException("生成token失败");
                    }
                } else {
                    throw new RuntimeException("访问网关失败！");
                }
            } else {
                throw new RuntimeException("消费者不存在！");
            }
        }
    }

    public PageQueryResult<ConsumerTokenVO> queryTokenList(CreateTokenVO param, int pageIndex, int pageSize) {
        String envCode = param.getEnvCode();
        PageQueryResult<ConsumerTokenVO> result = new PageQueryResult<>();
        if (StringUtil.isNotBlank(envCode)) {
            DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntById(param.getDceId());
            PageQueryResult<DagConsumerToken> pageQueryResult = consumersDao.queryTokenList(envCode, dsgcConsumerEntities.getCsmCode(), pageIndex, pageSize);
            result.setCount(pageQueryResult.getCount());
            Iterator<DagConsumerToken> iterator = pageQueryResult.getResult().iterator();
            List<ConsumerTokenVO> list = new ArrayList();
            while (iterator.hasNext()) {
                DagConsumerToken dagConsumerToken = iterator.next();
                ConsumerTokenVO consumerTokenVO = new ConsumerTokenVO();
                consumerTokenVO.setToken(dagConsumerToken.getToken());
                consumerTokenVO.setEnvName(dagConsumerToken.getEnvName());
                //   DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String expStr = secondToDate(Long.parseLong(dagConsumerToken.getExpTime()), "yyyy-MM-dd HH:mm:ss");
                //   LocalDateTime  expFormat = LocalDateTime.parse(dagConsumerToken.getExpTime(),dateTimeFormatter);
                consumerTokenVO.setExpTime(expStr);
                //    LocalDateTime  iatFormat = LocalDateTime.parse(dagConsumerToken.getIatTime(),dateTimeFormatter);
                String iatStr = secondToDate(Long.parseLong(dagConsumerToken.getIatTime()), "yyyy-MM-dd HH:mm:ss");
                consumerTokenVO.setIatTime(iatStr);
                Long duration = Long.parseLong(dagConsumerToken.getExpTime()) - Instant.now().getEpochSecond();
                String durationMinute = null;
                if(duration < 86400){
                    durationMinute = duration/3600 + "小时" + (duration%3600)/60 + "分" + (duration%3600)%60 + "秒";
                }else {
                    durationMinute = duration/86400 +"天" + (duration%86400)/3600 + "小时";
                }

               // String durationMinute = df.format(duration / (60 * 60));
                consumerTokenVO.setDuration(durationMinute);
                list.add(consumerTokenVO);
            }
            result.setResult(list);
        }
        return result;
    }

    public List<DagEnvInfoCfgVO> queryConsumerDeployEnv(String dceId){
        DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerDeployEnv(dceId);
        String envStr = dsgcConsumerEntities.getDeployEnv();
        List<DagEnvInfoCfgVO> result = new ArrayList<>();
        if(StringUtil.isNotBlank(envStr)){
            List<DSGCEnvInfoCfg> list =consumersDao.queryApiEnv();
            String[] deployEnvArr = envStr.split(",");
            for (int i = 0; i <deployEnvArr.length ; i++) {
                for (int j = 0; j < list.size(); j++) {
                    if(deployEnvArr[i].equals(list.get(j).getEnvCode())){
                        DagEnvInfoCfgVO dagEnvInfoCfgVO = new DagEnvInfoCfgVO();
                        dagEnvInfoCfgVO.setEnvCode(list.get(j).getEnvCode());
                        dagEnvInfoCfgVO.setEnvName(list.get(j).getEnvName());
                        result.add(dagEnvInfoCfgVO);
                    }
                }
            }
        }
    return result;

    }

    /**
     * 秒转换为指定格式的日期
     *
     * @param second
     * @param patten
     * @return
     */
    private String secondToDate(long second, String patten) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second * 1000);//转换为毫秒
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateString = format.format(date);
        return dateString;

    }
}
