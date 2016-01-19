import interfaces.Contact;
import interfaces.Meeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Workstation on 19/01/16.
 */
public class MeetingImplTest {
    int id;
    Calendar date;
    Contact contact, contact2, contact3;
    Set<Contact> contacts;
    Meeting meeting;

    @Before
    public void setUp() {
        id = 1;
        date = new GregorianCalendar(2016, 01, 01);
        contact = new ContactImpl(10, "Bob", "note");
        contact2 = new ContactImpl(20, "Mark", "note");
        contact3 = new ContactImpl(30, "Dan", "note");
        contacts = new HashSet<>();
        contacts.addAll(Arrays.asList(contact, contact2, contact3));
        meeting = new MeetingImpl(id, date, contacts);
    }

    @Test
    public void shouldReturnCorrectId() throws Exception {
        assertEquals(id, meeting.getId());
    }

    @Test
    public void shouldReturnCorrectDate() throws Exception {
        assertEquals(date, meeting.getDate());
    }

    @Test
    public void testGetContacts() throws Exception {
        assertEquals(contacts, meeting.getContacts());
    }
}