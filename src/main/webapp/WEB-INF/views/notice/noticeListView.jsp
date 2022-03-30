<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first</title>
<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/resources/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">

</script>
</head>
<body>
<!-- 상대경로로 대상 파일의 위치를 지정한 경우 -->
<c:import url="../common/menubar.jsp" />
<!-- jstl에서 절대경로 표기 : /WEB-INF/views/common/menubar.jsp -->
<hr>
<h1 align="center">공지 사항</h1>

<!-- 로그인한 회원이 관리자인 경우는 공지사항 등록 버튼이 보이게 함 -->
<center>
	<c:if test="${ loginMember.admin eq 'Y'.charAt(0) }">
		<!-- jsp에서 jsp로의 이동은 반드시 컨트롤러로 페이지  이동 요청을 해서 페이지가 바뀌게 함
			 스프링에서는 뷰리졸버를 거쳐서 뷰 페이지가 바뀌게 처리하도록 되어있음 -->
		<%-- <button onclick="javascript:location.href='${ pageContext.servletContext.contextPath }/WEB-INF/views/notice/noticewriteForm.jsp';">공지글 등록</button> --%>
		<button onclick="javascript:location.href='${ pageContext.servletContext.contextPath }/movewrite.do';">새 공지글 등록</button>
	</c:if>
</center>
<!-- 검색 항목 -->

<!-- 목록 출력 -->

<br>
<table align="center" width="500" border="1" cellspacing="0" cellpadding="1">
	<tr><th>번호</th><th>제목</th><th>작성자</th><th>첨부파일</th><th>날짜</th></tr>
	<c:forEach items="${ requestScope.list }" var="n">
		<tr align="center">
			<td>${ n.noticeno }</td>
			<!-- 공지 제목 클릭 시 상세보기로 넘어가게 처리 -->
			<c:url var="ndt" value="/ndetail.do">
				<c:param name="noticeno" value="${ n.noticeno }" />
			</c:url>
			<td><a href="${ ndt }">${ n.noticetitle }</a></td>
			<td>${ n.noticewriter }</td>
			<td>
				<c:if test="${ !empty n.original_filepath }">◎</c:if>
				<c:if test="${ empty n.original_filepath }">&nbsp;</c:if>
			</td>
			<td><fmt:formatDate value="${ n.noticedate }" pattern="yyyy-MM-dd" /></td>
		</tr>
	</c:forEach>
</table>

<hr>
<c:import url="../common/footer.jsp" />
</body>
</html>