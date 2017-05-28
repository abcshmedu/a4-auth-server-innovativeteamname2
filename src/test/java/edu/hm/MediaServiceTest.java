package edu.hm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.management.bib.Fsk;
import edu.hm.management.bib.IMediaService;
import edu.hm.management.bib.MediaResource;
import edu.hm.management.bib.MediaServiceImpl;
import edu.hm.management.bib.MediaServiceResult;
import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.media.Medium;
import edu.hm.management.user.AuthenticationResource;
import edu.hm.management.user.IAuthentication;
import edu.hm.management.user.AuthenticationImpl;

/**
 * Test Class for Media Service.
 * @author Daniel Gabl
 *
 */
public class MediaServiceTest {
    
    /**
     * Media Interface.
     */
    private IMediaService service = new MediaServiceImpl();
    private IAuthentication newToken = new AuthenticationImpl();
    
    private MediaResource resource = new MediaResource();
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    private Book bk1 = new Book("Richard Castle", "978-3864250101", "Frozen Heat");
    private Book bk2 = new Book("Rick Castle", "978-3864252969", "Deadly Heat");

    private Disc ds1 = new Disc("978-3864250101", "Director-Frozen", Fsk.FSK16.getFsk(), "Title-Frozen");
    private Disc ds2 = new Disc("978-3864252969", "Director-Deadly", Fsk.FSK12.getFsk(), "Title-Deadly");
    
    /**
     * Deleting the List each time. Simulating Jackson Behavior.
     * @throws Exception in case of failure
     */
    @Before
    public void setUp() throws Exception {
        newToken = new AuthenticationImpl();
        tokenResource = new AuthenticationResource(newToken);
        
        service.clearLibary();
        service = new MediaServiceImpl();
        resource = new MediaResource(service, newToken);
    }
    
    /**
     * Tests on addBook.
     * Möglicher Fehler: Ungültige ISBN
     * Möglicher Fehler: ISBN bereits vorhanden
     * Möglicher Fehler: Autor oder Titel fehlt
     */
    @Test
    public void testAddBook() {
        // ISBN wrong
        Book isbnFalse = new Book("Hans", "isbnistfalsch", "ISBN ist falsch");
        service.updateBook(isbnFalse);
        MediaServiceResult result = service.addBook(isbnFalse);
        Assert.assertEquals(MediaServiceResult.ISBNBROKEN.getNote(), result.getNote());
        
        Book isbnFalse2 = new Book("Hans", "987-3864252969", "978 -> 987");
        result = service.addBook(isbnFalse2);
        Assert.assertEquals(MediaServiceResult.ISBNBROKEN.getNote(), result.getNote());
        
        // Author / Title missing
        Book missingAuthor = new Book("", bk1.getIsbn(), "Autor fehlt");
        result = service.addBook(missingAuthor);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        Book missingTitle = new Book("Titel fehlt", bk1.getIsbn(), "");
        result = service.addBook(missingTitle);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Default Case
        result = service.addBook(bk1);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        result = service.addBook(bk2);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        Book newbook = new Book("Das Restaurant am Ende des Universums", "978-3-86631-007-0", "Adams Douglas");
        result = service.addBook(newbook);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Book already exists
        result = service.addBook(bk2);
        Assert.assertNotEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        Assert.assertEquals(MediaServiceResult.DUPLICATEOBJ.getNote(), result.getNote());
        
        // ISBN exists
        Book isbnDuplicate = new Book("Not Richard Castle", bk1.getIsbn(), "Not Frozen Heat");
        result = service.addBook(isbnDuplicate);
        Assert.assertEquals(MediaServiceResult.DUPLICATEISBN.getNote(), result.getNote());
    }
    
