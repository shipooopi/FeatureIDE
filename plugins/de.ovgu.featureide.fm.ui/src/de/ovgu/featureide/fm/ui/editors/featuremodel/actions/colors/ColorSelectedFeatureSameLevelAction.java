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

//import static de.ovgu.featureide.fm.core.localization.StringTable.COLOR_SELECTED_FEATURE_SAME_LEVEL;

import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import de.ovgu.featureide.fm.core.FeatureModel;

public class ColorSelectedFeatureSameLevelAction extends Action {

	protected String selectedFeature;

	protected void updateSetColorActionText(String featureName) {
		super.setText(featureName);
	}

	/**
	 * @param viewer
	 * @param featuremodel
	 */
	public ColorSelectedFeatureSameLevelAction(Object viewer, FeatureModel featuremodel) {

		if (viewer instanceof GraphicalViewerImpl)
			((GraphicalViewerImpl) viewer).addSelectionChangedListener(listener);
		//updateSetColorActionText(COLOR_SELECTED_FEATURE_SAME_LEVEL);
	}

	private ISelectionChangedListener listener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			//IStructuredSelection selection = (IStructuredSelection) event.getSelection();

		}

	};

	/* (non-Javadoc)
	 * @see de.ovgu.featureide.fm.ui.editors.featuremodel.actions.CreateConstraintAction#run()
	 */
	@Override
	public void run() {

	}

}
