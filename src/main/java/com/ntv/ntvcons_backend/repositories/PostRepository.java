package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsDeletedIsFalse(Pageable pageable);


    /* Id */
    Optional<Post> findByPostIdAndIsDeletedIsFalse(long postId);
    List<Post> findAllByPostIdInAndIsDeletedIsFalse(Collection<Long> postIdCollection);


    /* postTitle */
    Optional<Post> findByPostTitleAndIsDeletedIsFalse(String postTitle);
    List<Post> findAllByPostTitleContainsAndIsDeletedIsFalse(String postTitleCollection);
}
