package com.kjt.service.common.web.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjt.service.common.auth.AuthResourcesDto;
import com.kjt.service.common.auth.AuthUserDto;
import com.kjt.service.common.auth.ResourcesService;
import com.kjt.service.common.resource.WebAppConfig;
import com.kjt.service.common.util.StringUtil;
import com.kjt.service.common.web.util.HttpUtil;

public class AuthenticationFilter extends WebAppConfig implements Filter {

	private String ticketValidateUrl;

	private String serverLoginUrl;
	
	private String serverName;
	
	private ResourcesService resourcesService;

	private final static Map<String, String> resourcesMap = new HashMap<>();
	private String app = null;
	private String app_auth_switch;
	private String auth_switch_key_suffix="auth.switch";
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	    
	    app = filterConfig.getInitParameter("appName");
	    if(!StringUtil.isEmpty(app)){
	        app_auth_switch = app+"."+auth_switch_key_suffix;
	    }
	    
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		resourcesService = wac.getBean("resourcesService",
				ResourcesService.class);

		Map<String, Object> params = new HashMap<>();
		params.put("orders", " id desc ");
		com.kjt.service.common.auth.AuthResponse<AuthResourcesDto> response = resourcesService
				.selectList(params);
		List<AuthResourcesDto> resources = response.getList();
		if (resources != null && !resources.isEmpty()) {
			for (AuthResourcesDto resourcesDto : resources) {
				resourcesMap.put(resourcesDto.getLinkUrl(),
						resourcesDto.getCode());
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        
		boolean auth = this.getBoolean(app_auth_switch, false);
		
		if (!auth) {//不需要权限控制
			chain.doFilter(request, response);
		} else {//需要权限控制
		    
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			String url = req.getRequestURL().toString();
			String regex = "[\\w+](\\.css|\\.js|\\.jpg|\\.png|\\.gif)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {//过滤静态资源
				chain.doFilter(request, response);
			} else {
				String ticket = request.getParameter("ticket");
				if (ticket == null || "".equals(ticket.trim())) {
					Cookie[] cookies = req.getCookies();
					if (cookies != null && cookies.length > 0) {
						for (Cookie cookie : cookies) {
							if ("ticket".equals(cookie.getName())) {
								ticket = cookie.getValue();
							}
						}
					}
				}
				AuthUserDto dto = (AuthUserDto) req.getSession().getAttribute(ticket);
				if (dto != null) {//已经成功登陆
				    String resourcesCode = resourcesMap.get(url);
                    Map<String,Integer> auths = dto.getAuths();
                    boolean authFlag=auths.containsKey(resourcesCode);//资源是否已经授权
                    if(authFlag){
                        chain.doFilter(request, response);
                    }
                    else{
                        PrintWriter out = null;
                        try {
                            res.setCharacterEncoding("UTF-8");
                            res.setHeader("Cache-Control","no-cache, must-revalidate");
                            res.setContentType("text/html;charset=UTF-8");
                            out = res.getWriter();
                            out.write("无权限访问!");
                        } catch (IOException e) {

                        } finally {
                            if (out != null) {
                                out.flush();
                                out.close();
                                out = null;
                            }
                        }
                    }
				} else {//登陆流程
				    if(!StringUtil.isEmpty(app)){
				        ticketValidateUrl = getString(app+"."+"ticketValidateUrl",this.getString("ticketValidateUrl"));
				        serverLoginUrl = getString(app+"."+"serverLoginUrl",this.getString("serverLoginUrl")); 
				        serverName = getString(app+"."+"serverName",this.getString("serverName"));
				    }
				    
					if (ticket != null && !"".equals(ticket.trim())) {
						Map<String, String> headers = new HashMap<>();
						headers.put("Cookie", "JSESSIONID=" + ticket);
						String resultText = HttpUtil.postText(ticketValidateUrl + "?ticket=" + ticket,"ticket=" + ticket, headers);
						ObjectMapper mapper = new ObjectMapper();
						mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,	false);
						try {
							dto = mapper.readValue(resultText, AuthUserDto.class);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (dto != null) {
							req.getSession().setAttribute(ticket, dto);
							Cookie cookie = new Cookie("ticket", ticket);// 创建一个cookie，cookie的名字是lastAccessTime
							cookie.setPath("/");
							// 将cookie对象添加到response对象中，这样服务器在输出response对象中的内容时就会把cookie也输出到客户端浏览器
							res.addCookie(cookie);
							String resourcesCode = resourcesMap.get(url);
							Map<String,Integer> auths = dto.getAuths();
		                    boolean authFlag=auths.containsKey(resourcesCode);//资源是否已经授权
							if (authFlag) {//已经授权
								chain.doFilter(request, response);
							} else {
								PrintWriter out = null;
								try {
									res.setCharacterEncoding("UTF-8");
									res.setHeader("Cache-Control","no-cache, must-revalidate");
									res.setContentType("text/html;charset=UTF-8");
									out = res.getWriter();
									out.write("无权限访问!");
								} catch (IOException e) {

								} finally {
									if (out != null) {
										out.flush();
										out.close();
										out = null;
									}
								}
							}
						} else {
							res.sendRedirect(serverLoginUrl);
						}
					} else {
						res.sendRedirect(serverLoginUrl);
					}
				}
			}
		}
	}

	@Override
	public void destroy() {

	}

}