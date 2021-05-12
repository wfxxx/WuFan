package com.definesys.dsgc.service.rpt;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DSGCServerReportDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    @Lazy
    private DSGCServerReportService serverReportService;
    public List<Map<String,Object>> getServerMsg(Map<String,String> map){
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
       String temp = serverReportService.timeCheck(startTime,endTime);
       System.out.println(temp);
       if("minute".equals(temp)){
           return sw
                   .buildQuery()
                   .sql("SELECT round(avg(server.MEM_USAGE),4) as mem_usage,round(avg(server.THREAD_USAGE),4) as thread_usage,round(max(server.MEM_TOTAL),4) as mem_total,round(avg(server.MEM_USE),4) as mem_use,\n" +
                           "round(max(server.EXECUTE_THREAD_TOTAL_COUNT),4) as execute_thread_total_count,\n" +
                           "round(max(server.ACTIVE_EXE_THREAD_COUNT),4) as active_exe_thread_count,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM dsgc_server_info_his server WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                   .setVar("serverName",map.get("serverName"))
                   .setVar("startTime",map.get("startTime"))
                   .setVar("endTime",map.get("endTime"))
                   .doQuery();
       }
        if("hour".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(server.MEM_USAGE),4) as mem_usage,round(avg(server.THREAD_USAGE),4) as thread_usage,round(max(server.MEM_TOTAL),4) as mem_total,round(avg(server.MEM_USE),4) as mem_use,\n" +
                            "round(max(server.EXECUTE_THREAD_TOTAL_COUNT),0) as execute_thread_total_count,\n" +
                            "round(max(server.ACTIVE_EXE_THREAD_COUNT),0) as active_exe_thread_count,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM dsgc_server_info_his server WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("day".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(server.MEM_USAGE),4) as mem_usage,round(avg(server.THREAD_USAGE),4) as thread_usage,round(max(server.MEM_TOTAL),4) as mem_total,round(avg(server.MEM_USE),4) as mem_use,\n" +
                            "round(max(server.EXECUTE_THREAD_TOTAL_COUNT),4) as execute_thread_total_count,\n" +
                            "round(max(server.ACTIVE_EXE_THREAD_COUNT),4) as active_exe_thread_count,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM dsgc_server_info_his server WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("month".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(server.MEM_USAGE),4) as mem_usage,round(avg(server.THREAD_USAGE),4) as thread_usage,round(max(server.MEM_TOTAL),4) as mem_total,round(avg(server.MEM_USE),4) as mem_use,\n" +
                            "round(max(server.EXECUTE_THREAD_TOTAL_COUNT),4) as execute_thread_total_count,\n" +
                            "round(max(server.ACTIVE_EXE_THREAD_COUNT),4) as active_exe_thread_count,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM dsgc_server_info_his server WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
       return null;

    }



//    public List<Map<String,Object>> getmachineCpuMsg(Map<String,String> map){
//        String startTime = map.get("startTime");
//        String endTime = map.get("endTime");
//        String temp = serverReportService.timeCheck(startTime,endTime);
//        System.out.println(temp);
//        if("minute".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_CPU_INFO cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("hour".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_CPU_INFO cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("day".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_CPU_INFO cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("month".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_CPU_INFO cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        return null;
//
//    }
//
//    public List<Map<String,Object>> getmachineNetMsg(Map<String,String> map){
//        String startTime = map.get("startTime");
//        String endTime = map.get("endTime");
//        String temp = serverReportService.timeCheck(startTime,endTime);
//        System.out.println(temp);
//        if("minute".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_NET_INFO net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("hour".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_NET_INFO net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("day".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_NET_INFO net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("month".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_NET_INFO net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        return null;
//
//    }
//
//
//
//    public List<Map<String,Object>> getmachineIoMsg(Map<String,String> map){
//        String startTime = map.get("startTime");
//        String endTime = map.get("endTime");
//        String temp = serverReportService.timeCheck(startTime,endTime);
//        System.out.println(temp);
//        if("minute".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_IO_INFO io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("hour".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_IO_INFO io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("day".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_IO_INFO io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("month".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_IO_INFO io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        return null;
//
//    }
//
//    public List<Map<String,Object>> getmachineDiskMsg(Map<String,String> map){
//        String startTime = map.get("startTime");
//        String endTime = map.get("endTime");
//        String temp = serverReportService.timeCheck(startTime,endTime);
//        System.out.println(temp);
//        if("minute".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(disk.USAGE) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_DISK_INFO disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("hour".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(disk.USAGE) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_DISK_INFO disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("day".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(disk.USAGE) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_DISK_INFO disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        if("month".equals(temp)){
//            return sw
//                    .buildQuery()
//                    .sql("SELECT avg(disk.USAGE) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_DISK_INFO disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
//                    .setVar("serverName",map.get("serverName"))
//                    .setVar("startTime",map.get("startTime"))
//                    .setVar("endTime",map.get("endTime"))
//                    .doQuery();
//        }
//        return null;
//
//    }
}
