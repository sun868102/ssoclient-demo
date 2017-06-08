package net.bingosoft.demo;

import net.bingosoft.oss.ssoclient.internal.Base64;
import net.bingosoft.oss.ssoclient.internal.Urls;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by KAEL on 2017/5/10.
 *
 * 安全拦截器，对于所有用户未登录的请求，自动重定向到登录地址。
 *
 * 仅作示例使用。
 *
 */
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        Object authentication = req.getSession().getAttribute("loginUser");
        // 检查用户是否登录
        if(authentication != null || req.getRequestURI().endsWith("/ssoclient/login")
        		|| req.getRequestURI().contains("/api/")
        		|| req.getRequestURI().contains("/swagger.json")){
            chain.doFilter(request,response);
        }else {
        	String contextPath = "/".equals(req.getContextPath()) ? "" : req.getContextPath();
        	String returnUrl=contextPath+"/user.jsp";

            resp.sendRedirect(req.getContextPath()+"/ssoclient/login?return_url="+ Urls.encode(Utils.convertFullPathUrl(req, returnUrl)));
        }
    }

    @Override
    public void destroy() {

    }
}
