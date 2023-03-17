package project.bookservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.comment.Comment;
import project.bookservice.repository.comment.CommentRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceV1 implements CommentService{

    private final CommentRepository commentRepository;
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findComments(String isbn) {
        return commentRepository.findComments(isbn);
    }
}
