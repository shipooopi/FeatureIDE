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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.ui.wizards.NewColorSchemeWizard;

/**
 * TODO description
 * 
 * @author Jonas Weigt
 */
public class RenameProfileSchemeAction extends Action{
	
	public RenameProfileSchemeAction(String text){
		super(text);
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_CLEAR));
	}
	//protected
	public boolean action(FeatureModel fm, String collName) {
		NewColorSchemeWizard wizard = new NewColorSchemeWizard(fm);

		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.create();
		if (dialog.open() == WizardDialog.OK && 
				fm.getColorschemeTable().getSelectedColorscheme() == fm.getColorschemeTable().size()) {
			return true;
		}
		return false;
	}
}
