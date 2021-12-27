package com.liferay.training.assignment.action;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.assignment.constants.FileUploadPortletKeys;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = { "javax.portlet.name=" + FileUploadPortletKeys.FILEUPLOAD,
		"mvc.command.name=/addFileExistingFolder" }, service = MVCActionCommand.class)

public class AddFileMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

		UploadPortletRequest req = PortalUtil.getUploadPortletRequest(actionRequest);

		_storeFiles(req, actionRequest);

	}

	private void _storeFiles(UploadPortletRequest req, ActionRequest actionRequest)
			throws PortalException, IOException {
		final FileItem[] arr = req.getMultipartParameterMap().get("attachedFile");
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long repositoryId = themeDisplay.getScopeGroupId();
		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest);

		String folderName = ParamUtil.getString(actionRequest, "folder");

		Folder folder = _dlAppService.getFolder(repositoryId, parentFolderId, folderName);

		List<FileEntry> fileEntries = _dlAppService.getFileEntries(repositoryId, folder.getFolderId());

		// checking for duplicate files
		String title;

		for (final FileItem fi : arr) {

			title = fi.getFileName();
			for (FileEntry existingFile : fileEntries) {
				if (existingFile.getFileName().equals(title)) {
					_dlAppService.deleteFileEntryByTitle(repositoryId, folder.getFolderId(), title);

				}
			}

		}

		for (final FileItem fi : arr) {

			_createFileEntry(repositoryId, folder.getFolderId(), fi.getFileName(), fi.getContentType(), fi.getSize(),
					fi.getInputStream(), serviceContext);

		}
	}

	private void _createFileEntry(final long repositoryId, final long folderId, final String fileName,
			final String mimeType, final long fileLength, final InputStream is, final ServiceContext serviceContext) {
		try {
			_dlAppService.addFileEntry(repositoryId, folderId, fileName, mimeType, fileName, null, null, is, fileLength,
					serviceContext);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Reference
	private DLAppService _dlAppService;
}
