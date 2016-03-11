package utils;

import interfaces.Contact;
import interfaces.Meeting;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static utils.ContactManagerPredicates.*;

/**
 * Created by vladimirsivanovs on 11/03/2016.
 */
public class ContactManagerFilters {

    /**
     *
     * @param meetings
     * @param predicate
     * @param predicate2
     * @param comparator
     * @return
     */
    public static List<Meeting> customMeetingFilter(List<Meeting> meetings, Predicate<Meeting> predicate, Predicate<Meeting> predicate2, Comparator<Meeting> comparator) {
        return meetings.stream()
                .filter(predicate)
                .filter(predicate2)
                .sorted(comparator)
                .collect(Collectors.<Meeting>toList());
    }

    public static Set<Contact> customContactFilter(Set<Contact> contacts, Predicate<Contact> predicate) {
        return contacts.stream()
                .filter(predicate)
                .collect(Collectors.<Contact>toSet());
    }

    public static List<Meeting> filterAnyMeetingsWithID(List<Meeting> meetings, int id) {
        return customMeetingFilter(meetings,  meetingHasID(id), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterAnyMeetingsWithContact(List<Meeting> meetings, Contact contact) {
        return customMeetingFilter(meetings,  meetingHasContact(contact), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterFutureMeetings(List<Meeting> meetings) {
        return customMeetingFilter(meetings, isFutureMeeting(), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterFutureMeetingsWithID(List<Meeting> meetings, int id) {
        return customMeetingFilter(meetings, isFutureMeeting(), meetingHasID(id), new DateComparator());
    }

    public static List<Meeting> filterFutureMeetingsWithContact(List<Meeting> meetings, Contact contact) {
        return customMeetingFilter(meetings, isFutureMeeting(), meetingHasContact(contact), new DateComparator());
    }

    public static List<Meeting> filterPastMeetings(List<Meeting> meetings) {
        return customMeetingFilter(meetings, isPastMeeting(), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterPastMeetingsByID(List<Meeting> meetings, int id) {
        return customMeetingFilter(meetings, isPastMeeting(), meetingHasID(id), new DateComparator());
    }

    public static List<Meeting> filterPastMeetingsByContact(List<Meeting> meetings, Contact contact) {
        return customMeetingFilter(meetings, isFutureMeeting(), meetingHasContact(contact), new DateComparator());
    }

    public static Set<Contact> filterContactsWithName(Set<Contact> contacts, String name) {
        return customContactFilter(contacts, contactWithName(name));
    }

    public static Set<Contact> filterContactsWithID(Set<Contact> contacts, int id) {
        return customContactFilter(contacts, contactWithID(id));
    }
}
