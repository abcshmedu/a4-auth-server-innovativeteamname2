package edu.hm.management.user;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

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
        addUser(root, false);
    }
    
    /**
     * Functions returns IP address of Caller.
     * @return IP address of Caller
     */
    private String getIPaddr()  {
        String addr = null;
        try {
            addr = InetAddress.getLocalHost().getHostAddress();
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
    
    @Override
    public MediaServiceResult addUser(User usr) {
        return addUser(usr, true);
    }
    
    /**
     * Extended function to add a user to the Service which can select if a token should be created imediately.
     * @param usr User to add
     * @param createToken true if an initial token should be created
     * @return MediaServiceResult
     */
    public MediaServiceResult addUser(User usr, boolean createToken) {
        MediaServiceResult result = MediaServiceResult.DUPLICATEOBJ;
        boolean exists = false;
        for (User user : tokens.keySet())  {
            if (user.equals(usr))  {
                exists = true;
                break;
            }
        }
        if (!exists && createToken)  {
            result = generateToken(usr);
        }
        if (!exists && !createToken)  {
            tokens.put(usr, "0");
        }
        return result;
    }

    @Override
    public MediaServiceResult generateToken(User usr)  {
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
                    md.update(md5string.getBytes("UTF-8"));
                    String md5 = md.digest().toString();
                    
                    if (user.hasRole(Role.ROOT))  {
                        md5 = "rootToken";
                    }
                    tokens.put(user, md5);
                    logins.put(user, getIPaddr());
                    
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
    public MediaServiceResult validateToken(String token) {
        for (String value : tokens.values())  {
            if (value.equals(token))  {
                Set<User> users = getKeysByValue(tokens, value);
                for (User user : users)  {
                    if (logins.containsKey(user))  {
                        if (logins.get(user).equals(getIPaddr()))  {
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
        // TODO Auto-generated method stub
        return null;
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

    @Override
    public Map<User, String> getListOfToken() {
        return tokens;
    }

}
