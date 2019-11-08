/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.framework.main.datatable;

import java.awt.Component;
import java.util.Collections;
import java.util.List;

import docking.ActionContext;
import docking.ComponentProvider;
import ghidra.framework.main.DomainFileOperationTracker;
import ghidra.framework.model.*;

public class ProjectDataActionContext extends ActionContext implements DomainFileContext {

	private List<DomainFolder> selectedFolders;
	private List<DomainFile> selectedFiles;
	private Component comp;
	private boolean isActiveProject;
	private ProjectData projectData;
	private boolean isTransient;
	private DomainFileOperationTracker fileTracker;

	public ProjectDataActionContext(ComponentProvider provider, ProjectData projectData,
			Object contextObject, List<DomainFolder> selectedFolders,
			List<DomainFile> selectedFiles, Component comp, boolean isActiveProject) {
		this(provider, projectData, null, contextObject, selectedFolders, selectedFiles, comp,
			isActiveProject);
	}

	public ProjectDataActionContext(ComponentProvider provider, ProjectData projectData,
			DomainFileOperationTracker fileTracker, Object contextObject,
			List<DomainFolder> selectedFolders, List<DomainFile> selectedFiles, Component comp,
			boolean isActiveProject) {
		super(provider, contextObject, comp);
		this.projectData = projectData;
		this.fileTracker = fileTracker;
		this.selectedFolders = selectedFolders;
		this.selectedFiles = selectedFiles;
		this.comp = comp;
		this.isActiveProject = isActiveProject;
	}

	@Override
	public boolean isBusy() {
		return fileTracker != null && fileTracker.isBusy();
	}

	@Override
	public void setBusy(boolean isBusy) {
		if (fileTracker != null) {
			fileTracker.setBusy(isBusy);
		}
	}

	@Override
	public List<DomainFile> getSelectedFiles() {
		if (selectedFiles == null) {
			return Collections.emptyList();
		}
		return selectedFiles;
	}

	public List<DomainFolder> getSelectedFolders() {
		if (selectedFolders == null) {
			return Collections.emptyList();
		}
		return selectedFolders;
	}

	public boolean hasExactlyOneFileOrFolder() {
		return (getFolderCount() + getFileCount()) == 1;
	}

	public int getFolderCount() {
		if (selectedFolders == null) {
			return 0;
		}
		return selectedFolders.size();
	}

	@Override
	public int getFileCount() {
		if (selectedFiles == null) {
			return 0;
		}
		return selectedFiles.size();
	}

	public ProjectData getProjectData() {
		return projectData;
	}

	public Component getComponent() {
		return comp;
	}

	@Override
	public boolean isInActiveProject() {
		return isActiveProject;
	}

	public boolean isReadOnlyProject() {
		if (projectData == null) {
			return false;
		}
		return !projectData.getRootFolder().isInWritableProject();
	}

	public boolean hasOneOrMoreFilesAndFolders() {
		return getFolderCount() + getFileCount() > 0;
	}

	public boolean containsRootFolder() {
		if (getFolderCount() == 0) {
			return false;
		}
		List<DomainFolder> folders = getSelectedFolders();
		for (DomainFolder domainFolder : folders) {
			if (domainFolder.getParent() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Transient data is that which will appear in a temporary project dialog
	 * @param isTransient true if transient
	 */
	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	/**
	 * Transient data is that which will appear in a temporary project dialog
	 * @return true if transient
	 */
	public boolean isTransient() {
		return isTransient;
	}
}
