import interfaces.Contact;
import interfaces.Meeting;
import interfaces.PastMeeting;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class ContactManagerImplTest {
    ContactManagerImpl cm;
    Map<Integer, Meeting> meetings;
    Set<Contact> contacts;
    Meeting mp, mp2, mf, mf2;
    Calendar dp, dp2, df, df2;


    @Before
    public void setUp() throws Exception {
        cm = new ContactManagerImpl();
        meetings = new HashMap<>();
        contacts = new HashSet<>();
        cm.setMeetings(meetings);
        cm.setAllContacts(contacts);

        dp = new GregorianCalendar(2014, 1, 1);
        dp2 = new GregorianCalendar(2015, 1, 1);
        df = new GregorianCalendar(2020, 1, 1);
        df2 = new GregorianCalendar(2022, 1, 1);
        mp = new MeetingImpl(1, dp, null);
        mp2 = new MeetingImpl(2, dp2, null);
        mf = new MeetingImpl(3, df, null);
        mf2 = new MeetingImpl(4, df2, null);
        meetings.put(1, mp);
        meetings.put(2, mp2);
        meetings.put(3, mf);
        meetings.put(4, mf2);
    }

    @Test
    public void shouldReturnPastMeetingByID() throws Exception {
        int id = 1;
        int id2 = 2;
        PastMeeting result = cm.getPastMeeting(id);
        PastMeeting result2 = cm.getPastMeeting(id2);
        assertThat(result.getDate(), is(dp));
        assertThat(result.getId(), is(1));
        assertThat(result2.getDate(), is(dp2));
        assertThat(result2.getId(), is(2));
    }
}