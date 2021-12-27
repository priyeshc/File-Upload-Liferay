<%@ include file="/init.jsp" %>

<liferay-ui:success key="folderAdded" message="folder-added" />
<liferay-ui:error key="duplicateFolder" message="folder-added-failed-duplicate" />

<portlet:renderURL var="addFileURL">
	<portlet:param name="mvcPath" value="/add_file.jsp"></portlet:param>
</portlet:renderURL>

<aui:button-row>
	<aui:button onClick="<%=addFileURL.toString()%>"
		value="Upload File"></aui:button>
</aui:button-row>


