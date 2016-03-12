package utils;

import interfaces.Contact;
import interfaces.Meeting;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static utils.ContactManagerPredicates.*;

/**
 * Custom filtering methods to be used as helpers for meeting list filtering.
 *
 * Created by Vladimirs Ivanovs on 11/03/2016.
 */
public class ContactManagerFilters {

    /**
     * Custom method to filter list with 2 predicate.
     *
     * @param meetings list of meetings to be filtered
     * @param predicate first filter
     * @param predicate2 second filter
     * @param comparator comparator to sort
     * @return depends on set up. Can return filtered and sorted list
     */
    public static List<Meeting> customMeetingFilter(List<? super Meeting> meetings, Predicate<Meeting> predicate, Predicate<Meeting> predicate2, Comparator<Meeting> comparator) {
        return meetings.stream()
                .map(meeting -> (Meeting)meeting)
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

    public static List<Meeting> filterAnyMeetingsWithID(List<? super Meeting> meetings, int id) {
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

    public static List<Meeting> filterFutureMeetingsWithContact(List<? super Meeting> meetings, Contact contact) {
        return customMeetingFilter(meetings, isFutureMeeting(), meetingHasContact(contact), new DateComparator());
    }

    public static List<Meeting> filterFutureMeetingsOnDate(List<? super Meeting> meetings, Calendar date) {
        return customMeetingFilter(meetings, isFutureMeeting(), meetingOn(date), new DateComparator());
    }

    public static List<Meeting> filterPastMeetings(List<Meeting> meetings) {
        return customMeetingFilter(meetings, isPastMeeting(), dummyPredicate(), new DateComparator());
    }

    public static List<Meeting> filterPastMeetingsByID(List<Meeting> meetings, int id) {
        return customMeetingFilter(meetings, isPastMeeting(), meetingHasID(id), new DateComparator());
    }

    public static List<Meeting> filterPastMeetingsByContact(List<? super Meeting> meetings, Contact contact) {
        return customMeetingFilter(meetings, isPastMeeting(), meetingHasContact(contact), new DateComparator());
    }

    public static Set<Contact> filterContactsWithName(Set<Contact> contacts, String name) {
        return customContactFilter(contacts, contactWithName(name));
    }

    public static Set<Contact> filterContactsWithID(Set<Contact> contacts, int id) {
        return customContactFilter(contacts, contactWithID(id));
    }
}
