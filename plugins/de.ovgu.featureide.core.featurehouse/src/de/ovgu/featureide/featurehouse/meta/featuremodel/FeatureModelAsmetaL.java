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
package de.ovgu.featureide.featurehouse.meta.featuremodel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.prop4j.Node;
import org.prop4j.NodeWriter;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.editing.NodeCreator;

/**
 * This class provides all information needed, to create the FeatureModel.asm file.
 * 
 * @author Matthias Eiserloh
 */
public class FeatureModelAsmetaL implements IFeatureModelClass{
	private final static String HEAD = "/* \r\n * Variability encoding of the feature model for AsmetaL.\r\n * Auto-generated class.\r\n */\r\nasm FeatureModel \n\n";
	private final static String FIELD_MODIFIER = "\tdynamic controlled ";
	private static final String SELECTFEATURES = "\tstatic valid: Boolean\r\n";
	private FeatureModel featureModel;

	public FeatureModelAsmetaL(FeatureModel featureModel) {
		this.featureModel = featureModel;
	}
	
	@Override
	public String getImports() {
		return "import ../../../StandardLibrary \r\n\r\nexport *\r\n";
	}
		
	@Override
	public String getHead() {
		return HEAD;
	}
 
	@Override
	public String getFeatureFields() {
		StringBuilder fields = new StringBuilder();
		for (String f : featureModel.getFeatureNames()) {
			fields.append(FIELD_MODIFIER);
			fields.append(f.toLowerCase(Locale.ENGLISH) + "__refinementVar__");
			fields.append(": Boolean\r\n");
		}
		
		fields.append(SELECTFEATURES);
		
		return fields.toString();
	}

	@Override
	public String getFormula() {
		final Node nodes = NodeCreator.createNodes(featureModel.clone()).toCNF();//.eliminateNotSupportedSymbols(NodeWriter.javaSymbols);
		String formula = nodes.toString(NodeWriter.textualSymbols).toLowerCase(Locale.ENGLISH);
		if (formula.contains("  &&  true  &&  ! false")) {
			formula = formula.substring(0, formula.indexOf("  &&  true  &&  ! false"));
		}
		Set<String> featureNames = featureModel.getFeatureNames();
		for(String str : featureNames){
			str = str.toLowerCase();
			formula = formula.replaceAll("(\\W|\\A)"+str+"(\\W|\\z)", "$1" + str + "__refinementVar__" + "$2");
		}
		return "function valid =\r\n" + formula + "\r\n";
	}

	@Override
	public String getGetter() {
		return "";
	}

	@Override
	public String getSelection() {
		return "";
	}

}
