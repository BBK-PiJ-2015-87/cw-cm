import interfaces.Contact;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static utils.ContactManagerPredicates.*;

/**
 * Created by Workstation on 12/03/16.
 */
public class ContactManagerPredicatesTest {
    List<PastMeeting> pastMeetings;
    List<FutureMeeting> futureMeetings;
    List<Meeting> meetings;

    @Before
    public void setUp() throws Exception {
        Set<Contact> contacts = new HashSet<>(Arrays.asList(
                new ContactImpl(1, "", "")
        ));

        Set<Contact> contacts2 = new HashSet<>(Arrays.asList(
                new ContactImpl(2, "", "")
        ));

        futureMeetings = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(id -> new FutureMeetingImpl(id, new GregorianCalendar(2000+id, 0, 0), contacts))
                .collect(toList());

        pastMeetings = IntStream.rangeClosed(1, 5)
                .boxed()
                .map(id -> new PastMeetingImpl(id, new GregorianCalendar(2000-id, 0, 0), contacts2, null))
                .collect(toList());

        meetings = new ArrayList<>();
        meetings.addAll(futureMeetings);
        meetings.addAll(pastMeetings);
    }

    @Test
    public void testIsFutureMeeting() {
        Calendar now = new GregorianCalendar(2000, 0, 0);

        assertTrue(futureMeetings.stream().allMatch(isFutureMeeting(now)));
        assertFalse(pastMeetings.stream().allMatch(isFutureMeeting(now)));
    }

    @Test
    public void testIsFutureMeetingIfDifferentTypes() {
        Calendar now = new GregorianCalendar(1000, 0, 0);

        assertTrue(futureMeetings.stream().allMatch(isFutureMeeting(now)));
        assertFalse(pastMeetings.stream().allMatch(isFutureMeeting(now)));
    }

    @Test
    public void testIsPastMeeting() {
        Calendar now = new GregorianCalendar(2000, 0, 0);
        assertTrue(pastMeetings.stream().allMatch(isPastMeeting(now)));
        assertFalse(futureMeetings.stream().allMatch(isPastMeeting(now)));
    }

    @Test
    public void testIsPastMeetingDifferentType() throws Exception {
        Calendar now = new GregorianCalendar(3000, 0, 0);
        assertTrue(pastMeetings.stream().allMatch(isPastMeeting(now)));
        assertFalse(futureMeetings.stream().allMatch(isPastMeeting(now)));
    }

    @Test
    public void testMatchToProvidedDate() {
        Calendar past = new GregorianCalendar(1999, 0, 0);
        Calendar future = new GregorianCalendar(2001, 0, 0);


        List<Meeting> meetings1 = pastMeetings.stream().filter(meetingOn(past)).collect(Collectors.toList());
        List<Meeting> meetings2 = futureMeetings.stream().filter(meetingOn(future)).collect(Collectors.toList());

        assertThat(meetings1.size(), is(1));
        assertThat(meetings1.get(0).getDate(), is(past));
        assertThat(meetings2.size(), is(1));
        assertThat(meetings2.get(0).getDate(), is(future));
    }

    @Test
    public void testMatchToProvidedID() {
        Contact contact = new ContactImpl(1, "", "");
        List<Meeting> filtered = meetings.stream().filter(meetingWithContact(contact)).collect(Collectors.toList());

        assertThat(filtered.size(), is(5));
    }
}