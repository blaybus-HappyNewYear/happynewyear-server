package blaybus.happynewyear.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private LocalDate createdAt;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int views;
    @Column(columnDefinition = "TEXT")
    private String content;

}
