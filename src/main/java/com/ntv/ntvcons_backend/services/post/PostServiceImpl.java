package com.ntv.ntvcons_backend.services.post;

import com.ntv.ntvcons_backend.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    /* CREATE */

    /* READ */
    @Override
    public boolean existsById(long postId) throws Exception {
        return postRepository
                .existsByPostIdAndIsDeletedIsFalse(postId);
    }
    
    /* UPDATE */

    /* DELETE */

}
