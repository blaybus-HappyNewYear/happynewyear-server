package blaybus.happynewyear.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagingDto {
    private List<PostPreviewDto> posts;
    private int startPage;
    private int endPage;
}
