package edu.hm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testing all tests at the same time.
 * @author Daniel Gabl
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MediaServiceTest.class, MediaResourceTest.class, AuthenticationResourceTest.class, AuthenticationServiceTest.class, UserTest.class})
public class ShareItTest {

}