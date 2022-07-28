package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findAllByStatusNotIn
            (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByRequestIdAndStatusNotIn
            (long requestId, Collection<Status> statusCollection);
    Optional<Request> findByRequestIdAndStatusNotIn
            (long requestId, Collection<Status> statusCollection);
    boolean existsAllByRequestIdInAndStatusNotIn
            (Collection<Long> requestIdCollection, Collection<Status> statusCollection);
    List<Request> findAllByRequestIdInAndStatusNotIn
            (Collection<Long> requestIdCollection, Collection<Status> statusCollection);
    /* Id & projectId & requestName */
    /** Check duplicate for Update */
    boolean existsByProjectIdAndRequestNameAndRequestIdIsNotAndStatusNotIn
            (long projectId, String requestName, long requestId, Collection<Status> statusCollection);


    /* projectId */
    List<Request> findAllByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection);
    Page<Request> findAllByProjectIdAndStatusNotIn
            (long projectId, Collection<Status> statusCollection, Pageable paging);
    List<Request> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection);
    Page<Request> findAllByProjectIdInAndStatusNotIn
            (Collection<Long> projectIdCollection, Collection<Status> statusCollection, Pageable paging);

    /* projectId & isVerified */
    List<Request> findAllByProjectIdAndIsVerifiedAndStatusNotIn
            (long projectId, boolean isVerified, Collection<Status> statusCollection);

    /* projectId & isVerified = true & isApproved (Approval required Verification) */
    List<Request> findAllByProjectIdAndIsVerifiedIsTrueAndIsApprovedAndStatusNotIn
            (long projectId, boolean isApproved, Collection<Status> statusCollection);


    /* requestName */
    List<Request> findAllByRequestNameAndStatusNotIn
            (String requestName, Collection<Status> statusCollection);
    Page<Request> findAllByRequestNameAndStatusNotIn
            (String requestName, Collection<Status> statusCollection, Pageable paging);
    List<Request> findAllByRequestNameContainsAndStatusNotIn
            (String requestName, Collection<Status> statusCollection);
    Page<Request> findAllByRequestNameContainsAndStatusNotIn
            (String requestName, Collection<Status> statusCollection, Pageable paging);
    /* projectId & requestName */
    /** Check duplicate for Create */
    boolean existsByProjectIdAndRequestNameAndStatusNotIn
            (long projectId, String requestName, Collection<Status> statusCollection);


    /* requestTypeId */
    List<Request> findAllByRequestTypeIdAndStatusNotIn
            (long requestTypeId, Collection<Status> statusCollection);
    Page<Request> findAllByRequestTypeIdAndStatusNotIn
            (long requestTypeId, Collection<Status> statusCollection, Pageable paging);
    List<Request> findAllByRequestTypeIdInAndStatusNotIn
            (Collection<Long> requestTypeIdCollection, Collection<Status> statusCollection);
    Page<Request> findAllByRequestTypeIdInAndStatusNotIn
            (Collection<Long> requestTypeIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* requesterId */
    List<Request> findAllByRequesterIdAndStatusNotIn
            (long requesterId, Collection<Status> statusCollection);
    Page<Request> findAllByRequesterIdAndStatusNotIn
            (long requesterId, Collection<Status> statusCollection, Pageable paging);
    List<Request> findAllByRequesterIdInAndStatusNotIn
            (Collection<Long> requesterIdCollection, Collection<Status> statusCollection);
    Page<Request> findAllByRequesterIdInAndStatusNotIn
            (Collection<Long> requesterIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* requestDatetime */
    List<Request> findAllByRequestDateAfterAndStatusNotIn
            (LocalDateTime afterDatetime, Collection<Status> statusCollection);
    List<Request> findAllByRequestDateBeforeAndStatusNotIn
            (LocalDateTime beforeDatetime, Collection<Status> statusCollection);
    List<Request> findAllByRequestDateBetweenAndStatusNotIn
            (LocalDateTime fromDatetime, LocalDateTime toDatetime, Collection<Status> statusCollection);


    /* verifierId */
    List<Request> findAllByVerifierIdAndStatusNotIn
            (long requesterId, Collection<Status> statusCollection);
    Page<Request> findAllByVerifierIdAndStatusNotIn
            (long requesterId, Collection<Status> statusCollection, Pageable paging);
    List<Request> findAllByVerifierIdInAndStatusNotIn
            (Collection<Long> requesterIdCollection, Collection<Status> statusCollection);
    Page<Request> findAllByVerifierIdInAndStatusNotIn
            (Collection<Long> requesterIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* isVerified */
    List<Request> findAllByIsVerifiedAndStatusNotIn
            (boolean isVerified, Collection<Status> statusCollection);

    /* isVerified = true  & verifyDate */
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateAfterAndStatusNotIn
            (LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateBeforeAndStatusNotIn
            (LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateBetweenAndStatusNotIn
            (LocalDateTime fromDate, LocalDateTime toDate, Collection<Status> statusCollection);

    /* isVerified = true  & verifierId */
    List<Request> findAllByIsVerifiedIsTrueAndVerifierIdAndStatusNotIn
            (long verifierId, Collection<Status> statusCollection);
    List<Request> findAllByIsVerifiedIsTrueAndVerifierIdInAndStatusNotIn
            (Collection<Long> verifierIdCollection, Collection<Status> statusCollection);

    /* isVerified = true & isApproved (Approval required Verification) */
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndStatusNotIn
            (boolean isApproved, Collection<Status> statusCollection);

    /* isVerified = true & isApproved & verifyDate */
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateAfterAndStatusNotIn
            (boolean isApproved, LocalDateTime afterDate, Collection<Status> statusCollection);
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateBeforeAndStatusNotIn
            (boolean isApproved, LocalDateTime beforeDate, Collection<Status> statusCollection);
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateBetweenAndStatusNotIn
            (boolean isApproved, LocalDateTime fromDate, LocalDateTime toDate, Collection<Status> statusCollection);
}
