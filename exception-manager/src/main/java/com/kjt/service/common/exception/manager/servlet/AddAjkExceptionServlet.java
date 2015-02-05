package com.kjt.service.common.exception.manager.servlet;

import com.google.common.base.Strings;
import com.kjt.service.common.exception.basic.ExceptionLevel;
import com.kjt.service.common.exception.basic.ExceptionType;
import com.kjt.service.common.exception.manager.dao.AjkExceptionDao;
import com.kjt.service.common.exception.manager.dao.AjkSoaSpDao;
import com.kjt.service.common.exception.manager.model.AjkException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Created by kevin on 15/1/6.
 */
public class AddAjkExceptionServlet extends HttpServlet {


    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        int type=Integer.valueOf(req.getParameter("type"));
        int level=Integer.valueOf(req.getParameter("level"));
        int spid=Integer.valueOf(req.getParameter("sp"));
        String message = req.getParameter("message");
        if(Strings.isNullOrEmpty(message)){
            throw new ServletException("异常信息不能为空");
        }
        resp.sendRedirect("list?code="+ this.add(type,message,level,spid));

    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        req.setAttribute("types", ExceptionType.values());
        req.setAttribute("levels", ExceptionLevel.values());
        req.setAttribute("sps",new AjkSoaSpDao().list());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/pages/add-exception.jsp");
        dispatcher .forward(req, resp);
    }

    private synchronized int add(int type,String message,int level,int spid){
        AjkExceptionDao ajkExceptionDao = new AjkExceptionDao();
        int maxCode =ajkExceptionDao.getMaxCode(type);
        AjkException ajkException = new AjkException();
        ajkException.setCode(++maxCode);
        ajkException.setMessage(message);
        ajkException.setType(type);
        ajkException.setSpid(spid);
        ajkException.setLevel(level);
        ajkExceptionDao.addAjkException(ajkException);
        return ajkException.getCode();
    }
}
