package com.kjt.service.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kjt.service.common.util.RequestID;

public class RequestIDInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestIDInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String rid = request.getHeader("X-AJK-Request-ID");
		LOGGER.debug("rid: {}", rid);
		RequestID.set(rid);
		LOGGER.debug("preHandle");
		LOGGER.debug("{}", RequestID.get());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		RequestID.unset();
	}
}
