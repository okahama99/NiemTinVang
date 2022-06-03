package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Location;
import com.ntv.ntvcons_backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByIsDeletedFalse();


    /* Id */
    Optional<Post> findByPostIdAndIsDeletedIsFalse(int postId);
    List<Post> findAllByPostIdInAndIsDeletedIsFalse(Collection<Integer> postIdCollection);


    /* postTitle */
    Optional<Post> findByPostTitleAndIsDeletedIsFalse(String postTitle);
    List<Post> findAllByPostTitleLikeAndIsDeletedIsFalse(String postTitleCollection);


    /* authorId */
    List<Post> findAllByAuthorIdAndIsDeletedIsFalse(int authorId);
    List<Post> findAllByAuthorIdInAndIsDeletedIsFalse(Collection<Integer> authorIdCollection);


    /* createDate */
    List<Post> findAllByCreateDateAfterAndIsDeletedIsFalse(Instant afterDate);
    List<Post> findAllByCreateDateBeforeAndIsDeletedIsFalse(Instant beforeDate);
    List<Post> findAllByCreateDateBetweenAndIsDeletedIsFalse(Instant from, Instant to);
}
