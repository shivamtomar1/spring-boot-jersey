package com.microservice.nav.endopints;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.microservice.nav.NavApplication;
import com.microservice.nav.ErrorHandling.DateNotFoundException;
import com.microservice.nav.ErrorHandling.FormatException;
import com.microservice.nav.dao.Holding;
import com.microservice.nav.dao.Nav;
import com.microservice.nav.dao.Pricing;
import com.microservice.nav.dao.Security;
import com.microservice.nav.utils.HttpUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Path("/nav")
public class NavEndpoint {
	
	@Autowired
	private Environment env;
	
//	@Value("${serverUrl.userContent}")
	
	Logger logger = LoggerFactory.getLogger(NavApplication.class);
	
	Gson gson = new Gson();
    
    @GET
    @Produces("application/json")
    @Path("/getNavValue")
    public String getNavValue(@DefaultValue("default") @QueryParam("date") String date) throws Exception{
    	/**
    	 * accepts date in yyyymmdd format
    	 * default date is today
    	 * Returns nav, securities available and requested date to frontend
    	 * 
    	 * Returns {<date>,<securities>:{<security>:{<totalQty>,<price>,<assetsValue>}},<price>}
    	 * 
    	 */
    	logger.info("Calling /nav/getNavValue");
    	LocalDateTime now = LocalDateTime.now();
    	
    	long startTime = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    	
    	double nav=0;
    	
    	// Using LinkedTreeMap as facing casting issues with Hashmap
    	LinkedTreeMap<String,LinkedTreeMap<String,Security>> securitiesByDate;
    	Nav navObj;
    	
    	//pattern for date is 8 digits, throws error if pattern mismatch
    	Pattern datePattern = Pattern.compile("[0-9]{8}");
    	
    	//sets default date to today
    	if(date.equalsIgnoreCase("default")) {
    		date = now.format(DateTimeFormatter.ofPattern("yyyymmdd"));
    	}
    	
        Matcher dateMatcher = datePattern.matcher(date);
        boolean dateMatched = dateMatcher.find();
    	
        if(dateMatched) {
        	securitiesByDate = 
        			gson.fromJson(generateSecuritiesJsonByDate(),new TypeToken<Map<String,LinkedTreeMap<String,Security>>>(){}.getType());
        	
        	logger.debug(securitiesByDate.toString());
        	
        	LinkedTreeMap<String,Security> securitiesAtDate = securitiesByDate.getOrDefault(date, null);
        	
        	if(securitiesAtDate == null) {
        		throw new DateNotFoundException("Date not found");
        	}
        	else {
        		// accumulator for nav, asset value of individual securities already calculated in generateSecuritiesJsonByDate
        		for(Security security: securitiesAtDate.values()) {
        			nav += security.getAssetsValue();
        		}
        		
        		navObj = new Nav(date,securitiesAtDate,nav);
        		
        	}
    	}
    	else {
    		throw new FormatException("Format of date is incorrect");
    	}
        
        long endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        logger.info("Time taken for /nav/getNavValue "+(endTime-startTime)+"ms");
    	
//    	g.toJson(map);
    	
    	return gson.toJson(navObj);
    }
    
    public String generateSecuritiesJsonByDate() throws Exception{
    	/**
    	 * Gets All securities, quantities and prices for all dates by hitting and merging responses from
    	 * https://raw.githubusercontent.com/arcjsonapi/HoldingValueCalculator/master/api/holding
    	 * https://raw.githubusercontent.com/arcjsonapi/HoldingValueCalculator/master/api/pricing
    	 * 
    	 * not using pagination since irrelevant
    	 * 
    	 * returns JSON string in form {<date>:{<security>:{<totalQty>,<price>,<assetsValue>}}}
    	 * 
    	 * date: date in format yyyymmdd
    	 * security: name of security
    	 * totalQty: total quantity of stock
    	 * price: price of security on given date
    	 * assetsValue: total asset value of security on given date
    	 */
    	String userContentDataSource = env.getProperty("serverUrl.userContent");
    	
    	
    	HttpUtil http=new HttpUtil(userContentDataSource);
    	
    	String holdingsResponse = http.sendGet("/holding");
    	String pricingResponse = http.sendGet("/pricing");
    	
    	
    	//creating holdings and pricings array
    	Holding[] holdingArray = gson.fromJson(holdingsResponse,Holding[].class);
    	Pricing[] pricingArray = gson.fromJson(pricingResponse, Pricing[].class);
    	
    	HashMap<String,HashMap<String,Security>> securitiesByDate = new HashMap<String,HashMap<String,Security>>();
    	
    	//sets holdings values(totalQty), security name and date in securitiesByDate hashmap
    	for(Holding holding: holdingArray) {
    		
    		String date = holding.getDate();
    		HashMap<String,Security> securityCollection = securitiesByDate.getOrDefault(date, new HashMap<String,Security>());
    		
    		Security s= securityCollection.getOrDefault(holding.getSecurity(),null);
    		
    		if(s == null) {
    			s = new Security();
    			s.setTotalQty(holding.getQuantity());
    		}
    		else {
    			s.addTotalQty(holding.getQuantity());
    		}
    		
    		securityCollection.put(holding.getSecurity(), s);
    		
    		securitiesByDate.put(date, securityCollection);

    	}
    	
    	//sets price and total asset value for given date in securitiesByDate hashmap
    	for(Pricing pricing:pricingArray) {
    		String date = pricing.getDate();
    		HashMap<String,Security> securityCollection = securitiesByDate.getOrDefault(date, new HashMap<String,Security>());
    		
    		Security s= securityCollection.getOrDefault(pricing.getSecurity(),null);
    		
    		if(s!=null) {
    			s.setPrice(pricing.getPrice());
    			securityCollection.put(pricing.getSecurity(), s);
    			securitiesByDate.put(date, securityCollection);
    		}
    		//no need for else condition, as relevant data already added to hashmap while setting holdings value
    	}
    	
    	//returns as JSON string
    	return gson.toJson(securitiesByDate);
    }
}
