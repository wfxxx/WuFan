package com.definesys.dsgc.service.rpt.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;
import com.definesys.mpaas.query.annotation.Table;

/**
 *  拓扑图视图
 */


@SQLQuery(value = {
        @SQL(view = "DSGC_TOPOLOGY_V",
                sql = " select D.SERV_NO,d.serv_name,d.sub_system SUBORDINATE_SYSTEM,d.req_from SYS_CODE,d.inst_status from " +
                        "    (SELECT max(B.creation_date) max_date,B.req_from req_system,B.sub_system FROM " +
                        "        (SELECT A.*,ds.SUBORDINATE_SYSTEM sub_system FROM " +
                        "            (SELECT * FROM dsgc_log_instance dli WHERE dli.serv_no <> 'N/A' and dli.REQ_FROM in " +
                        "                (SELECT CSM_CODE from dsgc_consumer_entities) " +
                        "                    and dli.INST_STATUS in(0,1)) A " +
                        "                LEFT JOIN dsgc_services ds ON A.serv_no = ds.SERV_NO ) B GROUP BY B.sub_system,req_from) C " +
                        "        left join " +
                        "    (SELECT A.*,ds.SUBORDINATE_SYSTEM sub_system FROM " +
                        "        (SELECT * FROM dsgc_log_instance dli WHERE dli.serv_no <> 'N/A' and dli.REQ_FROM in " +
                        "            (SELECT CSM_CODE from dsgc_consumer_entities) " +
                        "               and dli.INST_STATUS in(0,1)) A " +
                        "            LEFT JOIN dsgc_services ds ON A.serv_no = ds.SERV_NO ) D " +
                        "    on C.req_system = D.REQ_FROM and C.sub_system = D.sub_system  and C.max_date = D.CREATION_DATE  "
        ),
        @SQL(view = "DSGC_TOPOLOGY_lasthour_V",
                sql = "select D.SERV_NO,d.serv_name,d.sub_system SUBORDINATE_SYSTEM,d.req_from SYS_CODE,d.inst_status from " +
                        "    (SELECT max(B.creation_date) max_date,B.req_from req_system,B.sub_system FROM " +
                        "        (SELECT A.*,ds.SUBORDINATE_SYSTEM sub_system FROM\n" +
                        "            (SELECT * FROM dsgc_log_instance dli WHERE dli.serv_no != 'N/A' and dli.REQ_FROM in\n" +
                        "                (SELECT CSM_CODE from dsgc_consumer_entities)\n" +
                        "                    and dli.INST_STATUS in(0,1)) A\n" +
                        "                LEFT JOIN dsgc_services ds ON A.serv_no = ds.SERV_NO ) B GROUP BY B.sub_system,req_from) C\n" +
                        "        left join\n" +
                        "    (SELECT A.*,ds.SUBORDINATE_SYSTEM sub_system FROM\n" +
                        "        (SELECT * FROM dsgc_log_instance dli WHERE dli.serv_no != 'N/A' and dli.REQ_FROM in\n" +
                        "            (SELECT CSM_CODE from dsgc_consumer_entities)\n" +
                        "               and dli.INST_STATUS in(0,1)) A\n" +
                        "            LEFT JOIN dsgc_services ds ON A.serv_no = ds.SERV_NO ) D\n" +
                        "    on C.req_system = D.REQ_FROM and C.sub_system = D.sub_system  and C.max_date = D.CREATION_DATE "
        )
})
@Table(value = "system_relation")
public class TopologyVO {


    private String id;

    private String servNo;

    private  String servName;

    private String subordinateSystem;

    private String sysCode;

    private String servDesc;

    private String instStatus;

    private String isInterface; //控制查看接口


    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getSubordinateSystem() {
        return subordinateSystem;
    }

    public void setSubordinateSystem(String subordinateSystem) {
        this.subordinateSystem = subordinateSystem;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getServDesc() {
        return servDesc;
    }

    public void setServDesc(String servDesc) {
        this.servDesc = servDesc;
    }

    public String getIsInterface() {
        return isInterface;
    }

    public void setIsInterface(String isInterface) {
        this.isInterface = isInterface;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getInstStatus() {
        return instStatus;
    }

    public void setInstStatus(String instStatus) {
        this.instStatus = instStatus;
    }

    @Override
    public String toString() {
        return "TopologyVO{" +
                "servNo='" + servNo + '\'' +
                ", servName='" + servName + '\'' +
                ", subordinateSystem='" + subordinateSystem + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", servDesc='" + servDesc + '\'' +
                ", instStatus='" + instStatus + '\'' +
                ", isInterface='" + isInterface + '\'' +
                '}';
    }
}
