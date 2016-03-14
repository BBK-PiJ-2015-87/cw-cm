import interfaces.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static utils.ContactManagerFilters.*;
import static utils.Utils.generateNewNumber;
import static utils.Utils.isFuture;

/**
 * Implementation of contact manager.
 *
 * Created by Vladimirs Ivanovs on 19/01/16.
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

    private static ContactManagerImpl contactManager = new ContactManagerImpl( );

    public static ContactManagerImpl getInstance( ) {
        return contactManager;
    }

    //non argument constructor for XML marshalling purpose
    private ContactManagerImpl(){}

    public List<? super Meeting> getMeetings() {
        return meetings;
    }

    public Set<Contact> getAllContacts() {
        return allContacts;
    }

    public void setMeetings(List<? super Meeting> meetings) {
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
        Calendar now = new GregorianCalendar();
        return filterFutureMeetingsWithContact(meetings, contact, now );
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
        return filterAnyMeetingsOnDate(meetings, date);
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
        Calendar now = new GregorianCalendar();
        updateStatus(now);
        if (contact == null) {
            throw new NullPointerException();
        }else if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException();
        }

        List<PastMeeting> result = filterPastMeetingsByContact(meetings, contact, now).stream()
                .map(m -> (PastMeeting)m)
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

        Optional<Meeting> returnedMeeting = findMeetingBy(id);

        if (text == null){
            throw new NullPointerException();
        } else if (!returnedMeeting.isPresent()){
            throw new IllegalArgumentException();
        } else if (isFuture(returnedMeeting.get().getDate())) {
            throw new IllegalStateException();
        }

        Meeting meeting = returnedMeeting.get();
        PastMeeting converted = toPastMeetingWithNotes(meeting, text);
        Collections.replaceAll(meetings, meeting, converted);

        return converted;
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
        if (name == null || notes == null) {
            throw new NullPointerException();
        } else if (name.equals("") || notes.equals("")){
            throw new IllegalArgumentException();
        }
        int id = generateUniqueId(allContacts);
        Contact contact = new ContactImpl(id, name, notes );
        allContacts.add(contact);
        return id;
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
        if(name == null){
            throw new NullPointerException();
        } else if (name.equals("")){
            return allContacts;
        }

        Set<Contact> contacts = allContacts.stream()
                .filter(contact -> contact.getName().contains(name))
                .collect(Collectors.toSet());
        return contacts;
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
        if (ids == null) throw new IllegalArgumentException();

        Set<Integer> allIDs = allContacts.stream().map(Contact::getId).collect(Collectors.toSet());
        List<Integer> providedIds = IntStream.of(ids).boxed().collect(Collectors.toList());

        if (!allIDs.containsAll(providedIds))throw new IllegalArgumentException();

        return allContacts.stream()
                .filter(contact -> providedIds.contains(contact.getId()))
                .collect(Collectors.toSet());
    }

    /**
     * Save all data to disk.
     *
     * This method must be executed when the program is
     * closed and when/if the user requests it.
     */
    @Override
    public void flush() {
        JAXBContext jaxbContext = null;
        Marshaller jaxbMarshaller = null;

        File file = new File("contacts.txt");

        try {
            jaxbContext = JAXBContext.newInstance(ContactManagerImpl.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(getInstance(), file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    //HELPER METHODS

    public static void updateStatus(Calendar date) {
        List<Meeting> casted = (List<Meeting>)meetings;
        List<? super Meeting> updated = casted.stream()
                .map(meeting -> {
                    if (meeting.getDate().before(date)) {
                        toPastMeeting(meeting);
                    }
                    return meeting;
                })
                .collect(Collectors.toList());
        meetings = updated;
    }

    /**
     * Helper method. Returns value of optional.
     *
     * @param matchedMeeting optional of a meeting
     * @return past meeting or null
     * @throws IllegalArgumentException if date of meeting is in future
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
     * Helper method. Returns value of optional.
     *
     * @param matchedMeeting optional of a meeting
     * @return
     */
    private Meeting returnMeetingOrNull(Optional<Meeting> matchedMeeting) {
       return (matchedMeeting.isPresent()) ? matchedMeeting.get() : null;
    }

    /**
     * Helper method. Returns value of optional.
     *
     * @param matchedMeeting optional of a meeting
     * @return past meeting or null
     * @throws IllegalArgumentException if date of meeting is in future
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
     *
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
     * Helper method to convert meeting to PastMeeting.
     *
     * @param meeting to be converted
     * @return copy of a meeting with PastMeeting type
     */
    private static PastMeeting toPastMeeting(Meeting meeting) {
        return toPastMeetingWithNotes(meeting, "");
    }

    /**
     * Helper method to convert meeting to PastMeeting with notes.
     *
     * @param meeting to be converted
     * @param notes to the meeting
     * @return new past meeting
     */
    private static PastMeeting toPastMeetingWithNotes(Meeting meeting, String notes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (meeting instanceof PastMeeting){
            stringBuilder.append(((PastMeeting) meeting).getNotes());
        }

        String fullNotes = stringBuilder.append(" " + notes).toString().trim();

        return new PastMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts(), fullNotes);
    }

    /**
     * Helper method to convert meeting to a FutureMeeting.
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
