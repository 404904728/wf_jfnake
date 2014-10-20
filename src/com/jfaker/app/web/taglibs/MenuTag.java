package com.jfaker.app.web.taglibs;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.jfaker.app.web.taglibs.builder.MenuTagBuilder;
import com.jfaker.framework.web.TagDTO;

/**
 * 系统首界面左栏导航菜单自定义标签
 * @author yuqs
 * @since 0.1
 */
public class MenuTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3041636263647268721L;
	//Servlet的上下文
	private ServletContext servletContext = null;

	public int doStartTag() throws JspException {
		//获取ServletContext
		servletContext = pageContext.getServletContext();
		JspWriter writer = pageContext.getOut();
		TagDTO dto = new TagDTO(servletContext);
		try {
			writer.write(MenuTagBuilder.builder.build(dto));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
