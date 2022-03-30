package com.st.smartrash.notice.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.st.smartrash.member.model.vo.Member;
import com.st.smartrash.notice.model.service.NoticeService;
import com.st.smartrash.notice.model.vo.Notice;

@Controller
public class NoticeController {
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@Autowired
	private NoticeService noticeService;
	
	// 뷰 페이지 이동 처리용 -------------------
	
	// 새 공지글 등록 페이지로 이동 처리용
	@RequestMapping("movewrite.do")
	public String moveWritePage() {
		return "notice/noticeWriteForm";
	}
	
	// 공지글 수정페이지로 이동 처리용
	@RequestMapping("upmove.do")
	public String moveUpdatePage(@RequestParam("noticeno") int noticeno, Model model) {
		Notice notice = noticeService.selectNotice(noticeno);
		if(notice != null) {
			model.addAttribute("notice", notice);
			return "notice/noticeUpdateForm";
		} else {
			model.addAttribute("message", "[" + noticeno + "]번 글 수정페이지로 이동 실패");
			return "common/error";
		}
	}
	
	// ----------------------------------
	
	@RequestMapping(value="ntop3.do", method=RequestMethod.POST)
	@ResponseBody
	public String noticeNewTop3(HttpServletResponse response) throws UnsupportedEncodingException {
		// 최근 등록 공지글 3개 조회해 옴
		ArrayList<Notice> list = noticeService.selectNewTop3();
		
		// 전송용 json 객체 준비
		JSONObject sendJson = new JSONObject();
		// list 옮길 json 배열 준비
		JSONArray jarr = new JSONArray();
		
		// list를 jarr로 옮기기(복사)
		for(Notice notice : list) {
			// notice 필드값 저장용 json객체 생성
			JSONObject job = new JSONObject();
			
			job.put("noticeno", notice.getNoticeno());
			job.put("noticetitle", URLEncoder.encode(notice.getNoticetitle(), "utf-8"));
			// 한글 데이터는 반드시 인코딩해서 json에 담아야 한글이 깨지지 않음
			job.put("noticedate", notice.getNoticedate().toString());
			// 날짜는 반드시 .toString()으로 문자열로 바꿔서 json에 담아야 함
			
			jarr.add(job);  // job을 jarr에 저장
		}
		
		// 전송용 객체에 jarr을 담음
		sendJson.put("list", jarr);
		
		return sendJson.toJSONString();  // json을 json string 형으로 바꿔서 전송함
	}
	
	// 공지사항 전체 글 목록 조회용
	@RequestMapping("nlist.do")
	public String noticeList(Model model) {
		ArrayList<Notice> list = noticeService.selectAll();
		
		if(list.size() > 0) {
			model.addAttribute("list", list);
			return "notice/noticeListView";
		} else {
			model.addAttribute("message", "등록된 공지사항 정보가 없습니다.");
			return "common/error";
		}
	}
	
	// 공지글 상세보기 요청 처리용
	@RequestMapping("ndetail.do")
	public String noticeDetailMethod(@RequestParam("noticeno") int noticeno, Model model, HttpSession session) {
		Notice notice = noticeService.selectNotice(noticeno);
		
		if(notice != null) {
			model.addAttribute("notice", notice);
			
			Member loginMember = (Member)session.getAttribute("loginMember");
			if(loginMember != null && loginMember.getAdmin() == 'Y') {
				// 관리자가 상세보기 요청했을 때
				return "notice/noticeDetailView";
			} else {
				// 관리자가 아닌 클라이언트가 상세보기 요청했을 때
				return "notice/noticeDetailView";
			}
		} else {
			model.addAttribute("message", noticeno + "번 공지글 상세보기 실패!");
			return "common/error";
		}
	}
	
	// 첨부파일 다운로드 요청 처리용
	@RequestMapping("nfdown.do")
	public ModelAndView fileDownMethod(HttpServletRequest request, @RequestParam("ofile") String originFileName, @RequestParam("rfile") String renameFileName, ModelAndView mv) {
		// 공지사항 첨부파일 저장 폴더 경로 지정
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		// 저장 폴더에서 읽을 파일에 대해 경로 추가하면서 File 객체 생성
		File renameFile = new File(savePath + "\\" + renameFileName);
		// 다운을 위해 내보내는 파일 객체 생성
		File originFile = new File(originFileName);
		
		mv.setViewName("filedown");  // 등록된 파일다운로드 처리용 뷰 클래스 id 명
		mv.addObject("renameFile", renameFile);  // 전달할 파일객체 Model에 저장
		mv.addObject("originFile", originFile);
		
		return mv;
	}
	
