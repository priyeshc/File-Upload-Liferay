<%@ include file="init.jsp"%>

<portlet:actionURL name="/addFileExistingFolder" var="addFileExistingFolderURL"></portlet:actionURL>

<aui:form action="<%=addFileExistingFolderURL%>" name="<portlet:namespace />fm" enctype="multipart/form-data">

	
	<aui:input name="folder"  />
	<aui:input name="attachedFile" type="file" multiple="true"></aui:input>
	
	

	<aui:button-row>
		<aui:button type="submit"></aui:button>
	</aui:button-row>
</aui:form>