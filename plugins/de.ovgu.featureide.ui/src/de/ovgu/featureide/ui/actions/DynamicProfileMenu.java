package de.ovgu.featureide.ui.actions;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.fm.core.ColorschemeTable;
import de.ovgu.featureide.fm.core.FeatureModel;

public class DynamicProfileMenu extends ContributionItem {
	private AddProfileColorSchemeAction addProfileSchemeAction;
	private RenameProfileColorSchemeAction renameProfileSchemeAction;
	private DeleteProfileColorSchemeAction deleteProfileSchemeAction;
	private IFeatureProject myFeatureProject = CorePlugin.getFeatureProject(((IJavaElement)((IStructuredSelection)((ISelection)((ISelectionService)Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService()).getSelection())).getFirstElement()).getJavaProject().getProject());
	private FeatureModel myFeatureModel = myFeatureProject.getFeatureModel();
//			CorePlugin.getFeatureProject(
//			(IProject) ((IStructuredSelection) Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService().getSelection()).getFirstElement())
//			.getFeatureModel();

	public DynamicProfileMenu() {

	}

	public DynamicProfileMenu(String id) {
		super(id);
	}

	@Override
	public void fill(Menu menu, int index) {

		MenuManager man = new MenuManager("Profile", PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD), "");

		man.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {

				fillContextMenu(m);
			}
		});
		man.fill(menu, index);
		man.setVisible(true);
		createActions();
		
	}

	private void fillContextMenu(IMenuManager menuMgr) {
//SetProfileColorSchemeAction setCSAction;
		FeatureModel fm = myFeatureModel;
		ColorschemeTable colorschemeTable = fm.getColorschemeTable();
		List<String> csNames = colorschemeTable.getColorschemeNames();

		int count = 0;
		for (String name : csNames) {
			//SetProfileColorSchemeAction 
			SetProfileColorSchemeAction setCSAction = new SetProfileColorSchemeAction(name, ++count, Action.AS_CHECK_BOX, myFeatureModel);
			if (count == colorschemeTable.getSelectedColorscheme()) {
				setCSAction.setChecked(true);
			}
			menuMgr.add(setCSAction);
			
			}
		menuMgr.add(new Separator());
		menuMgr.add(addProfileSchemeAction);
		menuMgr.add(renameProfileSchemeAction);
		menuMgr.add(deleteProfileSchemeAction);
//		renameProfileSchemeAction.setId("533");
		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.remove("533");
		

	}

	// create Actions

	private void createActions() {
		addProfileSchemeAction = new AddProfileColorSchemeAction("Add", myFeatureModel, myFeatureProject);
		renameProfileSchemeAction = new RenameProfileColorSchemeAction("Change Name", myFeatureModel, myFeatureProject);
		deleteProfileSchemeAction = new DeleteProfileColorSchemeAction("Delete Scheme", myFeatureModel, myFeatureProject);

	}

}
