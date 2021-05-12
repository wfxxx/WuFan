package com.definesys.dsgc.service.ystar.svcgen.dbcfg.bean;

import java.util.ArrayList;
import java.util.List;

public class Tree {

//    @Autowired
//    private SVCGenService svcGenService;

    List<TreeNode> nodes = new ArrayList<TreeNode>();
    String connectName = "";
    public Tree(List<TreeNode> nodes, String connectName) {
        super();
        this.nodes = nodes;
        this.connectName = connectName;
    }

    /**
     * 构建树形结构
     *
     * @return
     */
    public List<TreeNode> buildTree(TreeNode rootNode) {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
     //   List<TreeNode> rootNodes = getRootNodes();
       // for (TreeNode rootNode : rootNodes) {
          //  rootNode.setIsRoot(true);
            //rootNode.setTitle(rootNode.getParentId());
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
       // }
        return treeNodes;
    }

    /**
     * 递归子节点
     *
     * @param node
     */
    public void buildChildNodes(TreeNode node) {
        List<TreeNode> children = getChildNodes(node);
        if (!children.isEmpty()) {
            if(node.getChildren() != null && !node.getChildren().isEmpty()){
                List<TreeNode> childrenList = node.getChildren();
                childrenList.addAll(children);
                node.setChildren(childrenList);
            }else {
                node.setChildren(children);
            }

            for (TreeNode child : children) {
                buildChildNodes(child);
            }

        }
    }

    /**
     * 获取父节点下所有的子节点
     *
     * @param nodes
     * @param pnode
     * @return
     */
    public List<TreeNode> getChildNodes(TreeNode pnode) {
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for (TreeNode n : nodes) {
            if (pnode.getKey().equals(n.getParentId())) {
//                List<String> filedList = new ArrayList<>();
//               List<Map<String,Object>> result = new SVCGenService().queryTableFileds(n.getKey(),connectName);
//                for (Map<String,Object> item:result) {
//                    filedList.add(String.valueOf(item.get("columnName")));
//                }
//                n.setTableFiled(filedList);
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    /**
     * 判断是否为根节点
     *
     * @param nodes
     * @param inNode
     * @return
     */
    public boolean rootNode(TreeNode node) {
        boolean isRootNode = true;
        for (TreeNode n : nodes) {
            if (node.getParentId().equals(n.getKey())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    /**
     * 获取集合中所有的根节点
     *
     * @param nodes
     * @return
     */
    public List<TreeNode> getRootNodes() {
        List<TreeNode> rootNodes = new ArrayList<TreeNode>();
        for (TreeNode n : nodes) {
            if (rootNode(n)) {
                n.setIsRoot(true);
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }
}

