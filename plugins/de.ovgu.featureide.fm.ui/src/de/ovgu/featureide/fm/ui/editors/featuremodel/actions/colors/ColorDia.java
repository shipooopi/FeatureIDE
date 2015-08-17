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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

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

	protected ColorDia(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.MIN | SWT.RESIZE);
	}

	protected void configureShell(Shell newShell) {
		newShell.setMinimumSize(new Point(600, 600));
		super.configureShell(newShell);
		newShell.setText("Color");
	}

	protected Point getInitialSize() {
		return new Point(600, 600);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
//		Composite container2 = (Composite) super.createDialogArea(parent);
//		container2.setBackground(new Color(parent.getDisplay(), 240, 240, 240));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;

		GridData gridData = new GridData();

//		gridData.verticalAlignment = GridData.BEGINNING;
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

		final List list = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		list.setBackground(new Color(parent.getDisplay(), 240, 240, 240));
		list.setLayoutData(gridData);

		for (int i = 0; i < 50; i++) {
			list.add("Item " + i);
		}

		gridData = new GridData();
//		gridData.verticalSpan = 20;
//		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

//		Button button2 = new Button(container, SWT.PUSH);
//		button2.setLayoutData(gridData);
		Button button01 = new Button(container, SWT.PUSH);
		Button button02 = new Button(container, SWT.PUSH);
		Button button03 = new Button(container, SWT.PUSH);
		Button button04 = new Button(container, SWT.PUSH);
		Button button05 = new Button(container, SWT.PUSH);
		Button button06 = new Button(container, SWT.PUSH);
		Button button07 = new Button(container, SWT.PUSH);
		Button button08 = new Button(container, SWT.PUSH);
		Button button09 = new Button(container, SWT.PUSH);
		Button button010 = new Button(container, SWT.PUSH);
		
		button01.setText("Red");
		button01.setLayoutData(gridData);
		button02.setText("Orange");
		button02.setLayoutData(gridData);
		button03.setText("Yellow");
		button03.setLayoutData(gridData);
		button04.setText("Dark Green");
		button04.setLayoutData(gridData);
		button05.setText("Light Green");
		button05.setLayoutData(gridData);
		button06.setText("Cyan");
		button06.setLayoutData(gridData);
		button07.setText("Light Grey");
		button07.setLayoutData(gridData);
		button08.setText("Blue");
		button08.setLayoutData(gridData);
		button09.setText("Magenta");
		button09.setLayoutData(gridData);
		button010.setText("Pink");
		button010.setLayoutData(gridData);

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
