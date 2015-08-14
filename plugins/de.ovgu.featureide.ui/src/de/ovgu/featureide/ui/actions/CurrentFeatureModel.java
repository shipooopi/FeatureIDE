/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2015  FeatureIDE team, University of Magdeburg, Germany
 *
 * This file is part of FeatureIDE.
 * 
 * FeatureIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * FeatureIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatureIDE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://featureide.cs.ovgu.de/ for further information.
 */
package de.ovgu.featureide.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.FeatureModel;

/**
 * TODO description
 * 
 * @author Jonas Weigt
 */
public class CurrentFeatureModel {

//	Eclipse get current project selected
//	private static IProject getCurrentProject() {
//		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
//
//		ISelection selection = selectionService.getSelection();
//
//		IProject project = null;
//		if (selection instanceof IStructuredSelection) {
//			Object element = ((IStructuredSelection) selection).getFirstElement();
//
//			if (element instanceof IResource) {
//				project = ((IResource) element).getProject();
//			} else if (element instanceof PackageFragmentRootContainer) {
//				IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
//				project = jProject.getProject();
//			} else if (element instanceof IJavaElement) {
//				IJavaProject jProject = ((IJavaElement) element).getJavaProject();
//				project = jProject.getProject();
//			}
//		
//		}
//		return project;
//	}

//	public static IFeatureProject getCurrentFeatureProject() {
//		return CorePlugin.getFeatureProject(((IJavaElement)((Object)((IStructuredSelection)((ISelection)((ISelectionService)Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService()).getSelection())).getFirstElement())).getJavaProject().getProject());
//	}
	
//	public static FeatureModel getCurrentFeatureModel() {
//		IProject project = getCurrentProject();
//		IFeatureProject myproject = CorePlugin.getFeatureProject(project);
//		FeatureModel fm = myproject.getFeatureModel();

//		CorePlugin
//				.getFeatureProject((IProject) ((IStructuredSelection) Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService().getSelection())
//						.getFirstElement());
//
//		return fm;
//	}
//
//			public static IFeatureProject getCurrentFeatureProject(){
//				IProject project = getCurrentProject();
//				IFeatureProject myproject = CorePlugin.getFeatureProject(project);
//				return myproject;
//			}
//			
//	
}
