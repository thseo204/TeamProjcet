package project.bookservice.repository.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.comment.Comment;

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
}
