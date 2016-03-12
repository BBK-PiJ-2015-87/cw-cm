import interfaces.Contact;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactManagerImplTest {
    ContactManagerImpl cm;

    Set<Contact> contacts;
    Set<Contact> participantsPast;
    Set<Contact> participantsFuture;
    Set<Contact> participantsUnknown;

    List<Meeting> meetings;
    List<Meeting> pastMeetings;
    List<Meeting> futureMeetings;




    /**
     * Setup mock contact manager.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        meetings = new ArrayList<>();
        contacts = new HashSet<>();

        participantsFuture = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(id -> new ContactImpl(id, "name_"+id, "notes_"+id))
                .collect(Collectors.toSet());

        participantsPast = IntStream.rangeClosed(6, 10)
                .boxed()
                .map(id -> new ContactImpl(id, "name_"+id, "notes_"+id))
                .collect(Collectors.toSet());

        participantsUnknown = IntStream.rangeClosed(101, 105)
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

        meetings.addAll(pastMeetings);
        meetings.addAll(futureMeetings);

        contacts.addAll(participantsPast);
        contacts.addAll(participantsFuture);

        cm = new ContactManagerImpl(contacts, meetings);
    }


    //addFutureMeeting

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPastDateProvided() throws Exception {
        Calendar past = new GregorianCalendar(1000, 0, 10);

        cm.addFutureMeeting(new HashSet<>(participantsFuture), past);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfContactUnknown() throws Exception {
        Calendar future = new GregorianCalendar(3000, 0, 10);
        cm.addFutureMeeting(participantsUnknown, future);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfContactsNotProvided() throws Exception {
        Calendar future = new GregorianCalendar(3000, 0, 10);
        cm.addFutureMeeting(null, future);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfDateNotProvided() throws Exception {
        cm.addFutureMeeting(new HashSet<>(participantsFuture), null);
    }

    @Test
    public void shouldAddFutureMeetingToList(){
        Calendar future = new GregorianCalendar(3000, 0, 10);

        int meetingId = cm.addFutureMeeting(participantsFuture, future);
        int meetingId2 = cm.addFutureMeeting(participantsFuture, future);

        assertThat(cm.getMeetings().size(), is(12));
        assertThat(meetingId, is(0));
        assertThat(meetingId2, is(11));
    }

    //getPastMeeting

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenIdOfFutureMeetingProvided() {
        cm.getPastMeeting(1);
    }

    @Test
    public void shouldReturnPastMeetingWithCorrectId() {
        PastMeeting pm1 = cm.getPastMeeting(6);
        PastMeeting pm2 = cm.getPastMeeting(10);

        assertThat(pm1.getId(), is(6));
        assertThat(pm1, instanceOf(PastMeeting.class));
        assertThat(pm2.getId(), is(10));
        assertThat(pm2, instanceOf(PastMeeting.class));
    }

    @Test
    public void shouldReturnNullIfMeetingWithIdNotPresent() {
        assertNull(cm.getPastMeeting(1000));
    }

    @Test
    public void shouldReturnNullIfNoMeetingsExist() {
        meetings.clear();
        assertTrue(meetings.isEmpty());

        PastMeeting pm1 = cm.getPastMeeting(1000);
        assertNull(pm1);
    }


    //getFutureMeeting

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenIdOfPastMeetingProvided() {
        cm.getFutureMeeting(10);
    }

    @Test
    public void shouldReturnFutureMeetingWithGivenID() {
        FutureMeeting fm1 = cm.getFutureMeeting(1);
        FutureMeeting fm2 = cm.getFutureMeeting(5);

        assertThat(fm1.getId(), is(1));
        assertThat(fm1, instanceOf(FutureMeeting.class));
        assertThat(fm2.getId(), is(5));
        assertThat(fm2, instanceOf(FutureMeeting.class));
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
    public void shouldReturnPastOrFutureMeetingsWithGivenID() {
        Meeting m1 = cm.getMeeting(1);
        Meeting m2 = cm.getMeeting(10);

        assertThat(m1.getId(), is(1));
        assertThat(m2.getId(), is(10));
    }

    @Test
    public void shouldReturnNullIfMeetingNotFound() {
        Meeting m1 = cm.getMeeting(100);

        assertFalse(cm.getMeetings().isEmpty());
        assertNull(m1);
    }

    @Test
    public void shouldReturnNullWhenProvidedIdAndMeetingListIsEmpty() {
        cm.getMeetings().clear();
        assertTrue(cm.getMeetings().isEmpty());

        Meeting m1 = cm.getMeeting(2);
        assertNull(m1);
    }

    //getFutureMeetingList

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfContactDoesNotExist() {
        Contact contact = new ContactImpl(100, "", ""); //non existing contact

        cm.getFutureMeetingList(contact);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowAnExceptionIfContactIsNotGiven() {
        cm.getFutureMeetingList(null);
    }

    @Test
    public void shouldReturnEmptyListWhenContactIsPresentOnlyInPastMeetings() {
        Contact pastContact = new ContactImpl(10, "name_10", "notes_10"); //existing contact in Past Meetings
        List<Meeting> resultList = cm.getFutureMeetingList(pastContact);

        assertTrue(contacts.contains(pastContact)); //ensure that the contact exists
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void shouldReturnListOfFutureMeetingsWithProvidedContact() {
        Contact contact = new ContactImpl(1, "name", "notes"); //existing contact in FutureMeetings
        List<Meeting> resultList = cm.getFutureMeetingList(contact);

        assertThat(resultList.size(), is(5));
    }

    @Test
    public void shouldReturnSortedByDateListOfContacts() {
        Contact contact = new ContactImpl(5, "name", "notes"); //existing contact in FutureMeetings
        List<Meeting> expected = new ArrayList<>(futureMeetings);
        Collections.reverse(expected);

        List<Meeting> resultList = cm.getFutureMeetingList(contact);

        assertEquals(expected, resultList);
        assertTrue(resultList.containsAll(futureMeetings));
    }

    //getMeetingListOn

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfDateNull() {
        cm.getMeetingListOn(null);

    }

    @Test
    public void shouldReturnMeetingsWithProvidedDate() {
        Calendar date = new GregorianCalendar(2100, 1, 11); //set up date
        List <Meeting> sameDayMeetings = IntStream.rangeClosed(20, 23) //set up meetings
                .boxed()
                .map(id -> new MeetingImpl(id, date, participantsFuture))
                .collect(Collectors.toList());

        meetings.addAll(sameDayMeetings);
        assertThat(meetings.size(), is(14));

        List<Meeting> result = cm.getMeetingListOn(date);


        assertEquals(sameDayMeetings, result);
    }

    @Test
    public void shouldReturnEmptyListWhenMeetingsWithProvidedDateNotFound() {
        Calendar date = new GregorianCalendar(2100, 1, 11); //set up date

        List<Meeting> result = cm.getMeetingListOn(date);

        assertTrue(result.isEmpty());
    }


    //getPastMeetingListFor

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfContactNotExists() {
        Contact contact = new ContactImpl(11, "name_11", "notes_11");

        cm.getPastMeetingListFor(contact);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfContactNull() {
        cm.getPastMeetingListFor(null);
    }

    @Test
    public void shouldReturnPastMeetingListForProvidedContact() {
        Contact contact = new ContactImpl(6, "name", "notes");

        List<PastMeeting> result = cm.getPastMeetingListFor(contact);

        assertThat(result.size(), is(5));
        result.stream().forEach(meeting -> {
            assertThat(meeting, instanceOf(PastMeeting.class));
        });

    }

    @Test
    public void shouldReturnEmptyListIfContactIsInFutureMeetings() {
        Contact contact = new ContactImpl(5, "name_5", "notes_5");

        List<PastMeeting> result = cm.getPastMeetingListFor(contact);

        assertTrue(result.isEmpty());
    }


    // addNewPastMeeting

    @Test(expected = NullPointerException.class)
    public void shouldThrowAnExceptionIfContactsNotProvided() {
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfContactListIsEmpty() {
        participantsPast.clear();

        cm.addNewPastMeeting(participantsPast, new GregorianCalendar(), "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfAnyOfContactsNotExists() {
        Set<Contact> newContacts = new HashSet<>(Arrays.asList(new ContactImpl(11, "name_11", "notes_11")));

        cm.addNewPastMeeting(newContacts, new GregorianCalendar(), "notes");
    }

    @Test
    public void shouldAddNewPastMeeting() {
        Set<Contact> newContacts = new HashSet<>(Arrays.asList(new ContactImpl(1, "TEST", "notes_1")));
        meetings.remove(4);
        cm.addNewPastMeeting(newContacts, new GregorianCalendar(), "notes");

        Meeting getBack = meetings.get(4);

        assertThat(meetings.size(), is(10));
        assertThat(getBack.getId(), is(1));
//        assertThat(getBack.getNotes(), is("TEST"));
        assertThat(getBack, instanceOf(PastMeeting.class));
        assertThat(getBack.getContacts().size(), is(1));
    }

    @Test
    public void test() {
        List<FutureMeeting> future = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(id -> new FutureMeetingImpl(id, new GregorianCalendar(2020+id, 0, 10), participantsFuture))
                .collect(Collectors.toList());

        List<PastMeeting> past = IntStream.rangeClosed(6, 10)
                .boxed()
                .map(id -> new PastMeetingImpl(id, new GregorianCalendar(2000-id, 0, 10), participantsPast, "TEST"))
                .collect(Collectors.toList());

        List<? super Meeting> meetings = new ArrayList<>();
        meetings.addAll(future);
        meetings.addAll(past);
        assertTrue(!meetings.isEmpty());
    }
}