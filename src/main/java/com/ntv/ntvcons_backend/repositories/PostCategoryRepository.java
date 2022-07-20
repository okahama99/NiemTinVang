package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
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

    List<ShowPostCategoryModel> findAllByStatus(Status status);

    PostCategory findByPostCategoryNameAndStatus(String categoryName, Status status);
}
