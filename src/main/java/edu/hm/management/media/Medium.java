package edu.hm.management.media;

/**
 * Class represents a Medium Object which has a title.
 * @author Daniel Gabl
 *
 */
public class Medium {
    
    /**
     * Title of this Medium.
     */
    private final String title;
    
    /**
     * Constructor for a Medium Object.
     * @param title Title of Medium
     */
    public Medium(String title)  {
        this.title = title;
    }
    
    /**
     * Getter for the Title of this Medium.
     * @return title of medium.
     */
    public String getTitle()  {
        return title;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
    
    @Override
    public String toString()  {
        return String.format("The Title of this Medium is '%s'", title);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)  {
            return true;
        }
        if (obj == null)  {
            return false;
        }
        if (getClass() != obj.getClass())  {
            return false;
        }
        Medium other = (Medium) obj;
        if (title == null) {
            if (other.title != null)  {
                return false;
            }
        } else if (!title.equals(other.title))  {
            return false;
        }
        return true;
    }

}
