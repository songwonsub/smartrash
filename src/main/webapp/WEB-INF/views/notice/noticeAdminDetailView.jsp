<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first</title>
</head>
<body>
<!--  절대경로로 대상 파일의 위치를 지정한 경우 -->
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<hr>
<h2 align="center">[${ notice.noticeno }]번 공지 상세보기 (관리자용)</h2>
<br>
<table align="center" width="500" border="1" cellspacing="0" cellpadding="5">
	<tr><th>제목</th><td>${ notice.noticetitle }</td></tr>
	<tr><th>작성자</th><td>${ notice.noticewriter }</td></tr>
	<tr><th>날짜</th><td>${ notice.noticedate }</td></tr>
	<tr>
		<th>첨부파일</th>
		<td>
			<c:if test="${ !empty notice.original_filepath }">  <!-- 첨부파일이 있다면 다운로드 요청 설정함 -->
				<c:url var="nfd" value="/nfdown.do">
					<c:param name="ofile" value="${ notice.original_filepath }" />
					<c:param name="rfile" value="${ notice.rename_filepath }" />
				</c:url>
				<a href="${ nfd }">${ notice.original_filepath }</a>
			</c:if>
		</td>
	</tr>
	<tr><th>내용</th><td>${ notice.noticecontent }</td></tr>
	<tr>
		<td colspan="2">
			<button onclick="javascript:history.go(-1);">목록</button> &nbsp;
			<!-- 수정페이지로 이동 버튼 -->
			<c:url var="nup" value="/upmove.do">
				<c:param name="noticeno" value="${ notice.noticeno }" />
			</c:url>
			<button onclick="javascript:location.href='${ nup }';">글수정</button>
			
			<!-- 삭제하기 버튼 -->
			<c:url var="ndel" value="/ndel.do">
				<c:param name="noticeno" value="${ notice.noticeno }" />
				<c:if test="${ !empty notice.original_filepath }">
					<c:param name="rfile" value="${ notice.rename_filepath }" />
				</c:if>
			</c:url>
			<button onclick="javascript:location.href='${ ndel }';">글삭제</button>
		</td>
	</tr>
</table>
<br>
<hr>
<c:import url="../common/footer.jsp" />
</body>
</html>