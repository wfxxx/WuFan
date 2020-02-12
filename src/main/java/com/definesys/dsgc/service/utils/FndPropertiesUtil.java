package com.definesys.dsgc.service.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FndPropertiesUtil
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String OracleServerUrl = "OracleServerUrl";
    public static final String OracleUsername = "OracleUsername";
    public static final String OraclePassword = "OraclePassword";
    public static final String OracleHome = "OracleHome";
    public static final String SvnDirPath = "SvnDirPath";
    public static final String DeployOutputDirPath = "DeployOutputDirPath";
    public static final String BakDirPath = "BakDirPath";
    public static final String CdcXqueryDirPath = "CdcXqueryDirPath";
    public static final String ProjectBakDirPath = "ProjectBakDirPath";
    public static final String ServInfoPath = "ServInfoPath";
    public static final String ConfigCacheProjectName = "ConfigCacheProjectName";
    private Map<String, String> propertyMap = new HashMap();

    public void setPropertyValue(String key, String value) {
        if ((key != null) && (!"".equalsIgnoreCase(key.trim())) && (value != null) && (!"".equalsIgnoreCase(value.trim())))
            this.propertyMap.put(key, value);
    }

    public String getPropertyValue(String key)
    {
        if ((key != null) && (!"".equalsIgnoreCase(key.trim())) && (this.propertyMap.containsKey(key))) {
            return (String)this.propertyMap.get(key);
        }
        return null;
    }

    public String getCdcXqueryDirPath()
    {
        return (String)this.propertyMap.get("CdcXqueryDirPath");
    }

    public String getOracleServerUrl() {
        return (String)this.propertyMap.get("OracleServerUrl");
    }

    public String getOracleUsername() {
        return (String)this.propertyMap.get("OracleUsername");
    }

    public String getOraclePassword() {
        return (String)this.propertyMap.get("OraclePassword");
    }

    public String getOracleHome() {
        return (String)this.propertyMap.get("OracleHome");
    }

    public String getSvnDirPath() {
        return (String)this.propertyMap.get("SvnDirPath");
    }

    public String getDeployOutputDirPath() {
        return (String)this.propertyMap.get("DeployOutputDirPath");
    }

    public String getBakDirPath() {
        return (String)this.propertyMap.get("BakDirPath");
    }

    public String getProjectBakDirPath() {
        return (String)this.propertyMap.get("ProjectBakDirPath");
    }
    public String getServInfoPath() {
        return (String)this.propertyMap.get("ServInfoPath");
    }
}