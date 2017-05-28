package edu.hm.management.user;

import javax.servlet.http.HttpServletRequest;

import edu.hm.management.bib.MediaServiceResult;

/**
 * Interface for Token (Authorization Sub Domain).
 * @author Daniel Gabl
 *
 */
public interface IAuthentication {
    
    /**
     * Method to clear the Library.
     */
    void clearLibary();
    
    /**
     * Adds a User to the Sub-System.
     * @param usr User to add
     * @return Media Service Result
     */
    MediaServiceResult addUser(User usr);
    
    /**
     * Updates a given User.
     * @param usr User to update
     * @return Media Service Result
     */
    MediaServiceResult updateUser(User usr);
    
    /** Generates a Token for a given user.
     * 
     * @param usr User of Token
     * @param request Request Data of Client
     * @return Media Service Result
     */
    MediaServiceResult generateToken(User usr, HttpServletRequest request);
       
    /**
     * Checks if a token is valid or not.
     * @param token token to check
     * @param request Request Data of Client
     * @return Media Service Result
     */
    MediaServiceResult validateToken(String token, HttpServletRequest request);
    
    /**
     * Returns all Users.
     * @return all users
     */
    User[] getUsers();
    
    /**
     * Returns a User.
     * @param name Name of User to find
     * @return a user
     */
    User findUser(String name);
}
