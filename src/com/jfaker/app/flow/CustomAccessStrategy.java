package com.jfaker.app.flow;

import java.util.List;

import org.snaker.engine.impl.GeneralAccessStrategy;

import com.jfaker.framework.security.shiro.ShiroUtils;

/**
 * 自定义访问策略，根据操作人获取其所有组集合（部门、角色、权限）
 * @author yuqs
 * @since 0.1
 */
public class CustomAccessStrategy extends GeneralAccessStrategy {
	protected List<String> ensureGroup(String operator) {
		return ShiroUtils.getGroups();
	}
}
