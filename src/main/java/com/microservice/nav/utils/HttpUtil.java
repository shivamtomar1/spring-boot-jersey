package com.microservice.nav.utils;

import okhttp3.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import com.microservice.nav.NavApplication;

//import com.google.gson.Gson;

/**
 * 
 * Utility function to make HTTP Calls
 * @author Shivam Tomar
 *
 */

public class HttpUtil {
    
    private OkHttpClient httpClient = new OkHttpClient();
    
    private String defaultDomainWithDefaultPath = "";
    
    private Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    
    /**
     * 
     * @param domain Default domain to be instanciated while creating object
     */
    public HttpUtil(String domain) {
        this.defaultDomainWithDefaultPath = domain;
    }
    
    public HttpUtil() {}
    
    //unused
    //to be used for requests with queryParams
    /**
     * 
     * To be used if need to pass queryParameters with request
     * 
     * @param url endpoint to connect
     * @param params quetyParams
     * @return Response in String Format
     * @throws Exception Exception while calling service
     * 
     */
    public String sendGet(String url, HashMap<String,String> params) throws Exception {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(this.defaultDomainWithDefaultPath+url).newBuilder();
        
        if (params != null) {
           for(Map.Entry<String, String> param : params.entrySet()) {
               httpBuilder.addQueryParameter(param.getKey(),param.getValue());
           }
        }
        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return (response.body().string());
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new IOException("Unexpected code " + e.toString());//;return "{\"exception\":\""+e.toString()+"\"}";
        }

    }
    
    /**
     * 
     * To be used if no query param in request
     * @param url endpoint to connect
     * @return Response in string format
     * @throws Exception Generic HTTP Exception
     * 
     */
    //util to make http get requests
    public String sendGet(String url) throws Exception {
        
        logger.info("Calling "+this.defaultDomainWithDefaultPath+url);
        LocalDateTime now = LocalDateTime.now();
        
        long startTime = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Request request = new Request.Builder()
                .url(this.defaultDomainWithDefaultPath+url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return (response.body().string());
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new IOException("Unexpected code " + e.toString());
//          return "{\"exception\":\""+e.toString()+"\"}";
        }
        finally {
            long endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            
            logger.info("Time taken for "+this.defaultDomainWithDefaultPath+url+" "+(endTime-startTime)+"ms");
        }

    }

}
