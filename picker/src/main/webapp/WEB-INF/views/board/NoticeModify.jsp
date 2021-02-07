<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<script type="text/javascript" src="resources/js/summernote/summernote-lite.min.js"></script>
<script type="text/javascript" src="resources/js/summernote/summernote-ko-KR.min.js"></script>
<link type="text/css" rel="stylesheet" href="resources/css/summernote-lite-dark.min.css">
<link type="text/css" rel="stylesheet" href="resources/css/Board.css">
</head>
<body>
	<section class="wrap">
		<form id="writeForm" class="writeWrap" method="post">
			<h3>공지 수정</h3>
			<input type="text" name="n_title" id="iptTitle" value="${notice.n_title}" placeholder="제목을 입력해주세요">
			<input type="hidden" name="n_num" value="${notice.n_num}">
			<div>
				<textarea id="contentEditor" name="n_content">${notice.n_content}</textarea>
			</div>
			<div class="rightBtnDiv">
				<input type="reset" value="취소" class="btnWhite"><input type="button" value="작성" id="noticeModifyBtn" class="btnBlack">
			</div>
		</form>
	</section>
	<script type="text/javascript" src="resources/js/BoardWrite.js"></script>
</body>
