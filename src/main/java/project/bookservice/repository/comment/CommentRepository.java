package project.bookservice.repository.comment;

import project.bookservice.domain.comment.Comment;

import java.util.List;


public interface CommentRepository {



    Comment save(Comment comment);
    List<Comment> findComments(String isbn);

    void delete(Long id);

    void update(Comment comment);

}
