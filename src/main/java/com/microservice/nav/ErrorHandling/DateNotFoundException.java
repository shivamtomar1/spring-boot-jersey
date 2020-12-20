package com.microservice.nav.ErrorHandling;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DateNotFoundException extends Exception implements ExceptionMapper<DateNotFoundException> {

    /**
     * Exception Template for Date not found in responses /nav/getNavValue
     */
    private static final long serialVersionUID = -8300052716576709995L;
    
    public DateNotFoundException(String s){
        super(s);
    }
    
    public DateNotFoundException(){
        super("Date Not Found");
//        logger.error("In formstexception");
    }

    @Override
    public Response toResponse(DateNotFoundException exception) {
        String error="{\"errorCode\":\""+serialVersionUID+"\",\"errorMessage\":\""+exception.toString()+"\",\"errorType\":\"DateNotFoundException\"}";
        exception.printStackTrace();
        return Response.status(404).entity(error)
                .type("application/json").build();
    }

}
