package blaybus.happynewyear.post.repository;

import blaybus.happynewyear.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);
}
