package com.ntv.ntvcons_backend.repositories;

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
    Page<Request> findAllByIsDeletedIsFalse(Pageable paging);


    /* Id */
    boolean existsByRequestIdAndIsDeletedIsFalse(long requestId);
    Optional<Request> findByRequestIdAndIsDeletedIsFalse(long requestId);
    List<Request> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Long> requestIdCollection);


    /* projectId */
    List<Request> findAllByProjectIdAndIsDeletedIsFalse(long projectId);
    Page<Request> findAllByProjectIdAndIsDeletedIsFalse(long projectId, Pageable paging);
    List<Request> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection);
    Page<Request> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Long> projectIdCollection, Pageable paging);

    /* projectId & isVerified */
    List<Request> findAllByProjectIdAndIsVerifiedAndIsDeletedIsFalse(long projectId, boolean isVerified);

    /* projectId & isVerified = true & isApproved (Approval required Verification) */
    List<Request> findAllByProjectIdAndIsVerifiedIsTrueAndIsApprovedAndIsDeletedIsFalse(long projectId, boolean isApproved);


    /* requestName */
    Optional<Request> findByRequestNameAndIsDeletedIsFalse(String requestName);
    List<Request> findAllByRequestNameContainsAndIsDeletedIsFalse(String requestName);
    Page<Request> findAllByRequestNameContainsAndIsDeletedIsFalse(String requestName, Pageable paging);


    /* requestTypeId */
    List<Request> findAllByRequestTypeIdAndIsDeletedIsFalse(long requestTypeId);
    Page<Request> findAllByRequestTypeIdAndIsDeletedIsFalse(long requestTypeId, Pageable paging);
    List<Request> findAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Long> requestTypeIdCollection);
    Page<Request> findAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Long> requestTypeIdCollection, Pageable paging);


    /* requesterId */
    List<Request> findAllByRequesterIdAndIsDeletedIsFalse(long requesterId);
    Page<Request> findAllByRequesterIdAndIsDeletedIsFalse(long requesterId, Pageable paging);
    List<Request> findAllByRequesterIdInAndIsDeletedIsFalse(Collection<Long> requesterIdCollection);
    Page<Request> findAllByRequesterIdInAndIsDeletedIsFalse(Collection<Long> requesterIdCollection, Pageable paging);


    /* requestDatetime */
    List<Request> findAllByRequestDateAfterAndIsDeletedIsFalse(LocalDateTime afterDatetime);
    List<Request> findAllByRequestDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDatetime);
    List<Request> findAllByRequestDateBetweenAndIsDeletedIsFalse(LocalDateTime fromDatetime, LocalDateTime toDatetime);


    /* verifierId */
    List<Request> findAllByVerifierIdAndIsDeletedIsFalse(long requesterId);
    Page<Request> findAllByVerifierIdAndIsDeletedIsFalse(long requesterId, Pageable paging);
    List<Request> findAllByVerifierIdInAndIsDeletedIsFalse(Collection<Long> requesterIdCollection);
    Page<Request> findAllByVerifierIdInAndIsDeletedIsFalse(Collection<Long> requesterIdCollection, Pageable paging);


    /* isVerified */
    List<Request> findAllByIsVerifiedAndIsDeletedIsFalse(boolean isVerified);

    /* isVerified = true  & verifyDate */
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateAfterAndIsDeletedIsFalse(LocalDateTime afterDate);
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateBeforeAndIsDeletedIsFalse(LocalDateTime beforeDate);
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateBetweenAndIsDeletedIsFalse(LocalDateTime fromDate, LocalDateTime toDate);

    /* isVerified = true  & verifierId */
    List<Request> findAllByIsVerifiedIsTrueAndVerifierIdAndIsDeletedIsFalse(long verifierId);
    List<Request> findAllByIsVerifiedIsTrueAndVerifierIdInAndIsDeletedIsFalse(Collection<Long> verifierIdCollection);

    /* isVerified = true & isApproved (Approval required Verification) */
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndIsDeletedIsFalse(boolean isApproved);

    /* isVerified = true & isApproved & verifyDate */
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateAfterAndIsDeletedIsFalse
            (boolean isApproved, LocalDateTime afterDate);
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateBeforeAndIsDeletedIsFalse
            (boolean isApproved, LocalDateTime beforeDate);
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateBetweenAndIsDeletedIsFalse
            (boolean isApproved, LocalDateTime fromDate, LocalDateTime toDate);
}
