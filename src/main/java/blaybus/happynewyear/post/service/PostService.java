package blaybus.happynewyear.post.service;

import blaybus.happynewyear.post.dto.CreatePostDto;
import blaybus.happynewyear.post.dto.PostPreviewDto;
import blaybus.happynewyear.post.dto.ReadPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    void createPost(CreatePostDto createPostDto);
    ReadPostDto readPost(Long id);

    Page<PostPreviewDto> paging(Pageable pageable);
}
