import interfaces.Contact;
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
    Set<Contact> contacts;
    Set<Contact> participants;

    @Before
    public void setUp() throws Exception {
        pastMeetings = new ArrayList<>();
        contacts = new HashSet<>();

        participants = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new ContactImpl(id, "", ""))
                .collect(Collectors.toSet());

        pastMeetings = IntStream.rangeClosed(0, 5)
                .boxed()
                .map(id -> new MeetingImpl(id, new GregorianCalendar(), null))
                .collect(Collectors.toList());

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
        //change dates to future meetings
        cm.getMeetings().stream()
                .forEach(meeting -> meeting.getDate()
                        .roll(Calendar.MONTH, true));

        PastMeeting pm1 = cm.getPastMeeting(1);
        assertThat(pm1.getId(), is(1));
    }

    @Test
    public void shouldReturnNullIfNoMeetingWithGivenID() {
        PastMeeting pm1 = cm.getPastMeeting(1000);
        assertNull(pm1);
    }
}