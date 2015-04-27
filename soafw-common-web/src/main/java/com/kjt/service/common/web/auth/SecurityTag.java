package com.kjt.service.common.web.auth;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kjt.service.common.auth.AuthUserDto;

public class SecurityTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 993394191189813821L;

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("ticket".equals(cookie.getName())) {
					AuthUserDto user = (AuthUserDto)request.getSession().getAttribute(cookie.getValue());
					if (user != null) {
						Map<String,Integer> auths = user.getAuths();
						if(auths.containsKey(name)){//已经授权
						    return EVAL_BODY_INCLUDE;
						}
					}
					break;
				}
			}
		}
		return SKIP_BODY;
	}

	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
