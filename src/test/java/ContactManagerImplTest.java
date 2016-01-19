import interfaces.ContactManager;
import interfaces.FutureMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Workstation on 19/01/16.
 */
public class ContactManagerImplTest {
    List<FutureMeeting> futureMeetings;
    ContactManager contactManager;
    Calendar calendar;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldReturnFutureMeetingList() throws Exception {
        assertEquals(futureMeetings, contactManager.getFutureMeetingList(calendar));
    }

}