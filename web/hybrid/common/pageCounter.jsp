<div class="paging">
	<c:if test="${!empty pm}">
		<c:if test="${!pm.firstWindow}">
			<a href="javascript:moveto_page('${pm.prevWindow}')" class="btn_first" title="<tctl:msg key="comn.page.first" bundle="common"/>"><tctl:msg key="comn.page.first" bundle="common"/></a>
		</c:if>
		
		<c:if test="${!pm.firstPage}">
			<a href="javascript:moveto_page('${pm.prevPage}')" class="btn_prev" title="<tctl:msg key="comn.page.pre" bundle="common"/>"><tctl:msg key="comn.page.pre" bundle="common"/></a>
		</c:if>
		
		
		<span>
			<c:if test="${((pageBase * page)-(pageBase - currentCount)) > 0}">
			${(pageBase * (page-1))+1} - ${((pageBase * page)-(pageBase - currentCount))}
			</c:if>
			<c:if test="${((pageBase * page)-(pageBase - currentCount)) <= 0}">
			1
			</c:if>
		</span>
		
		<c:if test="${!pm.lastPage}">
			<a href="javascript:moveto_page('${pm.nextPage}')" class="btn_next" title="<tctl:msg key="comn.page.next" bundle="common"/>"><tctl:msg key="comn.page.next" bundle="common"/></a>
		</c:if>
		
		<c:if test="${!pm.lastWindow}">
			<a href="javascript:moveto_page('${pm.nextWindow}')" class="btn_last" title="<tctl:msg key="comn.page.end" bundle="common"/>"><tctl:msg key="comn.page.end" bundle="common"/></a>
		</c:if>
	</c:if>
</div>