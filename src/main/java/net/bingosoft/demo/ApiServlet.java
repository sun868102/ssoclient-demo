package net.bingosoft.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bingosoft.oss.ssoclient.SSOUtils;
import net.bingosoft.oss.ssoclient.exception.InvalidTokenException;
import net.bingosoft.oss.ssoclient.exception.TokenExpiredException;
import net.bingosoft.oss.ssoclient.internal.JSON;
import net.bingosoft.oss.ssoclient.model.AccessToken;
import net.bingosoft.oss.ssoclient.model.Authentication;

/**
 * Created by fulsh on 2017/06/08.
 */
public class ApiServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Utils.createClient(config);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String pathInfo = req.getRequestURI();
			if (pathInfo.endsWith("info")) {
				info(req, resp);
			} else if (pathInfo.endsWith("user")) {
				user(req, resp);
			} else if (pathInfo.endsWith("client")) {
				client(req, resp);
			} else if (pathInfo.endsWith("custom")) {
				custom(req, resp);
			} else if (pathInfo.endsWith("other")) {
				otherApi(req, resp);
			} else {
				throwCustomerError(resp, "无效的请求");
			}
		} catch (InvalidTokenException ex) {
			ex.printStackTrace();
			throwAccessTokenInvalid(resp);
		} catch (TokenExpiredException ex) {
			ex.printStackTrace();
			throwAccessTokenInvalid(resp);
		} catch (Exception ex) {
			ex.printStackTrace();
			throwServerError(resp);
		}
	}

	protected void info(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		Map<String, Object> info = new HashMap<>();

		Enumeration<String> headers = req.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			info.put(header, req.getHeader(header));
		}

		Enumeration<String> params = req.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			info.put(param, req.getParameter(param));
		}

		jsonResponse(resp, info);
	}

	protected void user(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String accessToken = SSOUtils.extractAccessToken(req);
		if (accessToken == null || accessToken.length() == 0) {
			throwAccessTokenEmpty(resp);
			return;
		}

		Authentication auth = Utils.getClient().verifyAccessToken(accessToken);

		if (auth.getUserId() == null || auth.getUserId().length() == 0) {
			throwCustomerError(resp, "无法从token获取用户信息");
			return;
		}
		// 根据auth.getUserId()进行用户权限验证
		// xxx
		jsonResponse(resp, auth);

	}

	protected void client(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String accessToken = SSOUtils.extractAccessToken(req);
		if (accessToken == null || accessToken.length() == 0) {
			throwAccessTokenEmpty(resp);
			return;
		}

		Authentication auth = Utils.getClient().verifyAccessToken(accessToken);

		if (auth.getClientId() == null || auth.getClientId().length() == 0) {
			throwCustomerError(resp, "无法从token获取调用端应用信息");
			return;
		}
		// 根据auth.getClientId()进行客户端权限验证
		// xxx
		jsonResponse(resp, auth);

	}

	protected void custom(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String needPermission = "manage";
		String accessToken = SSOUtils.extractAccessToken(req);
		if (accessToken == null || accessToken.length() == 0) {
			throwAccessTokenEmpty(resp);
			return;
		}
		Authentication auth = Utils.getClient().verifyAccessToken(accessToken);
		if (auth.getScope() == null || auth.getScope().length() == 0) {
			throwCustomerError(resp, "未授权的操作");
			return;
		}
		//检验当前token是否包含本接口需要的权限
		String[] atScopes = auth.getScope().split(",");
		boolean hasPermission = false;
		for (String scope : atScopes) {
			if (needPermission.equals(scope)) {
				hasPermission = true;
				break;
			}
		}
		if (!hasPermission) {
			throwCustomerError(resp, "未授权的操作");
			return;
		} else {
			jsonResponse(resp, auth);
		}

	}

	protected void otherApi(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String accessToken = SSOUtils.extractAccessToken(req);
		if (accessToken == null || accessToken.length() == 0) {
			throwAccessTokenEmpty(resp);
			return;
		}
		//根据已有access_token获取新的token
		AccessToken newAt = Utils.getClient().obtainAccessTokenByToken(accessToken);
		//使用新的token调用其它接口
		Map<String, Object> userInfo = UserService.getUserInfo(newAt);
		Map<String, Object> info = new HashMap<>();
		info.put("oldAccessToken", accessToken);
		info.put("newAccessToken", newAt);
		info.put("userInfo", userInfo);

		jsonResponse(resp, info);

	}

	protected void jsonResponse(HttpServletResponse resp, Object info) throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(JSON.encode(info));
		resp.flushBuffer();
	}

	protected void throwCustomerError(HttpServletResponse resp, String msg) throws IOException {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "application_error");
		error.put("error_description", msg);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		resp.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
		resp.getWriter().write(JSON.encode(error));
		resp.flushBuffer();
	}

	protected void throwServerError(HttpServletResponse resp) throws IOException {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "internal_error");
		error.put("error_description", "服务器内部错误");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		resp.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
		resp.getWriter().write(JSON.encode(error));
		resp.flushBuffer();
	}

	protected void throwAccessTokenEmpty(HttpServletResponse resp) throws IOException {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "invalid_request");
		error.put("error_description", "无效的请求");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		resp.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
		resp.getWriter().write(JSON.encode(error));
		resp.flushBuffer();
	}

	protected void throwAccessTokenInvalid(HttpServletResponse resp) throws IOException {
		Map<String, Object> error = new HashMap<>();
		error.put("error", "invalid_token");
		error.put("error_description", "无效的token");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		resp.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
		resp.getWriter().write(JSON.encode(error));
		resp.flushBuffer();
	}

}
