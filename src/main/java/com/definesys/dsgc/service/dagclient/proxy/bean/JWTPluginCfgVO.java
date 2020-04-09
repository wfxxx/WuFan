package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class JWTPluginCfgVO {
    private boolean secret_is_base64 = false;
    private boolean run_on_preflight = true;
    private List<String> uri_param_names;
    private String key_claim_name = "iss";
    private List<String> header_names;
    private Long maximum_expiration = 0L;
    private String anonymous;
    private List<String> claims_to_verify;
    private List<String> cookie_names;

    public boolean isSecret_is_base64() {
        return secret_is_base64;
    }

    public void setSecret_is_base64(boolean secret_is_base64) {
        this.secret_is_base64 = secret_is_base64;
    }

    public boolean isRun_on_preflight() {
        return run_on_preflight;
    }

    public void setRun_on_preflight(boolean run_on_preflight) {
        this.run_on_preflight = run_on_preflight;
    }

    public List<String> getUri_param_names() {
        return uri_param_names;
    }

    public void setUri_param_names(List<String> uri_param_names) {
        this.uri_param_names = uri_param_names;
    }

    public String getKey_claim_name() {
        return key_claim_name;
    }

    public void setKey_claim_name(String key_claim_name) {
        this.key_claim_name = key_claim_name;
    }

    public List<String> getHeader_names() {
        return header_names;
    }

    public void setHeader_names(List<String> header_names) {
        this.header_names = header_names;
    }

    public Long getMaximum_expiration() {
        return maximum_expiration;
    }

    public void setMaximum_expiration(Long maximum_expiration) {
        this.maximum_expiration = maximum_expiration;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public List<String> getClaims_to_verify() {
        return claims_to_verify;
    }

    public void setClaims_to_verify(List<String> claims_to_verify) {
        this.claims_to_verify = claims_to_verify;
    }

    public List<String> getCookie_names() {
        return cookie_names;
    }

    public void setCookie_names(List<String> cookie_names) {
        this.cookie_names = cookie_names;
    }
}
