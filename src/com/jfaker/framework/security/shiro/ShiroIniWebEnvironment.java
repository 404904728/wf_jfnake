package com.jfaker.framework.security.shiro;

import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

public class ShiroIniWebEnvironment extends IniWebEnvironment {
	public void init() {
		super.init();
		FilterChainResolver resolver = getFilterChainResolver();
		if(resolver != null && resolver instanceof PathMatchingFilterChainResolver) {
			PathMatchingFilterChainResolver pathResolver = (PathMatchingFilterChainResolver)resolver;
			ShiroPlugin.setFilterChainManager(pathResolver.getFilterChainManager());
		}
	}
}
