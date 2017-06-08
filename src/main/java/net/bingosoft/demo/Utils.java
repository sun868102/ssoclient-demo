package net.bingosoft.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.bingosoft.oss.ssoclient.SSOClient;
import net.bingosoft.oss.ssoclient.SSOConfig;

public  class Utils {
    private static SSOClient client = null;

    public static String clientId="link_demo";
    public static String clientSecret="jsjtzwsTpPNWeKs6hZCG";
    public static String resourceName="demo";
    /*public static String ssoBaseEndpoint="http://114.67.33.50:7077/ssov3";*/
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


	private static String regularUrl(String url){
		//remove default port
		url += "/";
		if(url.startsWith("https") || url.startsWith("HTTPS")){
			url = url.replaceFirst(":443/", "/");
		}else{
			url = url.replaceFirst(":80/", "/");
		}

		return url.substring(0,url.length()-1);
	}

	public static String convertFullPathUrl(HttpServletRequest request, String url){
		String returnUrl;
		if(url.startsWith("http")){
			returnUrl = url.replaceFirst("\\{host\\}", request.getServerName()).replaceFirst("\\{port\\}", String.valueOf(request.getServerPort()));
		}else{

			returnUrl = getServerUrl(request) + url;
		}
		return regularUrl(returnUrl);
	}

	public static String getServerName(HttpServletRequest request) {
		String host = request.getHeader("Host");
		if (host != null && !"".equals(host)) {
			int index = host.indexOf(":");
			if(index > 0){
				return host.substring(0,index);
			}else{
				return host;
			}
		}

		return request.getServerName();
	}

	/**
	 * 获取服务器全路径,反向代理时，需设置：host和x-forwarded-proto值，如:http://locahost:8080
	 * @param request
	 * @return
	 */
	public static String getServerUrl(HttpServletRequest request) {
		String schema=request.getHeader("x-forwarded-proto");
		if(schema==null || "".equals(schema)){
			schema=request.getScheme();
		}
		schema+="://";
		String host =request.getHeader("host");
		if(host==null || "".equals(host)){
			host=request.getServerName() + ":" + request.getServerPort();
		}
		String url=schema+host;
		url=regularUrl(url);
		return url;
	}


}
