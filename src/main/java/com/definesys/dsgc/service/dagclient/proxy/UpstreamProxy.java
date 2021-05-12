package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;

import java.util.Iterator;
import java.util.List;

public class UpstreamProxy extends DAGProxy {

    private String baseUrl;

    public UpstreamProxy(String admnUrl,String refId) {
        super(admnUrl,refId);
    }

    public boolean setUpstream(UpstreamSetVO req) {
        String jsonReq = "{\"name\":\"" + req.name + "\"}";
        if (!this.isFound()) {
            //网关中不存在，执行新增；
            this.entityProxy = HttpReqUtil.postJsonText(this.baseUrl,jsonReq,UpstreamEntity.class);
        } else {
            //网关中存在，执行更新
            HttpReqUtil.putJsonText(this.baseUrl + "/" + this.getId(),jsonReq,UpstreamEntity.class);
        }

        //更新target
        this.setTarget(req.targets);

        return true;
    }

    /**
     * 设置upstream 的targets
     * @param newTargets
     * @return
     */
    public boolean setTarget(List<TargetVO> newTargets) {
        TargetListEntity dagTargets = HttpReqUtil.getObject(this.baseUrl + "/" + this.getId() + "/targets",TargetListEntity.class);
        this.createOrUpdateTargets(dagTargets,newTargets);
        this.deleteNoExistTargets(dagTargets,newTargets);
        return true;
    }

    /**
     * 比较本地与网关的target，更新网关与本地不一样的target
     * @param dagTargets
     * @param newTargets
     */
    private void createOrUpdateTargets(TargetListEntity dagTargets,List<TargetVO> newTargets) {
        if (newTargets != null) {
            Iterator<TargetVO> newIters = newTargets.iterator();
            while(newIters.hasNext()){
                TargetVO newTarget = newIters.next();
                int operType = 0;
                String findTargetId = null;
                if (dagTargets != null && dagTargets.data != null && dagTargets.data.size() > 0) {

                    Iterator<TargetEntity> iter = dagTargets.data.iterator();
                    while (iter.hasNext()) {
                        TargetEntity te = iter.next();
                        if(te.target.equals(newTarget.target) && te.weight != newTarget.weight){
                            //update opertaion
                            operType = 2;
                            findTargetId = te.id;
                            break;
                        }
                    }

                } else {
                    //add opertion
                    operType = 1;
                }

                if(operType == 1){
                    HttpReqUtil.postObject(this.baseUrl+"/"+this.getId()+"/targets",newTarget,TargetEntity.class);
                } else if(operType == 2){
                    HttpReqUtil.putObject(this.baseUrl+"/"+this.getId()+"/targets/"+findTargetId,newTarget,TargetEntity.class);
                }
            }
        }
    }

    /**
     * 删除网关多余的targets
     * @param dagTargets
     * @param newTargets
     */
    private void deleteNoExistTargets(TargetListEntity dagTargets,List<TargetVO> newTargets) {
        if(dagTargets != null && dagTargets.data != null && dagTargets.data.size() > 0){
            Iterator<TargetEntity> iters = dagTargets.data.iterator();
            while(iters.hasNext()){
                TargetEntity te = iters.next();
                boolean needDelete = true;
                if(newTargets != null && newTargets.size() >0){
                    Iterator<TargetVO> newIters = newTargets.iterator();
                    while(newIters.hasNext()){
                        TargetVO newTarget = newIters.next();
                        if(te.target.equals(newTarget.target)){
                            needDelete = false;
                            break;
                        }
                    }
                }

                if(needDelete){
                    HttpReqUtil.delete(this.baseUrl+"/"+this.getId()+"/targets/"+te.id);
                }
            }
        }
    }

    @Override
    protected void retrieve(String refId) {
        this.baseUrl = this.adminUrl + "/upstreams";
        this.entityProxy = HttpReqUtil.getObject(this.baseUrl + "/" + refId,UpstreamEntity.class);

    }

    @Override
    public void delete() {
        HttpReqUtil.delete(this.baseUrl + "/" + this.getId());
    }

    public static class UpstreamEntity {
        public String hash_on;
        public String id;
        public String algorithm;
        public String name;
        public String[] tags;
        public String hash_fallback_header;
        public String hash_fallback;
        public String hash_on_cookie;
        public String host_header;
        public String hash_on_cookie_path;

    }

    public static class TargetListEntity {
        public List<TargetEntity> data;
    }

    public static class TargetEntity {
        public String id;
        public long created_at;
        public String target;
        public int weight;
        public String[] tags;
    }


    public static class UpstreamSetVO {
        public String name;
        public List<TargetVO> targets;
    }

    public static class TargetVO {
        public String target;
        public int weight;
        public String[] tags;
    }


//    public static class UpstreamSetVO{
//        public String hash_on = "none";
//        public String id;
//        public String algorithm = "round-robin";
//        public String name;
//        public String[] tags;
//        public String hash_fallback_header;
//        public String hash_fallback = "none";
//        public String hash_on_cookie;
//        public String host_header;
//        public String hash_on_cookie_path = "/";
//
//    }
//
//    {
//        "created_at": 1582515857,
//            "hash_on": "none",
//            "id": "28631a61-3285-42e0-8c34-3361957a4c15",
//            "algorithm": "round-robin",
//            "name": "aadadad",
//            "tags": null,
//            "hash_fallback_header": null,
//            "hash_fallback": "none",
//            "hash_on_cookie": null,
//            "host_header": null,
//            "hash_on_cookie_path": "/",
//            "healthchecks": {
//        "threshold": 0,
//                "active": {
//            "unhealthy": {
//                "http_statuses": [
//                429,
//                        404,
//                        500,
//                        501,
//                        502,
//                        503,
//                        504,
//                        505
//        ],
//                "tcp_failures": 0,
//                        "timeouts": 0,
//                        "http_failures": 0,
//                        "interval": 0
//            },
//            "type": "http",
//                    "http_path": "/",
//                    "timeout": 1,
//                    "healthy": {
//                "successes": 0,
//                        "interval": 0,
//                        "http_statuses": [
//                200,
//                        302
//        ]
//            },
//            "https_sni": null,
//                    "https_verify_certificate": true,
//                    "concurrency": 10
//        },
//        "passive": {
//            "unhealthy": {
//                "http_failures": 0,
//                        "http_statuses": [
//                429,
//                        500,
//                        503
//        ],
//                "tcp_failures": 0,
//                        "timeouts": 0
//            },
//            "healthy": {
//                "http_statuses": [
//                200,
//                        201,
//                        202,
//                        203,
//                        204,
//                        205,
//                        206,
//                        207,
//                        208,
//                        226,
//                        300,
//                        301,
//                        302,
//                        303,
//                        304,
//                        305,
//                        306,
//                        307,
//                        308
//        ],
//                "successes": 0
//            },
//            "type": "http"
//        }
//    },
//        "hash_on_header": null,
//            "slots": 1000
//    }
}
