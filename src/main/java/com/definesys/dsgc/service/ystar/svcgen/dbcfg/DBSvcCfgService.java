package com.definesys.dsgc.service.ystar.svcgen.dbcfg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.DBConnVO;
import com.definesys.dsgc.service.ystar.svcgen.dbcfg.bean.DBDeployProfileBean;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.dbcfg.bean.Tree;
import com.definesys.dsgc.service.ystar.svcgen.dbcfg.bean.TreeNode;
import com.definesys.dsgc.service.ystar.utils.JdbcConnection;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service("DBSvcCfgService")
public class DBSvcCfgService {

    @Autowired
    private DBSvcCfgDao dbSvcCfgDao;

    @Autowired
    private UserHelper userHelper;

    public List<Map<String, String>> getDBConnList(String dbType) {
        List<Map<String, String>> result = new ArrayList<>();
        final List<SvcgenConnBean> dbConnList = dbSvcCfgDao.getDBConnList(dbType);
        Iterator<SvcgenConnBean> iterator = dbConnList.iterator();
        while (iterator.hasNext()) {
            SvcgenConnBean svcgenConnBean = iterator.next();
            Map<String, String> map = new HashMap<>();
            map.put("connId", svcgenConnBean.getConnId());
            map.put("connName", svcgenConnBean.getConnName());
            map.put("dbType", svcgenConnBean.getAttr1());
            result.add(map);
        }
        return result;
    }



    public DBConnVO getDBConnDetailByName(String connName) {
        if (StringUtil.isNotBlank(connName)) {
            SvcgenConnBean svcgenConnBean = dbSvcCfgDao.getDBConnDetailByName(connName);
            DBConnVO dBconnVO = new DBConnVO();
            dBconnVO.setConnId(svcgenConnBean.getConnId());
            dBconnVO.setConnName(svcgenConnBean.getConnName());
            dBconnVO.setDbType(svcgenConnBean.getAttr1());
            dBconnVO.setDbIp(svcgenConnBean.getAttr2());
            dBconnVO.setPort(svcgenConnBean.getAttr3());
            dBconnVO.setConnUN(svcgenConnBean.getAttr4());
            dBconnVO.setConnPD(svcgenConnBean.getAttr5());
            if ("oracle".equals(svcgenConnBean.getAttr1())) {
                dBconnVO.setSidOrServNameLabel(svcgenConnBean.getAttr6());
                dBconnVO.setSidOrServNameValue(svcgenConnBean.getAttr7());
            } else if ("mysql".equals(svcgenConnBean.getAttr1())) {
                dBconnVO.setDbName(svcgenConnBean.getAttr6());
            }
            return dBconnVO;
        } else {
            throw new MpaasBusinessException("请求参数错误，请检查参数！");
        }
    }

    public String jointUrl(DBConnVO dBconnVO) {
        String url = "";
        if ("oracle".equals(dBconnVO.getDbType())) {
            url = "jdbc:oracle:thin:@//" + dBconnVO.getDbIp() + ":" + dBconnVO.getPort() + "/" + dBconnVO.getSidOrServNameValue();
        } else if ("mysql".equals(dBconnVO.getDbType())) {
            url = "jdbc:mysql://" + dBconnVO.getDbIp() + ":" + dBconnVO.getPort() + "/" + dBconnVO.getDbName() + "?useSSL=false";
        }
        return url;
    }

