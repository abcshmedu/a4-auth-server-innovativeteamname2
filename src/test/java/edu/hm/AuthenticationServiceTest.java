package edu.hm;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.management.bib.MediaServiceResult;
import edu.hm.management.user.AuthenticationImpl;
import edu.hm.management.user.AuthenticationResource;
import edu.hm.management.user.IAuthentication;
import edu.hm.management.user.Role;
import edu.hm.management.user.User;

/**
 * Test Class for AuthenticationImpl Class.
 * @author Daniel Gabl
 *
 */
public class AuthenticationServiceTest {
    
    private IAuthentication tokenService = new AuthenticationImpl();
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    private final String token = "rootToken";
    
    private final User usr1 = new User("Test", "Test", Role.USER);
    private final User usr2 = new User("Test2", "Test2", Role.USER);
    private final User rootUser = new User("rootUser", "rootpw", Role.ROOT);
    
    /**
     * Deleting the List each time.
     * @throws Exception in case of failure
     */
    @Before
    public void setUp() throws Exception {
        tokenService.clearLibary();
        tokenService = new AuthenticationImpl();
        tokenResource = new AuthenticationResource(tokenService);
    }
    
    /**
     * Test on generateToken.
     * Möglicher Fehler: User nicht in Datenbank.
     */
    @Test
    public void testGenerateToken()  {
        // Not in Database
        MediaServiceResult result = tokenService.generateToken(rootUser);
        Assert.assertEquals(MediaServiceResult.UNKNOWNUSER.getNote(), result.getNote());
        
        // Allright
        tokenService.addUser(usr1);
        
        tokenService.addUser(rootUser);
        result = tokenService.generateToken(rootUser);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
    }
    
    /**
     * Test on validateToken.
     * Möglicher Fehler: Ungültiger Token.
     */
    @Test
    public void testValidateToken()  {
        // Not Valid
        String unvalidToken = "notvalid";
        MediaServiceResult result = tokenService.validateToken(unvalidToken);
        Assert.assertEquals(MediaServiceResult.TOKENNOTVALID.getNote(), result.getNote());
        
        // Valid
        tokenService.addUser(usr2);
        tokenService.addUser(rootUser);
        result = tokenService.validateToken(token);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
    }
    
    /**
     * Tests on getUsers.
     */
    @Test
    public void testGetUsers() {
        User[] users = tokenService.getUsers();
        String discsJSON = objToJSON(users);
        String expected = "[{\"name\":\"root\",\"pass\":\"rootpasswort\",\"role\":\"ROOT\"}]";
        Assert.assertEquals(expected, discsJSON);
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
    
    /**
     * Test on findUser.
     * Möglicher Fehler: User nicht vorhanden
     */
    @Test
    public void testFindUser() {
        // Does not exists
        String noUser = "This User does not exists";
        Response rep = tokenResource.findUser(noUser);
        String repEntity = rep.getEntity().toString();
        String expected = "null";
        Assert.assertEquals(expected, repEntity);
        
        // Exists
        rep = tokenResource.createUser(usr1);
        rep = tokenResource.findUser(usr1.getName());
        repEntity = rep.getEntity().toString();
        expected = "{\"name\":\"Test\",\"pass\":\"Test\",\"role\":\"USER\"}";
        Assert.assertEquals(expected, repEntity);
    }
    

}
