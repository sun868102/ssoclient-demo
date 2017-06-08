package net.bingosoft.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.bingosoft.oss.ssoclient.SSOClient;
import net.bingosoft.oss.ssoclient.SSOConfig;

public  class Utils {
    private static SSOClient client = null;

    public static String clientId="link_demo";
    public static String clientSecret="jsjtzwsTpPNWeKs6hZCG";
    public static String resourceName="demo";
    public static String ssoBaseEndpoint="https://developer.bingosoft.net:12100/ssov3";

	public static SSOClient createClient(ServletConfig servletConfig) throws ServletException {
        if(client == null){
            SSOConfig config = new SSOConfig();
            //做为Client时需配置
            config.setClientId(clientId);
            config.setClientSecret(clientSecret);
            //做为资源服务器需配置
            config.setResourceName(resourceName);

            // 这个地址需要在应用注册的时候填写
            String redirectUri = servletConfig.getServletContext().getContextPath()+"/ssoclient/login";
            config.setRedirectUri(redirectUri);
            // 省公安厅开发测试环境sso:http://114.67.33.50:7077/ssov3
            // 本地开发测试sso:http://localhost:8089/ssov3
            config.autoConfigureUrls(ssoBaseEndpoint);
            client = new SSOClient(config);
        }
        return client;
    }

	public static SSOClient getClient(){
		return client;
	}
}
