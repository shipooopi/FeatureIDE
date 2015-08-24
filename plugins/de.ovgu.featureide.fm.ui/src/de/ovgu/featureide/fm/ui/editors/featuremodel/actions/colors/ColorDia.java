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

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import de.ovgu.featureide.fm.core.ProfileManager;
import de.ovgu.featureide.fm.core.ProfileManager.Project.Profile;
import de.ovgu.featureide.fm.core.ColorschemeTable;
import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.annotation.ColorPalette;
import de.ovgu.featureide.fm.ui.PlugInProfileSerializer;

/**
 * The purpose of this dialog is to display the content of a 'normal' {@link TreeViewer} in a {@link CheckboxTreeViewer} to select some of it's
 * content and then export it to *.csv.
 * 
 * @see CsvExporter
 * 
 * @author Dominik Hamann
 * @author Patrick Haese
 */
public class ColorDia extends Dialog {

	/**
	 * @param parentShell
	 */

	Shell shell = new Shell();
	ArrayList<Feature> featurelist = new ArrayList<Feature>();
	ArrayList<Feature> featurelistbuffer = new ArrayList<Feature>();
	int colorID = -1;

	protected ColorDia(Shell parentShell, ArrayList<Feature> featurelist) {
		super(parentShell);
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

		//gridData TopLevelBox
		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		Button button1 = new Button(container, SWT.PUSH);
		button1.setText("Oben");
		button1.setLayoutData(gridData);
		button1.setVisible(true);

		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		Label actionLABEL = new Label(container, SWT.NONE);
		actionLABEL.setLayoutData(gridData);
		actionLABEL.setBackground(new Color(null, 255, 255, 255));
		actionLABEL.setText("Choose action: ");

		final String[] dropdownITEMS = { "Selected", "Selected and Children", "Same Level" };
		Combo actionDROPDOWN = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		actionDROPDOWN.setLayoutData(gridData);
		actionDROPDOWN.setItems(dropdownITEMS);

		Label colorLABEL = new Label(container, SWT.NONE);
		colorLABEL.setLayoutData(gridData);
		colorLABEL.setBackground(new Color(null, 255, 255, 255));
		colorLABEL.setText("Choose color: ");

		final String[] dropdownITEMS2 = { "Red", "Orange", "Yellow", "Dark Green", "Light Green", "Cyan", "Light Grey", "Blue", "Magenta", "Pink" };
		Combo colorDROPDOWN = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		colorDROPDOWN.setLayoutData(gridData);
		colorDROPDOWN.setItems(dropdownITEMS2);

		Label featureLABEL = new Label(container, SWT.NONE);
		featureLABEL.setLayoutData(gridData);
		featureLABEL.setBackground(new Color(null, 255, 255, 255));
		featureLABEL.setText("Features: ");

		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		final Table table = new Table(container, SWT.NONE);
		//		table.setBackground(new Color(null, 0, 0, 0));
		table.setLayoutData(gridData);

		SelectionListener selectionListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				Combo colorLISTENER = ((Combo) event.widget);

				if (colorLISTENER.getText().equals(dropdownITEMS2[0])) {
					colorID = 0;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[1])) {
					colorID = 1;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[2])) {
					colorID = 2;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[3])) {
					colorID = 3;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[4])) {
					colorID = 4;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[5])) {
					colorID = 5;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[6])) {
					colorID = 6;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[7])) {
					colorID = 7;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[8])) {
					colorID = 8;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}

				if (colorLISTENER.getText().equals(dropdownITEMS2[9])) {
					colorID = 9;
					for (int i = 0; i < featurelistbuffer.size(); i++) {
						table.getItem(i).setBackground(new Color(null, ColorPalette.getRGB(colorID, 0.4f)));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			};
		};
		colorDROPDOWN.addSelectionListener(selectionListener);

		SelectionListener selectionListener2 = new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				Combo actionLISTENER = ((Combo) event.widget);

				// Selected
				if (actionLISTENER.getText().equals(dropdownITEMS[0])) {
					featurelistbuffer.clear();
					for (int i = 0; i < featurelist.size(); i++) {
						featurelistbuffer.add(featurelist.get(i));
					}

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(featurelistbuffer.get(i).getName());

						final Feature feature = featurelistbuffer.get(i);
						Profile profile = ProfileManager.getProject(feature.getFeatureModel().xxxGetEclipseProjectPath(),
								PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
						if (profile.hasFeatureColor(feature.getName()))
							item.setBackground(new Color(null, ColorPalette.getRGB(ProfileManager.toColorIndex(profile.getColor(feature.getName())), 0.4f)));
					}
				}

				// Selected + Children
				if (actionLISTENER.getText().equals(dropdownITEMS[1])) {
					featurelistbuffer.clear();
					for (int i = 0; i < featurelist.size(); i++) {
						featurelistbuffer.addAll(featurelist.get(i).getChildren());
					}

					for (int i = 0; i < featurelistbuffer.size(); i++) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(featurelistbuffer.get(i).getName());

						final Feature feature = featurelistbuffer.get(i);
						Profile profile = ProfileManager.getProject(feature.getFeatureModel().xxxGetEclipseProjectPath(),
								PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER).getActiveProfile();
						if (profile.hasFeatureColor(feature.getName()))
							item.setBackground(new Color(null, ColorPalette.getRGB(ProfileManager.toColorIndex(profile.getColor(feature.getName())), 0.4f)));
					}
				}

				// Same Level
				if (actionLISTENER.getText().equals(dropdownITEMS[2])) {

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			};

		};

		actionDROPDOWN.addSelectionListener(selectionListener2);

		return parent;

	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {

			for (int i = 0; i < featurelist.size(); i++) {

				// Marcus Fix
				final Feature feature = featurelist.get(i);
				final FeatureModel model = feature.getFeatureModel();
				ProfileManager.Project project = ProfileManager.getProject(model.xxxGetEclipseProjectPath(), PlugInProfileSerializer.FEATURE_PROJECT_SERIALIZER);
				ProfileManager.Project.Profile activeProfile = project.getActiveProfile();
				activeProfile.setFeatureColor(feature.getName(), ProfileManager.getColorFromID(colorID));
				// End Marcus Fix
			}
			okPressed();

		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		}
	}

	/**
	 * @param colorschemeTable
	 */
	private void printSchemas(ColorschemeTable colorschemeTable) {
		System.out.println("------");
		for (String s : colorschemeTable.getColorschemeNames())
			System.out.println(s);

	}

	protected void okPressed() {
		super.okPressed();
	}

	protected void cancelPressed() {
		super.cancelPressed();
	}
}
