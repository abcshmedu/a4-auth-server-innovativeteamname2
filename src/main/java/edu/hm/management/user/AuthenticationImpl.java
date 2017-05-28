package edu.hm.management.user;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.hm.management.bib.MediaServiceResult;

/**
 * Implementation of Token Class.
 * @author Daniel Gabl
 *
 */
public class AuthenticationImpl implements IAuthentication  {
    
    /**
     * List of Tokens and their Users.
     */
    private static Map<User, String> tokens = new HashMap<>();
    
    /**
     * List of Logins.
     */
    private static Map<User, String> logins = new HashMap<>();
    
    /**
     * Magic Constant.
     */
    private static final long UNIXDIVISOR = 1000L;
    
    /**
     * Default Constructor.
     */
    public AuthenticationImpl()  {
        User root = new User("root", "rootpasswort", Role.ROOT);
        addUser(root);
    }
    
    /**
     * Method to clear the Library.
     */
    public void clearLibary()  {
        tokens.clear();
        logins.clear();
    }
    
    /**
     * Functions returns IP address of Caller.
     * @return IP address of Caller
     */
    private String getIPaddr()  {
        String addr = null;
        try {
            addr = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return addr;
    }
    
    /**
     * Function to get all Keys from a Map by a value.
     * @param map Map to iterate over
     * @param value Value to search for
     * @param <T> Key Type
     * @param <E> Value Type
     * @return a Set of keys matching the given value
     * 
     * Credits go to: https://stackoverflow.com/a/2904266
     */
    private <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
    
    
    /**
     * Extended function to add a user to the Service which can select if a token should be created imediately.
     * @param usr User to add
     * @return MediaServiceResult
     */
    @Override
    public MediaServiceResult addUser(User usr)  {
        if (usr.getName().isEmpty() || usr.getPass().isEmpty())  {
            return MediaServiceResult.BADREQUEST;
        }
        MediaServiceResult result = MediaServiceResult.DUPLICATEOBJ;
        boolean exists = false;
        for (User user : tokens.keySet())  {
            if (user.equals(usr))  {
                exists = true;
                break;
            }
        }
        if (!exists)  {
            tokens.put(usr, "0");
            result = MediaServiceResult.OKAY;
        }
        return result;
    }
    
    /**
     * Generates a Token for a given user according to a Request.
     * @param usr User of Token
     * @param request Request of Client Execution
     * @return Media Service Result
     */
    public MediaServiceResult generateToken(User usr, HttpServletRequest request)  {
        boolean exists = false;
        User user = null;
        for (User userCheck : tokens.keySet())  {
            if (usr.equals(userCheck))  {
                exists = true;
                user = userCheck;
            }
        }
        
        if (exists)  {
            long unixTime = System.currentTimeMillis() / UNIXDIVISOR;
            String md5string = user.getName() + user.getPass() + user.getRole().getRoleName() + unixTime;
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.reset();
                try {
                    byte[] array = md.digest(md5string.getBytes("UTF-8"));
                    
                    StringBuffer sb = new StringBuffer();
                    final int max8bit = 0xFF; // = 255
                    final int twoPowerEight = 0x100; // 2^8 = 256
                    final int cutTwoByte = 3;
                    
                    for (int i = 0; i < array.length; ++i) {
                      sb.append(Integer.toHexString((array[i] & max8bit) | twoPowerEight).substring(1, cutTwoByte));
                    }
                    String md5 =  sb.toString();
                    
                    // Credit goes to https://stackoverflow.com/a/6565597
                    
                    if (user.hasRole(Role.ROOT))  {
                        md5 = "rootToken";
                    }
                    
                    String ipAddr = "";
                    if (request != null)  {
                        ipAddr = request.getRemoteAddr();
                    }
                    else  {
                        ipAddr = "127.0.0.1";
                    }
                    
                    // Implementation build on https://stackoverflow.com/a/9499961
                    
                    tokens.put(user, md5);
                    logins.put(user, ipAddr);
                    
                    System.out.println("Logins: " + logins);
                    
                    return MediaServiceResult.OKAY;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            
            return MediaServiceResult.BADREQUEST;
        }  else  {
            return MediaServiceResult.UNKNOWNUSER;
        }
    }

    @Override
    public MediaServiceResult validateToken(String token, HttpServletRequest request) {
        for (String value : tokens.values())  {
            if (value.equals(token))  {
                Set<User> users = getKeysByValue(tokens, value);
                for (User user : users)  {
                    if (logins.containsKey(user))  {
                        String ipAddr = "";
                        if (request != null)  {
                            ipAddr = request.getRemoteAddr();
                        }
                        else  {
                            ipAddr = "127.0.0.1";
                        }
                        if (logins.get(user).equals(ipAddr))  {
                            return MediaServiceResult.OKAY;
                        }
                    }
                }
            }
        }
        return MediaServiceResult.TOKENNOTVALID;
    }

    @Override
    public User[] getUsers() {
        User[] user = new User[tokens.size()];
        user = tokens.keySet().toArray(user);
        return user;
    }

    @Override
    public MediaServiceResult updateUser(User usr) {
        boolean userExists = false;
        for (User user : tokens.keySet())  {
            if (usr.equals(user))  {
                userExists = true;
            }
        }
        if (userExists)  {
            Set<User> users = tokens.keySet();
            Iterator<User> iter = users.iterator();
            while (iter.hasNext())  {
                User user = iter.next();
                if (usr.equals(user))  {
                    String username = user.getName();
                    String password = user.getPass();
                    Role role = user.getRole();
                    
                    if (!user.getPass().equals(usr.getPass()) && !usr.getPass().isEmpty())  {
                        password = usr.getPass();
                    }
                    
                    MediaServiceResult result = MediaServiceResult.BADREQUEST;
                    
                    if (!password.equals(user.getPass()))  {  // Data was modified
                        tokens.remove(user);

                        Set<User> userlogins = logins.keySet();
                        if (userlogins.contains(user))  {
                            logins.remove(user);
                        }
                        
                        User newUser = new User(username, password, role);
                        result = addUser(newUser);
                    }
                    return result;
                }
            }
        }
        return MediaServiceResult.UNKNOWNUSER;
    }

    @Override
    public User findUser(String name) {
        for (User user : tokens.keySet())  {
            if (name.equals(user.getName()))  {
                return user;
            }
        }
        return null;
    }

}
