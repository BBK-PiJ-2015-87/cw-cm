package utils;

import interfaces.Meeting;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Custom predicate to be used in Contact Manager class streams to compare meetings.
 *
 * Created by vladimirsivanovs on 11/03/2016.
 */
public class MeetingPredicates {

    public static Predicate<Meeting> dummyPredicate() {
        return meeting -> true;
    }

    public static Predicate<Meeting> isFutureMeeting() {
        Calendar now = new GregorianCalendar();
        return meeting -> meeting.getDate().after(now);
    }

    public static Predicate<Meeting> isPastMeeting() {
        Calendar now = new GregorianCalendar();
        return meeting -> meeting.getDate().before(now);
    }

    public static Predicate<Meeting> meetingOn(Calendar date) {
        return meeting -> meeting.getDate().equals(date);
    }

    public static Predicate<Meeting> meetingHasID(int id) {
        return meeting -> meeting.getId() == id;
    }

    public static Predicate<Meeting> meetingHasContact(int id) {
        return meeting -> meeting.getId() == id;
    }

}
