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
package de.ovgu.featureide.ui.navigator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.ProfileManager;
import de.ovgu.featureide.fm.core.ProfileManager.Project.Profile;
import de.ovgu.featureide.fm.core.annotation.ColorPalette;
import de.ovgu.featureide.fm.core.configuration.FeatureNotFoundException;
import de.ovgu.featureide.fm.ui.PlugInProfileSerializer;

/**
 * TODO description
 * 
 * @author Christian Harnisch
 */
public class NavigatorContentImageProvider implements  ILabelProvider, IColorProvider{
	private Color color_grey = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	
	//ColumnLabelProvider t = null;
	private Profile getCurrentProfile(FeatureModel featureModel) {
		return ProfileManager.getProject(featureModel.xxxGetEclipseProjectPath(), PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
	}
	/**
	 * TODO description
	 * @param resource
	 * @return
	 */
	private FeatureModel getFeatureModel(IResource resource) {
		return CorePlugin.getFeatureProject(resource).getFeatureModel();
	}

	/**
	 * TODO description
	 * @param myfeature
	 * @return
	 */
	private Color changeColor(Feature myfeature) {
		Color mycolor = new Color(null, 255, 255, 255);
		int mycolorasint;
		if ( ProfileManager.toColorIndex(getCurrentProfile(myfeature.getFeatureModel()).getColor(myfeature.getName())) != -1) {
			
			mycolorasint = ProfileManager.toColorIndex(getCurrentProfile(myfeature.getFeatureModel()).getColor(myfeature.getName()));
			mycolor = new Color(null, ColorPalette.getRGB(mycolorasint, 0));
		}
		return mycolor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		try {
			if (element instanceof IFile) {
				IFile file = (IFile) element;
				FeatureModel fm = getFeatureModel(file);
				for (Feature myfeature : fm.getFeatures()) {
					if (file.getParent().toString().endsWith(myfeature.getName())) {
						//Device d = this.getBackground(file).getDevice();
						return 	new Image(null, "H:\\git\\FeatureIDE\\plugins\\de.ovgu.featureide.fm.ui\\icons\\plus.gif");
						
						}
				}

			}
			if (element instanceof IFolder) {
				IFolder folder = (IFolder) element;

				if (folder.getProject().isOpen()) {
					FeatureModel fm = getFeatureModel(folder);// CorePlugin.getFeatureProject(folder.getProject()).getFeatureModel();
					for (Feature myfeature : fm.getFeatures()) {
						if (folder.getFullPath().toString().endsWith(myfeature.getName())) {
							
						}
					}

				}
			}
			if (element instanceof IProject) {
				IContainer folder = (IContainer) element;
				if (folder.getProject().isOpen()) {
					FeatureModel fm = getFeatureModel(folder);//CorePlugin.getFeatureProject(folder.getProject()).getFeatureModel();
					for (Feature myfeature : fm.getFeatures()) {
//						if (myfeature.getColorList().getColor() != -1) {
							
//						if (ProfileManager.toColorIndex(getCurrentProfile(myfeature.getFeatureModel()).getColor(myfeature.getName())) != -1){
//							getCurrentProfile(myfeature.getFeatureModel()).getColor(myfeature.getName());
//							return color_grey;
//						}
					}
				}
			}

		} catch (FeatureNotFoundException e) {
				CorePlugin.getDefault().logError(e);
		}
		return null;
	}









	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		color_grey.dispose();
	}

	//neu
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	@Override
	public Color getForeground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	@Override
	public Color getBackground(Object element) {
//		getImage(element);
		try {
			if (element instanceof IFile) {
				IFile file = (IFile) element;
				FeatureModel fm = getFeatureModel(file);
				for (Feature myfeature : fm.getFeatures()) {
					if (file.getParent().toString().endsWith(myfeature.getName())) {
//						Device d = this.getBackground(file).getDevice();
						return changeColor(myfeature);//new Image(d, "H:\\git\\FeatureIDE\\plugins\\de.ovgu.featureide.fm.ui\\icons\\plus.gif");
						
						}
				}

			}
			if (element instanceof IFolder) {
				IFolder folder = (IFolder) element;

				if (folder.getProject().isOpen()) {
					FeatureModel fm = getFeatureModel(folder);// CorePlugin.getFeatureProject(folder.getProject()).getFeatureModel();
					for (Feature myfeature : fm.getFeatures()) {
						if (folder.getFullPath().toString().endsWith(myfeature.getName())) {
							 return changeColor(myfeature);
						}
					}

				}
			}
			if (element instanceof IProject) {
				IContainer folder = (IContainer) element;
				if (folder.getProject().isOpen()) {
					FeatureModel fm = getFeatureModel(folder);//CorePlugin.getFeatureProject(folder.getProject()).getFeatureModel();
					for (Feature myfeature : fm.getFeatures()) {
//						if (myfeature.getColorList().getColor() != -1) {
							
//						if (ProfileManager.toColorIndex(getCurrentProfile(myfeature.getFeatureModel()).getColor(myfeature.getName())) != -1){
//							getCurrentProfile(myfeature.getFeatureModel()).getColor(myfeature.getName());
//							return color_grey;
//						}
					}
				}
			}

		} catch (FeatureNotFoundException e) {
				CorePlugin.getDefault().logError(e);
		}
		return null;
	}
	
}
