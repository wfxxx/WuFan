package com.definesys.dsgc.service.ystar.fnd.property;

import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.dsgc.service.lkv.bean.QueryPropertyParamBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.utils.YStarUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FndPropertiesService {
    public static final String PROPERTY_KEY_ROOT_HOME = "ROOT_HOME";
    public static final String PROPERTY_KEY_DSGC_HOME = "DSGC_HOME";
    public static final String PROPERTY_KEY_MULE_HOME = "MULE_HOME";
    public static final String PROPERTY_KEY_MAVEN_HOME = "MAVEN_HOME";
    public static final String PROPERTY_KEY_GIT_HOME = "GIT_HOME";
    public static final String PROPERTY_KEY_REPO_HOME = "REPO_HOME";
    @Autowired
    FndPropertiesDao fndPropertiesDao;

    @Transactional
    public String createFndProperties(FndProperties fndProperties) {
        return fndPropertiesDao.createFndProperties(fndProperties);
    }

    @Transactional
    public String updateFndProperties(FndProperties fndProperties) {
        return fndPropertiesDao.updateFndProperties(fndProperties);
    }

    @Transactional
    public String deleteFndPropertiesById(FndProperties fndProperties) {
        return fndPropertiesDao.deleteFndPropertiesById(fndProperties);
    }

    public PageQueryResult<FndProperties> query(FndProperties fndProperties, int pageSize, int pageIndex) {
        return fndPropertiesDao.query(fndProperties, pageSize, pageIndex, "and");
    }

    public PageQueryResult<FndProperties> queryOr(FndProperties fndProperties, int pageSize, int pageIndex) {
        return fndPropertiesDao.query(fndProperties, pageSize, pageIndex, "or");
    }

    public FndProperties findFndPropertiesById(FndProperties fndProperties) {
        return fndPropertiesDao.findFndPropertiesById(fndProperties);
    }

    public List<FndProperties> findFndPropertiesByListKey(String[] listKey) {
        return fndPropertiesDao.findFndPropertiesByListKey(listKey);
    }

    public boolean checkPropertiesKeyIsExist(FndProperties properties) {
        return fndPropertiesDao.checkPropertiesKeyIsExist(properties);
    }

    public FndProperties findFndPropertiesByKey(String key) {
        return fndPropertiesDao.findFndPropertiesByKey(key);
    }

    public List<QueryPropertyParamBean> queryProperties(QueryPropertyParamBean property) {
        return this.fndPropertiesDao.queryProperties(property);
    }

    /*** 获取主目录信息**/
    public String getLocalHome(String key) {
        String rootHome = this.fndPropertiesDao.queryPropertyByKey(PROPERTY_KEY_ROOT_HOME);
        String home;
        switch (key) {
            case "dsgc":
                home = this.fndPropertiesDao.queryPropertyByKey(PROPERTY_KEY_DSGC_HOME);
                if (StringUtil.isBlank(home)) {
                    rootHome += "/dsgcHome/dsgc-tomcat";
                } else {
                    rootHome = home;
                }
                break;
            case "mule":
                home = this.fndPropertiesDao.queryPropertyByKey(PROPERTY_KEY_MULE_HOME);
                if (StringUtil.isBlank(home)) {
                    rootHome += "/muleHome/mule-tomcat";
                } else {
                    rootHome = home;
                }
                break;
            case "maven":
                home = this.fndPropertiesDao.queryPropertyByKey(PROPERTY_KEY_MAVEN_HOME);
                if (StringUtil.isBlank(home)) {
                    rootHome += "/mavenHome/apache-maven";
                } else {
                    rootHome = home;
                }
                break;
            case "git":
                home = this.fndPropertiesDao.queryPropertyByKey(PROPERTY_KEY_GIT_HOME);
                if (StringUtil.isBlank(home)) {
                    rootHome += "/gitHome";
                } else {
                    rootHome = home;
                }
                break;
            case "repo":
                home = this.fndPropertiesDao.queryPropertyByKey(PROPERTY_KEY_REPO_HOME);
                if (StringUtil.isBlank(home)) {
                    rootHome += "/gitHome/local-repo";
                } else {
                    rootHome = home;
                }
                break;
            default:
                break;
        }
        return rootHome;
    }
}
