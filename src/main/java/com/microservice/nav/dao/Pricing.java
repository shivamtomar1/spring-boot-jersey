package com.microservice.nav.dao;

/**
 * 
 * Provide DAO for response from https://raw.githubusercontent.com/arcjsonapi/HoldingValueCalculator/master/api/pricing
 * @author Shivam Tomar
 */
public class Pricing {
    
//    {"date" : "20190101", "security" : "ABC Corporation", "price" : 666.63}
    /**
     * JSON structure for pricing response
     */
    
    private String date;
    private String security;
    private double price;
    
    public Pricing(String date, String security, double price) {
        this.date = date;
        this.security = security;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public String getSecurity() {
        return security;
    }

    public double getPrice() {
        return price;
    }
}
