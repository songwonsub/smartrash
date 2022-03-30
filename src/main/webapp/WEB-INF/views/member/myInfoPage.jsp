<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first</title>
<script type="text/javascript">
	function validate() {
		var pwd1 = document.getElementById("userpwd").value;
		var pwd2 = document.getElementById("userpwd2").value;
		
		if(pwd1 != pwd2) {
			alert("암호와 암호 확인이 일치하지 않습니다.\n다시 입력하세요.");
			document.getElementById("userpwd").select();
		}
	}
</script>
</head>
<body>
<c:import url="/WEB-INF/views/common/menubar.jsp" />
<hr>
<h1 align="center">내 정보 보기</h1>
<br>
	<form method="post" action="mupdate.do">
		<input type="hidden" name="origin_userpwd" value="${ member.userpwd }" />
		<table id="outer" align="center" width="500" cellspacing="5" cellpadding="5">
			<tr>
				<th colspan="2">회원 정보를 입력해 주세요. (* 표시는 필수입력 항목입니다.)</th>
			</tr>
			<tr>
				<th width="120"> 이름</th>
				<td><input type="text" name="username" value="${ requestScope.member.username }" readonly /></td>
			</tr>
			<tr>
				<th width="120"> 아이디</th>
				<td>
					<input type="text" name="userid" value="${ member.userid }" readonly />
				</td>
			</tr>
			<tr>
				<th width="120"> 암호</th>
				<td><input type="password" name="userpwd" id="userpwd" value="" /></td>
			</tr>
			<tr>
				<th width="120"> 암호확인</th>
				<td><input type="password" id="userpwd2" onblur="validate();" /></td>
				<!-- onblur : focus가 사라질 때 작동되는 이벤트 설정 -->
			</tr>
			<tr>
				<th width="120"> 성별</th>
				<td>
					<c:if test="${ member.gender eq 'M'.charAt(0) }">
						<input type="radio" name="gender" value="M" checked />남자
						&nbsp; <input type="radio" name="gender" value="F" />여자
					</c:if>
					<c:if test="${ member.gender eq 'F'.charAt(0) }">
						<input type="radio" name="gender" value="M" />남자
						&nbsp; <input type="radio" name="gender" value="F" checked />여자
					</c:if>
				</td>
			</tr>
			<tr>
				<th width="120"> 나이</th>
				<td><input type="number" name="age" min="19" max="100" value="${ member.age }" /></td>
			</tr>
			<tr>
				<th width="120"> 전화번호</th>
				<td><input type="tel" name="phone" value="${ member.phone }" /></td>
			</tr>
			<tr>
				<th width="120"> 이메일</th>
				<td><input type="email" name="email" value="${ member.email }" /></td>
			</tr>
			<tr>
				<th width="120"> 취미</th>
				<td>
					<!-- 취미 문자열을 각각의 문자열로 분리하면서, 취미에 적용할 변수 9개를 만듦
						 hobby : game,sport,movie => 문자열 분리 ["game", "sport", "movie"]
					-->
					<c:forTokens items="${ member.hobby }" delims="," var="hb">
						<c:if test="${ hb eq 'game' }">
							<c:set var="checked1" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'reading' }">
							<c:set var="checked2" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'climb' }">
							<c:set var="checked3" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'sport' }">
							<c:set var="checked4" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'music' }">
							<c:set var="checked5" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'movie' }">
							<c:set var="checked6" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'travel' }">
							<c:set var="checked7" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'cook' }">
							<c:set var="checked8" value="checked" />
						</c:if>
						<c:if test="${ hb eq 'etc' }">
							<c:set var="checked9" value="checked" />
						</c:if>
					</c:forTokens>
					<table width="350">
						<tr>
							<td><input type="checkbox" name="hobby" value="game" ${ checked1 } /> 게임</td>
							<td><input type="checkbox" name="hobby" value="reading" ${ checked2 } /> 독서</td>
							<td><input type="checkbox" name="hobby" value="climb" ${ checked3 } /> 등산</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="hobby" value="sport" ${ checked4 } /> 운동</td>
							<td><input type="checkbox" name="hobby" value="music" ${ checked5 } /> 음악듣기</td>
							<td><input type="checkbox" name="hobby" value="movie" ${ checked6 } /> 영화보기</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="hobby" value="travel" ${ checked7 } /> 게임</td>
							<td><input type="checkbox" name="hobby" value="cook" ${ checked8 } /> 게임</td>
							<td><input type="checkbox" name="hobby" value="etc" ${ checked9 }/> 기타</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<th width="120"> 추가 사항</th>
				<td><textarea name="etc" rows="5" cols="50">${ member.etc }</textarea></td>
			</tr>
			<tr>
				<th colspan="2">
					<input type="submit" value="수정하기" /> &nbsp;
					<input type="reset" value="수정취소" /> &nbsp;
					<a href="javascript:history.go(-1);">이전페이지로 이동</a> &nbsp;
					<a href="main.do">시작페이지로 이동</a> &nbsp;
					<!-- 탈퇴하기 요청 처리용 -->
					<c:url var="mdelete" value="mdel.do">
						<c:param name="userid" value="${ member.userid }" />
					</c:url>
					<a href="${ mdelete }">탈퇴하기</a> &nbsp;
				</th>
			</tr>
		</table>
	</form>
<hr>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>