package project.bookservice.domain.repository;

import project.bookservice.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

//    void update(Long itemId, ItemUpdateDto updateParam);

//    Optional<Member> findById(Long id);

//    List<Member> findAll(ItemSearchCond cond);

}
