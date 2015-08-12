package de.ovgu.featureide.ui;

import static de.ovgu.featureide.fm.core.localization.StringTable.NO_COLORSCHEME_SELECTED;
import static de.ovgu.featureide.fm.core.localization.StringTable.SHOW_FIELDS_AND_METHODS;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Menu;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.fstmodel.FSTConfiguration;
import de.ovgu.featureide.core.fstmodel.FSTFeature;
import de.ovgu.featureide.fm.core.ColorList;
import de.ovgu.featureide.fm.core.ColorschemeTable;
import de.ovgu.featureide.fm.core.FMCorePlugin;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.ui.views.collaboration.action.AddColorSchemeAction;
import de.ovgu.featureide.ui.views.collaboration.action.DeleteColorSchemeAction;
import de.ovgu.featureide.ui.views.collaboration.action.RenameColorSchemeAction;
import de.ovgu.featureide.ui.views.collaboration.action.SetColorSchemeAction;
import de.ovgu.featureide.ui.views.collaboration.action.ShowFieldsMethodsAction;
import de.ovgu.featureide.ui.views.collaboration.editparts.CollaborationEditPart;

public class MyDynamicMenu extends ContributionItem {
	private AddColorSchemeAction addColorSchemeAction =  new AddColorSchemeAction("Add", null, null);
	private RenameColorSchemeAction renameColorSchemeAction = new RenameColorSchemeAction("Remove", null, null);
	private DeleteColorSchemeAction	deleteColorSchemeAction = new DeleteColorSchemeAction("Leck mich", null, null);
	
	public MyDynamicMenu() {

	}

	public MyDynamicMenu(String id) {
		super(id);
	}

	@Override
	public void fill(Menu menu, int index) {
		MenuManager man = new MenuManager("Color");
		man.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				fillContextMenu(m);
			}
		});
		man.fill(menu, index);
		man.setVisible(true);
		
		
	
	}
	
	private void fillContextMenu(IMenuManager menuMgr) {
//		disableToolbarFilterItems();
//		if (featureProject == null) {
//			return;
//		}
//		boolean isNotEmpty = !viewer.getSelection().isEmpty();
//		addRoleAction.setEnabled(isNotEmpty);
//		filterAction.setEnabled(isNotEmpty);
//		delAction.setEnabled(isNotEmpty);
//		showUnselectedAction.setEnabled(isNotEmpty);

//		saveCursorPosition();

//		menuMgr.add(addRoleAction);
//		menuMgr.add(filterAction);
//		menuMgr.add(showUnselectedAction);
//		menuMgr.add(delAction);

//		if (featureProject.getComposer().showContextFieldsAndMethods()) {
//			MenuManager methodsFieldsSubMenu = new MenuManager(SHOW_FIELDS_AND_METHODS);
//
//			for (int i = 0; i < setFieldsMethodsActions.length; i++) {
//				methodsFieldsSubMenu.add(setFieldsMethodsActions[i]);
//				setFieldsMethodsActions[i].setChecked(false);
//
//				if ((i == ShowFieldsMethodsAction.SHOW_NESTED_CLASSES) || (i == ShowFieldsMethodsAction.HIDE_PARAMETERS_AND_TYPES)
//						|| (i == ShowFieldsMethodsAction.PRIVATE_FIELDSMETHODS)) {
//					methodsFieldsSubMenu.add(new Separator());
//				}
//			}
//			menuMgr.add(methodsFieldsSubMenu);
//		}

//		ResourcesPlugin.getWorkspace()
//		ResourcesPlugin.getWorkspace().getRoot().getProjects()
		IProject pr = null;
		ResourcesPlugin.getWorkspace().getRoot().
//		CorePlugin.getFeatureProjects().toArray();
		
		Object selection = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		if (selection instanceof CollaborationEditPart) {
			FSTFeature coll = ((CollaborationEditPart) selection).getCollaborationModel();
			if (!(coll instanceof FSTConfiguration)) {
				FeatureModel fm = featureProject.getFeatureModel();
				ColorschemeTable colorschemeTable = fm.getColorschemeTable();
				List<String> csNames = colorschemeTable.getColorschemeNames();

				String curColorSchemeName = colorschemeTable.getSelectedColorschemeName();
				MenuManager colorSchemeSubMenu = null;

				if (curColorSchemeName != null) {
					colorSchemeSubMenu = new MenuManager(curColorSchemeName);
				} else {
					colorSchemeSubMenu = new MenuManager(NO_COLORSCHEME_SELECTED);
				}

				int count = 0;
				for (String name : csNames) {
					SetColorSchemeAction setCSAction = new SetColorSchemeAction(name, viewer, this, ++count);
					if (count == colorschemeTable.getSelectedColorscheme()) {
						setCSAction.setChecked(true);
					}
					colorSchemeSubMenu.add(setCSAction);
				}

				menuMgr.add(new Separator());
				menuMgr.add(addColorSchemeAction);
				menuMgr.add(renameColorSchemeAction);
				menuMgr.add(deleteColorSchemeAction);
				colorSubMenu.removeAll();
				colorSubMenu.add(colorSchemeSubMenu);
				colorSubMenu.add(new Separator());

				boolean enableColorActions = colorschemeTable.getSelectedColorscheme() > 0;
				for (int i = 0; i < setColorActions.length; i++) {
					setColorActions[i].setEnabled(enableColorActions);
					setColorActions[i].setChecked(false);
					colorSubMenu.add(setColorActions[i]);
				}

				int color = fm.getFeature(coll.getName()).getColorList().getColor();
				if (ColorList.isValidColor(color)) {
					setColorActions[color].setChecked(true);
				}

				menuMgr.add(colorSubMenu);
			}
		}

		menuMgr.add(new Separator());
		MenuManager exportMenu = new MenuManager(EXPORT_AS_LABEL);
		exportMenu.add(exportAsImage);
		exportMenu.add(exportAsXML);
		menuMgr.add(exportMenu);
	}
	
