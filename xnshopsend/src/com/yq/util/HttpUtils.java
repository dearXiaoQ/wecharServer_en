package com.yq.util;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class HttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    	
    public static final String targetUrl = "http://localhost:3300/order";
    
    public static  String login(String json) throws IOException {
    	OkHttpClient client = new OkHttpClient();
        //������������ַ�ת��Ϊjson
    	RequestBody requestBody = RequestBody.create(JSON, json);   
 
    	//RequestBody formBody = new FormEncodingBuilder()

        Request request = new Request.Builder()
                .url(targetUrl)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string();

        System.out.println(result);
        return result;

    }


    public String bolwingJson(String username, String password) {
        return "{'username':" + username + "," + "'password':" + password + "}";
        //     "{'username':" + username + ","+"'password':"+password+"}";
    }

}
