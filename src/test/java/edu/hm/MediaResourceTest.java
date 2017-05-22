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
import edu.hm.management.user.AuthenticationImpl;

import javax.ws.rs.core.Response;

/**
 * Tests on MediaResource Class.
 * @author Daniel Gabl
 *
 */
public class MediaResourceTest {
    
    private IMediaService newService = new MediaServiceImpl();
    private IAuthentication newToken = new AuthenticationImpl();
    
    private MediaResource resource = new MediaResource();
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    private String token = "rootToken";
        
    private Book bk1 = new Book("Richard Castle", "978-3864250101", "Frozen Heat");
    private String isbn = "978-3-8642-5007-1";

    private Disc ds1 = new Disc("978-3864250101", "Director-Frozen", Fsk.FSK16.getFsk(), "Title-Frozen");
    private String barcode = "978-1-56619-909-4";
    
    /**
     * Deleting the List each time.
     * @throws Exception in case of failure
     */
    @Before
    public void setUp() throws Exception {
        newToken = new AuthenticationImpl();
        tokenResource = new AuthenticationResource(newToken);
        
        newService.clearLibary();
        newService = new MediaServiceImpl();
        resource = new MediaResource(newService, newToken);
    }
    
    /**
     * Test on createBook.
     */
    @Test
    public void testCreateBook() {
        Response rep = resource.createBook(bk1, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(repEntity, expected);
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
        
        Assert.assertEquals(repEntity, expected);
    }
    
    /**
     * Test on updateBook.
     */
    @Test
    public void testUpdateBook() {
        Book update = new Book("New Author", isbn, "New Title");
        Response rep = resource.updateBook(update, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(repEntity, expected);
    }
    
    /**
     * Test on findBook.
     */
    @Test
    public void testFindBook() {
        Response rep = resource.findBook(isbn, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"title\":\"Heat Wave\",\"author\":\"Richard Castle\",\"isbn\":\"978-3-8642-5007-1\"}";
        Assert.assertEquals(repEntity, expected);
    }
    
    
    /**
     * Test on createDisc.
     */
    @Test
    public void testCreatDisc()  {
        Response rep = resource.createDisc(ds1, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(repEntity, expected);
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
        
        Assert.assertEquals(repEntity, expected);
    }
    
    /**
     * Test on updateDisc.
     */
    @Test
    public void testUpdateDisc() {
        Disc update = new Disc(barcode, "New Director", Fsk.FSK0.getFsk(), "New Title");
        Response rep = resource.updateDisc(update, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(repEntity, expected);
    }
    
    /**
     * Test on findBook.
     */
    @Test
    public void testFindDisc() {
        Response rep = resource.findDisc(barcode, token);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"title\":\"Title-909-4\",\"barcode\":\"978-1-56619-909-4\",\"director\":\"Director-909-4\",\"fsk\":12}";
        Assert.assertEquals(repEntity, expected);
    }
}
