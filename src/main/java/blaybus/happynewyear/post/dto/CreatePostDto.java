package blaybus.happynewyear.post.dto;

import blaybus.happynewyear.post.entity.Post;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreatePostDto {
    private String title;
    private String content;

    public Post toEntity(LocalDate createdAt) {
        return Post.builder()
                .title(title)
                .author("관리자")
                .createdAt(createdAt)
                .views(0)
                .content(content)
                .build();
    }
}
