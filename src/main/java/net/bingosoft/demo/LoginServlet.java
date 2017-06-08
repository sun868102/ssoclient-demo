package net.bingosoft.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bingosoft.oss.ssoclient.SSOClient;
import net.bingosoft.oss.ssoclient.SSOUtils;
import net.bingosoft.oss.ssoclient.exception.HttpException;
import net.bingosoft.oss.ssoclient.internal.HttpClient;
import net.bingosoft.oss.ssoclient.internal.JSON;
import net.bingosoft.oss.ssoclient.model.AccessToken;
import net.bingosoft.oss.ssoclient.model.Authentication;
import net.bingosoft.oss.ssoclient.servlet.AbstractLoginServlet;

/**
 * Created by KAEL on 2017/5/10.
 */
public class LoginServlet extends AbstractLoginServlet {

    @Override
    protected SSOClient getClient(ServletConfig servletConfig) throws ServletException {
       return  Utils.createClient(servletConfig);
    }

    @Override
    protected void localLogin(HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication,
                              AccessToken accessToken) throws ServletException, IOException {

    	Map<String,Object> userInfo=UserService.getUserInfo(accessToken);
    	if(userInfo!=null){
	    	for (Map.Entry<String, Object> entry :userInfo.entrySet()) {
	    		authentication.setAttribute(entry.getKey(), entry.getValue());
			}
    	}

        // 完成本地登录
        request.getSession().setAttribute("loginUser",authentication);
        // 保存用户访问令牌
        request.getSession().setAttribute("accessToken",accessToken);
    }




}
