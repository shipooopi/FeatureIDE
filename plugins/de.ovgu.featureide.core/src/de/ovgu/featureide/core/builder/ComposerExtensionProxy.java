/* FeatureIDE - An IDE to support feature-oriented software development
 * Copyright (C) 2005-2010  FeatureIDE Team, University of Magdeburg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * See http://www.fosd.de/featureide/ for further information.
 */
package de.ovgu.featureide.core.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import de.ovgu.featureide.core.CorePlugin;
import de.ovgu.featureide.core.IFeatureProject;


/**
 * Handles a composer extension.
 * 
 * @author Tom Brosch
 */
public class ComposerExtensionProxy implements IComposerExtension {
	
	private final IConfigurationElement configElement;
	private final String name;
	private final String id;
	private final String description;
	private IComposerExtensionClass composerExtensionClass = null;

	public ComposerExtensionProxy(IConfigurationElement configurationElement) {
		this.configElement = configurationElement;
		name = configElement.getAttribute("name");
		id = configElement.getAttribute("id");
		description = configElement.getAttribute("description");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ovgu.featureide.core.builder.ICompositionTool#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ovgu.featureide.core.builder.ICompositionTool#getId()
	 */
	public String getId() {
		return id;
	}

	public String toString() {
		return "Name: " + name + "; ID: " + id;
	}

	/**
	 * Loads the CompositionExtension class if necessary.
	 */
	private void loadComposerExtension() {
		if (composerExtensionClass != null)
			return;
		try {
			composerExtensionClass = (IComposerExtensionClass) configElement
					.createExecutableExtension("class");
		} catch (CoreException e) {
			CorePlugin.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seefeatureide.core.builder.ICompositionTool#initialize(de.ovgu.featureide.core.
	 * IFeatureProject)
	 */
	public void initialize(IFeatureProject project) {
		loadComposerExtension();
		composerExtensionClass.initialize(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ovgu.featureide.core.builder.ICompositionTool#performFullBuild(org.eclipse
	 * .core.resources.IFile)
	 */
	public void performFullBuild(IFile equation) {
		CorePlugin.getDefault().logInfo(
				"Perform a full build for configuration '" + equation + "'");
		initialize(CorePlugin.getFeatureProject(equation));
		composerExtensionClass.performFullBuild(equation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ovgu.featureide.core.builder.ICompositionTool#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ovgu.featureide.core.builder.IComposerExtension#clean()
	 */
	public void clean() {
		loadComposerExtension();
		composerExtensionClass.clean();
	}
}