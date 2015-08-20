/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2013  FeatureIDE team, University of Magdeburg, Germany
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
 * See http://www.fosd.de/featureide/ for further information.
 */
package de.ovgu.featureide.fm.ui.editors.featuremodel.actions.colors;

import static de.ovgu.featureide.fm.core.localization.StringTable.COLOR_SELECTED_FEATURE;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelWriter;
import de.ovgu.featureide.fm.ui.editors.FeatureDiagramEditor;
import de.ovgu.featureide.fm.ui.editors.featuremodel.editparts.FeatureEditPart;

public class ColorSelectedFeatureAction extends Action {

	FeatureEditPart editPart = null;
	public FeatureModel fm;
	protected ArrayList<Feature> featureList = new ArrayList<Feature>();
	final Shell shell = new Shell();
	static final String COLOR_FILE_NAME = ".color.xml";
	protected XmlFeatureModelWriter writer;
	protected String featuretext;
	private IProject featureProject;
	private FeatureModel featuremodel;
	private FeatureDiagramEditor viewer; 

	protected void updateSetColorActionText(String menuname) {
		super.setText(menuname);
	}

	/**
	 * @param viewer
	 * @param featuremodel
	 */
	public ColorSelectedFeatureAction(FeatureDiagramEditor viewer, IProject project) {
		featureProject = project;
		this.viewer = viewer;
		if (viewer instanceof GraphicalViewerImpl)
			((GraphicalViewerImpl) viewer).addSelectionChangedListener(listener);
		updateSetColorActionText(COLOR_SELECTED_FEATURE);

	}

	public ISelectionChangedListener listener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();

			updateFeatureList(selection);
		}
	};

	public void updateFeatureList(IStructuredSelection selection) {

		if (!selection.isEmpty()) {

			Object[] editPartArray = selection.toArray();

			for (int i = 0; i < selection.size(); i++) {

				Object editPart = editPartArray[i];
				if (editPart instanceof FeatureEditPart) {
					FeatureEditPart editP = (FeatureEditPart) editPart;
					Feature f = editP.getFeature();
					if (!featureList.contains(f) )

					{
						featureList.add(f);
					}
				}
			}

		}else{
			featureList.clear();
		}
		return;
	}

	//	public void saveColorsToFile() {
	//		featureProject.getFeatureModel().getColorschemeTable().saveColorsToFile(featureProject.getProject());
	//	}

	public IFile getColorFile(IProject project) {
		return project.getFile(COLOR_FILE_NAME);
	}

	@Override
	public void run() {

		ColorDia dialog = new ColorDia(shell, this.featureList, this.fm);
		int returnstat = dialog.open();

		if (Window.OK == returnstat) {
			//refresh FeatureDiagram
			viewer.refresh();
			
			
			//Save new values to file
			featureList.get(0).getFeatureModel().getColorschemeTable().saveColorsToFile(featureProject);

			
//			featureList.get(0).getFeatureModel().get
			
			
			
			
			
			
//			for (FeatureEditPart editP : featureEditPartList) {
//
//				int col = editP.getFeature().getColorList().getColor();
//				if (col != -1 && !featureEditPartList.isEmpty()) {
//
//					editP.getFeatureFigure().setBackgroundColor(
//							new Color(Display.getDefault(), ColorPalette.getRGB(editP.getFeature().getColorList().getColor(), 0)));
//
//				}
//			}
		}
//		featuremodel = featureEditPartList.get(0).getFeature().getFeatureModel();
//
//		if (featuremodel != null) {
//			featuremodel.getColorschemeTable().saveColorsToFile(featureProject.getProject());
//		}

		featureList.clear();
		//this.fm.getColorschemeTable().saveColorsToFile(project);
		//writer = new XmlFeatureModelWriter(featuremodel);
		//featuretext = writer.writeToString();

	}

}
