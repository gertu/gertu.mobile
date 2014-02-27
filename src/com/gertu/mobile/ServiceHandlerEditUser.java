package com.gertu.mobile;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServiceHandlerEditUser {

	//private HttpEntity response;
   static String response = "";
    public final static int POST = 2;
    public final static int GET = 1;
    public final static int PUT = 3;
    public ServiceHandlerEditUser() {

    }

    public String makeServiceCall(String url, int method,
                                  StringEntity jsonParams) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity;
            HttpResponse httpResponse = null;
            if( method == POST){
            	HttpPost httpPost = new HttpPost(url);
                if (jsonParams != null) {
                    httpPost.setEntity(jsonParams);
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                }

                httpResponse = httpClient.execute(httpPost);
            }else if(method == GET){
            	
            	HttpGet httpget = new HttpGet(url);
            	httpResponse = httpClient.execute(httpget);
                    			
            }else if(method == PUT){
            	System.out.println("1");
            	HttpPut httpPut = new HttpPut(url);
            	
            	if (jsonParams != null) {
            		System.out.println("2");
            		httpPut.setEntity(jsonParams);
                    httpPut.addHeader("Accept", "application/json");
                    httpPut.addHeader("Content-type", "application/json");
                   
                }
            	httpResponse= httpClient.execute(httpPut);
            	
            }
            
            System.out.println(httpResponse.getStatusLine());
            
            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            	//response = httpResponse.getEntity();
            }
            
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return response;
    }
}