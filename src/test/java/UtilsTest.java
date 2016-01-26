import interfaces.Meeting;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test class for methods in Util.
 *
 * Created by Vladimirs Ivanovs on 26/01/16.
 */
public class UtilsTest {
    public Map<Integer, Meeting> map;

    @Before
    public void setUp() {
        map = new HashMap<>();
    }

    @Test
    public void shouldReturnNewIDWhenMapIsNotEmpty() throws Exception {
        map.put(0, null);
        map.put(1, null);
        map.put(2, null);
        map.put(3, null);
        map.remove(2, null);
        int newID = Utils.generateNewID(map);
        assertThat(newID, is(2));
    }

    @Test
    public void shouldReturnNewIDWhenMapContainsOneElement() throws Exception {
        map.put(0, null);
        int newID = Utils.generateNewID(map);
        assertThat(newID, is(1));
    }

    @Test
    public void shouldReturnNewIDWhenMapIsEmpty() throws Exception {
        int newID = Utils.generateNewID(map);
        assertThat(newID, is(0));
    }

    @Test
    public void shouldReturnCorrectNewIDWhenMapIsEmpty() throws Exception {
        int newID = Utils.generateNewID(map);
        assertThat(newID, is(0));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenMapIsNull() throws Exception {
        map = null;
        Utils.generateNewID(map);
    }
}