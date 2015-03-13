package de.ovgu.featureide.ui.statistics.core.composite.lazyimplementations;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import de.ovgu.featureide.core.fstmodel.FSTClass;
import de.ovgu.featureide.core.fstmodel.FSTClassFragment;
import de.ovgu.featureide.core.fstmodel.FSTFeature;
import de.ovgu.featureide.core.fstmodel.FSTInvariant;
import de.ovgu.featureide.core.fstmodel.FSTMethod;
import de.ovgu.featureide.core.fstmodel.FSTModel;
import de.ovgu.featureide.core.fstmodel.FSTRole;
import de.ovgu.featureide.core.fstmodel.FSTField;
import de.ovgu.featureide.core.fstmodel.IRoleElement;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.ui.statistics.core.composite.LazyParent;
import de.ovgu.featureide.ui.statistics.core.composite.Parent;
import de.ovgu.featureide.ui.statistics.core.composite.lazyimplementations.genericdatatypes.AbstractSortModeNode;

/**
 * displays the sums of the different components in the statistics
 * 
 * @author Schleicher Miro
 */
public class SumImplementationArtifactsParent extends AbstractSortModeNode {
	private FSTModel fstModel;

	private int type;

	public static final int NUMBER_OF_CLASSES = 0;
	public static final int NUMBER_OF_FIELDS = 1;
	public static final int NUMBER_OF_METHODS = 2;
	public static final int NUMBER_OF_INVARIANTS = 3;
	public static final int NUMBER_OF_CONTRACTS = 4;

	public SumImplementationArtifactsParent(String description, FSTModel fstModel, int type) {
		super(description);
		this.fstModel = fstModel;
		this.type = type;
	}

	@Override
	protected void initChildren() {

		if (type == NUMBER_OF_CLASSES) {
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
					addChild(new ClassNodeParent(iterable_element.get(0).getFullIdentifier() + ": " + iterable_element.size(), iterable_element.get(0),
							fstModel));
				}
			}
		} else if (type == NUMBER_OF_FIELDS) {
			LinkedList<FSTField> allFields = new LinkedList<FSTField>();
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : iterable_element) {
						allFields.addAll(fstFrag.getFields());
					}
				}
			}
			while (allFields.size() > 0) {
				addChild(new FieldNodeParent(allFields.get(0).getFullIdentifier() + " : " + allFields.get(0).getType(), allFields.get(0), allFields));
				int pointer = 0;
				String fullI = allFields.get(0).getFullIdentifier();
				while (pointer < allFields.size()) {
					if (allFields.get(pointer).getFullIdentifier().equals(fullI)) {
						allFields.remove(pointer);
						pointer--;
					}
					pointer++;
				}
			}

		} else if (type == NUMBER_OF_METHODS) {
			LinkedList<FSTMethod> allMethods = new LinkedList<FSTMethod>();
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : iterable_element) {
						allMethods.addAll(fstFrag.getMethods());
					}
				}
			}
			while (allMethods.size() > 0) {
				addChild(new MethodNodeParent(allMethods.get(0).getFullIdentifier() + " : " + allMethods.get(0).getType(), allMethods.get(0), allMethods));
				int pointer = 0;
				String fullI = allMethods.get(0).getFullIdentifier();
				while (pointer < allMethods.size()) {
					if (allMethods.get(pointer).getFullIdentifier().equals(fullI)) {
						allMethods.remove(pointer);
						pointer--;
					}
					pointer++;
				}
			}
		} else if (type == NUMBER_OF_INVARIANTS) {
			LinkedList<FSTInvariant> allInvariants = new LinkedList<FSTInvariant>();
			
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : iterable_element) {
						allInvariants.addAll(fstFrag.getInvariants());
					}
				}
			}
			while (allInvariants.size() > 0) {
				addChild(new InvariantNodeParent(allInvariants.get(0).getRole().getClassFragment().getFullIdentifier(), allInvariants.get(0), allInvariants));
				int pointer = 0;
				String fullI = allInvariants.get(0).getRole().getClassFragment().getFullIdentifier();
				while (pointer < allInvariants.size()) {
					if (allInvariants.get(pointer).getRole().getClassFragment().getFullIdentifier().equals(fullI)) {
						allInvariants.remove(pointer);
						pointer--;
					}
					pointer++;
				}
			}

		}else if(type == NUMBER_OF_CONTRACTS){
			
			LinkedList<FSTMethod> allMethodContracts = new LinkedList<FSTMethod>();
			
			//�ber alle klassen-->�ber alle rollen --> �ber alle methoden -->wenn es einen kontrakt hat z�hlen
			
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : iterable_element) {
						for (FSTMethod method : fstFrag.getMethods()) {
							if (method.hasContract()){				}
								allMethodContracts.add(method);
							}
						}
						
					}
				}
			}
		//Liste mit allen Methoden in denen es Contracts gibt
			
		
//		Z�hlen, wenn es sich um die gleiche Methode handelt
		
//			while (allMethodContracts.size() > 0) {
//				addChild(new MethodContractNodeParent(allMethodContracts.get(0).getRole().getClassFragment().getFullIdentifier(), allMethodContracts.get(0), allMethodContracts));
//				int pointer = 0;
//				String fullI = allInvariants.get(0).getRole().getClassFragment().getFullIdentifier();
//				while (pointer < allMethodContracts.size()) {
//					if (allMethodContracts.get(pointer).getRole().getClassFragment().getFullIdentifier().equals(fullI)) {
//						allMethodContracts.remove(pointer);
//						pointer--;
//					}
//					pointer++;
//				}
//			
			
//		}//CONTRACTS

	}

}
