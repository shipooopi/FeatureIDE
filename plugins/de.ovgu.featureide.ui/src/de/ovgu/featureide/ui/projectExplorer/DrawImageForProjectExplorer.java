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
package de.ovgu.featureide.ui.projectExplorer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.fm.core.annotation.ColorPalette;

/**
 * draws the images for the ProjectExplorer.
 * The image includes the file-, folder- or package - image
 * and the color of the feature.
 * 
 * 
 * @author Jonas Weigt
 */

public class DrawImageForProjectExplorer {

	/**
	 * constants from
	 * de.ovgu.featureide.fm.core.annotation.ColorPalette
	 */
	final static int RED = 0, ORANGE = 1, YELLOW = 2, DARKGREEN = 3, LIGHTGREEN = 4, CYAN = 5, LIGHTGREY = 6, BLUE = 7, MAGENTA = 8, PINK = 9;

	/*
	 * constants to know, which icon must be returned 			
	 */
	final static int FILE = 0, FOLDER = 1, PACKAGE = 2;

	/**
	 * constant for the width of the single colorImage
	 */
	final static int WIDTHCONSTANT = 4;

	private final static Map<Integer, Image> images = new HashMap<Integer, Image>();

	/**
	 * @param explorerObject
	 * @param colors
	 * @return the image with the icon of the file, folder or package (explorerObject) and the color of the feature
	 */
	public static Image drawExplorerImage(int explorerObject, List<Integer> colors) {
		colors.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer i0, Integer i1) {
				return i0.compareTo(i1);
			}
		});

		colors.add(explorerObject);
		Integer hashCode = colors.hashCode();
		if (images.containsKey(hashCode)) {
			return images.get(hashCode);
		}
		colors.remove(colors.size() - 1);

		Image dummyImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		Image finalImage = new Image(dummyImage.getDevice(), dummyImage.getImageData().width * 5, dummyImage.getImageData().height);
		ImageData data = null;
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(finalImage);

		ArrayList<Image> liste = new ArrayList<>();

		if (explorerObject == 0) {

			Image fileImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			gc.drawImage(fileImage, 0, 0);
			liste.add(fileImage);
		}
		if (explorerObject == 1) {
			Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			gc.drawImage(folderImage, 0, 0);
			liste.add(folderImage);
		}
		if (explorerObject == 2) {
			Image packageImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW);
			gc.drawImage(packageImage, 0, 0);
			liste.add(packageImage);
		}

		for (int i = 0; i < 10; i++) {
			if (colors.contains(i)) {
				gc.drawImage(getColorImage(i), 17 + WIDTHCONSTANT * i, 0);
			} else {
				gc.drawImage(getWhiteImage(), 17 + WIDTHCONSTANT * i, 0);
			}
		}
		data = finalImage.getImageData();
		gc.dispose();
		Image image = new Image(liste.get(0).getDevice(), data);
		images.put(hashCode, image);
		return image;
	}

	/**
	 * @param colors
	 * @return the image for the featureHouseExplorer with the folderIcon as default and only one color
	 */
	public static Image drawFeatureHouseExplorerImage(List<Integer> colors) {

		colors.add(FOLDER);
		Integer hashCode = colors.hashCode();
		if (images.containsKey(hashCode)) {
			return images.get(hashCode);
		}
		colors.remove(colors.size() - 1);

		Image dummyImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		Image finalImage = new Image(dummyImage.getDevice(), dummyImage.getImageData().width * 5, dummyImage.getImageData().height);
		ImageData data = null;
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(finalImage);

		Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		gc.drawImage(folderImage, 0, 0);
		gc.drawImage(getColorImage(colors.get(0)), 17, 0);
		data = finalImage.getImageData();
		gc.dispose();
		Image image = new Image(dummyImage.getDevice(), data);
		images.put(hashCode, image);

		return image;
	}

	/**
	 * @return a white image, is needed to fill the parts, where no color is selected
	 */
	public static Image getWhiteImage() {
		Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		Image finalImage = new Image(folderImage.getDevice(), folderImage.getImageData().width * 5, folderImage.getImageData().height);
		ImageData data = null;
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(finalImage);
		gc.setForeground(new Color(folderImage.getDevice(), 0, 0, 0));
		gc.drawRectangle(0, 0, folderImage.getImageData().width / 4, folderImage.getImageData().height);

		gc.setBackground(new Color(folderImage.getDevice(), 255, 255, 255));
		gc.fillRectangle(1, 1, (folderImage.getImageData().width / 4) - 1, (folderImage.getImageData().height) - 1);
		data = finalImage.getImageData();
		gc.dispose();
		return new Image(finalImage.getDevice(), data);

	}

	/**
	 * @param colorID
	 * @return a colored image with the original colors from
	 *         de.ovgu.featureide.fm.core.annotation.ColorPalette
	 */
	public static Image getColorImage(int colorID) {

		Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		Image finalImage = new Image(folderImage.getDevice(), folderImage.getImageData().width * 5, folderImage.getImageData().height);
		ImageData data = null;
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(finalImage);
		gc.setForeground(new Color(folderImage.getDevice(), 0, 0, 0));
		gc.drawRectangle(0, 0, folderImage.getImageData().width / 4, folderImage.getImageData().height);

		gc.setBackground(ColorPalette.getColor(colorID, 0.4f));
		gc.fillRectangle(1, 1, (folderImage.getImageData().width / 4) - 1, (folderImage.getImageData().height) - 1);
		data = finalImage.getImageData();
		gc.dispose();
		return new Image(finalImage.getDevice(), data);

	}

}
