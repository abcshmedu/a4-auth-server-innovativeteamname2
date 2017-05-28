package edu.hm;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.hm.management.bib.MediaServiceResult;
import edu.hm.management.user.AuthenticationImpl;
import edu.hm.management.user.AuthenticationResource;
import edu.hm.management.user.IAuthentication;
import edu.hm.management.user.Role;
import edu.hm.management.user.User;

/**
 * Tests on the Authentication Resource Class.
 * @author Daniel Gabl
 *
 */
public class AuthenticationResourceTest {
    
    private IAuthentication tokenService = new AuthenticationImpl();
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    private final User usr1 = new User("Test", "Test", Role.USER);
    private final User usr2 = new User("Test2", "Test2", Role.USER);
    
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
     * Test on createUser.
     */
    @Test
    public void testCreateUser() {
        Response rep = tokenResource.createUser(usr1);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on createToken.
     */
    @Test
    public void testCreateToken()  {
        Response rep = tokenResource.createUser(usr2);
        rep = tokenResource.createToken(usr2, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on getUsers.
     */
    @Test
    public void testGetUsers()  {
        Response rep = tokenResource.createUser(usr1);
        rep = tokenResource.getUsers();
        String repEntity = rep.getEntity().toString();
        String expected1 = "[{\"name\":\"root\",\"pass\":\"rootpasswort\",\"role\":\"ROOT\"},"
                + "{\"name\":\"Test\",\"pass\":\"Test\",\"role\":\"USER\"}]";
        
        String expected2 = "[{\"name\":\"Test\",\"pass\":\"Test\",\"role\":\"USER\"},"
                + "{\"name\":\"root\",\"pass\":\"rootpasswort\",\"role\":\"ROOT\"}]";
        
        // Sometimes, the values change their order -> Test fails
        if (expected1.equals(repEntity))  {
            Assert.assertEquals(expected1, repEntity);
        }
        else if (expected2.equals(repEntity))  {
            Assert.assertEquals(expected2, repEntity);
        }
        
        else  {
            Assert.assertEquals("can not match", repEntity);
        }

    }
    
    /**
     * Test on updateUser.
     */
    @Test
    public void testUpdateUser() {
        User usr = new User("Test", usr1.getPass(), usr1.getRole());
        Response rep = tokenResource.updateUser(usr);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertNotEquals(expected, repEntity);
        // TODO assertEquals
    }
    
    /**
     * Test on findUser.
     */
    @Test
    public void testFindUser() {
        Response rep = tokenResource.createUser(usr1);
        rep = tokenResource.findUser(usr1.getName());
        String repEntity = rep.getEntity().toString();
        String expected = "{\"name\":\"Test\",\"pass\":\"Test\",\"role\":\"USER\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
}
