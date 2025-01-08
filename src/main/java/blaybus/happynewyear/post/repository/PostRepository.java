package blaybus.happynewyear.post.repository;

import blaybus.happynewyear.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
