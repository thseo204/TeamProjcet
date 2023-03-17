package project.bookservice.repository.comment;

import org.apache.ibatis.annotations.Mapper;
import project.bookservice.domain.comment.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    void save(Comment comment);

    List<Comment> findComments(String isbn);
}
