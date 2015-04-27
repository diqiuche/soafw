package com.kjt.service.common.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {

    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Enumeration<String> rnames = request.getParameterNames();
        Map<String, Object> map = null;
        if (rnames != null) {
            map = new HashMap<String, Object>();
            for (Enumeration<String> e = rnames; e.hasMoreElements();) {
                String thisName = e.nextElement().toString();
                if ("offset".equals(thisName) || "pageSize".equals(thisName)) {
                    continue;
                }
                String thisValue = request.getParameter(thisName);
                if (StringUtils.isNotEmpty(thisValue)) {
                    map.put(thisName, thisValue);
                    log.debug("name======" + thisName + "-------value=====" + thisValue);
                }
            }
        }
        return map;
    }

    public static int CONNECTTIMEOUT = 5000;

    public static int READTIMEOUT = 10000;


    public static String CHARSET_UTF8 = "UTF-8";



    /**
     * 对url进行编码
     */
    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对url进行解码
     * 
     * @param url
     * @return
     */
    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断URL地址是否存在
     * 
     * @param url
     * @return
     */
    public static boolean isURLExist(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            int state = connection.getResponseCode();
            if (state == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断URL地址是否存在
     * 
     * @param url
     * @return
     */
    public static boolean isURLExist(HttpURLConnection connection) {
        try {
            int state = connection.getResponseCode();
            if (state == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将请求参数还原为key=value的形式,for struts2
     * 
     * @param params
     * @return
     */
    public static String getParamString(Map<?, ?> params) {
        StringBuffer queryString = new StringBuffer(256);
        Iterator<?> it = params.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] param = (String[]) params.get(key);
            for (int i = 0; i < param.length; i++) {
                if (count == 0) {
                    count++;
                } else {
                    queryString.append("&");
                }
                queryString.append(key);
                queryString.append("=");
                try {
                    queryString.append(URLEncoder.encode((String) param[i], "UTF-8"));
                } catch (UnsupportedEncodingException e) {}
            }
        }
        return queryString.toString();
    }

    /**
     * 获得请求的路径及参数
     * 
     * @param request
     * @return
     */
    public static String getRequestURL(HttpServletRequest request) {
        StringBuffer originalURL = new StringBuffer(request.getServletPath());
        Map<?, ?> parameters = request.getParameterMap();
        if (parameters != null && parameters.size() > 0) {
            originalURL.append("?");
            originalURL.append(getParamString(parameters));
        }
        return originalURL.toString();
    }

    /**
     * 抓取网页内容,自动识别编码
     * 
     * @param urlString
     * @return
     */
    public static String readWebPageContent(String urlLink) {
        HttpURLConnection conn = null;
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(urlLink);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1500);
            conn.setReadTimeout(5000);

            URLConnection c = url.openConnection();
            c.connect();

            String contentType = c.getContentType();
            String characterEncoding = null;
            int index = contentType.indexOf("charset=");
            if (index == -1) {
                characterEncoding = CHARSET_UTF8;
            } else {
                characterEncoding = contentType.substring(index + 8, contentType.length());
            }
            InputStreamReader isr = new InputStreamReader(conn.getInputStream(), characterEncoding);
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp).append("\n");
            }
            br.close();
            isr.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String postXml(String url, String xml) {
        return postXml(url, xml, CHARSET_UTF8);
    }

    public static String postXml(String url, String xml, Map<String, String> headers) {
        return postXml(url, xml, CHARSET_UTF8, headers);
    }

    public static String postXml(String url, String xml, String encode) {
        return send(url, xml, encode, null, "POST", "text/xml;charset=UTF-8");
    }

    public static String postXml(String url, String xml, String encode, Map<String, String> headers) {
        return send(url, xml, encode, headers, "POST", "text/xml;charset=UTF-8");
    }

    public static String post(String url, String text, Map<String, String> headers) {
        return post(url, text, CHARSET_UTF8, headers);
    }

    public static String post(String url, String text, String encode, Map<String, String> headers) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setConnectTimeout(CONNECTTIMEOUT);
            connection.setReadTimeout(READTIMEOUT);

            for (Map.Entry<String, String> map : headers.entrySet()) {
                connection.setRequestProperty(map.getKey(), map.getValue());
            }

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            connection.getOutputStream().write(text.getBytes(encode));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            StringBuilder sb = new StringBuilder();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public static String post(String url, String text) {
        return post(url, text, CHARSET_UTF8);
    }

    public static String post(String url, String text, String encode) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setConnectTimeout(CONNECTTIMEOUT);
            connection.setReadTimeout(READTIMEOUT);

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            connection.getOutputStream().write(text.getBytes(encode));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            StringBuilder sb = new StringBuilder();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public static String postText(String url, String text) {
        return postText(url, text, CHARSET_UTF8);
    }

    public static String postText(String url, String text, Map<String, String> headers) {
        return send(url, text, CHARSET_UTF8, headers, "POST", "text/plain;charset=UTF-8");
    }

    public static String postText(String url, String text, String encode,
            Map<String, String> headers) {
        return send(url, text, encode, headers, "POST", "text/plain;charset=UTF-8");
    }

    public static String postText(String url, String text, String encode) {
        return send(url, text, encode, null, "POST", "text/plain;charset=UTF-8");
    }

    public static String send(String url, String text, String encode, Map<String, String> headers,
            String method, String property) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setConnectTimeout(CONNECTTIMEOUT);
            connection.setReadTimeout(READTIMEOUT);
            connection.setRequestProperty("Content-type", property);
            if (headers != null) {
                for (Map.Entry<String, String> map : headers.entrySet()) {
                    connection.setRequestProperty(map.getKey(), map.getValue());
                }
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(method);

            connection.getOutputStream().write(text.getBytes(encode));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            StringBuilder sb = new StringBuilder();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }
    
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static InputStream getInputStream(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            int state = connection.getResponseCode();
            if (state == HttpURLConnection.HTTP_OK) {
                return connection.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getInputStream(HttpServletRequest request,
            String charset) {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), charset));

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获得请求路径
     * 
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
    }

    public static String getInputStream(HttpServletRequest request) {
        return getInputStream(request, "UTF-8");
    }


    public static boolean isAjaxRequest(HttpServletRequest request){  
        String header = request.getHeader("X-Requested-With");  
        String isAjax = request.getHeader("isAjax");
        if ("XMLHttpRequest".equals(header) || "isAjax".equalsIgnoreCase(isAjax)) {
            return true;
        } else {
            return false;
        }
    } 

    private static final Log log = LogFactory.getLog(HttpUtil.class);
}
