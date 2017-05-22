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
@SuiteClasses({ MediaServiceTest.class, MediaResourceTest.class})
public class ShareItTest {

}
