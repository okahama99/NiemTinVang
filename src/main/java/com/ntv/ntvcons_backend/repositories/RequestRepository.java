package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByIsDeletedFalse();


    /* Id */
    Optional<Request> findByRequestIdAndIsDeletedIsFalse(int requestId);
    List<Request> findAllByRequestIdInAndIsDeletedIsFalse(Collection<Integer> requestIdCollection);


    /* projectId */
    List<Request> findAllByProjectIdAndIsDeletedIsFalse(int projectId);
    List<Request> findAllByProjectIdInAndIsDeletedIsFalse(Collection<Integer> projectIdCollection);
    /* projectId & isVerified */
    List<Request> findAllByProjectIdAndIsVerifiedAndIsDeletedIsFalse(int projectId, boolean isVerified);
    /* projectId & isVerified = true & isApproved (Approval required Verification) */
    List<Request> findAllByProjectIdAndIsVerifiedIsTrueAndIsApprovedAndIsDeletedIsFalse(int projectId, boolean isApproved);


    /* requestTypeId */
    List<Request> findAllByRequestTypeIdAndIsDeletedIsFalse(int requestTypeId);
    List<Request> findAllByRequestTypeIdInAndIsDeletedIsFalse(Collection<Integer> requestTypeIdCollection);


    /* requesterId */
    List<Request> findAllByRequesterIdAndIsDeletedIsFalse(int requesterId);
    List<Request> findAllByRequesterIdInAndIsDeletedIsFalse(Collection<Integer> requesterIdCollection);


    /* requestDatetime */
    List<Request> findAllByRequestDatetimeAfterAndIsDeletedIsFalse(Instant afterDatetime);
    List<Request> findAllByRequestDatetimeBeforeAndIsDeletedIsFalse(Instant beforeDatetime);
    List<Request> findAllByRequestDatetimeBetweenAndIsDeletedIsFalse(Instant fromDatetime, Instant toDatetime);


    /* isVerified */
    List<Request> findAllByIsVerifiedAndIsDeletedIsFalse(boolean isVerified);

    /* isVerified = true  & verifyDate */
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Request> findAllByIsVerifiedIsTrueAndVerifyDateBetweenAndIsDeletedIsFalse(Instant fromDate, Instant toDate);

    /* isVerified = true  & verifierId */
    List<Request> findAllByIsVerifiedIsTrueAndVerifierIdAndIsDeletedIsFalse(int verifierId);
    List<Request> findAllByIsVerifiedIsTrueAndVerifierIdInAndIsDeletedIsFalse(Collection<Integer> verifierIdCollection);

    /* isVerified = true & isApproved (Approval required Verification) */
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndIsDeletedIsFalse(boolean isApproved);

    /* isVerified = true & isApproved & verifyDate */
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateAfterAndIsDeletedIsFalse
            (boolean isApproved, Instant afterDate);
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateBeforeAndIsDeletedIsFalse
            (boolean isApproved, Instant beforeDate);
    List<Request> findAllByIsVerifiedIsTrueAndIsApprovedAndVerifyDateBetweenAndIsDeletedIsFalse
            (boolean isApproved, Instant fromDate, Instant toDate);
}
