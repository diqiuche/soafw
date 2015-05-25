package com.kjt.service.common.web.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjt.service.common.auth.AuthResourcesDto;
import com.kjt.service.common.auth.AuthResponse;
import com.kjt.service.common.auth.AuthUserDto;
import com.kjt.service.common.auth.ResourcesService;
import com.kjt.service.common.config.dict.ConfigComponent;
import com.kjt.service.common.util.StringUtil;
import com.kjt.service.common.web.util.J2eeHttpUtil;


public class AuthenticationFilter implements Filter {

    private String ticketValidateUrl;

    private String serverLoginUrl;
    
    private String serverName;
    
    @Resource(name="resourcesService")
    private ResourcesService resourcesService;
    
    @Resource(name=ConfigComponent.WebAppConfig)
    private WebAppConfig config;

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
        System.setProperty("key_app_auth_switch", app_auth_switch);
        /**
        WebApplicationContext wac = WebApplicationContextUtils
                .getWebApplicationContext(filterConfig.getServletContext());
        resourcesService = wac.getBean("resourcesService",
                ResourcesService.class);
         */
        Map<String, Object> params = new HashMap<>();
        params.put("orders", " id desc ");
        AuthResponse<AuthResourcesDto> response = resourcesService
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
        
        boolean auth = config.getBoolean(app_auth_switch, false);
        System.setProperty(app_auth_switch, String.valueOf(auth));
        
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
                    if(!StringUtil.isEmpty(resourcesCode) && auths.containsKey(resourcesCode)){//资源是否已经授权
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
                    
                    ticketValidateUrl = config.getString("ticketValidateUrl");
                    serverLoginUrl = config.getString("serverLoginUrl"); 
                    serverName = config.getString("serverName");
                    
                    if(!StringUtil.isEmpty(app)){
                        ticketValidateUrl = config.getString(app+"."+"ticketValidateUrl",ticketValidateUrl);
                        serverLoginUrl = config.getString(app+"."+"serverLoginUrl",serverLoginUrl); 
                        serverName = config.getString(app+"."+"serverName",serverName);
                    }
                    
                    if (ticket != null && !"".equals(ticket.trim())) {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Cookie", "JSESSIONID=" + ticket);
                        String resultText = J2eeHttpUtil.postText(ticketValidateUrl + "?ticket=" + ticket,"ticket=" + ticket, headers);
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
                            if (!StringUtil.isEmpty(resourcesCode) && auths.containsKey(resourcesCode)) {//已经授权
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
