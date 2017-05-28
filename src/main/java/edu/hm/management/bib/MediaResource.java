package edu.hm.management.bib;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.user.IAuthentication;
import edu.hm.management.user.AuthenticationImpl;

/**
 * Class manages media ressources.
 * @author Daniel Gabl
 *
 */
@Path("/media")
public class MediaResource {
    
    /**
     * Media Interface.
     */
    private final IMediaService service;
    private final IAuthentication tokenService;
    
    /**
     * Default Constructor. Creating a new media service.
     */
    public MediaResource() {
        service = new MediaServiceImpl();
        tokenService = new AuthenticationImpl();
    }

    /**
     * Extended Constructor. Saving a given media service.
     * @param service Service to set for this service
     * @param tokenService Service to validate Tokens
     */
    public MediaResource(IMediaService service, IAuthentication tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }
    
    /**
     * Validates a given Token. Validation happens in Authorization Sub Routine.
     * @param token Token to check on
     * @param request 
     * @return true if token is valid, else false
     */
    private MediaServiceResult tokenValid(String token, HttpServletRequest request) {
        return tokenService.validateToken(token, request);
    }
    
    /**
     * Creates a given Book.
     * @param book Book to create
     * @param token Token to check on
     * @param request Request Data of Client
     * @return JSON response with status code and created json object.
     */
    @POST
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBook(Book book, @QueryParam("token") String token, @Context HttpServletRequest request)  {
        MediaServiceResult result = tokenValid(token, request);
        
        if (result.equals(MediaServiceResult.OKAY)) {
            result = service.addBook(book);
        }
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        return Response.status(result.getCode()).entity(json.toString()).build(); // toString, else not serializable
    }

    /**
     * Returns all books in our Service.
     * @param token Token to check on
     * @return JSON response.
     */
    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@QueryParam("token") String token)  {
        MediaServiceResult result = MediaServiceResult.OKAY;
        return Response.status(result.getCode()).entity(objToJSON(service.getBooks())).build();
    }
    
    /**
     * Updates a given Book.
     * @param book Book to update
     * @param token Token to check on
     * @param request Request Data of Client
     * @return JSON response.
     */
    @PUT
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(Book book, @QueryParam("token") String token, @Context HttpServletRequest request)  {
        MediaServiceResult result = tokenValid(token, request);
        
        if (result.equals(MediaServiceResult.OKAY)) {
            result = service.updateBook(book);
        }
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        return Response.status(result.getCode()).entity(json.toString()).build(); // toString, else not serializable
    }
    
    /**
     * Returns a book of our Service.
     * @param isbn ISBN of Book to find
     * @param token Token to check on
     * @return JSON response.
     */
    @GET
    @Path("/books/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findBook(@PathParam("isbn") String isbn, @QueryParam("token") String token)  {
        MediaServiceResult result = MediaServiceResult.OKAY;
        return Response.status(result.getCode()).entity(objToJSON(service.findBook(isbn))).build();
    }
    
    
    
    /**
     * Creates a given Disc.
     * @param disc Disc to create
     * @param token Token to check on
     * @param request Request Data of Client
     * @return JSON response.
     */
    @POST
    @Path("/discs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDisc(Disc disc, @QueryParam("token") String token, @Context HttpServletRequest request)  {
        MediaServiceResult result = tokenValid(token, request);
        
        if (result.equals(MediaServiceResult.OKAY)) {
            result = service.addDisc(disc);
        }
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        //System.out.println("JSON Answer:\n" + json.toString());
        return Response.status(result.getCode()).entity(json.toString()).build();
    }
    
    /**
     * Returns all discs in our Service.
     * @param token Token to check on
     * @return JSON response.
     */
    @GET
    @Path("/discs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscs(@QueryParam("token") String token)  {
        MediaServiceResult result = MediaServiceResult.OKAY;
        return Response.status(result.getCode()).entity(objToJSON(service.getDiscs())).build();
    }
    
    /**
     * Updates a given Disc.
     * @param disc Disc to update
     * @param token Token to check on
     * @param request Request Data of Client
     * @return JSON response.
     */
    @PUT
    @Path("/discs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDisc(Disc disc, @QueryParam("token") String token, @Context HttpServletRequest request)  {
        MediaServiceResult result = tokenValid(token, request);
        
        if (result.equals(MediaServiceResult.OKAY)) {
            result = service.updateDisc(disc);
        }
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        //System.out.println("JSON Answer:\n" + json.toString());
        return Response.status(result.getCode()).entity(json.toString()).build();
    }
    
    /**
     * Returns a disc of our Service.
     * @param barcode Barcode of Disc to find
     * @param token Token to check on
     * @return JSON response.
     */
    @GET
    @Path("/discs/{barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findDisc(@PathParam("barcode")String barcode, @QueryParam("token") String token)  {
        MediaServiceResult result = MediaServiceResult.OKAY;
        return Response.status(result.getCode()).entity(objToJSON(service.findDisc(barcode))).build();
    }
    
    /**
     * Converts an Object into an JSON String.
     * @param obj Object to convert
     * @return JSON representation of given Object.
     */
    private String objToJSON(Object obj)  {
        ObjectMapper mapper = new ObjectMapper();

        //Object to JSON in String
        String jsonInString = "{code: 400, detail: \"Bad Request\"}";
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        return jsonInString;
    }
}
