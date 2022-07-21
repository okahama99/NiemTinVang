package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    Page<PostCategory> findAllByStatus(Pageable pageable, Status status);

    Optional<PostCategory> findByPostCategoryIdAndStatus(Long postCategoryId, Status status);

    List<PostCategory> findAllByStatus(Status status);

    Page<PostCategory> findByPostCategoryNameAndStatus(String postCategoryName, Pageable pageable, Status status);

    Page<PostCategory> findByPostCategoryDescAndStatus(String postCategoryDesc, Pageable pageable, Status status);

    PostCategory findByPostCategoryNameAndStatus(String categoryName, Status status);
}
