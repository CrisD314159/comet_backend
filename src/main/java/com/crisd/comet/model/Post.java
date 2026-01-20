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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> media;
    @Column
    private PostType postType;
    @Column
    private PostState postState;
    @Column
    private LocalDate datePosted;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactions;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

}
