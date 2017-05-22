package edu.hm.management.bib;

/**
 * Enumeration for the fsk of some Medium objects.
 * @author Daniel Gabl
 *
 */
public enum Fsk {
    
    FSK0(0),
    FSK6(6),
    FSK12(12),
    FSK16(16),
    FSK18(18);
    
    private final int fskLevel;
    
    /**
     * Constructor for Fsk Level.
     * @param fsk Fsk Level (e. g. 0, 16, etc.)
     */
    Fsk(int fsk)  {
        fskLevel = fsk;
    }
    
    /**
     * Returns the Fsk Level.
     * @return fsk level
     */
    public int getFsk()  {
        return fskLevel;
    }  
}
