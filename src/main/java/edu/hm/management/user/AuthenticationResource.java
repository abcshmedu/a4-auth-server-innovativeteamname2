package edu.hm.management.user;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.management.bib.MediaServiceResult;

/**
 * Class manages media resources.
 * @author Daniel Gabl
 *
 */
@Path("")
public class AuthenticationResource {
    
    /**
     * Token Interface.
     */
    private final IAuthentication token;
    
    /**
     * Default Constructor. Creating a new token service.
     */
    public AuthenticationResource() {
        token = new AuthenticationImpl();
    }

    /**
     * Extended Constructor. Saving a given token resource.
     * @param token Token to set for this token
     */
    public AuthenticationResource(IAuthentication token) {
        this.token = token;
    }
    
    /**
     * Creates a given User.
     * @param user User to create a Token.
     * @return JSON response with status code and created json object.
     */
    @POST
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user)  {
        MediaServiceResult result = token.addUser(user);
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        return Response.status(result.getCode()).entity(json.toString()).build(); // toString, else not serializable
    }
    
    /**
     * Creates a Token for a given User.
     * @param user User to create a Token.
     * @param request Request Data sent by user
     * @return JSON response with status code and created json object.
     */
    @POST
    @Path("/users/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createToken(User user, @Context HttpServletRequest request)  {
        MediaServiceResult result = token.generateToken(user, request);
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        return Response.status(result.getCode()).entity(json.toString()).build(); // toString, else not serializable
    }
    
    /**
     * Returns all Users in our Service.
     * @return JSON response.
     */
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers()  {
        MediaServiceResult result = MediaServiceResult.OKAY;
        return Response.status(result.getCode()).entity(objToJSON(token.getUsers())).build();
    }
    
    /**
     * Updates a given User.
     * @param user User to update
     * @return JSON response.
     */
    @PUT
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user)  {
        MediaServiceResult result = token.updateUser(user);
        
        JSONObject json = new JSONObject();
        json.put("code", result.getCode());
        json.put("detail", result.getNote());    
        
        return Response.status(result.getCode()).entity(json.toString()).build(); // toString, else not serializable
    }
    
    /**
     * Returns a user of our Service.
     * @param name Name of user to find
     * @return JSON response.
     */
    @GET
    @Path("/users/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(@PathParam("name") String name)  {
        MediaServiceResult result = MediaServiceResult.OKAY;
        return Response.status(result.getCode()).entity(objToJSON(token.findUser(name))).build();
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
