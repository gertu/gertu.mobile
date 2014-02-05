package com.gertu.mobile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ServiceHandlerJson {


    static String response = "";
    public final static int POST = 2;

    public ServiceHandlerJson() {

    }

    public String makeServiceCall(String url, int method,
                                  StringEntity jsonParams) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity;
            HttpResponse httpResponse = null;

            HttpPost httpPost = new HttpPost(url);
            if (jsonParams != null) {
                httpPost.setEntity(jsonParams);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
            }

            httpResponse = httpClient.execute(httpPost);

            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
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
