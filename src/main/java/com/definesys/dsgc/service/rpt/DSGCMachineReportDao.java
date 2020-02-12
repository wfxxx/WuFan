package com.definesys.dsgc.service.rpt;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DSGCMachineReportDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    @Lazy
    private DSGCMachineReportService machineReportService;
    public List<Map<String,Object>> getMachineMemMsg(Map<String,String> map){
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
       String temp = machineReportService.timeCheck(startTime,endTime);
       if("minute".equals(temp)){
           return sw
                   .buildQuery()
                   .sql("SELECT avg(mem.MEM_FREE) as mem_free,avg(mem.MEM_USE) as mem_use,avg(mem.MEM_TOTAL) as mem_total,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_MEM_INFO_HIS mem WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                   .setVar("serverName",map.get("serverName"))
                   .setVar("startTime",map.get("startTime"))
                   .setVar("endTime",map.get("endTime"))
                   .doQuery();
       }
        if("hour".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(mem.MEM_FREE) as mem_free,avg(mem.MEM_USE) as mem_use,avg(mem.MEM_TOTAL) as mem_total,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_MEM_INFO_HIS mem WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("day".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(mem.MEM_FREE) as mem_free,avg(mem.MEM_USE) as mem_use,avg(mem.MEM_TOTAL) as mem_total,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_MEM_INFO_HIS mem WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("month".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(mem.MEM_FREE) as mem_free,avg(mem.MEM_USE) as mem_use,avg(mem.MEM_TOTAL) as mem_total,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_MEM_INFO_HIS mem WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
       return null;

    }



    public List<Map<String,Object>> getMachineCpuMsg(Map<String,String> map){
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        String temp = machineReportService.timeCheck(startTime,endTime);
        System.out.println(temp);
        if("minute".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_CPU_INFO_HIS cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("hour".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_CPU_INFO_HIS cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("day".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_CPU_INFO_HIS cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("month".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(cpu.USAGE),4) as cpu_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_CPU_INFO_HIS cpu WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName AND cpu.USAGE !='0.0' group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        return null;

    }

    public List<Map<String,Object>> getMachineNetMsg(Map<String,String> map){
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        String temp = machineReportService.timeCheck(startTime,endTime);
        System.out.println(temp);
        if("minute".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_NET_INFO_HIS net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("hour".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_NET_INFO_HIS net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("day".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_NET_INFO_HIS net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("month".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT round(avg(net.CUR_SPEC),6) as net_cur_spec,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_NET_INFO_HIS net WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        return null;

    }



    public List<Map<String,Object>> getMachineIoMsg(Map<String,String> map){
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        String temp = machineReportService.timeCheck(startTime,endTime);
        System.out.println(temp);
        if("minute".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_IO_INFO_HIS io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("hour".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_IO_INFO_HIS io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("day".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_IO_INFO_HIS io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("month".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(io.USAGE) as io_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_IO_INFO_HIS io WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        return null;

    }

    public List<Map<String,Object>> getMachineDiskMsg(Map<String,String> map){
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        String temp = machineReportService.timeCheck(startTime,endTime);
        System.out.println(temp);
        if("minute".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg() as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') as dates FROM DSGC_PC_DISK_INFO_HIS disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24:mi')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("hour".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(RTRIM(disk.USAGE,'%')) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') as dates FROM DSGC_PC_DISK_INFO_HIS disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd hh24')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("day".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(RTRIM(disk.USAGE,'%')) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') as dates FROM DSGC_PC_DISK_INFO_HIS disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd') order by to_char(LAST_UPDATE_DATE,'yyyy-mm-dd')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        if("month".equals(temp)){
            return sw
                    .buildQuery()
                    .sql("SELECT avg(RTRIM(disk.USAGE,'%')) as disk_usage,to_char(LAST_UPDATE_DATE,'yyyy-mm') as dates FROM DSGC_PC_DISK_INFO_HIS disk WHERE LAST_UPDATE_DATE >= to_date(#startTime,'yyyy-mm-dd hh24:mi:ss') AND LAST_UPDATE_DATE <= to_date(#endTime,'yyyy-mm-dd hh24:mi:ss') AND SERVER = #serverName group by to_char(LAST_UPDATE_DATE,'yyyy-mm') order by to_char(LAST_UPDATE_DATE,'yyyy-mm')")
                    .setVar("serverName",map.get("serverName"))
                    .setVar("startTime",map.get("startTime"))
                    .setVar("endTime",map.get("endTime"))
                    .doQuery();
        }
        return null;

    }
}
