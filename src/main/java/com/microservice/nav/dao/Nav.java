package com.microservice.nav.dao;

import java.util.HashMap;

//import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

@SuppressWarnings("unused")
public class Nav {
    
    /**
     * JSON structure for response of /nav/getNavValue
     */
    // {"date":"20190101","securities":{"Apples and Oranges":{"totalQty":336,"price":472.36,"assetsValue":158712.96},"ABC Corporation":{"totalQty":980,"price":666.63,"assetsValue":653297.4}},"nav":812010.36}
    
    private String date;
    private LinkedTreeMap<String,Security> securities;
    private double nav;
    
    
    public Nav(String date,LinkedTreeMap<String,Security> securities,double nav) {
        Gson g = new Gson();
        this.date = date;
//      this.securities = new HashMap<String,Security>();
        
        this.securities = securities;
        this.nav = nav;
    }
}
