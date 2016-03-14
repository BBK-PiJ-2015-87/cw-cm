package utils;

import interfaces.Meeting;

import java.util.Comparator;

/**
 * Helper class for ordering meetings in the list.
 *
 * Created by Vladimirs Ivanovs on 10/03/2016.
 */
public class DateComparator implements Comparator<Meeting> {

    @Override
    public int compare(Meeting m1, Meeting m2) {
        return m2.getDate().compareTo(m1.getDate());
    }
}
