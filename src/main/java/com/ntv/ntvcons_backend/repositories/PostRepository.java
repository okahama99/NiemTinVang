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
    Optional<Post> findByPostIdAndStatus(long postId, Status status);
    List<Post> findAllByPostIdInAndStatus(Collection<Long> postIdCollection);

    boolean existsByAddressAndStatus(String address, Status status);

    Page<Post> findAllByPostCategoryIdAndStatus(Long postCategoryId, Status status, Pageable pageable);

    Page<Post> findAllByScaleAndStatus(String scale, Status status, Pageable pageable);

    Page<Post> findAllByAuthorNameAndStatus(String authorName, Status status, Pageable pageable);

    Page<Post> findAllByPostTitleAndStatus(String postTitle, Status status, Pageable pageable);

    Page<Post> findAllByOwnerNameAndStatus(String ownerName, Status status, Pageable pageable);

    Page<Post> findAllByAddressAndStatus(String address, Status status, Pageable pageable);
}
