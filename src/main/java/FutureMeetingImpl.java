import interfaces.Contact;
import interfaces.FutureMeeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by Vladimirs Ivanovs on 19/01/16.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        super(id, date, contacts);
    }
}
