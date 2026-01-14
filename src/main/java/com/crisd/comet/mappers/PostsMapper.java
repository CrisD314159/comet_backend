package com.crisd.comet.mappers;

import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.Reaction;
import com.crisd.comet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostsMapper {

    @Mapping(source = "author", target = "authorId", qualifiedByName = "extractAuthorId")
    @Mapping(source = "author", target = "authorName", qualifiedByName = "extractAuthorName")
    @Mapping(source = "reactions", target = "reactionsCount", qualifiedByName = "extractReactions")
    List<GetPostDTO> toGetPostsDtoList(List<Post> posts);

    @Mapping(source = "author", target = "authorId", qualifiedByName = "extractAuthorId")
    @Mapping(source = "author", target = "authorName", qualifiedByName = "extractAuthorName")
    @Mapping(source = "reactions", target = "reactionsCount", qualifiedByName = "extractReactions")
    GetPostDTO toGetPostsDto(Post post);


    @Named("extractAuthorId")
    default UUID extractAuthorId(User user){
        return user.getId();
    }
    @Named("extractAuthorName")
    default String extractAuthorName(User user){
        return user.getName();
    }
    @Named("extractReactions")
    default int extractReactions(Set<Reaction> reactions) {
        return reactions.size();
    }
}