    public List<Map<String, Object>> queryTableList(String con0, String connectName) {
        DBConnVO dBconnVO = getDBConnDetailByName(connectName);
        String url = jointUrl(dBconnVO);
        Connection connection = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            connection = JdbcConnection.getDSGCDBConnectTest(dBconnVO.getDbType(), url, dBconnVO.getConnUN(), dBconnVO.getConnPD());
            String sql = "";
            if ("oracle".equals(dBconnVO.getDbType())) {
                sql = "select * from (SELECT table_name name, 'table' type FROM user_tables UNION SELECT view_name name,'view' type FROM user_views ) where name like ? order by type desc";

            } else if ("mysql".equals(dBconnVO.getDbType())) {
                sql = "select table_name name,(case when find_in_set('BASE TABLE',  table_type) then 'table' when find_in_set('VIEW',  table_type) then 'view' end) type from information_schema.tables where table_name like ? and  table_schema= ? order by table_type desc";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + con0 + "%");
            if ("mysql".equals(dBconnVO.getDbType())) {
                preparedStatement.setString(2, dBconnVO.getDbName());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", resultSet.getString("name") == null ? "" : resultSet.getString("name"));
                    map.put("typeName", resultSet.getString("type").equals("table") ? "表" : "视图");
                    map.put("type", resultSet.getString("type") == null ? "" : resultSet.getString("type"));
                    map.put("checked", false);
                    list.add(map);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("数据库驱动加载失败，无法连接数据库");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("getDBConnect异常，无法连接数据库");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Map<String, Object>> queryTableFileds(String tableName, String connectName) {
        DBConnVO dBconnVO = getDBConnDetailByName(connectName);
        String url = jointUrl(dBconnVO);
        Connection connection = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            connection = JdbcConnection.getDSGCDBConnectTest(dBconnVO.getDbType(), url, dBconnVO.getConnUN(), dBconnVO.getConnPD());
            String sql = "";
            if ("oracle".equals(dBconnVO.getDbType())) {
                sql = "select column_name,data_type,nullable from user_tab_columns where table_name=?";

            } else if ("mysql".equals(dBconnVO.getDbType())) {
                sql = "select column_name,(case when find_in_set('YES',  is_nullable) then 'Y' when find_in_set('NO',  is_nullable) then 'N' end) nullable,data_type from INFORMATION_SCHEMA.Columns where table_name= ? and table_schema= ? ";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableName.toUpperCase());
            if ("mysql".equals(dBconnVO.getDbType())) {
                preparedStatement.setString(2, dBconnVO.getDbName());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("columnName", resultSet.getString("column_name") == null ? "" : resultSet.getString("column_name"));
                    map.put("dataType", resultSet.getString("data_type") == null ? "VARCHAR2" : resultSet.getString("data_type"));
                    map.put("isNull", resultSet.getString("nullable") == null ? "Y" : resultSet.getString("nullable"));
                    map.put("checked", false);
                    list.add(map);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("数据库驱动加载失败，无法连接数据库");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("getDBConnect异常，无法连接数据库");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Object generateTableTree(JSONObject jsonObject) {
        JSONArray tableData = jsonObject.getJSONArray("tableData");
        JSONArray relationshipData = jsonObject.getJSONArray("relationshipData");
        String connectName = jsonObject.getString("connectName");
        if (tableData.isEmpty() && relationshipData.isEmpty()) {
            throw new MpaasBusinessException("参数错误，无法生成表字段");
        } else if (!tableData.isEmpty() && relationshipData.isEmpty()) {
            JSONObject object = tableData.getJSONObject(0);
            String tableName = object.getString("tableName");
            List<Map<String, Object>> list = this.queryTableFileds(tableName, connectName);
            JSONArray resultArray = new JSONArray();
            JSONObject resultObject = new JSONObject();
            resultObject.put("title", tableName);
            resultObject.put("key", tableName);
            resultObject.put("expanded", true);
            resultObject.put("isRoot", true);
            //  resultObject.put("rootNode",tableName);
            JSONArray childrenArray = new JSONArray();
            for (Map<String, Object> item : list) {
                JSONObject filedObject = new JSONObject();
                filedObject.put("title", item.get("columnName"));
                filedObject.put("key", item.get("columnName"));
                filedObject.put("colType", item.get("dataType"));
                filedObject.put("isNull", item.get("isNull"));
                filedObject.put("isLeaf", true);
                childrenArray.add(filedObject);
            }
            resultObject.put("children", childrenArray);
            resultArray.add(resultObject);
            return resultArray;
        } else {
            List<TreeNode> list = new ArrayList<>();
            for (int i = 0; i < relationshipData.size(); i++) {
                TreeNode treeNode = new TreeNode();
                treeNode.setParentId(relationshipData.getJSONObject(i).getString("mainTable"));
                treeNode.setKey(relationshipData.getJSONObject(i).getString("subTable"));
                treeNode.setTitle(relationshipData.getJSONObject(i).getString("nodeName"));
                List<Map<String, Object>> result = this.queryTableFileds(relationshipData.getJSONObject(i).getString("subTable"), connectName);
                List<Map<String, String>> fileds = new ArrayList<>();
                for (Map<String, Object> item : result) {
                    Map<String, String> map = new HashMap<>();
                    map.put("columnName", String.valueOf(item.get("columnName")));
                    map.put("dataType", String.valueOf(item.get("dataType")));
                    map.put("isNull", String.valueOf(item.get("isNull")));
                    // fileds.add(String.valueOf(item.get("columnName")));
                    fileds.add(map);
                }
                treeNode.setTableFiled(fileds);
                treeNode.setExpanded(true);
                list.add(treeNode);
            }
            Tree tree = new Tree(list, connectName);
            List<TreeNode> rootNodes = tree.getRootNodes();
            if (rootNodes != null && !rootNodes.isEmpty()) {
                String rootTableName = rootNodes.get(0).getParentId();
                TreeNode treeRootNode = new TreeNode();
                treeRootNode.setKey(rootTableName);
                treeRootNode.setParentId(null);
                treeRootNode.setTitle(rootTableName);
                treeRootNode.setIsRoot(true);
                treeRootNode.setLeaf(false);
                treeRootNode.setExpanded(true);
                List<Map<String, Object>> rootTableFiled = this.queryTableFileds(rootTableName, connectName);
                List<TreeNode> filedTreeNode = new ArrayList<>();
                for (Map<String, Object> item : rootTableFiled) {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setExpanded(true);
                    treeNode.setLeaf(true);
                    treeNode.setIsRoot(true);
                    treeNode.setColType(String.valueOf(item.get("dataType")));
                    treeNode.setIsNull(String.valueOf(item.get("isNull")));
                    treeNode.setParentId(rootTableName);
                    treeNode.setKey(String.valueOf(item.get("columnName")));
                    treeNode.setTitle(String.valueOf(item.get("columnName")));
                    filedTreeNode.add(treeNode);
                }
                treeRootNode.setChildren(filedTreeNode);
                List<TreeNode> treeNodeList = tree.buildTree(treeRootNode);
                List<TreeNode> result = dealWithTreeAter(treeNodeList);
                return result;
            } else {
                throw new MpaasBusinessException("表关系错误，请维护正确的关系！");
            }


        }
    }

    public List<TreeNode> dealWithTreeAter(List<TreeNode> list) {
        for (TreeNode item : list) {

            if (item.getChildren() != null && !item.getChildren().isEmpty() && item.getChildren().size() > 0) {
                dealWithTreeAter(item.getChildren());
            }
            List<TreeNode> treeNodeList = item.getChildren() != null ? item.getChildren() : new ArrayList<>();
            List<Map<String, String>> filedList = item.getTableFiled() != null ? item.getTableFiled() : new ArrayList<>();
            for (Map<String, String> filed : filedList) {
                TreeNode treeNode = new TreeNode();
                treeNode.setKey(filed.get("columnName"));
                treeNode.setTitle(filed.get("columnName"));
                treeNode.setIsNull(filed.get("isNull"));
                treeNode.setColType(filed.get("dataType"));
                treeNode.setIsChecked(true);
                treeNode.setLeaf(true);

                treeNode.setParentId(item.getKey());
                treeNodeList.add(treeNode);
            }
            item.setChildren(treeNodeList);
        }
        return list;
    }

    public String generateSelectSql(JSONObject jsonObject) {
        JSONArray filedArray = jsonObject.getJSONArray("colNameList");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < filedArray.size(); i++) {
            if (i == 0) {
                stringBuilder.append(filedArray.getString(i));
            } else {
                stringBuilder.append("," + filedArray.getString(i));
            }
        }
        String tableName = jsonObject.getString("tableName");
        String sql = "SELECT " + stringBuilder.toString() + " FROM " + tableName;
        return sql;
    }

    public Boolean checkConnectNameIsExsit(Map<String, String> map) {
        if (map != null && !map.isEmpty() && !"".equals(map.get("connectName"))) {
            String connectName = map.get("connectName");
            SvcgenConnBean svcgenConnBean = dbSvcCfgDao.getDBConnDetailByName(connectName);
            if (svcgenConnBean != null) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new MpaasBusinessException("请求参数错误！");
        }
    }

    /**
     * 新增DB部署配置项
     *
     * @param uid
     * @param dbDpl
     * @return
     */
//    public Response newDBDeployProfile(String uid, DBDeployProfileBean dbDpl) {
//        UserHelper uh = this.userHelper.user(uid);
//        boolean isEditor = uh.isSvcGenEditorByServNo(dbDpl.getServNo());
//        if (!isEditor) {
//            return Response.error("您无权限执行此操作！");
//        } else {
//            try {
//                String dpId = this.sgProxy.newDBDeployProfile(uid,dbDpl.getServNo(),dbDpl.getDplName(),dbDpl.getEnvCode(),dbDpl.getJndiName());
//                return Response.ok().setData(this.getDBDeployProfileDetail(dpId)).setMessage("新增成功！");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return Response.error("执行出错，请联系管理员处理！");
//            }
//        }
//    }
    public DBDeployProfileBean getDBDeployProfileDetail(String dpId) {
        return this.dbSvcCfgDao.getDBDeployProfileDetail(dpId);
    }

}
