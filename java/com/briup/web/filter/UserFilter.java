package com.briup.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;
        Object user = httpServletRequest.getSession().getAttribute("user");
        if (user==null){
            httpServletRequest.setAttribute("msg","还未登录请先登录");
            httpServletRequest.getRequestDispatcher("toLogin").forward(httpServletRequest,httpServletResponse);
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
