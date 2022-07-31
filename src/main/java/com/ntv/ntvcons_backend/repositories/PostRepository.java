package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByStatusNotIn(
            Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByPostIdAndStatusNotIn(
            long postId, Collection<Status> statusCollection);
    Optional<Post> findByPostIdAndStatusNotIn(
            long postId, Collection<Status> statusCollection);
    List<Post> findAllByPostIdInAndStatusNotIn(
            Collection<Long> postIdCollection, Collection<Status> statusCollection);


    /* postCategoryId */
    List<Post> findAllByPostCategoryIdAndStatusNotIn(
            long postCategoryId, Collection<Status> statusCollection);
    Page<Post> findAllByPostCategoryIdAndStatusNotIn(
            long postCategoryId, Collection<Status> statusCollection, Pageable paging);
    List<Post> findAllByPostCategoryIdInAndStatusNotIn(
            Collection<Long> postCategoryIdCollection, Collection<Status> statusCollection);
    Page<Post> findAllByPostCategoryIdInAndStatusNotIn(
            Collection<Long> postCategoryIdCollection, Collection<Status> statusCollection, Pageable paging);


    /* authorName */
    List<Post> findAllByAuthorNameAndStatusNotIn(
            String authorName, Collection<Status> statusCollection);
    Page<Post> findAllByAuthorNameAndStatusNotIn(
            String authorName, Collection<Status> statusCollection, Pageable paging);
    List<Post> findAllByAuthorNameContainsAndStatusNotIn(
            String authorName, Collection<Status> statusCollection);
    Page<Post> findAllByAuthorNameContainsAndStatusNotIn(
            String authorName, Collection<Status> statusCollection, Pageable paging);


    /* postTitle*/
    Optional<Post> findByPostTitleAndStatusNotIn(
            String postTitle, Collection<Status> statusCollection);
    List<Post> findAllByPostTitleContainsAndStatusNotIn(
            String postTitle, Collection<Status> statusCollection);
    Page<Post> findAllByPostTitleContainsAndStatusNotIn(
            String postTitle, Collection<Status> statusCollection, Pageable paging);


    /* ownerName */
    List<Post> findAllByOwnerNameAndStatusNotIn(
            String ownerName, Collection<Status> statusCollection);
    Page<Post> findAllByOwnerNameAndStatusNotIn(
            String ownerName, Collection<Status> statusCollection, Pageable paging);
    List<Post> findAllByOwnerNameContainsAndStatusNotIn(
            String ownerName, Collection<Status> statusCollection);
    Page<Post> findAllByOwnerNameContainsAndStatusNotIn(
            String ownerName, Collection<Status> statusCollection, Pageable paging);

    /* address */
    boolean existsByAddressAndStatusNotIn(
            String address, Collection<Status> statusCollection);
    Optional<Post> findByAddressAndStatusNotIn(
            String address, Collection<Status> statusCollection);
    List<Post> findAllByAddressContainsAndStatusNotIn(
            String address, Collection<Status> statusCollection);
    Page<Post> findAllByAddressContainsAndStatusNotIn(
            String address, Collection<Status> statusCollection, Pageable paging);

    /* scale */
    List<Post> findAllByScaleAndStatusNotIn(
            String scale, Collection<Status> statusCollection);
    Page<Post> findAllByScaleAndStatusNotIn(
            String scale, Collection<Status> statusCollection, Pageable paging);
    List<Post> findAllByScaleContainsAndStatusNotIn(
            String scale, Collection<Status> statusCollection);
    Page<Post> findAllByScaleContainsAndStatusNotIn(
            String scale, Collection<Status> statusCollection, Pageable paging);
}
