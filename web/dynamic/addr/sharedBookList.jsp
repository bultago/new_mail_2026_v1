<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>
<%@ taglib prefix="c"  uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fn"  uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="tctl"  uri="/terrace-tag.tld"%>

<c:forEach var="book" items="${bookList}" varStatus="loop">
<li class='closed'>
	<div class="folder">
	<a id="book_${book.addrbookSeq}" href="javascript:;" onclick="viewSharedGroupMember('${book.addrbookName}',${book.addrbookSeq}, 0);">${book.addrbookName}</a>
	</div>
	<ul id="${book.addrbookSeq}">
		<a href="javascript:;" onclick="viewSharedGroupMember('${book.addrbookName}',${book.addrbookSeq}, 0);" class="myfolder_plus"><tctl:msg key="addr.tree.all.label" bundle="addr"/></a>
		<li class='closed'>
			<div class="tree-toolbar">
				<a onclick='leftMenuControl.addSharedGroup(${book.addrbookSeq});' href='javascript:;'><tctl:msg key="addr.tree.add.label" bundle="addr"/></a> 
				<a onclick='leftMenuControl.manageSharedGroup(${book.addrbookSeq});' href='javascript:;'><tctl:msg key="addr.tree.manage.label" bundle="addr"/></a>
			</div>
			<div id="sharedBookGroup" class="folder" style="float:left;"><a onclick='leftMenuControl.loadSharedAddressGroup(${book.addrbookSeq});' href='javascript:;'><tctl:msg key="addr.tree.groups.label" bundle="addr"/></a></div>
			<div class="cls"></div>			
			<ul id='sharedGroups'>
			</ul>
		</li>
	</ul>
</li>
</c:forEach>

<script type="text/javascript">	
jQuery('#sharedTreeviewer').treeview();
</script>