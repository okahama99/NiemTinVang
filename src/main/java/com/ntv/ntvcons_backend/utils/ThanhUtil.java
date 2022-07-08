package com.ntv.ntvcons_backend.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ThanhUtil {

    public Pageable makePaging(int pageNo, int pageSize, String sortBy, boolean sortTypeAsc) {
        if (sortTypeAsc) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

}
