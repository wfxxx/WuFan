package com.definesys.dsgc.service.ystar.svcgen.bean;


import com.definesys.dsgc.service.svclog.bean.DSGCValidResult;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.definesys.mpaas.query.session.MpaasSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhenglong
 * @Description:
 * @Date 2019/3/13 16:30
 */
@Repository("dsgc_service")
public class DSGCServiceDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Autowired
    private SWordLogger logger;

    /**
     * 新增数据
     *
     * @param ds
     * @param as
     * @return
     */
    public String insertDsgcService(DSGCService ds, List<DSGCServicesUri> as, List<DSGCTab> tags) {
        if (ds != null && as != null && as.size() > 0 && tags != null && tags.size() > 0) {
            sw.buildQuery()
                    .eq("servNo", ds.getServNo())
                    .doDelete(DSGCService.class);
            sw.buildQuery()
                    .eq("servNo", ds.getServNo())
                    .doDelete(DSGCServicesUri.class);
            sw.buildQuery()
                    .eq("servNo", ds.getServNo())
                    .doDelete(DSGCTabServ.class);

            sw.buildQuery()
                    .doInsert(ds);
            for (DSGCServicesUri uri : as) {
                sw.buildQuery()
                        .doInsert(uri);
            }

            for (DSGCTab tab : tags) {
                if (tab.getId() == null) {
                    DSGCTab tempTab = this.sw.buildQuery()
                            .eq("name", tab.getName())
                            .doQueryFirst(DSGCTab.class);
                    String id = "";
                    if (tempTab == null) {
                        this.sw.buildQuery()
                                .doInsert(tab);
                        DSGCTab tempTab2 = this.sw.buildQuery()
                                .eq("name", tab.getName())
                                .doQueryFirst(DSGCTab.class);
                        id = tempTab2.getId();
                    } else {
                        id = tempTab.getId();
                    }
                    tab.setId(id);
                }
                sw.buildQuery()
                        .doInsert(new DSGCTabServ(tab.getId(), ds.getServNo()));
            }
        }
        return ds.getServNo();
    }

    /**
     * update data
     *
     * @param ds
     * @param as
     * @return
     */
    public String updateDsgcService(DSGCService ds, List<DSGCServicesUri> as, List<DSGCTab> tags) {
        DSGCService temp = sw.buildQuery()
                .eq("servNo", ds.getServNo())
                .doQueryFirst(DSGCService.class);
        ds.setBodyStoreType(temp.getBodyStoreType());
        ds.setNtyPolicy(temp.getNtyPolicy());
        ds.setLogPurge(temp.getLogPurge());
        ds.setLogDetail(temp.getLogDetail());
        sw.buildQuery()
                .eq("servNo", ds.getServNo())
                .doUpdate(ds);
//        sw.buildQuery()
//                .eq("servNo", ds.getServNo())
//                .doDelete(DSGCValidResult.class);
        sw.buildQuery()
                .eq("servNo", ds.getServNo())
                .doDelete(DSGCServicesUri.class);

        sw.buildQuery()
                .eq("servNo", ds.getServNo())
                .doDelete(DSGCTabServ.class);
        if (as != null && as.size() > 0) {
            for (int i = 0; i < as.size(); i++) {
                sw.buildQuery()
                        .doInsert(as.get(i));
            }
        }

        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                DSGCTab tab = tags.get(i);
                System.out.println(tab.toString());
                DSGCTabServ tabServ = new DSGCTabServ();
                tabServ.setServNo(ds.getServNo());
                if (tab.getId() == null) {
                    DSGCTab tempTab = this.sw.buildQuery()
                            .eq("name", tab.getName())
                            .doQueryFirst(DSGCTab.class);
                    String id = "";
                    if (tempTab == null) {
                        this.sw.buildQuery()
                                .doInsert(tab);
                        DSGCTab tempTab2 = this.sw.buildQuery()
                                .eq("name", tab.getName())
                                .doQueryFirst(DSGCTab.class);
                        id = tempTab2.getId();
                    } else {
                        id = tempTab.getId();
                    }
                    tab.setId(id);
                }
                tabServ.setTabId(tab.getId());
                sw.buildQuery()
                        .doInsert(tabServ);
            }
        }
        return ds.getServNo();
    }

    /**
     * delete
     *
     * @param
     * @return
     */
    public String deleteById(DSGCService ds, DSGCValidResult as) {

        sw.buildQuery()
                .eq("servNo", as.getServNo())
                .doDelete(as);
        sw.buildQuery()
                .eq("servNo", ds.getServNo())
                .doDelete(DSGCValidResult.class);
        return ds.getServNo();
    }


    public PageQueryResult<DSGCServiceVO> findAll(int pageIndex, int pageSize) {
        return sw.buildQuery()
                .sql("select  ds.* ,dr.system_code,dr.valid_type,dr.valid_column,dr.valid_value  from dsgc_services  ds ,dsgc_valid_result dr where ds.serv_no = dr.serv_no")
                .doPageQuery(pageIndex, pageSize, DSGCServiceVO.class);
    }

    public PageQueryResult<DSGCService> query(String isAdmin, DSGCService service, int pageIndex, int pageSize) {
        if ("SuperAdministrators".equals(isAdmin) || "Administrators".equals(isAdmin) || "SystemLeader".equals(isAdmin)) {
            return sw.buildQuery()
                    .like("servNo", service.getServNo())
                    .like("servName", service.getServName())
                    .like("servStatus", service.getServStatus())
                    .like("servTempLate", service.getServTempLate())
                    .like("dataDecode", service.getDataDecode())
                    .like("servDesc", service.getServDesc())
                    .doPageQuery(pageIndex, pageSize, DSGCService.class);
        }
        return sw.buildQuery()
                .sql("SELECT * FROM ( SELECT ser.* FROM dsgc_services ser, dsgc_service_user su WHERE (ser.serv_no = su.serv_no(+) AND su.user_id = #userId AND su.is_show = 'Y') )")
                .like("servNo", service.getServNo())
                .like("servName", service.getServName())
                .like("servStatus", service.getServStatus())
                .like("servTempLate", service.getServTempLate())
                .like("dataDecode", service.getDataDecode())
                .like("servDesc", service.getServDesc())
                .setVar("userId", MpaasSession.getCurrentUser())
                .doPageQuery(pageIndex, pageSize, DSGCService.class);
    }

    public DSGCService findServiceByServNo(String servNo) {
        DSGCService re = sw.buildQuery()
                .eq("servNo", servNo)
                .doQueryFirst(DSGCService.class);
        return re;

    }

}