    /**
     * Tests on addDisc.
     * Möglicher Fehler: Ungültige Barcode
     * Möglicher Fehler: Barcode bereits vorhanden
     * Möglicher Fehler: Director oder Titel fehlt
     */
    @Test
    public void testAddDisc() {
        // All missing wrong
        Disc allMissing = new Disc("", "", Fsk.FSK0.getFsk(), "");
        MediaServiceResult result = service.addDisc(allMissing);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Barcode wrong
        Disc barcodeFalse = new Disc("8-88837-34272-8", "James Arthur", Fsk.FSK0.getFsk(), "Impossible");
        result = service.addDisc(barcodeFalse);
        Assert.assertEquals(MediaServiceResult.ISBNBROKEN.getNote(), result.getNote());
        
        // Director / Title missing
        Disc missingDirector = new Disc(ds1.getBarcode(), "Titel fehlt", Fsk.FSK0.getFsk(), "");
        result = service.addDisc(missingDirector);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        Disc missingTitle = new Disc(ds1.getBarcode(), "", Fsk.FSK0.getFsk(), "Director fehlt");
        result = service.addDisc(missingTitle);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Default Case
        result = service.addDisc(ds1);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        result = service.addDisc(ds2);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Disc already exists
        result = service.addDisc(ds2);
        Assert.assertNotEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        Assert.assertEquals(MediaServiceResult.DUPLICATEOBJ.getNote(), result.getNote());
        
        // Barcode exists
        Disc barcodeDuplicate = new Disc(ds1.getBarcode(), "Not Director-Frozen", Fsk.FSK16.getFsk(), "Not Title-Frozen");
        result = service.addDisc(barcodeDuplicate);
        Assert.assertEquals(MediaServiceResult.DUPLICATEISBN.getNote(), result.getNote());
    }
    
    /**
     * Test on getBooks.
     */
    @Test
    public void testGetBooks() {
        Medium[] books = service.getBooks();
        String booksJSON = objToJSON(books);
        String expected = "[{\"title\":\"Title-909-4\",\"author\":\"Author-909-4\",\"isbn\":\"978-1-56619-909-4\"},"
                + "{\"title\":\"Title-9462-6\",\"author\":\"Author-9462-6\",\"isbn\":\"978-1-4028-9462-6\"},"
                + "{\"title\":\"Heat Wave\",\"author\":\"Richard Castle\",\"isbn\":\"978-3-8642-5007-1\"}]";
        Assert.assertEquals(expected, booksJSON);
    }
    
    /**
     * Tests on getDiscs.
     */
    @Test
    public void testGetDiscs() {
        Medium[] discs = service.getDiscs();
        String discsJSON = objToJSON(discs);
        String expected = "[{\"title\":\"Title-909-4\",\"barcode\":\"978-1-56619-909-4\",\"director\":\"Director-909-4\",\"fsk\":12},"
                + "{\"title\":\"Title-9462-6\",\"barcode\":\"978-1-4028-9462-6\",\"director\":\"Director-9462-6\",\"fsk\":18}]";
        Assert.assertEquals(expected, discsJSON);
    }
    
    /**
     * Tests on updateBook.
     * 
     * Möglicher Fehler: ISBN nicht gefunden
     * Möglicher Fehler: Autor und Titel fehlen
     * Möglicher Fehler: Neue Daten entsprechen den alten Daten
     */
    @Test
    public void testUpdateBook()  {
        Book bk2Copy = new Book(bk2.getAuthor(), bk2.getIsbn(), bk2.getTitle());
        MediaServiceResult result = service.addBook(bk2Copy);
        
        // Same Object
        result = service.updateBook(bk2Copy);
        Assert.assertNotEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Empty Object
        Book empty = new Book("", bk2.getIsbn(), "");
        result = service.updateBook(empty);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Wrong ISBN
        Book wrongIsbn = new Book(bk1.getAuthor(), "1231312123", bk1.getTitle());
        result = service.updateBook(wrongIsbn);
        Assert.assertEquals(MediaServiceResult.ISBNNOTFOUND.getNote(), result.getNote());
        
        // All correct
        Book correct = new Book(bk1.getAuthor(), bk2.getIsbn(), bk1.getTitle());
        result = service.updateBook(correct);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // New Data
        Book correct2 = new Book("New Author", bk2.getIsbn(), "New Title");
        result = service.updateBook(correct2);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Change only Author
        Book author = new Book(bk1.getAuthor(), bk2.getIsbn(), "");
        result = service.updateBook(author);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Change only Title
        Book title = new Book(bk1.getAuthor(), bk2.getIsbn(), bk1.getTitle());
        result = service.updateBook(title);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
    }
    
