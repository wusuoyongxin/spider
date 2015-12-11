package com.wusyx.spider.teach;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {
    
    @Test
    public void getIp() throws Exception{
        HttpClientBuilder build = HttpClients.custom();
        CloseableHttpClient client = build.build();
        HttpGet request = new HttpGet("http://vxer.daili666api.com/ip/?tid=556166258225734&num=1000&operator=2&delay=3");
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String ips = EntityUtils.toString(entity);
        String[] split = ips.split("\n");
        for (String ip : split) {
            String[] ipPort = ip.trim().split(":");
            System.out.println("IP:" + ipPort[0] + ",Port:" + ipPort[1]);
        }
    }
    
    String url = "http://www.crxy.cn/";
    String ip = "202.107.233.85";
    int port = 8080;
    String username = "";
    String password = "";
    
    /**
     * 使用httpclient4实现代理
     * 202.107.233.85
     * 8080
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        HttpClientBuilder build = HttpClients.custom();
        HttpHost proxy = new HttpHost(ip, port);
        CloseableHttpClient client = build.setProxy(proxy ).build();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    /**
     * 使用httpclient3实现代理
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        HttpClient httpClient = new HttpClient();
        httpClient.getHostConfiguration().setProxy(ip, port);
        
        GetMethod method = new GetMethod(url);
        httpClient.executeMethod(method );
        String result = new String(method.getResponseBody());
        System.out.println(result);
    }
    
    /**
     * 使用httpclient4实现代理(带密码的代理)
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        HttpClientBuilder build = HttpClients.custom();
        
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        AuthScope authscope = new AuthScope(ip, port);
        Credentials credentials = new UsernamePasswordCredentials(username,password);
        credentialsProvider.setCredentials(authscope , credentials);
        
        CloseableHttpClient client = build.setDefaultCredentialsProvider(credentialsProvider ).build();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    /**
     * 使用httpclient3实现代理(带密码的代理)
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        HttpClient httpClient = new HttpClient();
        
        org.apache.commons.httpclient.auth.AuthScope authscope = new org.apache.commons.httpclient.auth.AuthScope(ip, port);
        org.apache.commons.httpclient.Credentials credentials = new org.apache.commons.httpclient.UsernamePasswordCredentials(username,password);
        httpClient.getState().setProxyCredentials(authscope, credentials);
        
        GetMethod method = new GetMethod(url);
        httpClient.executeMethod(method );
        String result = new String(method.getResponseBody());
        System.out.println(result);
        
    }
    /**
     * 模拟登录官网（www.crxy.cn）2015-07-01 已失效
     * @throws Exception
     */
    @Test
    public void testLogin() throws Exception {
        HttpClientBuilder build = HttpClients.custom();
        CloseableHttpClient client = build.build();
        HttpPost post = new HttpPost("http://www.crxy.cn/userlogining");
        
        
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("username", "123456"));
        params.add(new BasicNameValuePair("password", "123456"));
        params.add(new BasicNameValuePair("loginType", "0"));
        params.add(new BasicNameValuePair("returnUrl", ""));
        HttpEntity entity = new UrlEncodedFormEntity(params,"UTF-8");
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if(statusCode==302){
            Header[] location = response.getHeaders("location");
            String rediretUrl = null;
            if(location.length==1){
                rediretUrl = location[0].getValue();
            }
            post.setURI(new URI(rediretUrl));
            response = client.execute(post);
            
            HttpEntity entity2 = response.getEntity();
            System.out.println(EntityUtils.toString(entity2));
        }
    }
    
    
    /**
     * 模拟登录
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        HttpClientBuilder builder = HttpClients.custom();
        CloseableHttpClient client = builder.build();
        HttpPost httpPost = new HttpPost("http://svn.jundie.net/user/login");
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("uid", "crxy"));
        parameters.add(new BasicNameValuePair("pwd", "www.crxy.cn"));
        
        HttpEntity entity = new UrlEncodedFormEntity(parameters);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = client.execute(httpPost);
        //System.out.println(EntityUtils.toString(response.getEntity()));
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode==302){
            Header[] headers = response.getHeaders("location");
            String redirectorurl = null;
            if(headers.length>0){
                redirectorurl = headers[0].getValue();
            }
            httpPost.setURI(new URI("http://svn.jundie.net"+redirectorurl));
            response = client.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

}
