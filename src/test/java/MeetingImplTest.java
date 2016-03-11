import interfaces.Contact;
import interfaces.Meeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Meeting implementation class. Represent a meeting with contacts1 date and unique id.
 *
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class MeetingImplTest {
    Set<Contact> contacts1;
    Set<Contact> contacts2;
    Meeting meeting1;
    Meeting meeting2;
    Meeting meeting3;
    Meeting meeting4;

    @Before
    public void setUp() {
        Calendar date1 = new GregorianCalendar(2016, 01, 01);
        Calendar date2 = new GregorianCalendar(2020, 01, 01);

        Contact contact1 = new ContactImpl(10, "Bob", "note");
        Contact contact2 = new ContactImpl(20, "Mark", "note");
        Contact contact3 = new ContactImpl(30, "Dan", "note");

        contacts1 = new HashSet<>(Arrays.asList(contact1, contact2, contact3));
        contacts2 = new HashSet<>(Arrays.asList(contact1, contact3));

        meeting1 = new MeetingImpl(1, date1, contacts1);
        meeting2 = new MeetingImpl(2, date2, contacts2);
        meeting3 = new MeetingImpl(1, date2, contacts2);
        meeting4 = new MeetingImpl(1, date2, contacts2);
    }

    @Test
    public void shouldReturnCorrectId() throws Exception {
        assertEquals(1, meeting1.getId());
        assertEquals(2, meeting2.getId());
    }

    @Test
    public void shouldReturnCorrectDate() throws Exception {
        assertEquals(new GregorianCalendar(2016, 01, 01), meeting1.getDate());
        assertEquals(new GregorianCalendar(2020, 01, 01), meeting2.getDate());
    }

    @Test
    public void shouldBeEqualIfJustSameIDs() throws Exception {
        assertEquals(meeting1, meeting3);
    }

    @Test
    public void shouldNotBeEqualIfDifferentIDs() throws Exception {
        assertNotEquals(meeting2, meeting4);
    }

    @Test
    public void shouldReturnCorrectContacts() throws Exception {
        assertEquals(contacts1, meeting1.getContacts());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConstructWithEmptySet() throws Exception {
        new MeetingImpl(1, new GregorianCalendar(), new HashSet<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenContactsNotProvided() throws Exception {
        new MeetingImpl(1, new GregorianCalendar(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDateNotProvided() throws Exception {
        new MeetingImpl(1, null, contacts1);
    }
}