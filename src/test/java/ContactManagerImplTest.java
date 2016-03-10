import interfaces.Contact;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactManagerImplTest {
    ContactManagerImpl cm;
    List<Meeting> pastMeetings;
    List<Meeting> futureMeetings;
    Set<Contact> contacts;
    Set<Contact> participants;

    @Before
    public void setUp() throws Exception {
        pastMeetings = new ArrayList<>();
        contacts = new HashSet<>();
        futureMeetings = new ArrayList<>();

        participants = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new ContactImpl(id, "", ""))
                .collect(Collectors.toSet());

        pastMeetings = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(), null))
                .collect(Collectors.toList());

        futureMeetings = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(), null))
                .collect(Collectors.toList());

        //update date to future
        futureMeetings.stream()
                .forEach(meeting -> meeting.getDate().roll(Calendar.MONTH, true));

        cm = new ContactManagerImpl(pastMeetings, contacts);
    }

    @Test
    public void shouldAddFutureMeetingIfFutureDateProvided(){
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.roll(Calendar.DATE, true);
        int meetingId = cm.addFutureMeeting(participants, tomorrow);
        assertThat(meetingId, is(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPastDateProvided() throws Exception {
        Calendar yesterday = Calendar.getInstance();
        yesterday.roll(Calendar.MONTH, false);
        cm.addFutureMeeting(participants, yesterday);
    }

    @Test
    public void shouldReturnPastMeetingWithGivenID() {
        PastMeeting pm1 = cm.getPastMeeting(2);
        PastMeeting pm2 = cm.getPastMeeting(5);
        assertThat(pm1.getId(), is(2));
        assertThat(pm2.getId(), is(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGivenIdOfFutureMeeting() {
        cm.setMeetings(futureMeetings);

        PastMeeting pm1 = cm.getPastMeeting(1);
        assertThat(pm1.getId(), is(1));
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

    @Test
    public void shouldReturnFutureMeetingWithGivenID() {
        cm.setMeetings(futureMeetings);

        FutureMeeting fm1 = cm.getFutureMeeting(0);
        FutureMeeting fm2 = cm.getFutureMeeting(5);
        assertThat(fm1.getId(), is(0));
        assertThat(fm2.getId(), is(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGivenIdOfPastMeeting() {
        cm.setMeetings(pastMeetings);

        FutureMeeting fm1 = cm.getFutureMeeting(1);
        assertThat(fm1.getId(), is(1));
    }

    @Test
    public void shouldReturnNullIfNoFutureMeetingWithGivenID() {
        cm.setMeetings(futureMeetings);
        FutureMeeting fm1 = cm.getFutureMeeting(1000);
        assertNull(fm1);
    }

    @Test
    public void shouldReturnNullIfNoMeetingsExistWhenFutureMeetingProvided() {
        futureMeetings.clear();
        cm.setMeetings(futureMeetings);

        FutureMeeting fm1 = cm.getFutureMeeting(2);
        assertNull(fm1);
    }

    @Test
    public void shouldReturnMeetingWithGivenID() {
        Meeting m1 = cm.getMeeting(2);
        Meeting m2 = cm.getMeeting(5);
        assertThat(m1.getId(), is(2));
        assertThat(m2.getId(), is(5));
    }

    @Test
    public void shouldReturnNullIfNoMeetingWithGivenID() {
        Meeting m1 = cm.getMeeting(1000);
        assertNull(m1);
    }

    @Test
    public void shouldReturnNullIfNoMeetingsExistWhenMeetingProvided() {
        futureMeetings.clear();
        cm.setMeetings(futureMeetings);

        Meeting m1 = cm.getMeeting(2);
        assertNull(m1);
    }
}