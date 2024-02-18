package com.bekrenov.clinic.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public class PageUtil {
    public static <T> Page<T> paginateList(List<T> list, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<T> pageContent = getSubListForPageable(list, pageable);
        return new PageImpl<>(pageContent, pageable, list.size());
    }

    private static <T> List<T> getSubListForPageable(List<T> list, Pageable pageable){
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        try {
            return list.subList(start, end);
        } catch (IllegalArgumentException ex){
            return Collections.emptyList();
        }
    }
}
