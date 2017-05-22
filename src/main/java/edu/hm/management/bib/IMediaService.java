package edu.hm.management.bib;

import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.media.Medium;

/**
 * Interface for the Media Service.
 * @author Daniel Gabl
 *
 */
public interface IMediaService {
    
    /**
     * Method to clear the Library.
     */
    void clearLibary();
    
    /**
     * Adds a given book to our Service Routine.
     * @param book Book to add.
     * @return Media Service Result (Enum)
     */
    MediaServiceResult addBook(Book book);
    
    /**
     * Adds a given disc to our Service Routine.
     * @param disc Disc to add.
     * @return Media Service Result (Enum)
     */
    MediaServiceResult addDisc(Disc disc);
    
    /**
     * Returns all Books of our Service routine.
     * @return all Books of our Service routine.
     */
    Medium[] getBooks();
    
    /**
     * Returns all Discs of our Service routine.
     * @return all Discs of our Service routine.
     */
    Medium[] getDiscs();
    
    /**
     * Updates a given Book in our Service Routine.
     * @param book Book to update.
     * @return Media Service Result (Enum)
     */
    MediaServiceResult updateBook(Book book);
    
    /**
     * Updates a given Disc in our Service Routine.
     * @param disc Disc to update.
     * @return Media Service Result (Enum)
     */
    MediaServiceResult updateDisc(Disc disc);
    
    /**
     * Returns a Book of our Service routine.
     * @param isbn ISBN of Book to find.
     * @return a Book of our Service routine.
     */
    Medium findBook(String isbn);
    
    /**
     * Returns a Disc of our Service routine.
     * @param barcode Barcode of Disc to find.
     * @return a Disc of our Service routine.
     */
    Medium findDisc(String barcode);
}
