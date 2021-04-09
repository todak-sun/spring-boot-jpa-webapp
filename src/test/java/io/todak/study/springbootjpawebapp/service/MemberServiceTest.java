package io.todak.study.springbootjpawebapp.service;

import io.todak.study.springbootjpawebapp.domain.Member;
import io.todak.study.springbootjpawebapp.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원가입 테스트")
    @Test
    public void join_test() throws Exception {
        //given
        Member member = new Member();
        member.setName("yongjoo_sun");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @DisplayName("중복 회원 예외")
    @Test
    public void duplicate_exception() {
        //given
        Member member1 = new Member();
        member1.setName("syj");

        Member member2 = new Member();
        member2.setName("syj");


        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }

}