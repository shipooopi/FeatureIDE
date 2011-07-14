/* FeatureIDE - An IDE to support feature-oriented software development
 * Copyright (C) 2005-2010  FeatureIDE Team, University of Magdeburg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * See http://www.fosd.de/featureide/ for further information.
 */
package de.ovgu.featureide.fm.ui.editors.featuremodel.actions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.fm.ui.editors.featuremodel.GUIDefaults;
import de.ovgu.featureide.fm.ui.editors.featuremodel.operations.LegendHideOperation;

/**
 * shows/hides the legend when executed
 * 
 * @author Fabian Benduhn
 */
public class LegendAction extends Action implements GUIDefaults {

	private final FeatureModel featureModel;

	public LegendAction(GraphicalViewerImpl viewer, FeatureModel featureModel) {
		super();
		this.featureModel = featureModel;
		if (featureModel.hasLegend()) {
			this.setText("Hide Legend");
		} else {
			this.setText("Show Legend");
		}

	}

	@Override
	public void run() {

		LegendHideOperation op = new LegendHideOperation(featureModel);
		op.addContext((IUndoContext) featureModel.getUndoContext());

		try {
			PlatformUI.getWorkbench().getOperationSupport()
					.getOperationHistory().execute(op, null, null);
		} catch (ExecutionException e) {
			FMUIPlugin.getDefault().logError(e);

		}

	}

	public void refresh() {
		if (featureModel.hasLegend()) {
			this.setText("Hide Legend");
		} else {
			this.setText("Show Legend");
		}

	}
}
