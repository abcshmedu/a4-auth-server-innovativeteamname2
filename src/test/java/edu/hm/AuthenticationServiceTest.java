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
    
    private final User usr = new User("Test", "Test", Role.USER);
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
     * Tests on addUser.
     * Möglicher Fehler: Name oder Passwort leer.
     * Möglicher Fehler: User existiert bereits.
     */
    @Test
    public void testAddUser()  {
        // Missing Data
        User noName = new User("", "password");
        User noPass = new User("username", "");
        
        MediaServiceResult result = tokenService.addUser(noName);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        result = tokenService.addUser(noPass);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Everything Okay
        result = tokenService.addUser(rootUser);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Duplicate User
        result = tokenService.addUser(rootUser);
        Assert.assertEquals(MediaServiceResult.DUPLICATEOBJ.getNote(), result.getNote());
    }
    
    /**
     * Test on generateToken.
     * Möglicher Fehler: User nicht in Datenbank.
     */
    @Test
    public void testGenerateToken()  {
        // Not in Database
        MediaServiceResult result = tokenService.generateToken(rootUser, null);
        Assert.assertEquals(MediaServiceResult.UNKNOWNUSER.getNote(), result.getNote());
        
        // Allright
        tokenService.addUser(usr);
        
        tokenService.addUser(rootUser);
        result = tokenService.generateToken(rootUser, null);
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
        MediaServiceResult result = tokenService.validateToken(unvalidToken, null);
        Assert.assertEquals(MediaServiceResult.TOKENNOTVALID.getNote(), result.getNote());
        
        // Valid
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        result = tokenService.validateToken(token, null);
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
     *  Tests on updateUser.
     *  Mögliche Fehler: User nicht vorhanden
     *  Mögliche Fehler: Passwort ist identisch
     */
    @Test
    public void testUpdateUser()  {
        // User not in Database
        User noUser = new User("Not exist", "Password", Role.USER);
        MediaServiceResult result = tokenService.updateUser(noUser);
        Assert.assertEquals(MediaServiceResult.UNKNOWNUSER.getNote(), result.getNote());
        
        // Same password / data
        result = tokenService.addUser(rootUser);
        User samePassword = new User(rootUser.getName(), rootUser.getPass(), rootUser.getRole());
        result = tokenService.updateUser(samePassword);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Empty Password
        User emptyPassword = new User(rootUser.getName(), "", rootUser.getRole());
        result = tokenService.updateUser(emptyPassword);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // All correct
        String newPassword = "New Password";
        User newUser = new User(rootUser.getName(), newPassword, rootUser.getRole());
        result = tokenService.updateUser(newUser);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Existing User which was not logged in yet
        newPassword = "Newer Password";
        User root = new User("root", newPassword, Role.ROOT);
        result = tokenService.updateUser(root);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
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
        rep = tokenResource.createUser(usr);
        rep = tokenResource.findUser(usr.getName());
        repEntity = rep.getEntity().toString();
        expected = "{\"name\":\"Test\",\"pass\":\"Test\",\"role\":\"USER\"}";
        Assert.assertEquals(expected, repEntity);
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
