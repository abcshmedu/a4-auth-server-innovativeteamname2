package edu.hm.management.user;

/**
 * Class to handle Roles.
 * @author Daniel Gabl
 *
 */
public enum Role {
    
    ROOT(0, "Root"),
    ADMIN(1, "Admin"),
    USER(2, "User");
    
    private final int level;
    private final String name;
    
    /**
     * Constructor for a Permission.
     * @param level Level of Permission
     * @param name Name of Permission
     */
    Role(int level, String name)  {
        this.level = level;
        this.name = name;
    }
   /**
    * Returns Level of Permission.
    * @return level
    */
    public int getRoleLevel()  {
        return level;
    }
    
    /**
     * Returns Name of Permission.
     * @return name
     */
    public String getRoleName()  {
        return name;
    }
    
    @Override
    public String toString()  {
        return "Role: " + name + "; Level: " + level;
    }
}
