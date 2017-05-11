package net.bingosoft.demo;

import net.bingosoft.oss.ssoclient.model.AccessToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by KAEL on 2017/5/10.
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 移除用户登录信息
        req.getSession().removeAttribute("loginUser");
        // 移除用户访问令牌
        req.getSession().removeAttribute("accessToken");
        // 设置session失效
        req.getSession().invalidate();
        
    }
}
