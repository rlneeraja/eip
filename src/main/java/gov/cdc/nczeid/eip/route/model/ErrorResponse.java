package gov.cdc.nczeid.eip.route.model;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorResponse {
	private Date timestamp;
    private ERROR_CODES errorCode;
    private String description;
    private int status;
    private String path;
    private String exception;
    
    
    public ErrorResponse() {
    }

    public ErrorResponse(Date timestamp, ERROR_CODES errorCode, String description, String path, int status, String exception) {
        this.timestamp = timestamp;
    	this.errorCode = errorCode;
        this.description = description;
        this.status = status;
        this.path = path;
        this.exception = exception;
    }
}

