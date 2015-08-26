package de.ovgu.featureide.ui.projectExplorer;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.builder.IComposerExtensionClass;
import de.ovgu.featureide.core.fstmodel.FSTClass;
import de.ovgu.featureide.core.fstmodel.FSTFeature;
import de.ovgu.featureide.core.fstmodel.FSTModel;
import de.ovgu.featureide.core.fstmodel.FSTRole;

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
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		//element.getClass();
		Collection<Integer> myColors = new ArrayList<Integer>();
		//		System.out.println("ProjectExplorerLabelProvider.getImage()");
		//		System.out.println(element.getClass());
		//PACKAGE FRAGMENT
		if (element instanceof PackageFragment) {
			PackageFragment frag = (PackageFragment) element;
			IFeatureProject featureProject = CorePlugin.getFeatureProject(frag.getParent().getResource());
			IComposerExtensionClass composer = featureProject.getComposer();
			//featurehouse
			if (composer != null) {
				Image m = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_DEC_FIELD_ERROR);
				Image PackageImage = new Image(m.getDevice(), new ImageData(m.getImageData().width * 8, m.getImageData().height, m.getImageData().depth,
						m.getImageData().palette));
				return PackageImage;
			}
		}

		// element IResource 
		if (element instanceof IResource) {
			IFeatureProject featureProject = CorePlugin.getFeatureProject((IResource) element);
			IComposerExtensionClass composer = featureProject.getComposer();

			//composer FeatureHouse Folder
			if (composer.getName().equals("FeatureHouse") && element instanceof IFolder) {

				IFolder folder = (IFolder) element;
				//folder inSourceFolder but not SourceFolder itself
				if (isInSourceFolder(folder) && !folder.equals(featureProject.getSourceFolder())) {
		

					Image m = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_ETOOL_DELETE);
					Image featureFolderImage = new Image(m.getDevice(), new ImageData(m.getImageData().width * 8, m.getImageData().height,
							m.getImageData().depth, m.getImageData().palette));
					return featureFolderImage;
				}
			}

			//preprocessor
			if (composer.getName().equals("Munge")){
					if (element instanceof IFolder) {
				IFolder folder = (IFolder)element;
				if (isInSourceFolder(folder) && !folder.equals(featureProject.getSourceFolder())){
					System.out.println("im the source folder of munge");// feature colors
					Image m = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_ETOOL_DELETE);
					Image featureFolderImage = new Image(m.getDevice(), new ImageData(m.getImageData().width * 8, m.getImageData().height,
							m.getImageData().depth, m.getImageData().palette));
					return featureFolderImage;
				}
			}
					if (element instanceof IFile){
						IFile file = (IFile)element;
						if (isInSourceFolder((IFolder)file.getParent())){
							Image m = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_ETOOL_DELETE);
							Image featureFolderImage = new Image(m.getDevice(), new ImageData(m.getImageData().width * 8, m.getImageData().height,
									m.getImageData().depth, m.getImageData().palette));
							return featureFolderImage;
						}
					}
					}
		}

		//COMPOSED FILES 
		if (element instanceof org.eclipse.jdt.internal.core.CompilationUnit) {

			CompilationUnit cu = (CompilationUnit) element;

			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath path = cu.getPath();
			IFile myfile = root.getFile(path);
			IFeatureProject featureProject = CorePlugin.getFeatureProject(myfile);
			FSTModel model = featureProject.getFSTModel();
			FSTClass clazz = model.getClass(model.getAbsoluteClassName(myfile));
			for (FSTRole r: clazz.getRoles()) {
				if (r.getFeature().getColor() != -1){
					myColors.add(r.getFeature().getColor());
				}
			}
			
			IComposerExtensionClass composer = featureProject.getComposer();
			if (composer != null) {
				for (FSTFeature feature : featureProject.getFSTModel().getFeatures()) {
					if (feature.getColor() != -1) {
						myColors.add(feature.getColor());
					}
					
				}
				Image featureHouseComposedFileImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_ETOOL_DELETE);
				return featureHouseComposedFileImage;
			}

			//			if (composer.getName().equals("FeatureHouse")) {
			//				for ( FSTFeature feature : featureProject.getFSTModel().getFeatures()){
			//					myColors.add(feature.getColor());
			//				}
			//				//clear mycolors after image
			//				
			//				Image featureHouseComposedFileImage = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_ETOOL_DELETE);
			//				return featureHouseComposedFileImage;
			//			}
			//			//preprocessor
			//			if (composer.getName().equals("Munge") || composer.getName().equals("Antenna")) {
			//					for (FSTFeature feature : featureProject.getFSTModel().getFeatures()){
			//						myColors.add(feature.getColor());
			//					}
			//					
			//					//clear mycolors after image
			//					Image PreprocesseComposed = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_PROJECT_CLOSED);
			//					return PreprocesseComposed;
			//				}

		}

		return null;
	}

	/**
	 * @param folder
	 * @return
	 */
	private boolean isInSourceFolder(IFolder folder) {
		if (folder.equals(CorePlugin.getFeatureProject(folder).getSourceFolder())) {
			return true;
		}
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			return isInSourceFolder((IFolder) parent);
		}
		return false;
	}

	@Override
	public String getText(Object element) {

		if (element instanceof PackageFragment) {
			PackageFragment frag = (PackageFragment) element;
			IFeatureProject featureProject = CorePlugin.getFeatureProject(frag.getParent().getResource());
			IComposerExtensionClass composer = featureProject.getComposer();
			String mytest = frag.getElementName();
			//featurehouse

			if (composer.getName().equals("FeatureHouse") || composer.getName().equals("Antenna") || composer.getName().equals("Munge")) {
				if (mytest.equals("")) {
					return "           " + frag.toString();
				}
				return "          " + mytest;
			}
		}

		if (element instanceof IResource) {
			IFeatureProject featureProject = CorePlugin.getFeatureProject((IResource) element);
			if (featureProject != null) {
				IComposerExtensionClass composer = featureProject.getComposer();
				//composer FeatureHouse Folder
				if (composer.getName().equals("FeatureHouse") && element instanceof IFolder) {

					IFolder folder = (IFolder) element;
					//folder inSourceFolder but not SourceFolder itself
					if (isInSourceFolder(folder) && !folder.equals(featureProject.getSourceFolder())) {
						return "          " + folder.getName();
					}
				}
			}

		}

		if (element instanceof org.eclipse.jdt.internal.core.CompilationUnit) {

			CompilationUnit cu = (CompilationUnit) element;

			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath path = cu.getPath();
			IFile myfile = root.getFile(path);
			IFeatureProject featureProject = CorePlugin.getFeatureProject(myfile);
			IComposerExtensionClass composer = featureProject.getComposer();
			if (composer.getName().equals("FeatureHouse")) {
				return "              " + myfile.getName();
			}
			if (composer.getName().equals("Munge") || composer.getName().equals("Antenna")) {
				return "              " + myfile.getName();
			}
		}

		return null;
	}

}
