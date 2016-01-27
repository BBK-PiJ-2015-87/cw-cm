import interfaces.Meeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test class for methods in Util.
 *
 * Created by Vladimirs Ivanovs on 26/01/16.
 */
public class UtilsTest {
    Map<Integer, Meeting> map;
    List<Meeting> list;
    Calendar date;
    Meeting meeting;

    @Before
    public void setUp() {
        map = new HashMap<>();
        date = new GregorianCalendar();
        list = new ArrayList<>();
        list.addAll(Arrays.asList(
                new MeetingImpl(0, null,null),
                new MeetingImpl(1, null,null),
                new MeetingImpl(2, null,null),
                new MeetingImpl(3, null,null),
                new MeetingImpl(4, null,null)
        ));
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

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenMapIsNull() throws Exception {
        map = null;
        Utils.generateNewID(map);
    }

    @Test
    public void shouldReturnNewIDWhenListIsNotEmpty() throws Exception {
        int result = Utils.generateNewID(list);
        assertThat(result, is(5));
    }

    @Test
    public void shouldReturnNewIDWhenListIsEmpty() throws Exception {
        list = new ArrayList<>();
        int result = Utils.generateNewID(list);
        assertThat(result, is(0));
    }

    @Test
    public void shouldReturnTrueIfMeetingIsInFuture() throws Exception {
        date.setTimeInMillis(System.currentTimeMillis() + 60000);
        meeting = new MeetingImpl(1, date, null);
        boolean result = Utils.isPastMeeting(meeting);
        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnFalseIfMeetingIsInFuture() throws Exception {
        date.setTimeInMillis(System.currentTimeMillis() - 60000);
        meeting = new MeetingImpl(1, date, null);
        boolean result = Utils.isPastMeeting(meeting);
        assertThat(result, is(true));
    }
}