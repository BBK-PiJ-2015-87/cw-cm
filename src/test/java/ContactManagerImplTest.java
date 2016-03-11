import interfaces.Contact;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactManagerImplTest {
    ContactManagerImpl cm;
    List<Meeting> pastMeetings;
    List<Meeting> futureMeetings;
    List<Meeting> pastAndFutureMeetings;
    Set<Contact> participantsFuture;
    Set<Contact> participantsPast;

    /**
     * Setup mock contact manager.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        pastAndFutureMeetings = new ArrayList<>();

        participantsFuture = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(id -> new ContactImpl(id, "name_"+id, "notes_"+id))
                .collect(Collectors.toSet());

        participantsPast = IntStream.rangeClosed(6, 10)
                .boxed()
                .map(id -> new ContactImpl(id, "name_"+id, "notes_"+id))
                .collect(Collectors.toSet());

        futureMeetings = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(2020+id, 0, 10), participantsFuture))
                .collect(Collectors.toList());

        pastMeetings = IntStream.rangeClosed(6, 10)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(2000-id, 0, 10), participantsPast))
                .collect(Collectors.toList());


        pastAndFutureMeetings.addAll(futureMeetings);
        pastAndFutureMeetings.addAll(pastMeetings);

        cm = new ContactManagerImpl(pastAndFutureMeetings);
    }

    //Tests for helper methods

    @Test
    public void shouldReturnAllExistingContacts(){
        Set<Contact> contacts = cm.testGetExistingContacts();

        assertThat(contacts.size(), is(10));
        assertTrue(contacts.containsAll(participantsFuture));
        assertTrue(contacts.containsAll(participantsPast));
    }

    @Test
    public void shouldReturnAllExistingContactsWithNoDuplicates(){
        Contact duplicated = new ContactImpl(11, "name_11", "notes_11");
        participantsFuture.add(duplicated);
        participantsPast.add(duplicated);

        Set<Contact> contacts = cm.testGetExistingContacts();

        assertTrue(contacts.containsAll(participantsFuture));
        assertTrue(contacts.containsAll(participantsPast));
        assertThat(participantsFuture.size(), is(6));
        assertThat(participantsPast.size(), is(6));
        assertThat(contacts.size(), is(11));
    }

    //addFutureMeeting

    @Test
    public void shouldAddFutureMeetingIfFutureDateProvided(){
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.roll(Calendar.DATE, true);

        int meetingId = cm.addFutureMeeting(participantsFuture, tomorrow);
        int meetingId2 = cm.addFutureMeeting(participantsFuture, tomorrow);

        assertThat(meetingId, is(0));
        assertThat(meetingId2, is(11));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPastDateProvided() throws Exception {
        Calendar yesterday = Calendar.getInstance();
        yesterday.roll(Calendar.MONTH, false);

        cm.addFutureMeeting(participantsFuture, yesterday);
    }

    //getPastMeeting

    @Test
    public void shouldReturnPastMeetingWithGivenID() {
        PastMeeting pm1 = cm.getPastMeeting(6);
        PastMeeting pm2 = cm.getPastMeeting(10);

        assertThat(pm1.getId(), is(6));
        assertThat(pm2.getId(), is(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGivenIdOfFutureMeeting() {
        cm.getPastMeeting(1);
    }

    @Test
    public void shouldReturnNullIfNoPastMeetingWithGivenID() {
        PastMeeting pm1 = cm.getPastMeeting(1000);
        assertNull(pm1);
    }

    @Test
    public void shouldReturnNullIfNoMeetingsExistWhenPastMeetingProvided() {
        PastMeeting pm1 = cm.getPastMeeting(1000);
        assertNull(pm1);
    }

    //getFutureMeeting

    @Test
    public void shouldReturnFutureMeetingWithGivenID() {
        FutureMeeting fm1 = cm.getFutureMeeting(1);
        FutureMeeting fm2 = cm.getFutureMeeting(5);

        assertThat(fm1.getId(), is(1));
        assertThat(fm2.getId(), is(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenIdOfPastMeetingProvided() {
        cm.getFutureMeeting(10);
    }

    @Test
    public void shouldReturnNullIfNoFutureMeetingWithGivenID() {
        FutureMeeting fm1 = cm.getFutureMeeting(1000);
        assertNull(fm1);
    }

    @Test
    public void shouldReturnNullIfMeetingListIsEmpty() {
        cm.getMeetings().clear();
        FutureMeeting fm1 = cm.getFutureMeeting(1);

        assertTrue(cm.getMeetings().isEmpty());
        assertNull(fm1);
    }

    //getMeeting

    @Test
    public void shouldReturnMeetingWithGivenID() {
        Meeting m1 = cm.getMeeting(1);
        Meeting m2 = cm.getMeeting(10);

        assertThat(m1.getId(), is(1));
        assertThat(m2.getId(), is(10));
    }

    @Test
    public void shouldReturnNullIfNoMeetingWithGivenID() {
        Meeting m1 = cm.getMeeting(10000);

        assertFalse(cm.getMeetings().isEmpty());
        assertNull(m1);
    }

    @Test
    public void shouldReturnNullIfMeetingListIsEmptyAndIdProvided() {
        cm.getMeetings().clear();

        Meeting m1 = cm.getMeeting(2);

        assertTrue(cm.getMeetings().isEmpty());
        assertNull(m1);
    }

    //getFutureMeetingList

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfContactDoesNotExist() {
        Contact contact = new ContactImpl(-1, "", ""); //non existing contact

        cm.getFutureMeetingList(contact);
    }

    @Test
    public void shouldReturnEmptyListWhenIfContactIsPresentOnlyInPastMeetings() {
        Contact pastContact = new ContactImpl(10, "name_10", "notes_10"); //existing contact in Past Meetings

        List<Meeting> resultList = cm.getFutureMeetingList(pastContact);

        assertTrue(cm.testGetExistingContacts().contains(pastContact)); //ensure that the contact exists
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void shouldReturnListOfFutureMeetingsWithProvidedContact() {
        Contact contact = new ContactImpl(1, "name_1", "notes_1"); //existing contact in FutureMeetings

        List<Meeting> resultList = cm.getFutureMeetingList(contact);

        assertThat(resultList.size(), is(5));
    }

    @Test
    public void shouldReturnSortedByDateListOfContacts() {
        Contact contact = new ContactImpl(5, "name_5", "notes_5"); //existing contact in FutureMeetings

        List<Meeting> expected = new ArrayList<>(futureMeetings);
        Collections.reverse(expected);

        List<Meeting> resultList = cm.getFutureMeetingList(contact);
        assertEquals(expected, resultList);
        assertTrue(resultList.containsAll(futureMeetings));
    }

    @Test
    public void shouldReturnMeetingsWithProvidedDate() {
        Calendar date = new GregorianCalendar(2100, 1, 11); //set up date
        List <Meeting> sameDayMeetings = IntStream.rangeClosed(20, 23) //set up meetings
                .boxed()
                .map(id -> new MeetingImpl(id, date, participantsFuture))
                .collect(Collectors.toList());

        cm.addMeetings(sameDayMeetings);

        List<Meeting> result = cm.getFutureMeetingList(date);

        assertThat(result.size(), is(4));
        assertEquals(sameDayMeetings, result);
        assertTrue(result.containsAll(sameDayMeetings));
    }

    @Test
    public void shouldReturnEmptyListWhenMeetingsWithProvidedDateNotFound() {
        Calendar date = new GregorianCalendar(2100, 1, 11); //set up date

        List<Meeting> result = cm.getFutureMeetingList(date);

        assertTrue(result.isEmpty());
    }

    //getPastMeetingList

    @Test
    public void shouldReturnPastMeetingListWhenMeetingsWithProvidedDateNotFound() {
        Contact contact = new ContactImpl(6, "name_6", "notes_6");

        List<PastMeeting> result = cm.getPastMeetingList(contact);

        assertThat(result.size(), is(5));
        assertThat(result.get(0), instanceOf(PastMeeting.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfContactNotExists() {
        Contact contact = new ContactImpl(11, "name_11", "notes_11");

        cm.getPastMeetingList(contact);
    }

    @Test
    public void shouldReturnEmptyListIfNoPastMeetings() {
        Contact contact = new ContactImpl(5, "name_5", "notes_5");
        cm.setMeetings(futureMeetings);
        List<PastMeeting> result = cm.getPastMeetingList(contact);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListIfContactExistsButNoInPastMeetings() {
        Contact contact = new ContactImpl(5, "name_5", "notes_5");
        List<PastMeeting> result = cm.getPastMeetingList(contact);

        assertTrue(result.isEmpty());
    }

    // addNewPastMeeting
    @Test(expected = NullPointerException.class)
    public void shouldThrowAnExceptionIfContactAreNotProvided() {
        cm.addNewPastMeeting(null, new GregorianCalendar(), "notes");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowAnExceptionIfDateIsNotProvided() {
        cm.addNewPastMeeting(participantsPast, null, "notes");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowAnExceptionIfNotesAreNotProvided() {
        cm.addNewPastMeeting(participantsPast, new GregorianCalendar(), null);
    }
}