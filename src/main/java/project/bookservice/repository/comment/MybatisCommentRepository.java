package project.bookservice.repository.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.comment.Comment;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MybatisCommentRepository implements CommentRepository{

    private final CommentMapper commentMapper;

    @Override
    public Comment save(Comment comment) {
        log.info("commentMapper class={}", comment);
        commentMapper.save(comment);
        return comment;
    }

    @Override
    public List<Comment> findComments(String isbn) {
        return commentMapper.findComments(isbn);
    }

    @Override
    public void delete(Long id) {
        log.info("������ ��۹�ȣ ={}", id);
        commentMapper.delete(id);
    }

    @Override
    public void update(Comment comment) {
        log.info("������ ��۹�ȣ ={}",comment.getId());
        commentMapper.update(comment);
    }


}
