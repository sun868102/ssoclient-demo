package net.bingosoft.demo;

import net.bingosoft.oss.ssoclient.SSOClient;
import net.bingosoft.oss.ssoclient.SSOConfig;
import net.bingosoft.oss.ssoclient.model.AccessToken;
import net.bingosoft.oss.ssoclient.model.Authentication;
import net.bingosoft.oss.ssoclient.servlet.AbstractLoginServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by KAEL on 2017/5/10.
 */
public class LoginServlet extends AbstractLoginServlet {
    @Override
    protected SSOClient getClient(ServletConfig servletConfig) throws ServletException {
        SSOConfig config = new SSOConfig();
        config.setClientId("ssoSDKClientId");
        config.setClientSecret("ssoSDKClientSecret");
        
        // 这个地址需要在应用注册的时候填写
        String redirectUri = servletConfig.getServletContext().getContextPath()+"/ssoclient/login";
        config.setRedirectUri(redirectUri);
        
        config.autoConfigureUrls("http://114.67.33.50:7077/ssov3");
        SSOClient client = new SSOClient(config);
        return client;
    }

    @Override
    protected void localLogin(HttpServletRequest request, 
                              HttpServletResponse response,
                              Authentication authentication,
                              AccessToken accessToken) throws ServletException, IOException {
        // 完成本地登录
        request.getSession().setAttribute("loginUser",authentication);
        // 保存用户访问令牌
        request.getSession().setAttribute("accessToken",accessToken);
    }
}