    /**
     * Tests on updateDisc.
     * 
     * Möglicher Fehler: Barcode nicht gefunden
     * Möglicher Fehler: Director und Titel fehlen
     * Möglicher Fehler: Neue Daten entsprechen den alten Daten
     */
    @Test
    public void testUpdateDisc()  {
        Disc ds1copy = new Disc(ds1.getBarcode(), ds1.getDirector(), ds1.getFsk(), ds1.getTitle());
        Disc ds2copy = new Disc(ds2.getBarcode(), ds2.getDirector(), ds2.getFsk(), ds2.getTitle());
        MediaServiceResult result = service.addDisc(ds1copy);
        result = service.addDisc(ds2copy);
         
        // Same Object
        result = service.updateDisc(ds1copy);
        Assert.assertNotEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Empty Object
        Disc empty = new Disc(ds2.getBarcode(), "", -1, "");
        result = service.updateDisc(empty);
        Assert.assertEquals(MediaServiceResult.BADREQUEST.getNote(), result.getNote());
        
        // Wrong ISBN
        Disc wrongBarcode = new Disc("1231312123", ds1.getDirector(), Fsk.FSK0.getFsk(), ds1.getTitle());
        result = service.updateDisc(wrongBarcode);
        Assert.assertEquals(MediaServiceResult.ISBNNOTFOUND.getNote(), result.getNote());
        
        // All correct
        Disc correct = new Disc(ds1.getBarcode(), ds2.getDirector(), ds2.getFsk(), ds2.getTitle());
        result = service.updateDisc(correct);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // New Data
        Disc correct2 = new Disc(ds1.getBarcode(), "New Director", Fsk.FSK18.getFsk(), "New Title");
        result = service.updateDisc(correct2);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Change only Director
        Disc author = new Disc(ds2.getBarcode(), "New Director", ds2.getFsk(), ds2.getTitle());
        result = service.updateDisc(author);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Change only Title
        Disc title = new Disc(ds2.getBarcode(), "New Director", ds2.getFsk(), "New Title");
        result = service.updateDisc(title);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
        
        // Change only Fsk
        Disc fsk = new Disc(ds2.getBarcode(), "New Director", ds2.getFsk() + Fsk.FSK18.getFsk(), "New Title");
        result = service.updateDisc(fsk);
        Assert.assertEquals(MediaServiceResult.OKAY.getNote(), result.getNote());
    }
    
    /**
     * Tests on findBook.
     */
    @Test
    public void testFindBook()  {
        Book bk1Copy = new Book(bk1.getAuthor(), bk1.getIsbn(), bk1.getTitle());
        service.addBook(bk1Copy);
        
        // wrong ISBN
        Medium wrong = service.findBook("1234567890123");
        String bookJSON = objToJSON(wrong);
        String expected = "null";
        Assert.assertEquals(expected, bookJSON);
        
        // correct ISBN
        Medium correct = service.findBook(bk1.getIsbn());
        bookJSON = objToJSON(correct);
        expected = "{\"title\":\"Frozen Heat\",\"author\":\"Richard Castle\",\"isbn\":\"978-3864250101\"}";
        Assert.assertEquals(expected, bookJSON);
    }
    
    /**
     * Tests on findDisc.
     */
    @Test
    public void testFindDisc()  {
        Disc ds1copy = new Disc(ds1.getBarcode(), ds1.getDirector(), ds1.getFsk(), ds1.getTitle());
        service.addDisc(ds1copy);
        
        // wrong barcode
        Medium wrong = service.findDisc("1234567890123");
        String discJSON = objToJSON(wrong);
        String expected = "null";
        Assert.assertEquals(expected, discJSON);
        
        // correct barcode
        Medium correct = service.findDisc(ds1.getBarcode());
        discJSON = objToJSON(correct);
        expected = "{\"title\":\"Title-Frozen\",\"barcode\":\"978-3864250101\",\"director\":\"Director-Frozen\",\"fsk\":16}";
        Assert.assertEquals(expected, discJSON);
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
