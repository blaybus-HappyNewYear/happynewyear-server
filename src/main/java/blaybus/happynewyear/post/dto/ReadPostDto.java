package blaybus.happynewyear.post.dto;

import blaybus.happynewyear.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReadPostDto {
    private Long id;
    private String title;
    private String author;
    private LocalDate createdAt;
    private String content;

    public static ReadPostDto toDto(Post post) {
        return ReadPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .createdAt(post.getCreatedAt())
                .content(post.getContent())
                .build();
    }
}
