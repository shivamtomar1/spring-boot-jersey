package com.microservice.nav.ErrorHandling;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class FormatException extends Exception implements ExceptionMapper<FormatException> {

    /**
     * Exception Template for Date Format Mismatch in responses /nav/getNavValue
     */
    
//    Logger logger = LoggerFactory.getLogger(NavApplication.class);
    private static final long serialVersionUID = -2262518668984742328L;
    
    public FormatException(String s){
        super(s);
//        logger.error("In formstexception");
    }
    
    
    public FormatException(){
        super("Date Format Exception");
//        logger.error("In formstexception");
    }

    @Override
    public Response toResponse(FormatException exception) {
        String error="{\"errorCode\":\""+serialVersionUID+"\",\"errorMessage\":\""+exception.toString()+"\",\"errorType\":\"FormatException\"}";
        exception.printStackTrace();
//        logger.error("In formstexception");
        return Response.status(504).entity(error)
                .type("application/json").build();
    }
}
