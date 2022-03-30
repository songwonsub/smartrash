package com.st.smartrash.notice.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.st.smartrash.common.SearchDate;
import com.st.smartrash.notice.model.vo.Notice;

@Repository("noticeDao")
public class NoticeDao {
	// 스프링-마이바티스 연동 객체 사용 : root-context.xml에 선언되어 있음
	@Autowired
	private SqlSessionTemplate session;

	public ArrayList<Notice> selectList() {
		List<Notice> list = session.selectList("noticeMapper.selectAll");
		return (ArrayList<Notice>)list;
	}

	public Notice selectOne(int noticeno) {
		return session.selectOne("noticeMapper.selectNotice", noticeno);
	}

	public int insertNotice(Notice notice) {
		return session.insert("noticeMapper.insertNotice", notice);
	}

	public int updateNotice(Notice notice) {
		return session.update("noticeMapper.updateNotice", notice);
	}

	public int deleteNotice(int noticeno) {
		return session.delete("noticeMapper.deleteNotice", noticeno);
	}

	public ArrayList<Notice> selectNewTop3() {
		List<Notice> list = session.selectList("noticeMapper.selectNewTop3");
		return (ArrayList<Notice>)list;
	}

	public ArrayList<Notice> selectSearchTitle(String keyword) {
		List<Notice> list = session.selectList("noticeMapper.searchTitle", keyword);
		return (ArrayList<Notice>)list;
	}

	public ArrayList<Notice> selectSearchWriter(String keyword) {
		List<Notice> list = session.selectList("noticeMapper.searchWriter", keyword);
		return (ArrayList<Notice>)list;
	}

	public ArrayList<Notice> selectSearchDate(SearchDate date) {
		List<Notice> list = session.selectList("noticeMapper.searchDate", date);
		return (ArrayList<Notice>)list;
	}

}
