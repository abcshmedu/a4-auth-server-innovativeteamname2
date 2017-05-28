package edu.hm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.hm.management.bib.Fsk;
import edu.hm.management.bib.IMediaService;
import edu.hm.management.bib.MediaResource;
import edu.hm.management.bib.MediaServiceImpl;
import edu.hm.management.bib.MediaServiceResult;
import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.user.AuthenticationResource;
import edu.hm.management.user.IAuthentication;
import edu.hm.management.user.Role;
import edu.hm.management.user.User;
import edu.hm.management.user.AuthenticationImpl;

import javax.ws.rs.core.Response;

/**
 * Tests on MediaResource Class.
 * @author Daniel Gabl
 *
 */
public class MediaResourceTest {
    
    private IMediaService service = new MediaServiceImpl();
    private IAuthentication tokenService = new AuthenticationImpl();
    
    private MediaResource resource = new MediaResource();
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    private final String token = "rootToken";
        
    private final Book bk1 = new Book("Richard Castle", "978-3864250101", "Frozen Heat");
    private final String isbn = "978-3-8642-5007-1";

    private final Disc ds1 = new Disc("978-3864250101", "Director-Frozen", Fsk.FSK16.getFsk(), "Title-Frozen");
    private final String barcode = "978-1-56619-909-4";
    
    private final User rootUser = new User("rootUsr", "rootpw", Role.ROOT);
    
    /**
     * Deleting the List each time.
     * @throws Exception in case of failure
     */
    @Before
    public void setUp() throws Exception {
        tokenService.clearLibary();
        tokenService = new AuthenticationImpl();
        tokenResource = new AuthenticationResource(tokenService);
        
        service.clearLibary();
        service = new MediaServiceImpl();
        resource = new MediaResource(service, tokenService);
    }
    
    /**
     * Test on createBook.
     */
    @Test
    public void testCreateBook() {
        // First wrong
        Response rep = resource.createBook(bk1, token, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        rep = resource.createBook(bk1, token, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on getBooks.
     */
    @Test
    public void testGetBooks()  {
        Response rep = resource.getBooks(token);
        String repEntity = rep.getEntity().toString();
        String expected = "[{\"title\":\"Title-909-4\",\"author\":\"Author-909-4\",\"isbn\":\"978-1-56619-909-4\"},"
                + "{\"title\":\"Title-9462-6\",\"author\":\"Author-9462-6\",\"isbn\":\"978-1-4028-9462-6\"},"
                + "{\"title\":\"Heat Wave\",\"author\":\"Richard Castle\",\"isbn\":\"" + isbn + "\"}]";
        
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on updateBook.
     */
    @Test
    public void testUpdateBook() {
        // First wrong
        Book update = new Book("New Author", isbn, "New Title");
        Response rep = resource.updateBook(update, token, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        rep = resource.updateBook(update, token, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on findBook.
     */
    @Test
    public void testFindBook() {
        Response rep = resource.findBook(isbn, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"title\":\"Heat Wave\",\"author\":\"Richard Castle\",\"isbn\":\"978-3-8642-5007-1\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    
    /**
     * Test on createDisc.
     */
    @Test
    public void testCreatDisc()  {
        // First wrong
        Response rep = resource.createDisc(ds1, token, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        rep = resource.createDisc(ds1, token, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on getDiscs.
     */
    @Test
    public void testGetDiscs()  {
        Response rep = resource.getDiscs(token);
        String repEntity = rep.getEntity().toString();
        String expected = "[{\"title\":\"Title-909-4\",\"barcode\":\"978-1-56619-909-4\",\"director\":\"Director-909-4\",\"fsk\":12},"
                + "{\"title\":\"Title-9462-6\",\"barcode\":\"978-1-4028-9462-6\",\"director\":\"Director-9462-6\",\"fsk\":18}]";
        
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on updateDisc.
     */
    @Test
    public void testUpdateDisc() {
        // First wrong
        Disc update = new Disc(barcode, "New Director", Fsk.FSK0.getFsk(), "New Title");
        Response rep = resource.updateDisc(update, token, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        rep = resource.updateDisc(update, token, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on findBook.
     */
    @Test
    public void testFindDisc() {
        Response rep = resource.findDisc(barcode, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"title\":\"Title-909-4\",\"barcode\":\"978-1-56619-909-4\",\"director\":\"Director-909-4\",\"fsk\":12}";
        Assert.assertEquals(expected, repEntity);
    }
}
