import interfaces.Contact;
import interfaces.Meeting;

import javax.xml.bind.annotation.*;
import java.util.Calendar;
import java.util.Set;

/**
 * Concrete class representing meeting.
 *
 * Created by Vladimirs Ivanovs on 19/01/16.
 */

//@XmlSeeAlso(ContactImpl.class)
@XmlRootElement(name = "meeting")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeetingImpl implements Meeting {

    @XmlAttribute
    private int id;

    private Calendar date;

    @XmlElementWrapper(name = "contacts")
    @XmlAnyElement
    private Set<Contact> contacts;

    //for XML marshalling purpose
    private MeetingImpl(){}

    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    @Override
    public int getId() {
        return  id;
    }

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    @Override
    public Calendar getDate() {
        return date;
    }

    /**
     * Return the details of people that attended the meeting.
     * <p>
     * The list contains a minimum of one contact (if there were
     * just two people: the user and the contact) and may contain an
     * arbitraty number of them.
     *
     * @return the details of people that attended the meeting.
     */
    @Override
    public Set<Contact> getContacts() {
        return contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        Meeting meeting = (Meeting) o;

        if (getId() != meeting.getId()) return false;
        if (getDate() != null ? !getDate().equals(meeting.getDate()) : meeting.getDate() != null) return false;
        return getContacts() != null ? getContacts().equals(meeting.getContacts()) : meeting.getContacts() == null;

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MeetingImpl{" +
                "date=" + date.getTime() +
                ", id=" + id +
                '}';
    }
}
