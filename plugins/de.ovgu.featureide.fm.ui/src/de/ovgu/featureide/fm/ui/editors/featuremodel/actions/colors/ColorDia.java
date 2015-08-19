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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;

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

	final Shell shell = new Shell();
	ArrayList<Feature> flist = new ArrayList<Feature>();
	public FeatureModel fm;

	protected ColorDia(Shell parentShell, ArrayList<Feature> flist, FeatureModel fm) {
		super(parentShell);
		this.flist = flist;
		this.fm = fm;
		setShellStyle(SWT.DIALOG_TRIM | SWT.MIN | SWT.RESIZE);
	}

	protected void configureShell(Shell newShell) {
		newShell.setMinimumSize(new Point(600, 600));
		super.configureShell(newShell);
		newShell.setText("Color Dialog");
	}

	protected Point getInitialSize() {
		return new Point(600, 600);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;

		GridData gridData = new GridData();

		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 5;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		Button button1 = new Button(container, SWT.PUSH);
		button1.setText("Oben");
		button1.setLayoutData(gridData);

		gridData = new GridData();
		gridData.verticalSpan = 20;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		final List list = new List(container, /*SWT.BORDER |*/SWT.MULTI | SWT.V_SCROLL);
		list.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		list.setLayoutData(gridData);
		//		final Table table = new Table (container,SWT.NONE); 
		//		table.setLayoutData(gridData);

		for (int i = 0; i < flist.size(); i++) {
			//			TableItem item = new TableItem(table,SWT.NONE);
			//			item.setText(flist.get(i).getName());
			list.add(flist.get(i).getName());
		}

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		final Button buttonRED = new Button(container, SWT.PUSH);
		final Button buttonORANGE = new Button(container, SWT.PUSH);
		final Button buttonYELLOW = new Button(container, SWT.PUSH);
		final Button buttonDARKGREEN = new Button(container, SWT.PUSH);
		final Button buttonLIGHTGREEN = new Button(container, SWT.PUSH);
		final Button buttonCYAN = new Button(container, SWT.PUSH);
		final Button buttonLIGHTGREY = new Button(container, SWT.PUSH);
		final Button buttonBLUE = new Button(container, SWT.PUSH);
		final Button buttonMAGENTA = new Button(container, SWT.PUSH);
		final Button buttonPINK = new Button(container, SWT.PUSH);
		final Button buttonCLEAR = new Button(container, SWT.PUSH);

		buttonRED.setText("Red");
		buttonRED.setLayoutData(gridData);
		buttonORANGE.setText("Orange");
		buttonORANGE.setLayoutData(gridData);
		buttonYELLOW.setText("Yellow");
		buttonYELLOW.setLayoutData(gridData);
		buttonDARKGREEN.setText("Dark Green");
		buttonDARKGREEN.setLayoutData(gridData);
		buttonLIGHTGREEN.setText("Light Green");
		buttonLIGHTGREEN.setLayoutData(gridData);
		buttonCYAN.setText("Cyan");
		buttonCYAN.setLayoutData(gridData);
		buttonLIGHTGREY.setText("Light Grey");
		buttonLIGHTGREY.setLayoutData(gridData);
		buttonBLUE.setText("Blue");
		buttonBLUE.setLayoutData(gridData);
		buttonMAGENTA.setText("Magenta");
		buttonMAGENTA.setLayoutData(gridData);
		buttonPINK.setText("Pink");
		buttonPINK.setLayoutData(gridData);
		buttonCLEAR.setText("Clear");
		buttonCLEAR.setLayoutData(gridData);

		SelectionListener listener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {

				if (e.getSource() == buttonRED) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(0);
					}

				}
				if (e.getSource() == buttonORANGE) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(1);
					}

				}
				if (e.getSource() == buttonYELLOW) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(2);
					}

				}
				if (e.getSource() == buttonDARKGREEN) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(3);
					}

				}
				if (e.getSource() == buttonLIGHTGREEN) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(4);
					}

				}
				if (e.getSource() == buttonCYAN) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(5);
					}

				}
				if (e.getSource() == buttonLIGHTGREY) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(6);
					}

				}
				if (e.getSource() == buttonBLUE) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(7);
					}

				}
				if (e.getSource() == buttonMAGENTA) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(8);
					}

				}
				if (e.getSource() == buttonPINK) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(9);
					}

				}
				if (e.getSource() == buttonCLEAR) {
					for (int i = 0; i < flist.size(); i++) {
						flist.get(i).getColorList().setColor(-1);
					}

				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};

		buttonRED.addSelectionListener(listener);
		buttonORANGE.addSelectionListener(listener);
		buttonYELLOW.addSelectionListener(listener);
		buttonDARKGREEN.addSelectionListener(listener);
		buttonLIGHTGREEN.addSelectionListener(listener);
		buttonCYAN.addSelectionListener(listener);
		buttonLIGHTGREY.addSelectionListener(listener);
		buttonBLUE.addSelectionListener(listener);
		buttonMAGENTA.addSelectionListener(listener);
		buttonPINK.addSelectionListener(listener);
		buttonCLEAR.addSelectionListener(listener);

		return parent;

	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
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
