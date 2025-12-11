<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<form name="shareTreeForm">
<c:forEach var="book" items="${bookList}" varStatus="loop">
<li class='closed'>
	<div class="folder">
	<a href="javascript:;" onclick="viewSharedGroupMember('${book.addrbookName}',${book.addrbookSeq}, 0);">${book.addrbookName}</a>
	</div>
	<ul id="${book.addrbookSeq}">
		<a id="tree_${book.addrbookSeq}_0" href="javascript:;" onclick="viewSharedGroupMember('${book.addrbookName}',${book.addrbookSeq}, 0);" class="myfolder_plus padr"><label><tctl:msg key="addr.tree.all.label" bundle="addr"/></label></a>
		<li id="tree_${book.addrbookSeq}" class='closed'>
			<div id="sharedBookGroup" class="folder"><a onclick='leftMenuControl.loadSharedAddressGroup(${book.addrbookSeq});' href='javascript:;'><tctl:msg key="addr.tree.groups.label" bundle="addr"/></a></div>
			<ul id='sharedGroups'>
			</ul>
		</li>
	</ul>
</li>
</c:forEach>
</form>
<script type="text/javascript">	
jQuery('#sharedTreeviewer').treeview();
</script>