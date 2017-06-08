package net.bingosoft.demo;

import java.util.HashMap;
import java.util.Map;

import net.bingosoft.oss.ssoclient.SSOUtils;
import net.bingosoft.oss.ssoclient.exception.HttpException;
import net.bingosoft.oss.ssoclient.internal.HttpClient;
import net.bingosoft.oss.ssoclient.internal.JSON;
import net.bingosoft.oss.ssoclient.model.AccessToken;

public class UserService {


	public static Map<String, Object> getUserInfo(AccessToken accessToken) {
		String url = Utils.ssoBaseEndpoint + "/oauth2/userinfo";

		//将token加入到请求头，通过Authorization：Bearer xxxxxx方式传输
		Map<String, String> headers = new HashMap<>();
		headers.put(SSOUtils.AUTHORIZATION_HEADER, SSOUtils.BEARER + " " + accessToken.getAccessToken());

		Map<String, Object> userInfo = null;
		try {
			String reVal = HttpClient.get(url, null, headers);
			userInfo = JSON.decodeToMap(reVal);
		} catch (HttpException ex) {
			if (!ex.getMessage().contains("invalid_token")) {
				throw ex;
			}
			//当为401请求,并且返回的信息错误码为{"error": "invalid_token"}，刷新token，并进行一次重试
			AccessToken at = Utils.getClient().refreshToken(accessToken.getRefreshToken());
			if (at != null) {
				headers.put(SSOUtils.AUTHORIZATION_HEADER, SSOUtils.BEARER + " " + at.getAccessToken());
				String reVal = HttpClient.get(url, null, headers);
				userInfo = JSON.decodeToMap(reVal);
			}
		}
		return userInfo;
	}
}
