package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.RequestDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    Page<RequestDetail> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    Optional<RequestDetail> findByRequestDetailIdAndStatusNotIn(
            long requestDetailId, Collection<Status> statusCollection);
    List<RequestDetail> findAllByRequestDetailIdInAndStatusNotIn(
            Collection<Long> requestDetailIdCollection, Collection<Status> statusCollection);
    /* Id & requestId & itemDesc & itemPrice */
    /** Check duplicate for Update */
    boolean existsByRequestIdAndItemDescAndItemPriceAndRequestDetailIdIsNotAndStatusNotIn(
            long requestId, String itemDesc, double itemPrice, long requestDetailId, Collection<Status> statusCollection);


    /* requestId */
    List<RequestDetail> findAllByRequestIdAndStatusNotIn(
            long requestId, Collection<Status> statusCollection);
    Page<RequestDetail> findAllByRequestIdAndStatusNotIn(
            long requestId, Collection<Status> statusCollection, Pageable paging);
    List<RequestDetail> findAllByRequestIdInAndStatusNotIn(
            Collection<Long> requestIdCollection, Collection<Status> statusCollection);
    Page<RequestDetail> findAllByRequestIdInAndStatusNotIn(
            Collection<Long> requestIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* requestId & itemDesc & itemPrice */
    /** Check duplicate for Update */
    boolean existsByRequestIdAndItemDescAndItemPriceAndStatusNotIn(
            long requestId, String itemDesc, double itemPrice, Collection<Status> statusCollection);
}
