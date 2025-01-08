package blaybus.happynewyear.post.dto;

import blaybus.happynewyear.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PostPreviewDto {
    private Long id;
    private String title;
    private LocalDate createdAt;
    private int views;

    public static PostPreviewDto toDto(Post post) {
        return PostPreviewDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .views(post.getViews())
                .build();
    }
}
