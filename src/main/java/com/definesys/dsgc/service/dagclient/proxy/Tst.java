package com.definesys.dsgc.service.dagclient.proxy;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.dagclient.proxy.bean.BasicAuthPluginCfgVO;
import com.definesys.dsgc.service.dagclient.proxy.bean.PluginSettingVO;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;

import java.util.HashMap;
import java.util.Map;

public class Tst {

    public static void main(String[] args){
        String adminUrl = "http://esb.definesys.com:8801";
        try {

//            PluginsProxy p = new PluginsProxy(adminUrl,PluginsProxy.PLUGIN_TARGET_ROUTE,"317765cf-b192-4105-86eb-363ef90cbf61");
//
//            Map<String,PluginSettingVO> pluginCfg = new HashMap<String,PluginSettingVO>();
//            PluginSettingVO auth = new PluginSettingVO();
//            auth.setEnabled(true);
//            BasicAuthPluginCfgVO bcfg = new BasicAuthPluginCfgVO();
//            auth.setConfig(bcfg);
//            pluginCfg.put("basic-auth",auth);
//            p.setPlugins(pluginCfg,true);

            String s = "sfsfs";
            System.out.println(s.indexOf("111"));
//            PluginsProxy p = new PluginsProxy(adminUrl,PluginsProxy.PLUGIN_TARGET_ROUTE,"317765cf-b192-4105-86eb-363ef90cbf61");
//            p.delete("basic-auth");

//            BasicAuthPluginCfgVO b = new BasicAuthPluginCfgVO();
//            b.setSs("sfsfsf");
//
//            PluginConfigVO s = b;
//
//            System.out.println(JSONObject.toJSONString(s));

//            String json = "{\n" +
//                    //"  \"created_at\": 1585105544,\n" +
//                    "  \"id\": \"c95fb639-a509-4398-bdf0-8d9ac037ffc0\",\n" +
//                    "  \"config\": {\n" +
//                    "    \"hide_credentials\": true,\n" +
//                    "  \"enabled\": false,\n" +
//                    "    \"anonymous\": null\n" +
//                    "  },\n" +
//                    "  \"name\": \"basic-auth\",\n" +
//
//                    //"  \"service\": null,\n" +
//                    "  \"enabled\": true,\n" +
////                    "  \"protocols\": [\n" +
////                    "    \"grpc\",\n" +
////                    "    \"grpcs\",\n" +
////                    "    \"http\",\n" +
////                    "    \"https\"\n" +
////                    "  ],\n" +
//
//                    //"  \"consumer\": null,\n" +
//                    "  \"route\": {\n" +
//                    "    \"id\": \"317765cf-b192-4105-86eb-363ef90cbf61\"\n" +
//                    "  }\n" +
//                   // "  \"tags\": null\n" +
//                    "}";
//
//            HttpReqUtil.putJsonText(adminUrl+"/plugins/c95fb639-a509-4398-bdf0-8d9ac037ffc0",json);

          //  ConsumerProxy cp = new ConsumerProxy(adminUrl,"tstConsumer5");
          // System.out.println(cp.setBasicAuth("fffffffgggffffff"));
            //cp.add("tstConsumer5",new String[]{"remoteTst","demo"});
//            Set<String> groups = new HashSet<String>();
//            groups.add("sfsfsfs");
//            groups.add("sfsfsf1s");
//            groups.add("sfsfsf3s");
//            groups.add("sfsfsfs4");
//            cp.removeGroups(groups);
           // cp.removeGroup(null);

//
//            ServiceProxy sp = new ServiceProxy(adminUrl,"sfsfsfsfsfs1");
//
//
//            String serviceJsonType = "{\"host\":\"localhost2\",\"connect_timeout\":60001,\"protocol\":\"http\",\"name\":\"sfsfsfsfsfs1\",\"read_timeout\":60000,\"port\":8001,\"path\":\"/sfsf/sfsf\",\"retries\":5,\"write_timeout\":60000,\"tags\":[]}";
//
//            ServiceProxy.ServiceUpdateVO req = JSONObject.parseObject(serviceJsonType,ServiceProxy.ServiceUpdateVO.class);
//
//            sp.setService(req);
//            sp.delete();


//            //路由设置
//            String jsonText = "{\n" +
//                    "            \n" +
//                    "                \"paths\": [\n" +
//                    "            \"/sss/aaa/sfsfs\"\n" +
//                    "  ],\n" +
//                    "                \"protocols\": [\n" +
//                    "            \"http\",\n" +
//                    "                    \"https\"\n" +
//                    "  ],\n" +
//                    "            \"methods\": [\n" +
//                    "            \"POST\"\n" +
//                    "  ],\n" +
//                    "                \"service\": {\n" +
//                    "            \"id\": \"ef700cc7-ecae-4dff-b257-76b3fe616f54\"\n" +
//                    "        },\n" +
//                    "            \"name\": \"wodesfsfs\",\n" +
//                    "                \"strip_path\": true,\n" +
//                    "                \"preserve_host\": false,\n" +
//                    "                \"regex_priority\": 0,\n" +
//                    "                \"updated_at\": 1582482664,\n" +
//                    "                \"sources\": null,\n" +
//                    "                \"hosts\": [\n" +
//                    "            \"localhost\"\n" +
//                    "  ],\n" +
//                    "            \"https_redirect_status_code\": 426,\n" +
//                    "                \"tags\": null                \n" +
//                    "        }";
//
//
//            RouteProxy route = new RouteProxy(adminUrl,"wodesfsfs");
////            RouteProxy.RouteSetVO routReq =  JSONObject.parseObject(jsonText,RouteProxy.RouteSetVO.class);
////            route.setRoute(routReq);
//            route.delete();

//            String upsteamName = "TestUpstream1";
//            UpstreamProxy us = new UpstreamProxy(adminUrl,upsteamName);
//
//            UpstreamProxy.UpstreamSetVO req = new UpstreamProxy.UpstreamSetVO();
//            req.name = upsteamName;
//            UpstreamProxy.TargetVO targ1 = new UpstreamProxy.TargetVO();
//            targ1.tags = new String[]{"targ1","geege"};
//            targ1.target = "10.10.10.10:8888";
//            targ1.weight = 110;
//            UpstreamProxy.TargetVO targ2 = new UpstreamProxy.TargetVO();
//            targ2.tags = new String[]{"targ2","geege"};
//            targ2.target = "10.10.10.11:8888";
//            targ2.weight = 101;
//
//            req.targets = new ArrayList<>();
//            req.targets.add(targ1);
//           // req.targets.add(targ2);
//
//            us.setUpstream(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        {"created_at":1582281361,"consumer":{"id":"27f05990-b18b-4970-adaf-35320cc7a056"},"id":"46c9d11f-9253-4d0f-9fa9-1a55004cab2f","tags":null,"password":"863ed7016bde729fc90d3ea49bd29f8ab9aaf54b","username":"tstConsumer3f3"}
//        true




    }
}