	// 파일업로드 기능이 있는 공지글 등록 요청 처리용
	@RequestMapping(value="ninsert.do", method=RequestMethod.POST)
	public String noticeInsertMethod(Notice notice, HttpServletRequest request, Model model, @RequestParam(name="upfile", required=false) MultipartFile mfile) {
		// 업로드된 파일 저장 폴더 지정
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		
		// 첨부파일이 있을때만 업로드된 파일을 지정 폴더로 옮기기
		if(!mfile.isEmpty()) {
			String fileName = mfile.getOriginalFilename();
			// 이름바꾸기 처리 : 년월일시분초.확장자
			if(fileName != null && fileName.length() > 0) {
				// 바꿀 파일명에 대한 문자열 만들기
				// 공지글 등록 요청시점의 날짜정보를 이용함
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 변경할 파일이름 만들기
				String renameFileName = sdf.format(new java.sql.Date(System.currentTimeMillis()));
				// 원본 파일의 확장자를 추출해서, 변경 파일명에 붙여줌
				renameFileName += "." + fileName.substring(fileName.lastIndexOf(".") + 1);
				
				// 파일 객체 만들기
				File originFile = new File(savePath + "\\" + fileName);
				File renameFile = new File(savePath + "\\" + renameFileName);
				
				// 업로드 파일 저장시키고, 바로 이름바꾸기 실행함
				try {
					mfile.transferTo(renameFile);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("message", "전송파일 저장 실패");
					return "common/error";
				}
				
				notice.setOriginal_filepath(fileName);
				notice.setRename_filepath(renameFileName);
			}
		}  // 첨부파일이 있을때만
		
		if(noticeService.insertNotice(notice) > 0) {  // 새 공지글 등록 성공 시
			return "redirect:nlist.do";
		} else {
			model.addAttribute("message", "새 공지글 등록 실패");
			return "common/error";
		}
	}
	
	// 공지글 삭제 요청 처리용
	@RequestMapping("ndel.do")
	public String noticeDeleteMethod(@RequestParam("noticeno") int noticeno, @RequestParam(name="rfile", required=false) String renameFileName, HttpServletRequest request, Model model) {
		if(noticeService.deleteNotice(noticeno) > 0) {
			// 첨부된 파일이 있는 글일때는, 저장 폴더에 있는 파일도 삭제함
			if(renameFileName != null) {
				new File(request.getSession().getServletContext().getRealPath("resources/notice_upfiles") + "\\" + renameFileName).delete();
			}
			return "redirect:nlist.do";
		} else {
			model.addAttribute("message", "[" + noticeno + "]번 공지 삭제 실패");
			return "common/error";
		}
	}
	
	// 공지글 수정 요청 처리용
	@RequestMapping(value="nupdate.do", method=RequestMethod.POST)
	public String noticeUpdateMethod(Notice notice, HttpServletRequest request, Model model, @RequestParam(name="delFlag", required=false) String delFlag, @RequestParam(name="upfile", required=false) MultipartFile mfile) {
		// 업로드된 파일 저장 폴더 지정하기
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		
		// 첨부파일 수정 처리
		// 원래 첨부파일이 있는데, 삭제를 선택한 경우
		if(notice.getOriginal_filepath() != null && delFlag != null && delFlag.equals("yes")) {
			// 저장 폴더에서 해당 파일을 삭제함
			new File(savePath + "\\" + notice.getRename_filepath()).delete();
			// notice의 파일정보도 제거함
			notice.setOriginal_filepath(null);
			notice.setRename_filepath(null);
		}
		// 새로운  첨부파일이 있을 때
		if(!mfile.isEmpty()) {
			// 저장폴더의 이전 파일을 삭제함
			if(notice.getOriginal_filepath() != null) {
				// 저장 폴더에서 해당 파일을 삭제함
				new File(savePath + "\\" + notice.getRename_filepath()).delete();
				// notice의 파일정보도 제거함
				notice.setOriginal_filepath(null);
				notice.setRename_filepath(null);
			}
			
			// 이전 첨부파일이 없는 경우
			String fileName = mfile.getOriginalFilename();
			// 이름바꾸기 처리 : 년월일시분초.확장자
			if(fileName != null && fileName.length() > 0) {
				// 바꿀 파일명에 대한 문자열 만들기
				// 공지글 등록 요청시점의 날짜정보를 이용함
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 변경할 파일이름 만들기
				String renameFileName = sdf.format(new java.sql.Date(System.currentTimeMillis()));
				// 원본 파일의 확장자를 추출해서, 변경 파일명에 붙여줌
				renameFileName += "." + fileName.substring(fileName.lastIndexOf(".") + 1);
				
				// 파일 객체 만들기
				File originFile = new File(savePath + "\\" + fileName);
				File renameFile = new File(savePath + "\\" + renameFileName);
				
				// 업로드 파일 저장시키고, 바로 이름바꾸기 실행함
				try {
					mfile.transferTo(renameFile);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("message", "전송파일 저장 실패");
					return "common/error";
				}
				
				notice.setOriginal_filepath(fileName);
				notice.setRename_filepath(renameFileName);
			}  // 이름바꾸기해서 저장 처리
		}  // 새로 첨부된 파일이 있다면
		
		// 서비스 메소드 실행시키고 결과 받아서 성공 | 실패 페이지 내보내기
		if(noticeService.updateNotice(notice) > 0) {
			return "redirect:nlist.do";
		} else {
			model.addAttribute("message", "[" + notice.getNoticeno() + "]번 공지 수정 실패");
			return "common/error";
		}
	}
}
