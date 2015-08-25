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
package de.ovgu.featureide.fm.ui.editors.featuremodel.actions.colors;

import static de.ovgu.featureide.fm.core.localization.StringTable.BLUE;
import static de.ovgu.featureide.fm.core.localization.StringTable.CYAN;
import static de.ovgu.featureide.fm.core.localization.StringTable.DARKGREEN;
import static de.ovgu.featureide.fm.core.localization.StringTable.LIGHTGREEN;
import static de.ovgu.featureide.fm.core.localization.StringTable.LIGHTGREY;
import static de.ovgu.featureide.fm.core.localization.StringTable.MAGENTA;
import static de.ovgu.featureide.fm.core.localization.StringTable.ORANGE;
import static de.ovgu.featureide.fm.core.localization.StringTable.PINK;
import static de.ovgu.featureide.fm.core.localization.StringTable.RED;
import static de.ovgu.featureide.fm.core.localization.StringTable.YELLOW;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.ProfileManager;
import de.ovgu.featureide.fm.core.ProfileManager.Project.Profile;
import de.ovgu.featureide.fm.core.annotation.ColorPalette;
import de.ovgu.featureide.fm.ui.PlugInProfileSerializer;

public class ColorDia extends Dialog {

	/**
	 * @param parentShell
	 */

	Shell shell = new Shell();
	protected List<Feature> featurelist;
	protected ArrayList<Feature> featurelistbuffer = new ArrayList<Feature>();
	protected int colorID = -1;

	/**
	 * @param parentShell
	 * @param featurelist
	 */
	protected ColorDia(Shell parentShell, List<Feature> featurelist) {
		//		assert(featurelist != null);

		super(parentShell);
		if (featurelist == null)
			throw new NullPointerException();

		this.featurelist = featurelist;
		setShellStyle(SWT.DIALOG_TRIM | SWT.MIN | SWT.RESIZE);
	}

	protected void configureShell(Shell newShell) {
		newShell.setMinimumSize(new Point(500, 500));
		super.configureShell(newShell);
		newShell.setText("Color Dialog");
	}

