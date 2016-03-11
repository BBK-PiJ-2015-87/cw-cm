import interfaces.Contact;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Future meeting implementation.
 *
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class FutureMeetingImplTest {
    Calendar date, date2;
    Contact contact, contact2, contact3;
    Set<Contact> contacts, contacts2;
    FutureMeetingImpl meeting, meeting2;

    @Before
    public void setUp() {
        date = new GregorianCalendar(2016, 01, 01);
        date2 = new GregorianCalendar(2020, 01, 01);
        contact = new ContactImpl(10, "Bob", "note");
        contact2 = new ContactImpl(20, "Mark", "note");
        contact3 = new ContactImpl(30, "Dan", "note");
        contacts = new HashSet<>(Arrays.asList(contact, contact2, contact3));
        contacts2 = new HashSet<>(Arrays.asList(contact, contact3));

        meeting = new FutureMeetingImpl(1, date, contacts);
        meeting2 = new FutureMeetingImpl(1, date2, contacts2);
    }

    @Test
    public void shouldReturnCorrectId(){
        assertEquals(1, meeting.getId());
    }

    @Test
    public void shouldReturnCorrectDate() {
        assertEquals(date, meeting.getDate());
    }

    @Test
    public void shouldReturnCorrectContacts(){
        assertEquals(contacts, meeting.getContacts());
    }

    @Test
    public void shouldBeEqualWithSameIDs(){
        assertEquals(meeting, meeting2);
    }
}