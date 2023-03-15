package project.bookservice.repository.comment;

import org.apache.ibatis.annotations.Mapper;
import project.bookservice.domain.comment.Comment;

@Mapper
public interface CommentMapper {

    void save(Comment comment);
}
