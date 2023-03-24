package project.bookservice.service.comment;


import project.bookservice.domain.comment.Comment;

import java.util.List;


public interface CommentService {
   Comment save(Comment comment);

   List<Comment> findComments(String isbn);
}
