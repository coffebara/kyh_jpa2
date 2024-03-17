package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
    // 업데이트 메서드 실행 순서.
    // 1. 메서드가 실행될 때 스프링 AOP에 의해 @Transactional 어노테이션을 확인 후 트랜잭션을 시작함.
    // 2. memberRepository.findOne(id)를 통해 영속성 컨텍스트에 해당 id의 Member가 이미 있는지 확인함.
    // 2-1. 영속성 컨텍스트에 존재한다면, 캐시에서 반환되고 Db 쿼리는 발생하지 않음.
    // 2-2. 영속성 컨텍스트에 존재하지 않는다면, DB를 조회하여 영속성 컨텍스트에 저장 후 반환함.
    // 3. 엔티티 변경
    // 3-1. member.setName(name)을 통해 엔티티 상태를 변경하면, 영속성 컨텍스트를 통해 Member가 관리되고 있기 때문에 JPA가 변경사항을 추적함
    // 4. 더티체킹
    // 4-1. 트랜잭션(메서드)가 종료될 때, 모든 영속 상태의 엔티티는 더티체킹을 함.
    // 4-2. 더티체킹이란, 영속 상태의 엔티티의 현재 상태와 스냅샷(최초 로딩 시점의 상태)를 비교하여 변경된 부분이 있는 지 확인하는 프로세스
    // 5. 더티체킹 결과 변경 사항이 있으면 이를 db에 반영하기 위한 update 쿼리가 지연 쓰기로 작성됨.
    // 6. 모든 비지니스 로직이 끝나면 트랜잭션이 커밋되고, 커밋되는 시점에 영속성 컨텍스트의 flush가 발생하여 쓰기 지연되었던 sql 업데이트 문이 db로 전송되어 실제 db에 반영됨.
    // 7. 트랜잭션이 종료되면, 영속성 컨텍스트와 생명주기가 같은 엔티티들은 준영속 상태가 됩니다. 즉, 이후의 변경 사항은 더 이상 영속성 컨텍스트에 의해 추적되거나 관리되지 않음.
    // 요약하자면, @Transactional 어노테이션이 붙은 update 메서드가 호출되는 순간부터
    //             JPA와 스프링에 의해 트랜잭션 관리 및 영속성 컨텍스트를 통한 엔티티의 생명주기, 더티 체킹, DB에 대한 업데이트 순서로 데이터가 반영됩니다.
}
