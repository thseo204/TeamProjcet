package project.bookservice.service;


import project.bookservice.domain.comment.Comment;

import java.util.List;


public interface CommentService {
   Comment save(Comment comment);

   List<Comment> findComments(String isbn);
}
