import interfaces.Contact;
import interfaces.ContactManager;
import interfaces.FutureMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Workstation on 19/01/16.
 */
public class ContactManagerImplTest {
    Contact ct0, ct1, ct2, ct3, ct4;
    List<FutureMeeting> futureMeetings;
    ContactManager contactManager;
    Calendar calendar;

    @Before
    public void setUp() throws Exception {
        ct0  = new ContactImpl(0, "Name", "note");
        ct1 = new ContactImpl(1, "Name", "note");
        ct2 = new ContactImpl(2, "Name", "note");
        ct3 = new ContactImpl(3, "Name", "note");
        ct4 = new ContactImpl(4, "Name", "note");
        List<Contact> contacts = Arrays.asList(ct0, ct1, ct2, ct3, ct4);

    }

    @Test
    public void shouldAddFutureMeeting() throws Exception {
        assertEquals(futureMeetings, contactManager.getFutureMeetingList(calendar));
    }

    @Test
    public void shouldReturnFutureMeetingList() throws Exception {
        assertEquals(futureMeetings, contactManager.getFutureMeetingList(calendar));
    }

}