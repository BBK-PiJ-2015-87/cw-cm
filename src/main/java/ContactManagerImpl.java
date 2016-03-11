import interfaces.*;

import javax.xml.bind.annotation.*;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @XmlElementWrapper(name = "meetings")
    @XmlAnyElement
    private static List<Meeting> meetings;

    public ContactManagerImpl(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    //for XML marshalling purpose
    private ContactManagerImpl(){}

    public static void setMeetings(List<Meeting> meetings) {
        ContactManagerImpl.meetings = meetings;
    }
    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void addMeetings(List<Meeting> anotherMeetings) {
        meetings.addAll(anotherMeetings);
    }

    /**
     * Add a new meeting to be held in the future.
     *
     * An ID is returned when the meeting is put into the system. This
     * ID must be positive and non-zero.
     *
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time
     * in the past, of if any contact is unknown / non-existent.
     * @throws NullPointerException if the meeting or the date are null
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if (!isFuture(date)) throw new IllegalArgumentException("Date is in the past.");

        int newID = generateUniqueMeetingId();
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
     * @param contact one of the users contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    public List<Meeting> getMeetingListOn(Contact contact) {
        if (!getAllContacts().contains(contact)) throw new IllegalArgumentException("Contact doesn't exist.");
        return filterFutureMeetingsWithContact(meetings, contact);
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
        return filterFutureMeetingsOnDate(meetings, date);
    }

    /**
     * Returns the list of past meetings in which this contact has participated.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the users contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        if (!getAllContacts().contains(contact)) throw new IllegalArgumentException("Contact doesn't exist.");
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
     * @throws IllegalArgumentException if the list of contacts is
     * empty, or any of the contacts does not exist
     * @throws NullPointerException if any of the arguments is null
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        if(contacts == null || date == null || text == null) throw new NullPointerException();

        if(contacts.isEmpty()) throw new IllegalArgumentException();

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
     * Returns a list with the contacts whose name contains that string.
     *
     * If the string is the empty string, this methods returns the set
     * that contains all current contacts.
     *
     * @param name the string to search for
     * @return a list with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null
     */
    @Override
    public Set<Contact> getContacts(String name) {
        return null;
    }

    /**
     * Returns a list containing the contacts that correspond to the IDs.
     * Note that this method can be used to retrieve just one contact by passing only one ID.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a list containing the contacts that correspond to the IDs.
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
            if (isFuture(meeting.getDate()))throw new IllegalArgumentException("Invalid ID for past meeting.");
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
     * Generate lowest possible in as a unique ID for the meeting. Checks all existing IDs in meeting
     * list.
     *
     * @return unique int ID
     */
    private static int generateUniqueMeetingId() {
        return generateNewNumber(getAllMeetingIDs());
    }

    /**
     * Returns all IDs of existing meetings.
     *
     * @return
     */
    private static Set<Integer> getAllMeetingIDs() {
        return meetings.stream()
                .map(meeting -> meeting.getId())
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

    /**
     * Helper method to return all existing contacts without duplicates.
     *
     * @return Set of existing contacts
     */
    private Set<Contact> getAllContacts(){
        Set<Contact>  contacts = meetings.stream()
                .flatMap(meeting -> meeting.getContacts()
                        .stream())
                .collect(Collectors.toSet());
        return contacts;
    }

    /**
     * Wrapper public methods of getAllContacts for testing purposes
     *
     * @return Set of existing contacts
     */
    public Set<Contact> testGetExistingContacts(){
        return getAllContacts();
    }

}
