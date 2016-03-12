import interfaces.*;

import javax.xml.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static utils.ContactManagerFilters.*;
import static utils.Utils.generateNewNumber;
import static utils.Utils.isFuture;

/**
 * Created by Workstation on 19/01/16.
 */

@XmlSeeAlso({MeetingImpl.class, ContactImpl.class})
@XmlRootElement(name = "contactManager")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactManagerImpl implements ContactManager {

    @XmlElementWrapper(name = "contacts")
    @XmlAnyElement
    private static Set<Contact> allContacts;

    @XmlElementWrapper(name = "meetings")
    @XmlAnyElement
    private static List<? super Meeting> meetings;

    public ContactManagerImpl(Set<Contact> contacts, List<Meeting> meetings) {
        this.allContacts = contacts;
        this.meetings = meetings;
    }

    //non argument constructor for XML marshalling purpose
    private ContactManagerImpl(){}

    public List<? super Meeting> getMeetings() {
        return meetings;
    }

    public Set<Contact> getAllContacts() {
        return allContacts;
    }

    public void setMeetings(List<Meeting> meetings) {
        ContactManagerImpl.meetings = meetings;
    }

    public void setContacts(Set<Contact> contacts) {
        ContactManagerImpl.allContacts = contacts;
    }

    /**
     * Add a new meeting to be held in the future.
     *
     * An ID is returned when the meeting is put into the system. This
     * ID must be positive and non-zero.
     *
     * @param contacts a list of allContacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time
     * in the past, of if any contact is unknown / non-existent.
     * @throws NullPointerException if the meeting or the date are null
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if (!isFuture(date) || !allContacts.containsAll(contacts)) throw new IllegalArgumentException();

        int newID = generateUniqueId(meetings); //always returns > 0
        meetings.add(new MeetingImpl(newID, date, contacts));

        return newID;
    }

    /**
     * Returns the PAST meeting with the requested ID, or null if it there is none.
     *
     * The meeting must have happened at a past date.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalStateException if there is a meeting with that ID happening
     * in the future
     */
    @Override
    public PastMeeting getPastMeeting(int id) {
        return returnPastOrThrow(findMeetingBy(id));
    }

    /**
     * Returns the FUTURE meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening
     * in the past
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return returnFutureOrThrow(findMeetingBy(id));
    }

    /**
     * Returns the meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     */
    @Override
    public Meeting getMeeting(int id) {
        return returnMeetingOrNull(findMeetingBy(id));
    }

    /**
     * Returns the list of future meetings scheduled with this contact.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the users allContacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if (contact == null){
            throw new NullPointerException();
        } else if (!allContacts.contains(contact)){
            throw new IllegalArgumentException();
        }

        return filterFutureMeetingsWithContact((List<Meeting>)meetings, contact);
    }

    /**
     * Returns the list of meetings that are scheduled for, or that took
     * place on, the specified date
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param date the date
     * @return the list of meetings
     * @throws NullPointerException if the date are null
     */
    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        if (date == null) throw new NullPointerException();
        return filterFutureMeetingsOnDate(meetings, date);
    }

    /**
     * Returns the list of past meetings in which this contact has participated.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the users allContacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        if (contact == null) {
            throw new NullPointerException();
        }else if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException();
        }

        List<PastMeeting> result = filterPastMeetingsByContact(meetings, contact).stream()
                .map(ContactManagerImpl::toPastMeeting)
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of allContacts is
     * empty, or any of the allContacts does not exist
     * @throws NullPointerException if any of the arguments is null
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        if(contacts == null || date == null || text == null){
            throw new NullPointerException();
        } else if(contacts.isEmpty() || !allContacts.containsAll(contacts)) {
            throw new IllegalArgumentException();
        }

        Meeting meeting = new PastMeetingImpl(generateUniqueId(meetings), date, contacts, text);
        meetings.add(meeting);
    }

    /**
     * Add notes to a meeting.
     *
     * This method is used when a future meeting takes place, and is
     * then converted to a past meeting (with notes) and returned.
     *
     * It can be also used to add notes to a past meeting at a later date.
     *
     * @param id the ID of the meeting
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException if the meeting is set for a date in the future
     * @throws NullPointerException if the notes are null
     */
    @Override
    public PastMeeting addMeetingNotes(int id, String text) {
        return null;
    }

    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     * @return the ID for the new contact
     * @throws IllegalArgumentException if the name or the notes are empty strings
     * @throws NullPointerException if the name or the notes are null
     */
    @Override
    public int addNewContact(String name, String notes) {
        return 0;
    }

    /**
     * Returns a list with the allContacts whose name contains that string.
     *
     * If the string is the empty string, this methods returns the set
     * that contains all current allContacts.
     *
     * @param name the string to search for
     * @return a list with the allContacts whose name contains that string.
     * @throws NullPointerException if the parameter is null
     */
    @Override
    public Set<Contact> getContacts(String name) {
        return null;
    }

    /**
     * Returns a list containing the allContacts that correspond to the IDs.
     * Note that this method can be used to retrieve just one contact by passing only one ID.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the allContacts that correspond to the IDs.
     * @throws IllegalArgumentException if no IDs are provided or if
     * any of the provided IDs does not correspond to a real contact
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        return null;
    }

    /**
     * Save all data to disk.
     *
     * This method must be executed when the program is
     * closed and when/if the user requests it.
     */
    @Override
    public void flush() {

    }


    //HELPER METHODS

    /**
     *
     * @param matchedMeeting
     * @return
     */
    private PastMeeting returnPastOrThrow(Optional<Meeting> matchedMeeting) {
        if (!matchedMeeting.isPresent()) {
            return null;
        } else {
            Meeting meeting = matchedMeeting.get();
            if (isFuture(meeting.getDate()))throw new IllegalStateException();
            return toPastMeeting(meeting);
        }
    }

    /**
     *
     * @param matchedMeeting
     * @return
     */
    private Meeting returnMeetingOrNull(Optional<Meeting> matchedMeeting) {
       return (matchedMeeting.isPresent()) ? matchedMeeting.get() : null;
    }

    /**
     *
     * @param matchedMeeting
     * @return
     */
    private FutureMeeting returnFutureOrThrow(Optional<Meeting> matchedMeeting) {
        if (!matchedMeeting.isPresent()) {
            return null;
        } else {
            Meeting meeting = matchedMeeting.get();
            if (!isFuture(meeting.getDate()))throw new IllegalArgumentException("Invalid ID for future meeting.");
            return toFutureMeeting(meeting);
        }
    }

    /**
     * Returns Optional<Meeting> by given id.
     *
     * @param id of a meeting to be found
     * @return Optional of a meeting
     */
    private Optional<Meeting> findMeetingBy(int id) {
        return filterAnyMeetingsWithID(meetings, id).stream().findFirst();
    }


    /**
     * Wrapper method to generate unique id based on ids of elements in a collection.
     *
     * @param collection
     * @param <T>
     * @return
     */
    private static <T> int generateUniqueId(Collection<T> collection) {
        return generateNewNumber(getExistingIDs(collection));
    }

    /**
     * Generic method to return all IDs of a collection, where element have getId method.
     * @param collection
     * @param <T>
     * @return Set of all unique IDs of elements of a collection
     */
    private static <T> Set<Integer> getExistingIDs(Collection<T> collection) {
        return collection.stream()
                .map(object -> {
                    int id = 0;
                    try {
                        id = (int) object.getClass().getMethod("getId").invoke(object);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return id;
                })
                .collect(Collectors.toSet());
    }

    /**
     * Helper method to convert a type of a meeting to PastMeeting.
     *
     * @param meeting to be converted
     * @return copy of a meeting with PastMeeting type
     */
    private static PastMeeting toPastMeeting(Meeting meeting) {
        if (meeting instanceof PastMeeting){
            return (PastMeeting) meeting;
        } else {
            return new PastMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts(), null);
        }
    }

    /**
     * Helper method to convert a type of a meeting to a FutureMeeting.
     *
     * @param meeting to be converted
     * @return copy of a meeting with FutureMeeting type
     */
    private static FutureMeeting toFutureMeeting(Meeting meeting) {
        if (meeting instanceof FutureMeeting){
            return (FutureMeeting) meeting;
        } else {
            return new FutureMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts());
        }
    }

}