	protected Point getInitialSize() {
		return new Point(500, 500);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		Label actionLabel = new Label(container, SWT.NONE);
		actionLabel.setLayoutData(gridData);
		actionLabel.setBackground(new Color(null, 255, 255, 255));
		actionLabel.setText("Choose action: ");

		final String[] actionDropDownItems = { "Selected", "Selected and direct Children", "Selected and all Children", "Selected and all Siblings" };
		Combo actionDropDownMenu = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		actionDropDownMenu.setLayoutData(gridData);
		actionDropDownMenu.setItems(actionDropDownItems);

		Label chooseColorLabel = new Label(container, SWT.NONE);
		chooseColorLabel.setLayoutData(gridData);
		chooseColorLabel.setBackground(new Color(null, 255, 255, 255));
		chooseColorLabel.setText("Choose color: ");

		final String[] colorDropDownItems = { RED, ORANGE, YELLOW, DARKGREEN, LIGHTGREEN, CYAN, LIGHTGREY, BLUE, MAGENTA, PINK };
		Combo colorDropDownMenu = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		colorDropDownMenu.setLayoutData(gridData);
		colorDropDownMenu.setItems(colorDropDownItems);

		Label featureLabel = new Label(container, SWT.NONE);
		featureLabel.setLayoutData(gridData);
		featureLabel.setBackground(new Color(null, 255, 255, 255));
		featureLabel.setText("Features: ");

		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		final Table featureTable = new Table(container, SWT.NONE);
		featureTable.setLayoutData(gridData);
		
		SelectionListener selectionListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				Combo colorLISTENER = ((Combo) event.widget);

				for (int i = 0; i < colorDropDownItems.length; i++) {
					if (colorLISTENER.getText().equals(colorDropDownItems[i])) {
						colorID = i;
						for (int j = 0; j < featurelistbuffer.size(); j++) {
							featureTable.getItem(j).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			};
		};
		colorDropDownMenu.addSelectionListener(selectionListener);

		SelectionListener selectionListener2 = new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				Combo actionLISTENER = ((Combo) event.widget);

				// Selected
				if (actionLISTENER.getText().equals(actionDropDownItems[0])) {
					featurelistbuffer.clear();
					for (int i = 0; i < featurelist.size(); i++) {
						featurelistbuffer.add(featurelist.get(i));
					}

					featureTable.redraw();
					featureTable.removeAll();

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						TableItem item = new TableItem(featureTable, SWT.NONE);
						item.setText(featurelistbuffer.get(i).getName());

						final Feature feature = featurelistbuffer.get(i);
						Profile profile = ProfileManager.getProject(feature.getFeatureModel().xxxGetEclipseProjectPath(),
								PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
						if (profile.hasFeatureColor(feature.getName()))
							item.setBackground(new Color(null, ColorPalette.getRGB(ProfileManager.toColorIndex(profile.getColor(feature.getName())), 0.4f)));
					}
				}

				// Selected + direct Children
				if (actionLISTENER.getText().equals(actionDropDownItems[1])) {
					featurelistbuffer.clear();

					for (int i = 0; i < featurelist.size(); i++) {
						featurelistbuffer.add(featurelist.get(i));
						featurelistbuffer.addAll(featurelist.get(i).getChildren());
					}

					featureTable.redraw();
					featureTable.removeAll();

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						TableItem item = new TableItem(featureTable, SWT.NONE);
						item.setText(featurelistbuffer.get(i).getName());

						final Feature feature = featurelistbuffer.get(i);
						Profile profile = ProfileManager.getProject(feature.getFeatureModel().xxxGetEclipseProjectPath(),
								PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
						if (profile.hasFeatureColor(feature.getName()))
							item.setBackground(new Color(null, ColorPalette.getRGB(ProfileManager.toColorIndex(profile.getColor(feature.getName())), 0.4f)));
					}
				}

				// Selected + all Children
				if (actionLISTENER.getText().equals(actionDropDownItems[2])) {
					featurelistbuffer.clear();

					for (int i = 0; i < featurelist.size(); i++) {
						featurelistbuffer.add(featurelist.get(i));
					}

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						featurelistbuffer.addAll(featurelistbuffer.get(i).getChildren());
					}

					featureTable.redraw();
					featureTable.removeAll();

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						TableItem item = new TableItem(featureTable, SWT.NONE);
						item.setText(featurelistbuffer.get(i).getName());

						final Feature feature = featurelistbuffer.get(i);
						Profile profile = ProfileManager.getProject(feature.getFeatureModel().xxxGetEclipseProjectPath(),
								PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
						if (profile.hasFeatureColor(feature.getName()))
							item.setBackground(new Color(null, ColorPalette.getRGB(ProfileManager.toColorIndex(profile.getColor(feature.getName())), 0.4f)));
					}
				}

				// Selected + Siblings
				if (actionLISTENER.getText().equals(actionDropDownItems[3])) {
					featurelistbuffer.clear();

					for (int i = 0; i < featurelist.size(); i++) {
						if (featurelist.get(i).isRoot()) {
							featurelistbuffer.add(featurelist.get(i));
						} else {
							featurelistbuffer.addAll(featurelist.get(i).getParent().getChildren());
						}
					}

					featureTable.redraw();
					featureTable.removeAll();

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						TableItem item = new TableItem(featureTable, SWT.NONE);
						item.setText(featurelistbuffer.get(i).getName());

						final Feature feature = featurelistbuffer.get(i);
						Profile profile = ProfileManager.getProject(feature.getFeatureModel().xxxGetEclipseProjectPath(),
								PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
						if (profile.hasFeatureColor(feature.getName()))
							item.setBackground(new Color(null, ColorPalette.getRGB(ProfileManager.toColorIndex(profile.getColor(feature.getName())), 0.4f)));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			};

		};

		actionDropDownMenu.addSelectionListener(selectionListener2);

		return parent;

	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {

			for (int i = 0; i < featurelistbuffer.size(); i++) {

				// Marcus Fix
				final Feature feature = featurelistbuffer.get(i);
				final FeatureModel model = feature.getFeatureModel();
				ProfileManager.Project project = ProfileManager
						.getProject(model.xxxGetEclipseProjectPath(), PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER);
				ProfileManager.Project.Profile activeProfile = project.getActiveProfile();
				activeProfile.setFeatureColor(feature.getName(), ProfileManager.getColorFromID(colorID));
				// End Marcus Fix
			}
			okPressed();

		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		}
	}

	protected void okPressed() {
		super.okPressed();
	}

	protected void cancelPressed() {
		super.cancelPressed();
	}
}
