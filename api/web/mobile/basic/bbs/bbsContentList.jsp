<%@ page language="java"  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
	<head>
		<%@include file="/mobile/basic/common/header.jsp"%>
		<%--<link rel="stylesheet" type="text/css" href="/design/mobile/css/bbs_blue.css" />--%>
		<script type="text/javascript">			
			function moveto_page(page) {
				var url="/mobile/bbs/bbsContentList.do";
				var paramArray = [];
				paramArray.push({name:"bbsId", value:"${bbsContentInfo.bbsId}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode("${keyWord}")});
				paramArray.push({name:"page", value:page});
				executeUrl(url, paramArray);
			}

			function search() {
				var url="/mobile/bbs/bbsContentList.do";
				var paramArray = [];
				var f = document.searchForm;
				var keyWord = f.inputKeyWord.value;
				if (keyWord == "") {
		            alert('<tctl:msg key="alert.search.nostr"/>');
		            return;
		        }
				paramArray.push({name:"bbsId", value:"${bbsContentInfo.bbsId}"});
				paramArray.push({name:"keyWord", value:doubleUrlEncode(keyWord)});
				executeUrl(url, paramArray);
			}
			function viewContent(bbsId,contentId,parentId,orderNo) {
				
				var f = document.listForm;
				f.bbsId.value = bbsId;
				f.contentId.value = contentId;
				f.parentId.value = parentId;
				f.orderNo.value = orderNo;

				
			  	f.action = "/mobile/bbs/bbsContentView.do";
			    f.method = "post";
				f.submit();		
			}
			jQuery().ready(function(){
				var writeAuth = '${bbsContentInfo.writeAuth}';
				var isCreator = '${bbsContentInfo.bbsCreator}';
				var bbsType = '${bbsContentInfo.bbsType}';
				var isAdmin = '${bbsContentInfo.bbsAdmin}';
				var isSecret = '${bbsContentInfo.bbsType}';
				
				//alert(writeAuth+":"+isCreator+":"+bbsType+":"+isAdmin);
				jQuery("#wirteBbsBtnT").hide();
				jQuery("#wirteBbsBtnB").hide();
				if (isAdmin == 'true' && writeAuth == "ADMIN") {
				   jQuery("#wirteBbsBtnT").show();
				   jQuery("#wirteBbsBtnB").show();
				}else if (writeAuth == "LOGIN" || writeAuth == "ALL") {
				   jQuery("#wirteBbsBtnT").show();
				   jQuery("#wirteBbsBtnB").show();
				}	
				
				if(isSecret =="secret"){
					jQuery("#searchForm").hide();
				}
			});
			
		</script>
	</head>
	<body>
		<div class="wrapper">		
			<%@include file="bbsTop.jsp"%>
			<%@include file="/mobile/basic/bbs/bbs_body_top.jsp"%>
			<div class="hh">
				<h2>
					<a href="javascript:viewSelectMenu(this, 'bbs');" class="btn_dr">
						${bbsContentInfo.bbsName}
					</a>
				</h2>
			</div>
			<div class="container">	
				<form id="listForm" name="listForm">
					<input type="hidden" id="bbsType" name="bbsType" value="${bbsContentInfo.bbsType}"/>
					<input type="hidden" id="bbsId" name="bbsId"/>
					<input type="hidden" id="contentId" name="contentId"/>
					<input type="hidden" id="parentId" name="parentId"/>	
					<input type="hidden" id="orderNo" name="orderNo"/>
				</form>			
				<div class="title_box">
					<div class="btn_l"><a class="btn2" href="/mobile/bbs/bbsList.do"><span><tctl:msg key="bbs.mobile.001" bundle="bbs"/></span></a></div>
					<div class="btn_r"><a class="btn2" href="/mobile/bbs/bbsContentWrite.do?bbsId=${bbsContentInfo.bbsId}" id="wirteBbsBtnT"><span><tctl:msg key="bbs.write" bundle="bbs"/></span></a></div>
				</div>
				<ul class="normal_type board_list">
					<c:if test="${!empty bbsContentInfo.noticeContentList}">
					<c:forEach var="noticeContent" items="${bbsContentInfo.noticeContentList}">
					<li>
						<a href="/mobile/bbs/bbsContentView.do?bbsId=${bbsContentInfo.bbsId}&contentId=${noticeContent.contentId}&parentId=${noticeContent.parentId}&orderNo=${noticeContent.orderNo}">
							<span class="ic_notice"><tctl:msg key="bbs.notice" bundle="bbs"/></span>
							<span>${noticeContent.subject}</span>
							<span class="info ${noticeContent.attCnt > 0 ? 'file':''}">${noticeContent.creatorName} | ${fn:substring(noticeContent.createTime,0,4)}-${fn:substring(noticeContent.createTime,4,6)}-${fn:substring(noticeContent.createTime,6,8)} | <tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/> ${noticeContent.refCnt}</span>
						</a>
					</li>
					</c:forEach>
					</c:if>
					<c:if test="${empty bbsContentInfo.bbsContentList}">
					<li style="text-align:center;">
						<a href="javascript:;">
							<span><tctl:msg key="bbs.content.empty" bundle="bbs"/></span>
						</a>
					</li>
					</c:if>
					<c:if test="${!empty bbsContentInfo.bbsContentList}">
					<c:forEach var="bbsContent" items="${bbsContentInfo.bbsContentList}">
						<li class="<c:if test="${bbsContent.depth > 0}">reply depth${bbsContent.depth}</c:if>">
							<a href="javascript:viewContent('${bbsContentInfo.bbsId}','${bbsContent.contentId}','${bbsContent.parentId}','${bbsContent.orderNo}')">
								<c:if test="${fn:substring(bbsContent.createTime,0,8) eq today}"><span class="ic_newContent">new</span></c:if>
								<span>${bbsContent.subject}</span>
								<span class="info ${bbsContent.attCnt > 0 ? 'file':''}">${bbsContent.creatorName} | ${fn:substring(bbsContent.createTime,0,4)}-${fn:substring(bbsContent.createTime,4,6)}-${fn:substring(bbsContent.createTime,6,8)} | <tctl:msg key="bbs.content.list.refcnt" bundle="bbs"/> ${bbsContent.refCnt}</span>
							</a>
						</li>
					</c:forEach>
					</c:if>
				</ul>
				<%@include file="/mobile/basic/common/pageCounter.jsp"%>
				<div class="title_box">
					<div class="btn_l"><a class="btn2" href="/mobile/bbs/bbsList.do"><span><tctl:msg key="bbs.mobile.001" bundle="bbs"/></span></a></div>
					<div class="btn_r"><a class="btn2" href="/mobile/bbs/bbsContentWrite.do?bbsId=${bbsContentInfo.bbsId}" id="wirteBbsBtnB"><span><tctl:msg key="bbs.write" bundle="bbs"/></span></a></div>
				</div>
				<form id="searchForm" name="searchForm" onsubmit="return false;">
				<fieldset class="search_wrap">
					<div class="search_area">
						<div class="search_area_content">
							<input type="text" id="inputKeyWord" name="inputKeyWord" value="${keyWord}" class="ip_search"/>
						</div>
						<input type="button" onclick="javascript:search()" value='<tctl:msg key="comn.search" bundle="common"/>' class="btn_search"/>
					</div>
				</fieldset>
				</form>
			</div>

			<%@include file="/mobile/basic/common/footer.jsp"%>
		</div>
	</body>
</html>