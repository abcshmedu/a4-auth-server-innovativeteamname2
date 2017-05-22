package edu.hm.management.media;

/**
 * Class represents a Book Object which has an author and an isbn.
 * @author Daniel Gabl
 *
 */
public class Book extends Medium {
    /**
     * Author of this Book.
     */
    private final String author;
    
    /**
     * ID for this Book (unique).
     */
    private final String isbn;
    
    /**
     * Default Constructor for a Book Object, only for Jackson.
     */
    private Book() {
        this("John Doe", "0", null);
    }
       
    /**
     * Extended Constructor for a Book Object.
     * @param author Author of the Book.
     * @param isbn ID of the Book.
     * @param title Title of the Book.
     */
    public Book(String author, String isbn, String title)  {
        super(title);
        this.author = author;
        this.isbn = isbn;
        
        // Erzeugtes Element in Liste speichern.
        //books.add(this);
    }
    
    /**
     * Getter for the Author of the Book.
     * @return author of book.
     */
    public String getAuthor()  {
        return author;
    }
    
    /**
     * Getter for ID of the Book.
     * @return id of book.
     */
    public String getIsbn()  {
        return isbn;
    }
    
    @Override
    public String toString()  {
        return String.format("This Book is called '%s', has the ISBN '%s' and was written by '%s'", super.getTitle(), isbn, author);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))  {
            if (this == obj)  {
                return true;
            }
            if (!super.equals(obj))  {
                return false;
            }
            if (getClass() != obj.getClass())  {
                return false;
            }
            Book other = (Book) obj;
            if (author == null) {
                if (other.author != null)  {
                    return false;
                }
            } else if (!author.equals(other.author))  {
                return false;
            }
            if (isbn == null) {
                if (other.isbn != null)  {
                    return false;
                }
            } else if (!isbn.equals(other.isbn))  {
                return false;
            }
            return true;
        }
        return false;
    }
}
