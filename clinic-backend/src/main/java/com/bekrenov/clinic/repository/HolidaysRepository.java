package com.bekrenov.clinic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void addHolidays(List<LocalDate> dates){
        Query query = entityManager.createNativeQuery(composeInsertStatement(dates));
        query.executeUpdate();
    }

    @Transactional
    public void removeHolidays(List<LocalDate> dates){
        Query query = entityManager.createNativeQuery(composeDeleteStatement(dates));
        query.executeUpdate();
    }

    private String composeInsertStatement(List<LocalDate> dates) {
        StringBuilder queryBuilder = new StringBuilder("insert into holidays values ");
        for(int i = 0; i < dates.size(); i++){
            boolean isLast = i == dates.size()-1;
            queryBuilder
                    .append("('")
                    .append(dates.get(i))
                    .append("')")
                    .append(isLast ? "" : ", ");
        }
        return queryBuilder.toString();
    }

    private String composeDeleteStatement(List<LocalDate> dates) {
        StringBuilder queryBuilder = new StringBuilder("delete from holidays where holiday_date in (");
        for(int i = 0; i < dates.size(); i++){
            boolean isLast = i == dates.size()-1;
            queryBuilder
                    .append('\'')
                    .append(dates.get(i))
                    .append('\'')
                    .append(isLast ? ")" : ", ");
        }
        return queryBuilder.toString();
    }
}
