package P3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LocationTest {

    //Testing strategy: just test some location
    Location location1;
    Location location11;
    @Before
    public void setUp() throws Exception {
        location1 = new Location(0, 0);
        location11 = new Location(0, 0);
    }
    //test if equals is true,the hash code is the same
    @Test
    public void testEqualsandHashCode() {
        assertTrue("expected the equals is true",location1.equals(location11));
        assertEquals("the same hashcode", location1.hashCode(),location11.hashCode());
    }
    //just test get
    @Test
    public void testGets() {
        assertTrue("expected the x",location1.getX() == location11.getX());
        assertTrue("expected the y",location1.getY() == location11.getY());
    }

}
