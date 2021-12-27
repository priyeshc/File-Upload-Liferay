<%@ include file="init.jsp"%>

<h3>Where do you want to add file?</h3>

<portlet:renderURL var="addToExistingFolderURL">
	<portlet:param name="mvcPath" value="/add_existing_folder.jsp"></portlet:param>
</portlet:renderURL>

<portlet:renderURL var="addToNewFolderURL">
	<portlet:param name="mvcPath" value="/add_new_folder.jsp"></portlet:param>
</portlet:renderURL>

<aui:button-row>
	<aui:button onClick="<%=addToExistingFolderURL.toString()%>"
		value="Add to existing Folder"></aui:button>
</aui:button-row>

<aui:button-row>
	<aui:button onClick="<%=addToNewFolderURL.toString()%>"
		value="Create new Folder"></aui:button>
</aui:button-row>