package com.bekrenov.clinic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class HolidaysRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<LocalDate> getAllHolidays(){
        Query query = entityManager.createNativeQuery("select * from holidays;");
        return ((List<Date>) query.getResultList()).stream()
                .map(Date::toLocalDate)
                .toList();
    }
}
