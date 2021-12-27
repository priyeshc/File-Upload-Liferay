package com.liferay.training.assignment.action;

import com.liferay.document.library.kernel.exception.DuplicateFolderNameException;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.assignment.constants.FileUploadPortletKeys;

import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = { "javax.portlet.name=" + FileUploadPortletKeys.FILEUPLOAD,
		"mvc.command.name=/addFolder" }, service = MVCActionCommand.class)

public class AddFolderMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

		System.out.println("I am reaching here...");

		_updateFolder(actionRequest, actionResponse);

	}

	private void _updateFolder(ActionRequest actionRequest, ActionResponse actionResponse)
			throws DuplicateFolderNameException, IOException {

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		// long folderId = ParamUtil.getLong(actionRequest, "folderId");

		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest);

			long repositoryId = themeDisplay.getScopeGroupId();
			long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
			Folder folder = _dlAppService.addFolder(repositoryId, parentFolderId, name, description, serviceContext);

			// now adding files to created folder
			UploadPortletRequest req = PortalUtil.getUploadPortletRequest(actionRequest);
			final FileItem[] arr = req.getMultipartParameterMap().get("attachedFile");
			for (final FileItem fi : arr) {
				createFileEntry(repositoryId, folder.getFolderId(), fi.getFileName(), fi.getContentType(), fi.getSize(),
						fi.getInputStream(), serviceContext);

			}

			SessionMessages.add(actionRequest, "folderAdded");

			// List<Folder> folders = _dlAppService.getFolders(repositoryId,
			// parentFolderId);

		}

		catch (DuplicateFolderNameException e) {
			System.out.println("Duplicate folder. Illegal operation");
			SessionErrors.add(actionRequest, "duplicateFolder");
			// e.printStackTrace();
		}

		catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createFileEntry(final long repositoryId, final long folderId, final String fileName,
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
