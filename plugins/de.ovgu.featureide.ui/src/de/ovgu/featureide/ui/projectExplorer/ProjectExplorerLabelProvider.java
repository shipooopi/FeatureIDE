package de.ovgu.featureide.ui.projectExplorer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.PlatformUI;

public class ProjectExplorerLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		
		Image m = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_DEC_FIELD_ERROR);
		Image hallo = new Image(m.getDevice(), new ImageData(m.getImageData().width*8, m.getImageData().height, m.getImageData().depth, m.getImageData().palette));
		return hallo;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		return "            " + element.toString();
	}

}