//	private void fillContextMenu(IMenuManager menuMgr) {
//		System.out.println();
//		FMCorePlugin.getDefault().logInfo("füllen");
		
//		menuMgr.add(addColorSchemeAction);
		
//		disableToolbarFilterItems();
//		if (featureProject == null) {
//			return;
//		}
//		addRoleAction.setEnabled(isNotEmpty);
//		filterAction.setEnabled(isNotEmpty);
//		delAction.setEnabled(isNotEmpty);
//		showUnselectedAction.setEnabled(isNotEmpty);
//
//		saveCursorPosition();

//		menuMgr.add(filterAction);
//		menuMgr.add(showUnselectedAction);
//		menuMgr.add(delAction);

//		if (featureProject.getComposer().showContextFieldsAndMethods()) {
//			MenuManager methodsFieldsSubMenu = new MenuManager(SHOW_FIELDS_AND_METHODS);
//
//			for (int i = 0; i < setFieldsMethodsActions.length; i++) {
//				methodsFieldsSubMenu.add(setFieldsMethodsActions[i]);
//				setFieldsMethodsActions[i].setChecked(false);
//
//				if ((i == ShowFieldsMethodsAction.SHOW_NESTED_CLASSES) || (i == ShowFieldsMethodsAction.HIDE_PARAMETERS_AND_TYPES)
//						|| (i == ShowFieldsMethodsAction.PRIVATE_FIELDSMETHODS)) {
//					methodsFieldsSubMenu.add(new Separator());
//				}
//			}
//			menuMgr.add(methodsFieldsSubMenu);
//		}
//
//		Object selection = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
//		if (selection instanceof CollaborationEditPart) {
//			FSTFeature coll = ((CollaborationEditPart) selection).getCollaborationModel();
//			if (!(coll instanceof FSTConfiguration)) {
//				FeatureModel fm = featureProject.getFeatureModel();
//				ColorschemeTable colorschemeTable = fm.getColorschemeTable();
//				List<String> csNames = colorschemeTable.getColorschemeNames();
//
//				String curColorSchemeName = colorschemeTable.getSelectedColorschemeName();
//				MenuManager colorSchemeSubMenu = null;
//
//				if (curColorSchemeName != null) {
//					colorSchemeSubMenu = new MenuManager(curColorSchemeName);
//				} else {
//					colorSchemeSubMenu = new MenuManager(NO_COLORSCHEME_SELECTED);
//				}
//
//				int count = 0;
//				for (String name : csNames) {
//					SetColorSchemeAction setCSAction = new SetColorSchemeAction(name, viewer, this, ++count);
//					if (count == colorschemeTable.getSelectedColorscheme()) {
//						setCSAction.setChecked(true);
//					}
//					colorSchemeSubMenu.add(setCSAction);
//				}
//
//				colorSchemeSubMenu.add(new Separator());
//				colorSchemeSubMenu.add(addColorSchemeAction);
//				colorSchemeSubMenu.add(renameColorSchemeAction);
//				colorSchemeSubMenu.add(deleteColorSchemeAction);
//				colorSubMenu.removeAll();
//				colorSubMenu.add(colorSchemeSubMenu);
//				colorSubMenu.add(new Separator());
//
//				boolean enableColorActions = colorschemeTable.getSelectedColorscheme() > 0;
//				for (int i = 0; i < setColorActions.length; i++) {
//					setColorActions[i].setEnabled(enableColorActions);
//					setColorActions[i].setChecked(false);
//					colorSubMenu.add(setColorActions[i]);
//				}
//
//				int color = fm.getFeature(coll.getName()).getColorList().getColor();
//				if (ColorList.isValidColor(color)) {
//					setColorActions[color].setChecked(true);
//				}
//
//				menuMgr.add(colorSubMenu);
//			}
//		}
//
//		menuMgr.add(new Separator());
//		MenuManager exportMenu = new MenuManager(EXPORT_AS_LABEL);
//		exportMenu.add(exportAsImage);
//		exportMenu.add(exportAsXML);
//		menuMgr.add(exportMenu);
//	}
	

}