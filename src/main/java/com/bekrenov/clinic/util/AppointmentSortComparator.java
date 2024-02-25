package com.bekrenov.clinic.util;

import com.bekrenov.clinic.model.entity.Appointment;

import java.time.LocalDateTime;
import java.util.Comparator;

public class AppointmentSortComparator implements Comparator<Appointment> {
    @Override
    public int compare(Appointment o1, Appointment o2) {
        LocalDateTime dateTime1 = LocalDateTime.of(o1.getDate(), o1.getTime());
        LocalDateTime dateTime2 = LocalDateTime.of(o2.getDate(), o2.getTime());
        if(dateTime1.isAfter(dateTime2))
            return 1;
        else if(dateTime1.isBefore(dateTime2))
            return -1;
        return 0;
    }
}
