package org.oc.paymybuddy.service;

import org.oc.paymybuddy.constants.Pagination;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PaginationService {
    public PageImpl<?> getPaginatedList(Pageable pageable, List<?> rawList) {
        int pageSize = Pagination.DEFAULT_SIZE;
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage*pageSize;
        List<?> list;

        if (rawList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, rawList.size());
            list = rawList.subList(startItem, toIndex);
        }

        return new PageImpl<>(list,
                PageRequest.of(currentPage, pageSize, Sort.by("date").descending()),
                rawList.size());
    }
}
