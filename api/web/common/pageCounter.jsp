<c:if test="${!empty pm}">
	<c:if test="${!pm.firstWindow}">
		<a href="javascript:moveto_page('${pm.prevWindow}')" class="first"><img src="/design/default/image/btn/btn_tofirst.gif" align="absmiddle" border="0"></a>
	</c:if>
	
	<c:if test="${!pm.firstPage}">
		<a href="javascript:moveto_page('${pm.prevPage}')" class="prev"><img src="/design/default/image/btn/btn_toprev.gif" align="absmiddle" border="0"></a>
	</c:if>
	
	<c:forEach var="page" items="${pm.pages}">
		<c:if test="${page == pm.page}">
			<span class="choiseNum">${page}</span>
		</c:if>
		
		<c:if test="${page != pm.page}">
			<a href="javascript:moveto_page('${page}')" style="padding:1px 5px">${page}</a>
		</c:if>
	</c:forEach>
	
	<c:if test="${!pm.lastPage}">
		<a href="javascript:moveto_page('${pm.nextPage}')" class="next"><img src="/design/default/image/btn/btn_tonext.gif" align="absmiddle" border="0"></a>
	</c:if>
	
	<c:if test="${!pm.lastWindow}">
		<a href="javascript:moveto_page('${pm.nextWindow}')" class="end"><img src="/design/default/image/btn/btn_tolast.gif" align="absmiddle" border="0"></a>
	</c:if>
</c:if>