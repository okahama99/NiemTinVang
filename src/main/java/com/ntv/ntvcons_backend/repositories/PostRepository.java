package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<Post> findByPostIdAndIsDeletedIsFalse(long postId);
    List<Post> findAllByPostIdInAndIsDeletedIsFalse(Collection<Long> postIdCollection);

    boolean existsByAddressAndIsDeletedIsFalse(String address);

    Page<Post> findAllByPostCategoryIdAndIsDeletedIsFalse(Long postCategoryId, Pageable pageable);

    Page<Post> findAllByScaleContainingAndIsDeletedIsFalse(String scale, Pageable pageable);

    Page<Post> findAllByAuthorNameContainingAndIsDeletedIsFalse(String authorName, Pageable pageable);

    Page<Post> findAllByPostTitleContainingAndIsDeletedIsFalse(String postTitle, Pageable pageable);

    Page<Post> findAllByOwnerNameContainingAndIsDeletedIsFalse(String ownerName, Pageable pageable);

    Page<Post> findAllByAddressContainingAndIsDeletedIsFalse(String address, Pageable pageable);
}
