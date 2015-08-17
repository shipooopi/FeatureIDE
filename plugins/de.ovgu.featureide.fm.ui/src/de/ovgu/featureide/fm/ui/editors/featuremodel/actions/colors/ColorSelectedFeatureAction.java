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

import java.util.ArrayList;

import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.ui.editors.featuremodel.editparts.FeatureEditPart;

public class ColorSelectedFeatureAction extends Action {

	public FeatureModel fm;
	private Viewer viewer;
	protected ArrayList<Feature> flist = new ArrayList<Feature>();
	final Shell shell = new Shell();

	//	protected void updateSetColorActionText(String menuname) {
	//		super.setText(menuname);
	//	}

	/**
	 * @param viewer
	 * @param featuremodel
	 */
	public ColorSelectedFeatureAction(Object viewer, FeatureModel featuremodel) {
		if (viewer instanceof GraphicalViewerImpl)
			((GraphicalViewerImpl) viewer).addSelectionChangedListener(listener);

	}
	
	

	public ISelectionChangedListener listener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();

			updateFeatureList(selection); 			
		}
	};

	public ArrayList<Feature> updateFeatureList(IStructuredSelection selection) {

//		flist.add(f);
//		System.out.println("featname" + f.getName());
//		System.out.println("listengröße" + flist.size());
		
		if (!selection.isEmpty()) {

			if (selection.size() == 1) {
				Object editPart = selection.getFirstElement();
				if (editPart instanceof FeatureEditPart) {
					FeatureEditPart editP = (FeatureEditPart) editPart;
					Feature f = editP.getFeature();
					flist.add(f);
					editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 0, 0, 45));

				}

			}

			else {
				Object[] editPartArray = selection.toArray();

				for (int i = 0; i < selection.size(); i++) {

					Object editPart = editPartArray[i];
					if (editPart instanceof FeatureEditPart) {
						FeatureEditPart editP = (FeatureEditPart) editPart;

						Feature f = editP.getFeature();

						flist.add(f);

						//f.getColorList().setColor(color);
						editP.getFeatureFigure().setBackgroundColor(new Color(Display.getDefault(), 10, 155, 45));
					}
				}

			}

			// Übergabe der flist 
			//return flist;
			//flist.clear();
		}
		return flist;
		
	}
	
	

	@Override
	public void run() {
		
		ColorDia dialog = new ColorDia(shell, this.flist);
		flist.clear();
		dialog.open();

	}

}
