package de.ovgu.featureide.ui.projectExplorer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.builder.IComposerExtensionClass;
import de.ovgu.featureide.core.fstmodel.FSTClass;
import de.ovgu.featureide.core.fstmodel.FSTModel;
import de.ovgu.featureide.core.fstmodel.FSTRole;
import de.ovgu.featureide.ui.projectExplorer.DrawImageForProjectExplorer.ExplorerObject;

@SuppressWarnings("restriction")
public class ProjectExplorerLabelProvider implements ILabelProvider {

	/**
	 * 
	 */
	private static final String SPACE_STRING = "             ";

	//	DrawImageForProjectExplorer featuredImage = new DrawImageForProjectExplorer();
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
		List<Integer> myColors = new ArrayList<Integer>();

		//PACKAGE FRAGMENT
		if (element instanceof PackageFragment) {
			PackageFragment frag = (PackageFragment) element;
			IFolder folder = (IFolder) frag.getResource();
			//			System.out.println(folder);
			IFeatureProject featureProject = CorePlugin.getFeatureProject(frag.getParent().getResource());
			FSTModel model = featureProject.getFSTModel();
			if (model.getClasses().isEmpty()) {
				featureProject.getComposer().buildFSTModel();
				model = featureProject.getFSTModel();
			}
			IComposerExtensionClass composer = featureProject.getComposer();
			getPackageColors(folder, myColors, model, !composer.hasFeatureFolder());

			return DrawImageForProjectExplorer.drawExplorerImage(ExplorerObject.PACKAGE, myColors);

		}

		// element IResource 
		if (element instanceof IResource) {
			IFeatureProject featureProject = CorePlugin.getFeatureProject((IResource) element);
			IComposerExtensionClass composer = featureProject.getComposer();
			FSTModel model = featureProject.getFSTModel();

			//composer FeatureHouse Folder
			if (composer.hasFeatureFolder() && element instanceof IFolder) {
				IFolder folder = (IFolder) element;
				//folder inSourceFolder but not SourceFolder itself
				if (folder.getParent().equals(featureProject.getSourceFolder())) {
					getFeatureFolderColors(folder, myColors, model);
					return DrawImageForProjectExplorer.drawFeatureHouseExplorerImage(myColors);
				}
			}

			//preprocessor 
			if (composer.hasSourceFolder() && !composer.hasFeatureFolder()) {
//			if (composer.getName().equals("Munge")) {
				if (element instanceof IFolder) {
					IFolder folder = (IFolder) element;
					if (isInSourceFolder(folder) && !folder.equals(featureProject.getSourceFolder())) {
						getPackageColors(folder, myColors, model, true);
						return DrawImageForProjectExplorer.drawExplorerImage(ExplorerObject.FOLDER, myColors);
					}
				}
				if (element instanceof IFile) {
					IFile file = (IFile) element;
					IContainer folder = file.getParent();
					if (folder instanceof IFolder) {
						if (isInSourceFolder((IFolder) folder)) {
							getPackageColors((IFolder)folder, myColors, model, true);
							return DrawImageForProjectExplorer.drawExplorerImage(ExplorerObject.FILE, myColors);
						}
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
			getColors(myColors, myfile, model, !featureProject.getComposer().hasFeatureFolder());

			return DrawImageForProjectExplorer.drawExplorerImage(ExplorerObject.FILE, myColors);

		}

		return null;
	}


	/**
	 * @param folder
	 * @param model
	 */
	private void getFeatureFolderColors(IFolder folder, List<Integer> myColors, FSTModel model) {
		myColors.add(model.getFeature(folder.getName()).getColor());
	}

	/**
	 * @param myColors
	 * @param myfile
	 * @param model
	 * @param colorUnselectedFeature TODO
	 */
	private void getColors(List<Integer> myColors, IFile myfile, FSTModel model, boolean colorUnselectedFeature) {
		FSTClass clazz = model.getClass(model.getAbsoluteClassName(myfile));
		for (FSTRole r : clazz.getRoles()) {
			if (colorUnselectedFeature || r.getFeature().isSelected()) {
				if (r.getFeature().getColor() != -1) {
					myColors.add(r.getFeature().getColor());
				}
			}
		}
	}

	/**
	 * @param folder
	 * @param colorUnselectedFeature TODO
	 */
	private void getPackageColors(IFolder folder, List<Integer> myColors, FSTModel model, boolean colorUnselectedFeature) {
		try {
			for (IResource member : folder.members()) {
				if (member instanceof IFile) {
					IFile file = (IFile) member;
					getColors(myColors, file, model, colorUnselectedFeature);
				}
				if (member instanceof IFolder) {
					getPackageColors((IFolder) member, myColors, model, colorUnselectedFeature);
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			if (featureProject == null) {
				return null;
			}
			String mytest = frag.getElementName();
			if (mytest.isEmpty()) {
				return SPACE_STRING + "(default package)";
			}
			return SPACE_STRING + mytest;	
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
						return " " + folder.getName();
					}
				}
				if (composer.getName().equals("Munge")){
					if (element instanceof IFolder){
						IFolder folder = (IFolder) element;
						return SPACE_STRING + folder.getName();
					}
					if (element instanceof IFile){
						IFile file = (IFile) element;
						return SPACE_STRING + file.getName();
					}
				}
			}

		}

		if (element instanceof org.eclipse.jdt.internal.core.CompilationUnit) {

			CompilationUnit cu = (CompilationUnit) element;

			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath path = cu.getPath();
			IFile myfile = root.getFile(path);			
			return SPACE_STRING + myfile.getName();
			
		}

		return null;
	}

}
