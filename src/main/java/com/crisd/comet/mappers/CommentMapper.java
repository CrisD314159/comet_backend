package com.crisd.comet.mappers;

import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.model.Comment;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(source = "author", target = "authorId", qualifiedByName = "extractAuthorId")
    @Mapping(source = "author", target = "authorName", qualifiedByName = "extractAuthorName")
    @Mapping(source = "post", target = "postId", qualifiedByName = "extractPostId")
    List<GetPostCommentDTO> toGetPostCommentDTOList(List<Comment> comments);

    @Named("extractAuthorId")
    default UUID extractAuthorId(User user){
        return user.getId();
    }
    @Named("extractAuthorName")
    default String extractAuthorName(User user){
        return user.getName();
    }
    @Named("extractPostId")
    default UUID extractPostId(Post post){
        return post.getId();
    }
}
