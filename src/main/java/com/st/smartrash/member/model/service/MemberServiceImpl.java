package com.st.smartrash.member.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.smartrash.common.SearchDate;
import com.st.smartrash.member.model.dao.MemberDao;
import com.st.smartrash.member.model.vo.Member;

@Service("memberService") //servlet=context.xml에 자동등록
public class MemberServiceImpl implements MemberService{
   @Autowired //자동 의존성주입 처리됨(자동 객체 생성됨)
   private MemberDao memberDao;
   
   @Override
   public Member selectLogin(Member member) {
      return memberDao.selectLogin(member);
   }

   @Override
   public int insertMember(Member member) {
      return memberDao.insertMember(member);
   }

   @Override
   public int selectDupCheckId(String userid) {
      return memberDao.selectDupCheckId(userid);
   }

   @Override
   public int updateMember(Member member) {
      return memberDao.updateMember(member);
   }

   @Override
   public int deleteMember(String userid) {
      return memberDao.deleteMember(userid);
   }

   @Override
   public ArrayList<Member> selectList() {
      return memberDao.selectList();
   }

   @Override
   public Member selectMember(String userid) {
      return memberDao.selectMember(userid);
   }

   @Override
   public int updateLoginOk(Member member) {
      return memberDao.updateLoginOK(member);
   }

   @Override
   public ArrayList<Member> selectSearchUserid(String keyword) {
      return memberDao.selectSearchUserid(keyword);
   }

   @Override
   public ArrayList<Member> selectSearchGender(String keyword) {
      return memberDao.selectSearchGender(keyword);
   }

   @Override
   public ArrayList<Member> selectSearchAge(int age) {
      return memberDao.selectSearchAge(age);
   }

   @Override
   public ArrayList<Member> selectSearchEnrollDate(SearchDate searchDate) {
      return memberDao.selectSearchEnrollDate(searchDate);
   }

   @Override
   public ArrayList<Member> selectSearchLoginOk(String keyword) {
      return memberDao.selectSearchLoginOK(keyword);
   }

}