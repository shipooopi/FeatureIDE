package de.ovgu.featureide.cide.actions;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import de.ovgu.featureide.cide.dialogs.MarkWithFeatureDialog;
import de.ovgu.featureide.core.cide.ColorXmlManager;


public class MarkWithFeatureAction implements IObjectActionDelegate {

	ColorXmlManager colorXmlManager;
	MarkWithFeatureDialog markWithFeatureDialog = new MarkWithFeatureDialog();
	public ITextEditor activeEditor = null;

	public void run(IAction action) {

		ISelectionProvider selectionProvider = activeEditor.getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();
		ITextSelection textSelection = (ITextSelection) selection;

		Integer offset = Integer.valueOf(textSelection.getOffset());
		Integer offsetEnd = Integer.valueOf(textSelection.getLength()) + Integer.valueOf(textSelection.getOffset());

		FileEditorInput input = (FileEditorInput) activeEditor.getEditorInput();
		IFile file = input.getFile();
		IProject activeProject = file.getProject();

		String activeProjectPath = activeProject.getLocation().toFile().getAbsolutePath();
		String activeProjectPathToFile = file.getLocation().toFile().getAbsolutePath();

		this.colorXmlManager = new ColorXmlManager(activeProjectPath);

		// get selected features from dialog
		ArrayList<String> features = markWithFeatureDialog.open(activeEditor);
		if (features != null) {
			for (String feature : features) {
				this.colorXmlManager.addAnnotation(activeProjectPathToFile, offset, offsetEnd, feature);
				while (this.colorXmlManager.mergeSelections(activeProjectPathToFile, feature))
					;
			}
		}
	}


	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		if (targetPart instanceof ITextEditor) {
			activeEditor = (ITextEditor) targetPart;
		}
	}


	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}
}
