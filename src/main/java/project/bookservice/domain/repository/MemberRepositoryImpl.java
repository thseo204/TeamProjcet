package project.bookservice.domain.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.bookservice.domain.member.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    // 대부분 매퍼에 위임하는 코드임
    private final MemberMapper memberMapper;

    @Override
    public Member save(Member member) {
        log.info("member info={}", member);

        memberMapper.save(member);
        return member;
    }

//    @Override
//    public void update(Long itemId, ItemUpdateDto updateParam) {
//        itemMapper.update(itemId, updateParam);
//    }

//    @Override
//    public Optional<Member> findById(Long id) {
//        return itemMapper.findById(id);
//    }

//    @Override
//    public List<Member> findAll(ItemSearchCond cond) {
//        return itemMapper.findAll(cond);
//    }
}
