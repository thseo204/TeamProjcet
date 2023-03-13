package project.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookservice.domain.member.Member;
import project.bookservice.domain.repository.MemberRepository;
import project.bookservice.domain.repository.MemberRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

//    @Override
//    public void update(Long itemId, ItemUpdateDto updateParam) {
//        itemRepository.update(itemId, updateParam);
//    }
//
//    @Override
//    public Optional<Item> findById(Long id) {
//        return itemRepository.findById(id);
//    }
//
//    @Override
//    public List<Item> findItems(ItemSearchCond cond) {
//        return itemRepository.findAll(cond);
//    }

}
