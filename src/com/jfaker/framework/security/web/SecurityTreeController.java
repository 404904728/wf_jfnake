package com.jfaker.framework.security.web;

import java.util.ArrayList;
import java.util.List;

import com.jfaker.framework.security.model.Org;
import com.jfaker.framework.security.model.TreeNode;
import com.jfaker.framework.security.model.User;
import com.jfinal.core.Controller;

public class SecurityTreeController extends Controller {
	public void orgTree() {
		List<Org> orgs = Org.dao.getByParent(getParaToInt("parentId"));
		List<TreeNode> trees = new ArrayList<TreeNode>();
		TreeNode node = null;
		for(Org org : orgs) {
			node = new TreeNode();
			node.setId(org.getLong("id"));
			Long parentOrg = org.getLong("parent_org");
			node.setpId(parentOrg == null ? Org.ROOT_ORG_ID : parentOrg);
			node.setName(org.getStr("name"));
			if(parentOrg == null) {
				node.setOpen(true);
			}
			trees.add(node);
		}
		renderJson(trees);
	}
	
	public void orgUserTree() {
		List<Org> orgs = Org.dao.getByParent(getParaToInt("parentId"));
		List<TreeNode> trees = new ArrayList<TreeNode>();
		TreeNode node = null;
		for(Org org : orgs) {
			node = new TreeNode();
			node.setId(org.getLong("id"));
			Long parentOrg = org.getLong("parent_org");
			node.setpId(parentOrg == null ? Org.ROOT_ORG_ID : parentOrg);
			node.setName(org.getStr("name"));
			if(parentOrg == null) {
				node.setOpen(true);
			}
			trees.add(node);
		}
		
		List<User> users = User.dao.getByOrg(getParaToInt("parentId"));
		for(User user : users) {
			node = new TreeNode();
			node.setId(user.getLong("id"));
			Long parentOrg = user.getLong("org");
			node.setpId(parentOrg == null ? Org.ROOT_ORG_ID : parentOrg);
			node.setName(user.getStr("fullname"));
			trees.add(node);
		}
		renderJson(trees);
	}
}
