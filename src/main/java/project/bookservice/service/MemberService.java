package project.bookservice.service;

import project.bookservice.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member save(Member item);

//    void update(Long itemId, ItemUpdateDto updateParam);

//    Optional<Member> findById(Long id);

//    List<Member> findItems(ItemSearchCond itemSearch);
}
