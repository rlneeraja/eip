package gov.cdc.nczeid.eip.route.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.Data;

@Data
public class ErrorResponse {
	private String timestamp;
    private ERROR_CODES errorCode;
    private String description;
    private int status;
    private String path;
    private String exception;
    
    private Object[] details = {};
    
    public ErrorResponse() {
    	 this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }

    public ErrorResponse(ERROR_CODES errorCode, String description, String path, int status, String exception) {
    	this();
    	this.errorCode = errorCode;
        this.description = description;
        this.status = status;
        this.path = path;
        this.exception = exception;
    }
}

