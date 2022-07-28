package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    List<PostCategory> findAllByStatusNotIn
                (Collection<Status> statusCollection);
    Page<PostCategory> findAllByStatusNotIn
                (Collection<Status> statusCollection, Pageable paging);


    /* Id */
    boolean existsByPostCategoryIdAndStatusNotIn
            (long postCategoryId, Collection<Status> statusCollection);
    Optional<PostCategory> findByPostCategoryIdAndStatusNotIn
            (long postCategoryId, Collection<Status> statusCollection);
    List<PostCategory> findAllByPostCategoryIdInAndStatusNotIn
            (Collection<Long> postCategoryIdCollection, Collection<Status> statusCollection);
    Page<PostCategory> findAllByPostCategoryIdInAndStatusNotIn
            (Collection<Long> postCategoryIdCollection, Collection<Status> statusCollection, Pageable paging);
    /* Id & postCategoryName */
    /** Check duplicate for Update */
    boolean existsByPostCategoryNameAndPostCategoryIdIsNotAndStatusNotIn
            (String categoryName, long postCategoryId, Collection<Status> statusCollection);


    /* postCategoryName */
    /** Check duplicate for Create */
    boolean existsByPostCategoryNameAndStatusNotIn
            (String categoryName, Collection<Status> statusCollection);
    Optional<PostCategory> findByPostCategoryNameAndStatusNotIn
            (String categoryName, Collection<Status> statusCollection);
    List<PostCategory> findByPostCategoryNameContainsAndStatusNotIn
            (String postCategoryName, Collection<Status> statusCollection);
    Page<PostCategory> findByPostCategoryNameContainsAndStatusNotIn
            (String postCategoryName, Collection<Status> statusCollection, Pageable paging);


    /* postCategoryDesc */
    List<PostCategory> findByPostCategoryDescAndStatusNotIn
            (String postCategoryDesc, Collection<Status> statusCollection);
    Page<PostCategory> findByPostCategoryDescAndStatusNotIn
            (String postCategoryDesc, Collection<Status> statusCollection, Pageable paging);
    List<PostCategory> findByPostCategoryDescContainsAndStatusNotIn
            (String postCategoryDesc, Collection<Status> statusCollection);
    Page<PostCategory> findByPostCategoryDescContainsAndStatusNotIn
            (String postCategoryDesc, Collection<Status> statusCollection, Pageable paging);



}
