package com.definesys.dsgc.service.wls;

import com.definesys.dsgc.service.wls.bean.WeblogicDataSourceInfo;
import com.definesys.dsgc.service.wls.bean.WeblogicServesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.management.ObjectName;
import java.util.*;

@Component
public class WeblogicInfoComponent {
	
	@Autowired
	private WeblogicServerInfoService weblogicServerInfoService;
	
	//dev
	@Value("${wldevserver.hostname}")
	private String devhostName;
	@Value("${wldevserver.portname}")
	private String devportName;
	@Value("${wldevserver.username}")
	private String devuserName;
	@Value("${wldevserver.password}")
	private String devpassword;
	
	/**
	 * 获取实时服务器 信息
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getServerInfo() throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> alllist=new ArrayList<String>();
		List<WeblogicServesInfo> server=new ArrayList<WeblogicServesInfo>();
		List<String> run=new ArrayList<String>();
		List<String> error=new ArrayList<String>();
		//连接weblogic
		weblogicServerInfoService.initConnection(devhostName, devportName, devuserName, devpassword);
		//获取所有服务器
		ObjectName[] allServerRT= weblogicServerInfoService.getServer();
		for(int i=0;i<allServerRT.length;i++) {
			String allServerName= weblogicServerInfoService.getServerName(allServerRT[i]);
			alllist.add(allServerName);
		}
		//获取runtime的服务器
		ObjectName[] serverRT= weblogicServerInfoService.getServerRuntimes();
		for(int j=0;j<serverRT.length;j++) {
			 run.add(weblogicServerInfoService.getServerName(serverRT[j]));
			 server.add(weblogicServerInfoService.getServerRuntimeForFirstPage(serverRT[j]));
		}
		//获取数据源使用占比Top5
		List<WeblogicDataSourceInfo> dataSourceInfoList = weblogicServerInfoService.getAllDataSource();
		error.addAll(alllist);
		error.removeAll(run);
		Collections.sort(error);
		Collections.sort(server);
		Collections.sort(alllist);
		
		map.put("allserverName", alllist);
		map.put("serverInfo", server);
		map.put("dataSource", dataSourceInfoList);
		map.put("error", error);
		//关闭连接
		WeblogicServerInfoService.connector.close();
		return map;
	}


	public Map<String,Object> getServer() throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> alllist=new ArrayList<String>();
		List<String> error=new ArrayList<String>();

		//连接weblogic
		weblogicServerInfoService.initConnection(devhostName, devportName, devuserName, devpassword);

		//获取所有服务器
		ObjectName[] allServerRT= weblogicServerInfoService.getServer();
		for(int i=0;i<allServerRT.length;i++) {
			String allServerName= weblogicServerInfoService.getServerName(allServerRT[i]);
			alllist.add(allServerName);
		}

		error.addAll(alllist);
		Collections.sort(error);
		Collections.sort(alllist);

		map.put("allserverName", alllist);
		map.put("error", error);
		//关闭连接
		WeblogicServerInfoService.connector.close();
		return map;
	}
}
