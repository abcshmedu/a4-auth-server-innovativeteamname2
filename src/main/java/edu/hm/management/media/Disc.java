package edu.hm.management.media;

/**
 * Class represents a Disc Object which has a barcode, a director and a fsk.
 * @author Daniel Gabl
 *
 */
public class Disc extends Medium {
    
    /**
     * Barcode of the Disc (unique).
     */
    private final String barcode;
    
    /**
     * Creator of the Disc.
     */
    private final String director;
    
    /**
     * Age-Restriction for the Disc.
     */
    private final int fsk;
        
    /**
     * Default-Constructor for a Disc Object, only for Jackson.
     */
    private Disc() {
        this("0", "John Doe", 0, null);
    }
    
    /**
     * Extended Constructor for a Disc Object.
     * @param barcode Barcode of a Disc
     * @param director Director of a Disc
     * @param fsk Age-Restriction of a Disc
     * @param title Title of this Disc
     */
    public Disc(String barcode, String director, int fsk, String title)  {
        super(title);
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
        
        //discs.add(this);
    }
    
    /**
     * Getter for the Barcode of a Disc.
     * @return barcode of this disc.
     */
    public String getBarcode()  {
        return barcode;
    }
    
    /**
     * Getter for the Director of a Disc.
     * @return director of a disc.
     */
    public String getDirector()  {
        return director;
    }
    
    /**
     * Getter for the Age-Restriction of a Disc.
     * @return age restriction of a disc.
     */
    public int getFsk()  {
        return fsk;
    }
    
    @Override
    public String toString()  {
        return String.format("Information about this Disc: Title: '%s', Barcode: '%s', Director: '%s', FSK: %d", super.getTitle(), barcode, director, fsk);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((director == null) ? 0 : director.hashCode());
        result = prime * result + fsk;
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
            Disc other = (Disc) obj;
            if (barcode == null) {
                if (other.barcode != null)  {
                    return false;
                }
            } else if (!barcode.equals(other.barcode))  {
                return false;
            }
            if (director == null) {
                if (other.director != null)  {
                    return false;
                }
            } else if (!director.equals(other.director))  {
                return false;
            }
            if (fsk != other.fsk)  {
                return false;
            }
            return true;
        }
        return false;
    }

}
