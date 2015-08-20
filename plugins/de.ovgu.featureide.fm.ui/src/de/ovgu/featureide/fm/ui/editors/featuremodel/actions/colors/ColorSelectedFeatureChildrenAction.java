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

import static de.ovgu.featureide.fm.core.localization.StringTable.COLOR_SELECTED_FEATURE_CHILDREN;

import java.awt.List;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.ui.editors.featuremodel.editparts.FeatureEditPart;

public class ColorSelectedFeatureChildrenAction extends Action {

	FeatureEditPart editPart = null;
	public FeatureModel fm;
	protected ArrayList<Feature> featureList = new ArrayList<Feature>();
	protected ArrayList<FeatureEditPart> featureEditPartList = new ArrayList<FeatureEditPart>();
	final Shell shell = new Shell();

	protected void updateSetColorActionText(String menuname) {
		super.setText(menuname);
	}

	/**
	 * @param viewer
	 * @param featuremodel
	 */
	public ColorSelectedFeatureChildrenAction(Object viewer, FeatureModel featuremodel) {
		if (viewer instanceof GraphicalViewerImpl)
			((GraphicalViewerImpl) viewer).addSelectionChangedListener(listener);
		updateSetColorActionText(COLOR_SELECTED_FEATURE_CHILDREN);

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
					if (!featureList.contains(f) && !featureEditPartList.contains(editP))

					{
						featureEditPartList.add(editP);
						featureList.add(f);
					}
				}
			}

		}
		return;
	}

//	public void addChildren() {
//
//		for (int i = 0; i < featureList.size(); i++) {
//			Feature f = featureList.get(i);
//
//			if (f.hasChildren()) {
//				LinkedList<Feature> childrenList = f.getChildren();
//				for (int j = 0; j < childrenList.size(); j++) {
//					Feature child = childrenList.get(j);
//
//					if (!featureList.contains(child)) {
//						featureList.add(child);
//
//					}
//					
//				}
//
//			}
//
//		}
//
//	}
// use for ColorSelectedFeatureChildrenAction	
//	public void addChildren() {
//
//		for (int i = 0; i < featureEditPartList.size(); i++) {
//			Feature f = featureList.get(i);
//			FeatureEditPart editP = featureEditPartList.get(i);
//
//			if (f.hasChildren()) {
//				LinkedList<Feature> childrenFeatureList = f.getChildren();
//				AbstractList <FeatureEditPart> childrenEditPartList =  (AbstractList<FeatureEditPart>) editP.getChildren();
//				for (int j = 0; j < childrenFeatureList.size(); j++) {
//					Feature childFeature = childrenFeatureList.get(j);
//					FeatureEditPart childeditP = childrenEditPartList.get(j);
//
//					if (!featureList.contains(childFeature)) {
//						featureList.add(childFeature);
//						
//
//					}
//					
//				}
//
//			}
//
//		}
//
//	}


	@Override
	public void run() {
		//addChildren();

		ColorDia dialog = new ColorDia(shell, this.featureList);
		int returnstat = dialog.open();

		if (Window.OK == returnstat) {
			for (FeatureEditPart editP : featureEditPartList) {

				int col = editP.getFeature().getColorList().getColor();
				if (col != -1 && !featureEditPartList.isEmpty()) {

					switch (col) {

					case 0:
						
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 255, 175, 177));
						break;//red
					case 1:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 255, 216, 166));
						break;//orange
					case 2:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 237, 255, 166));
						break;//yellow
					case 3:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 164, 228, 148));
						break;//darkgreen
					case 4:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 166, 255, 201));
						break;//lightgreen
					case 5:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 163, 251, 251));
						break;//cyan
					case 6:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 228, 228, 228));
						break;//light grey
					case 7:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 164, 148, 228));
						break;//blue
					case 8:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 240, 166, 255));
						break;//magenta
					case 9:
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 228, 148, 194));
						break;//pink
					}
				}
			}
		}
		featureList.clear();
		featureEditPartList.clear();
	}

}
