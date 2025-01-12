package blaybus.happynewyear.post.controller;

import blaybus.happynewyear.post.dto.CreatePostDto;
import blaybus.happynewyear.post.dto.PagingDto;
import blaybus.happynewyear.post.dto.PostPreviewDto;
import blaybus.happynewyear.post.dto.ReadPostDto;
import blaybus.happynewyear.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시판 조회
    @GetMapping("/board")
    public ResponseEntity<PagingDto> paging(@PageableDefault(page = 1) Pageable pageable) {
        Page<PostPreviewDto> postPreviewDtos = postService.paging(pageable);


        int blockLimit = 5;     // 현재 사용자가 선택한 페이지 앞 뒤로 3페이지 씩만 보여줌
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), postPreviewDtos.getTotalPages());

        PagingDto pagingDto = PagingDto.builder()
                .posts(postPreviewDtos.getContent())
                .startPage(startPage)
                .endPage(endPage)
                .build();

        return ResponseEntity.ok(pagingDto);
        //return ResponseEntity.ok(postPreviewDtos.getContent());
    }

    //게시글 작성
    @PostMapping("/board/write")
    public ResponseEntity<String> createPost(@RequestBody CreatePostDto createPostDto) {
        postService.createPost(createPostDto);
        return ResponseEntity.ok("게시글 작성이 완료되었습니다.");
    }

    //게시글 조회
    @PostMapping("/board/{id}")
    public ResponseEntity<ReadPostDto> readPost(@PathVariable Long id) {
        ReadPostDto readPostDto = postService.readPost(id);
        return ResponseEntity.ok(readPostDto);
    }
}
