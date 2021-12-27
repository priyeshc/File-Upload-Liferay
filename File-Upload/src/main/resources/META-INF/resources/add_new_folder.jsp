<%@ include file="init.jsp"%>
<portlet:actionURL name="/addFolder" var="addFolderURL"></portlet:actionURL>


<aui:form action="<%=addFolderURL%>" name="<portlet:namespace />fm">

	
	<aui:input name="name"  />
	<aui:input name="description"  />
	<aui:input name="attachedFile" type="file" multiple="true"></aui:input>
	

	<aui:button-row>
		<aui:button type="submit"></aui:button>
	</aui:button-row>
</aui:form>