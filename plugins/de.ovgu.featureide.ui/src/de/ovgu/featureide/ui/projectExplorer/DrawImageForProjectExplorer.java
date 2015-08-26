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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.ISharedImages;
//import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.fm.core.annotation.ColorPalette;

/**
 * TODO description
 * 
 * @author Jonas Weigt
 */
public class DrawImageForProjectExplorer { 
	
 	final static int RED = 0, ORANGE = 1, YELLOW = 2, DARKGREEN = 3, LIGHTGREEN = 4, CYAN = 5, LIGHTGREY = 6, BLUE = 7, MAGENTA = 8, PINK = 9;
 	final static int FILE = 0, FOLDER = 1, PACKAGE = 2;
	
	final static int WIDTHCONSTANT = 4;
	
	
	public DrawImageForProjectExplorer(){
		
	}

	public class ImageComarator implements Comparator<Image> {
		@Override
		public int compare(Image image1, Image image2) {
			return image1.getImageData().bytesPerLine > image2.getImageData().bytesPerLine ? 1 : 0;
		}
	}
	
	private final static Map<Integer, Image> images = new HashMap<Integer, Image>();
	
	public static Image drawColor(int explorerObject , List<Integer> colors){
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
		
		if(explorerObject == 0){
			Image fileImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			gc.drawImage(fileImage, 0, 0);
			liste.add(fileImage);
		}if(explorerObject == 1){
			Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			gc.drawImage(folderImage, 0, 0);
			liste.add(folderImage);
		}if(explorerObject == 2){
			Image packageImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
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
		gc.setForeground(new Color(liste.get(0).getDevice(), 255, 255, 255));	
		gc.drawLine(57, 0, 57, 16);
//			for(int i = 0; i < 10; i++){
//				liste.add(getColorImage(colors.get(i)));
//			}
//			
//			for(int i = 0;i<10; i++){
//				liste.add(getColorImage(i));
//			}
			
		//gc.setForeground(new Color(null, 255, 0, 0));
			//gc.drawImage(folderImage, 0, 15);
			//Image standard = new Image(images.get(0).getDevice(), images.get(0).getBounds());
			//gc.drawLine(0, 0, 15, 15);
//			int x = 0;
//			int y = 0;
			
			
			
//			for (int j = 0; j < images.size(); j++) {
//				if (images.size() == 1) {
					//x = finalImage.getBounds().width;
					
					
//				} else if ((j + 1) % 2 != 0) {
//					x = 0;
//					y = (j / 2) * 8;
//				} else {
//					x = 8;
//				}
//				gc.drawImage(images.get(j), 4, 4, images.get(j).getBounds().width - 2 * 4, images.get(j).getBounds().height - 2 * 4, x, y,
//						finalImage.getBounds().width / 2, finalImage.getBounds().height / 2);
				
				
			
//			gc.drawImage(images.get(1),0, 0);
//			gc.setForeground(new Color(images.get(0).getDevice(), 255, 0, 0));
//			gc.drawLine(images.get(0).getImageData().width, 0, 50, 10);
//			gc.setBackground(new Color(images.get(0).getDevice(), 255, 0, 0));
//			gc.fillRectangle(images.get(0).getImageData().width, 0, 50- images.get(0).getImageData().width, 10);
//				Image m = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
//				gc.drawImage(m, m.getBounds().x, m.getBounds().y);
				
//			}
			
//			
//				gc.drawImage(liste.get(0), 0, 0);
//			for(int i = 1;i<11; i++){
//				gc.drawImage(liste.get(i), 16 + ((WIDTHCONSTANT)*i), 0);
//			}
			
//			gc.drawImage(liste.get(0), 0, 0);
//			gc.drawImage(getWhiteImage(), 16, 0);
//			gc.drawImage(getWhiteImage(), 20, 0);
//			gc.drawImage(getWhiteImage(), 24, 0);
//			gc.drawImage(liste.get(0), 28, 0);
//			
			
		//gc.drawImage(getOrangemage(), 8 ,0);
			
			
		
			
			data = finalImage.getImageData();
			//data.transparentPixel = finalImage.getImageData().palette.getPixel(new RGB(255, 255, 255));
			gc.dispose();
			//return new Image(finalImage.getDevice(), data);
			Image image = new Image(liste.get(0).getDevice(), data);
			System.out.println("blablabla ");
			images.put(hashCode, image);
			return image;
		}
	
	
	

		
		
	
	public static Image getWhiteImage(){
		Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		Image finalImage = new Image(folderImage.getDevice(), folderImage.getImageData().width * 5 + 5, folderImage.getImageData().height);
		ImageData data = null;
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(finalImage);
		gc.drawRectangle(0, 0, folderImage.getImageData().width / 4, folderImage.getImageData().height);

		gc.setBackground(new Color(folderImage.getDevice(), 255, 255, 255));
		gc.fillRectangle(0, 0, folderImage.getImageData().width / 4, folderImage.getImageData().height);
		data = finalImage.getImageData();
		gc.dispose();
		return new Image(finalImage.getDevice(), data);

	}
	
	public static Image getColorImage(int colorID) {

		Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		Image finalImage = new Image(folderImage.getDevice(), folderImage.getImageData().width * 5 + 5, folderImage.getImageData().height);
		ImageData data = null;
		org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(finalImage);
		gc.drawRectangle(0, 0, folderImage.getImageData().width / 4, folderImage.getImageData().height);

		gc.setBackground(ColorPalette.getColor(colorID, 0.4f));
		gc.fillRectangle(0, 0, folderImage.getImageData().width / 4, folderImage.getImageData().height);
		data = finalImage.getImageData();
		gc.dispose();
		return new Image(finalImage.getDevice(), data);

	}
	
	
}
