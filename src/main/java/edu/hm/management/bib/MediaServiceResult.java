package edu.hm.management.bib;

import javax.ws.rs.core.Response;

/**
 * Enumeration for the results of the Media Service.
 * @author Daniel Gabl
 *
 */
public enum MediaServiceResult {
    
    OKAY(Response.Status.OK.getStatusCode(), "Okay"),
    FORBIDDEN(Response.Status.FORBIDDEN.getStatusCode(), "Forbidden"),
    NOTFOUND(Response.Status.NOT_FOUND.getStatusCode(), "Not Found"),
    INTERNALSERVERERROR(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal Server Error"),
    BADREQUEST(Response.Status.BAD_REQUEST.getStatusCode(), "Bad Request"),
    DUPLICATEOBJ(Response.Status.BAD_REQUEST.getStatusCode(), "Object already existing"),
    DUPLICATEISBN(Response.Status.BAD_REQUEST.getStatusCode(), "ISBN already exists"),
    ISBNBROKEN(Response.Status.BAD_REQUEST.getStatusCode(), "ISBN is not valid"),
    ISBNNOTFOUND(Response.Status.BAD_REQUEST.getStatusCode(), "ISBN was not found"),
    UNKNOWNUSER(Response.Status.NOT_FOUND.getStatusCode(), "User was not found in the Libary"),
    TOKENNOTVALID(Response.Status.FORBIDDEN.getStatusCode(), "Token is not Valid");
    
    private final int errorCode;
    private final String errorNote;
    
    /**
     * Constructor for ErrorCode Handling.
     * @param code Code of Result (e. g. 200, 404, etc.)
     * @param note Note of the Result (e. g. "Okay", "Not Found", etc.) 
     */
    MediaServiceResult(int code, String note)  {
        errorCode = code;
        errorNote = note;
    }
    
    /**
     * Returns the Status Code for a Response.
     * @return status code
     */
    public int getCode()  {
        return errorCode;
    }
    
    /**
     * Returns the Description of an error code of a response.
     * @return an status description
     */
    public String getNote()  {
        return errorNote;
    }
    
    /**
     * Returns a String containing the Status Code and its description.
     * @return status code and status description
     */
    String getStatus()  {
        return String.format("Status-Code: %d - %s", errorCode, errorNote);
    }
    
}
