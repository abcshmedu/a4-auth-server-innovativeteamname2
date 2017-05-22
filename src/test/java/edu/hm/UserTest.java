package edu.hm;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.hm.management.user.Role;
import edu.hm.management.user.User;

/**
 * Testing some User specific tasks.
 * @author Daniel Gabl
 *
 */
public class UserTest {
    
    /**
     * Test on hasRole.
     */
    @Test
    public void testHasRole() {
        User usr1 = new User("Hans Peter", "hp", Role.USER);
        User usr2 = new User("Hans", "password", Role.ADMIN);
        User usr3 = new User("root", "root", Role.ROOT);

        assertTrue(usr1.hasRole(Role.USER));
        assertFalse(usr1.hasRole(Role.ADMIN));
        assertFalse(usr1.hasRole(Role.ROOT));
        
        assertTrue(usr2.hasRole(Role.USER));
        assertTrue(usr2.hasRole(Role.ADMIN));
        assertFalse(usr2.hasRole(Role.ROOT));
        
        assertTrue(usr3.hasRole(Role.USER));
        assertTrue(usr3.hasRole(Role.ADMIN));
        assertTrue(usr3.hasRole(Role.ROOT));
    }

}
