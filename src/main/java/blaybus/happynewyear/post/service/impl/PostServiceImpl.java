package blaybus.happynewyear.post.service.impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.post.dto.CreatePostDto;
import blaybus.happynewyear.post.dto.PostPreviewDto;
import blaybus.happynewyear.post.dto.ReadPostDto;
import blaybus.happynewyear.post.entity.Post;
import blaybus.happynewyear.post.repository.PostRepository;
import blaybus.happynewyear.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void createPost(CreatePostDto createPostDto) {
        LocalDate now = LocalDate.now();
        Post entity = createPostDto.toEntity(now);
        postRepository.save(entity);
    }

    @Override
    @Transactional
    public ReadPostDto readPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        post.setViews(post.getViews() + 1);
        return ReadPostDto.toDto(post);
    }

    @Override
    @Transactional
    public Page<PostPreviewDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;    // page 위치에 있는 값은 0부터 시작
        int pageLimit = 10;  // 한 페이지에 보여줄 글 개수

        //한 페이지 당 10개씩 글을 보여주고 정렬 기준은 ID 기준으로 내림차순
        Page<Post> postsPage = postRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        Page<PostPreviewDto> PostPreviewDtos = postsPage.map(PostPreviewDto::toDto);

        return PostPreviewDtos;
    }
}
