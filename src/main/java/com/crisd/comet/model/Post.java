package com.crisd.comet.model;

import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.model.enums.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private User author;
    @Column(nullable = true, length = 200)
    private String description;
    @Column(nullable = true)
    private String media;
    @Column
    private PostType postType;
    @Column
    private PostState postState;
    @Column
    private LocalDate datePosted;
    @OneToMany
    private Set<Post> reactions;
    @OneToMany
    private List<Comment> comments;

}
