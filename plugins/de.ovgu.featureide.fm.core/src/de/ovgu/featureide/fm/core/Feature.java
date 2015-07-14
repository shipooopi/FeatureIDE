/* FeatureIDE - A Framework for Feature-Oriented Software Development
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
package de.ovgu.featureide.fm.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.prop4j.NodeWriter;

import de.ovgu.featureide.fm.core.IGraphicItem.GraphicItem;

/**
 * Provides all properties of a feature. This includes its connections to parent
 * and child features.
 * 
 * @author Thomas Thuem
 * @author Marcus Pinnecke, July 2015
 * 
 */
public class Feature implements PropertyConstants, PropertyChangeListener, IGraphicItem {
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	F I E L D S
	//
	//---------------------------------------------------------------------------------------------------------------------------------------------------------

	private ModelContext context;

	private Properties properties;

	private Graphics graphics;
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	G E T T E R
	//
	//---------------------------------------------------------------------------------------------------------------------------------------------------------

	public Properties getProperties() {
		return properties;
	}

	public Graphics getGraphicalRepresentation() {
		return graphics;
	}

	public ModelContext getContext() {
		return context;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	C O N S T R U C T O R S
	//
	//---------------------------------------------------------------------------------------------------------------------------------------------------------

	public Feature(FeatureModel featureModel) {
		this(featureModel, "Unknown");
	}

	public Feature(FeatureModel featureModel, String name) {
		this.context = new ModelContext(this, featureModel);
		this.properties = new Properties(name, this);
		this.graphics = new Graphics(this, 0, 0);
	}

	protected Feature(Feature feature, FeatureModel featureModel, boolean complete) {
		this.context.setFeatureModel(featureModel);
		this.properties = new Properties(feature.getProperties(), this);

		if (complete) {
			this.graphics = new Graphics(feature.graphics, feature);
		} else {
			this.graphics = new Graphics(this);
		}

		this.context.getFeatureModel().addFeature(this);
		for (Feature child : feature.context.getChildren()) {
			Feature thisChild = this.context.getFeatureModel().getFeature(child.getName());
			if (thisChild == null) {
				thisChild = child.clone(featureModel, complete);
			}
			this.context.getFeatureModel().addFeature(thisChild);
			context.getChildren().add(thisChild);
		}

		if (feature.context.getParent() != null) {
			this.context.setParent(this.context.getFeatureModel().getFeature(feature.context.getParent().getName()));
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	S T A T I C   H E L P E R   F U N C T I O N S
	//
	//---------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Changes <b>feature</b> to <i>and</i> and returns the result. 
	 * Please note, that the input feature is actually changed and not cloned.
	 * 
	 * @param feature
	 * @return the input feature but changed
	 */
	public static Feature changeToAnd(Feature feature) {
		feature.getProperties().setAnd(true);
		feature.getProperties().setMultiple(false);
		feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		return feature;
	}

	/**
	 * Changes <b>feature</b> to <i>or</i> and returns the result. 
	 * Please note, that the input feature is actually changed and not cloned.
	 * 
	 * @param feature
	 * @return the input feature but changed
	 */
	public static Feature changeToOr(Feature feature) {
		feature.getProperties().setAnd(false);
		feature.getProperties().setMultiple(true);
		feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		return feature;
	}
	
	/**
	 * Changes <b>feature</b> to <i>alternative</i> and returns the result. 
	 * Please note, that the input feature is actually changed and not cloned.
	 * 
	 * @param feature
	 * @return the input feature but changed
	 */
	public static Feature changeToAlternative(Feature feature) {
		feature.getProperties().setAnd(false);
		feature.getProperties().setMultiple(false);
		feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		return feature;
	}
	
	/**
	 * Set <b>feature</b> to <i>end</i> and returns the result. 
	 * Please note, that the input feature is actually changed and not cloned.
	 * 
	 * @param feature
	 * @return the input feature but changed
	 */
	public static Feature setAND(Feature feature, boolean and) {
		feature.getProperties().setAnd(and);
		feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		return feature;
	}
	
	public static final Collection<String> extractOperatorNamesFromFeatuers(final Set<String> features) {
		List<String> result = new ArrayList<>();
		for (String feature : features) {
			final String str = feature.toLowerCase().trim();
			if (Operator.isOperatorName(str))
				result.add(str);
		}
		return result;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	//
	// C L O N I N G   A N D   T O   S T R I N G
	//
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Returns the value of clone(this.getFeatureModel(), true).
	 * 
	 * @return a deep copy from the feature
	 * 
	 * @see #clone(FeatureModel, boolean)
	 */
	@Override
	public Feature clone() {
		return clone(getFeatureModel(), true);
	}

	/**
	 * Clones the feature.
	 * If the parent feature is not contained in the given feature model, the cloned features parent will be {@code null}.
	 * 
	 * @param featureModel the new feature model, which is assigned to the copy.
	 * @param complete If {@code false} the fields colorList and location will not be copied for a faster cloning process.
	 * @return a deep copy from the feature
	 * 
	 * @see FeatureModel#clone()
	 * @see FeatureModel#clone(boolean)
	 */
	public Feature clone(FeatureModel featureModel, boolean complete) {
		return new Feature(this, featureModel, complete);
	}
	

	@Override
	public int hashCode() {
		return properties.getName().hashCode();
	}

	@Override
	public String toString() {
		return properties.getName();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

	}

	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	F E A T U R E   M O D E L   C O N T E X T   C L A S S
	//
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	
	public static class ModelContext {
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	F I E L D S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------
		
		private static final List<FeatureConnection> EMPTY_LIST = Collections.<FeatureConnection> emptyList();
		
		private List<Constraint> partOfConstraints;
		
		private FeatureModel featureModel;
		
		private Feature parent;
		
		private Feature feature;
		
		private LinkedList<Feature> children;
		
		private FeatureConnection parentConnection;
		
		private List<FeatureConnection> sourceConnections;

		private LinkedList<FeatureConnection> targetConnections;
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	C O N S T R U C T O R S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------

		public ModelContext(Feature currentFeature, FeatureModel featureModel) {
			this(new LinkedList<Constraint>(), new LinkedList<Feature>(), new FeatureConnection(currentFeature), new LinkedList<FeatureConnection>(), new LinkedList<FeatureConnection>(), featureModel, currentFeature);
		}

		
		public ModelContext(List<Constraint> partOfConstraints, LinkedList<Feature> children, FeatureConnection parentConnection,
				LinkedList<FeatureConnection> sourceConnections, LinkedList<FeatureConnection> targetConnections, FeatureModel featureModel, Feature currentFeature) {
			this.partOfConstraints = partOfConstraints;
			this.children = children;
			this.parentConnection = parentConnection;
			this.sourceConnections = sourceConnections;
			this.targetConnections = targetConnections;
			this.featureModel = featureModel;
			this.feature = currentFeature;
			setParent(null);
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	P U B L I C   M E T H O D S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------

		public void addChild(Feature newChild) {
			getChildren().add(newChild);
			newChild.getContext().setParent(feature);
			feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);		
		}

		public void addChildAtPosition(int index, Feature newChild) {
			getChildren().add(index, newChild);
			newChild.getContext().setParent(feature);
			feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		}

		public void addTargetConnection(FeatureConnection connection) {
			getTargetConnections().add(connection);
		}

		public int getChildIndex(Feature feature) {
			return getChildren().indexOf(feature);
		}

		public LinkedList<Feature> getChildren() {
			return children;
		}

		public int getChildrenCount() {
			return getChildren().size();
		}

		public FeatureModel getFeatureModel() {
			return featureModel;
		}

		public Feature getFirstChild() {
			if (getChildren().isEmpty())
				return null;
			return getChildren().get(0);
		}

		public Feature getLastChild() {
			if (!getChildren().isEmpty()) {
				return getChildren().getLast();
			}
			return null;
		}

		public Feature getParent() {
			return parent;
		}
		
		public FeatureConnection getParentConnection() {
			return parentConnection;
		}

		public List<Constraint> getPartOfConstraints() {
			return partOfConstraints;
		}

		public String getRelevantConstraintsString() {
			StringBuilder relevant = new StringBuilder();
			for (Constraint constraint : getFeatureModel().getConstraints()) {
				for (Feature f : constraint.getContainedFeatures()) {
					if (f.getProperties().getName().equals(feature.getProperties().getName())) {
						relevant.append((relevant.length() == 0 ? " " : "\n ") + constraint.getNode().toString(NodeWriter.logicalSymbols) + " ");
						break;
					}
				}
			}
			return relevant.toString();
		}

		public List<FeatureConnection> getSourceConnections() {
			return getParent() == null ? EMPTY_LIST : sourceConnections;
		}

		public LinkedList<FeatureConnection> getTargetConnections() {
			return targetConnections;
		}

		public boolean hasChildren() {
			return !getChildren().isEmpty();
		}

		public boolean isAncestorOf(Feature next) {
			while (next.getContext().getParent() != null) {
				if (next.getContext().getParent() == feature)
					return true;
				next = next.getContext().getParent();
			}
			return false;
		}

		public boolean isAndPossible() {
			if (getParent() == null || getParent().getProperties().isAnd())
				return false;
			for (Feature child : getChildren()) {
				if (child.getProperties().isAnd())
					return false;
			}
			return true;
		}

		public boolean isFirstChild(Feature child) {
			return getChildren().indexOf(child) == 0;
		}

		public boolean isRoot() {
			return getParent() == null;
		}

		public void removeChild(Feature child) {
			getChildren().remove(child);
			child.getContext().setParent(null);
			feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		}

		public Feature removeLastChild() {
			Feature child = getChildren().removeLast();
			child.getContext().setParent(null);
			feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
			return child;
		}

		public boolean removeTargetConnection(FeatureConnection connection) {
			return getTargetConnections().remove(connection);
		}

		public void replaceChild(Feature oldChild, Feature newChild) {
			int index = getChildren().indexOf(oldChild);
			getChildren().set(index, newChild);
			oldChild.getContext().setParent(null);
			newChild.getContext().setParent(feature);
			feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		}

		public void setChildren(LinkedList<Feature> children) {
			if (getChildren() == children)
				return;
			for (Feature child : children) {
				child.getContext().setParent(feature);
			}
			this.children = children;
			feature.getProperties().getEventListener().fireEventForListeners(feature.getProperties().EVENT_CHILDREN_CHANGED);
		}

		public void setFeatureModel(FeatureModel featureModel) {
			this.featureModel = featureModel;
		}

		public void setParent(Feature newParent) {
			if (newParent == parent)
				return;

			// delete old parent connection (if existing)
			if (parent != null) {
				parent.removeTargetConnection(getParentConnection());
				parent.getContext().getParentConnection().setTarget(null);
			}

			// update the target
			parent = newParent;
			if (newParent != null) {
				getParentConnection().setTarget(newParent);
				newParent.addTargetConnection(getParentConnection());
			}
		}

		public void setParentConnection(FeatureConnection parentConnection) {
			this.parentConnection = parentConnection;
		}

		public void setPartOfConstraints(List<Constraint> partOfConstraints) {
			this.partOfConstraints = partOfConstraints;
		}

		public void setRelevantConstraints() {
			List<Constraint> constraintList = new LinkedList<Constraint>();
			for (Constraint constraint : getFeatureModel().getConstraints()) {
				for (Feature f : constraint.getContainedFeatures()) {
					if (f.getProperties().getName().equals(feature.getProperties().getName())) {
						constraintList.add(constraint);
						break;
					}
				}
			}
			setPartOfConstraints(constraintList);
		}

		public void setSourceConnections(LinkedList<FeatureConnection> sourceConnections) {
			this.sourceConnections = sourceConnections;
		}

		public void setTargetConnections(LinkedList<FeatureConnection> targetConnections) {
			this.targetConnections = targetConnections;
		}
		
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	F E A T U R E   P R O P E R T E I S   C L A S S
	//
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	
	public class Properties {
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	F I E L D S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------

		private Feature feature;

		public PropertyChangeEvent EVENT_NAME_CHANGED;

		public PropertyChangeEvent EVENT_HIDDEN_CHANGED;

		public PropertyChangeEvent EVENT_CHILDREN_CHANGED;

		public PropertyChangeEvent EVENT_MANDATORY_CHANGED;

		private PropertyChangedEventListener eventListeners = new PropertyChangedEventListener();

		private String name;

		private FeatureStatus status;
		
		private boolean and;

		private boolean multiple;
		
		private boolean mandatory;
		
		private boolean concret;
		
		private boolean hidden;
		
		private boolean constraintSelected;
		
		private String description;
				
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	C O N S T R U C T O R
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------
		
		public Properties(Properties properties, Feature feature) {
			this.feature = feature;

			name = properties.getName();
			mandatory = properties.isMandatory();
			concret = properties.isConcrete();
			and = properties.isAnd();
			multiple = properties.isMultiple();
			hidden = properties.isHidden();
			constraintSelected = properties.isConstraintSelected();
			status = properties.getStatus();
			description = properties.getDescription();
			initEvents();
		}
		
		/**
		 * @param name
		 * 
		 */
		public Properties(String name, Feature feature) {
			this.feature = feature;

			this.name = name;
			mandatory = false;
			concret = true;
			and = true;
			multiple = false;
			hidden = false;
			constraintSelected = false;
			status = FeatureStatus.NORMAL;
			description = null;
			initEvents();
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	P U B L I C   M E T H O D S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------

		public String getDescription() {
			return description;
		}

		public PropertyChangedEventListener getEventListener() {
			return eventListeners;
		}

		public String getName() {
			return name;
		}

		public FeatureStatus getStatus() {
			return status;
		}

		public boolean hasHiddenParent() {
			if (feature.getProperties().isHidden())
				return true;
			if (feature.getContext().isRoot())
				return false;
			Feature p = feature.getContext().getParent();

			while (!p.getContext().isRoot()) {
				if (p.getProperties().isHidden())
					return true;
				p = p.getContext().getParent();
			}

			return false;
		}

		public boolean hasInlineRune() {
			return feature.getContext().getChildrenCount() > 1 && isAnd() && isMandatory() && !isMultiple();
		}

		private void initEvents() {
			EVENT_NAME_CHANGED = new PropertyChangeEvent(this, PropertyConstants.NAME_CHANGED, Boolean.FALSE, Boolean.TRUE);
			EVENT_HIDDEN_CHANGED = new PropertyChangeEvent(this, PropertyConstants.HIDDEN_CHANGED, Boolean.FALSE, Boolean.TRUE);
			EVENT_CHILDREN_CHANGED = new PropertyChangeEvent(this, PropertyConstants.CHILDREN_CHANGED, Boolean.FALSE, Boolean.TRUE);
			EVENT_MANDATORY_CHANGED = new PropertyChangeEvent(this, PropertyConstants.MANDATORY_CHANGED, Boolean.FALSE, Boolean.TRUE);
		}

		public boolean isAbstract() {
			return !isConcrete();
		}

		public boolean isAlternative() {
			return !isAnd() && !isMultiple();
		}

		public boolean isAnd() {
			return and;
		}

		public boolean isConcrete() {
			return concret;
		}

		public boolean isConstraintSelected() {
			return constraintSelected;
		}

		public boolean isHidden() {
			return hidden;
		}

		public boolean isMandatory() {
			return feature.getContext().getParent() == null || !feature.getContext().getParent().getProperties().isAnd() || mandatory;
		}

		public boolean isMandatorySet() {
			return isMandatory();
		}

		public boolean isMultiple() {
			return multiple;
		}

		public boolean isOr() {
			return !isAnd() && isMultiple();
		}

		public void setAbstract(boolean b) {
			setConcret(!b);
		}

		public void setAlternative() {
			setAnd(false);
			setMultiple(false);
		}

		public void setAnd() {
			setAnd(true);
		}

		void setAnd(boolean b) {
			and = b;
			getEventListener().fireEventForListeners(EVENT_CHILDREN_CHANGED);
		}

		public void setConcret(boolean b) {
			this.concret = b;
			eventListeners.fireEventForListeners(EVENT_CHILDREN_CHANGED);
		}

		public void setConstraintSelected(boolean selection) {
			this.constraintSelected = selection;
			getEventListener().fireEventForListeners(new PropertyChangeEvent(this, PropertyConstants.ATTRIBUTE_CHANGED, Boolean.FALSE, Boolean.TRUE));
		}

		public void setDescription(String description2) {
			this.description = description2;
		}

		public void setHidden(boolean hid) {
			this.hidden = hid;
			eventListeners.fireEventForListeners(EVENT_HIDDEN_CHANGED);
		}

		public void setMandatory(boolean mandatory2) {
			this.mandatory = mandatory2;
			eventListeners.fireEventForListeners(EVENT_MANDATORY_CHANGED);
		}

		void setMultiple(boolean b) {
			this.multiple = b;
			eventListeners.fireEventForListeners(EVENT_CHILDREN_CHANGED);
		}

		public void setName(String name2) {
			this.name = name2;
			eventListeners.fireEventForListeners(EVENT_NAME_CHANGED);
		}

		public void setOr() {
			setAnd(false);
			setMultiple(true);
		}

		public void setStatus(FeatureStatus stat, boolean fire) {
			this.status = stat;
			if (fire)
				getEventListener().fireEventForListeners(new PropertyChangeEvent(this, PropertyConstants.ATTRIBUTE_CHANGED, Boolean.FALSE, Boolean.TRUE));

		}
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	G R A P H I C A L   R E P R E S E N A T I O N 
	//
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	
	public class Graphics {
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	F I E L D S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------
		
		private ColorList colorList;
		
		private FMPoint location;

		private Feature feature;
		
		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	C O N S T R U C T O R S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------
		
		public Graphics(Feature feature) {
			colorList = null;
			location = null;
			this.feature = feature;
		}
		
		public Graphics(Feature feature, int x, int y) {
			colorList = new ColorList(feature);
			location = new FMPoint(0, 0);
			this.feature = feature;
		}
		
		public Graphics(Graphics graphics, Feature feature) {
			setColorList(graphics.getColorList().clone(feature));
			setLocation(new FMPoint(graphics.getLocation().getX(), graphics.getLocation().getY()));
		}

		//-----------------------------------------------------------------------------------------------------------------------------------------
		//
		//	P U B L I C   M E T H O D S
		//
		//-----------------------------------------------------------------------------------------------------------------------------------------

		public ColorList getColorList() {
			return colorList;
		}

		Feature getFeature() {
			return feature;
		}

		public GraphicItem getItemType() {
			return GraphicItem.Feature;
		}

		public FMPoint getLocation() {
			return location;
		}

		public void setColorList(ColorList colorList) {
			this.colorList = colorList;
			
		}

		public void setLocation(FMPoint newLocation) {
			this.location = newLocation;
		}

	}
	
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------
	//
	//	D E P R E C A T E D   A R E A
	//
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------

	// --------------------------------------------------------------------------------------------------------------------------------------------
	//	P R O P E R T I E S
	// --------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @return The description of the Feature.
	 * @deprecated as of release 2.7.5, use {@link getProperties()} instead
	 */
	@CheckForNull
	@Deprecated
	public String getDescription() {
		return properties.getDescription();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#getName()} instead
	 */
	@Deprecated
	public String getDisplayName() {
		return properties.getName();
	}
	
	@Deprecated
	public String toString(boolean writeMarks) {
		if (writeMarks) {
			if (this.properties.getName().contains(" ") || Operator.isOperatorName(this.properties.getName())) {
				return "\"" + this.properties.getName() + "\"";
			}
			return properties.getName();
		} else {
			return toString();
		}
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isAbstract() {
		return properties.isAbstract();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isConcrete() {
		return properties.isConcrete();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isAnd() {
		return properties.isAnd();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isOr() {
		return properties.isOr();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isAlternative() {
		return properties.isAlternative();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isMandatorySet() {
		return properties.isMandatorySet();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isHidden() {
		return properties.isHidden();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isConstraintSelected() {
		return properties.isConstraintSelected();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public FeatureStatus getFeatureStatus() {
		return properties.getStatus();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public boolean isMultiple() {
		return properties.isMultiple();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()} instead
	 */
	@Deprecated
	public String getName() {
		return properties.getName();
	}
	
	/**
	 * @deprecated as of release 2.7.5, use static helper method {@link Feature #changeToAnd(Feature)} instead
	 */
	@Deprecated
	public void changeToAnd() {
		changeToAnd(this);
	}

	/**
	 * @deprecated as of release 2.7.5, use static helper method {@link Feature #changeToOr(Feature)} instead
	 */
	@Deprecated
	public void changeToOr() {
		changeToOr(this);
	}

	/**
	 * @deprecated as of release 2.7.5, use static helper method {@link Feature #changeToAlternative(Feature)} instead
	 */
	@Deprecated
	public void changeToAlternative() {
		changeToAlternative(this);
	}

	/**
	 * @deprecated as of release 2.7.5, use static helper method {@link Feature #setAND(Feature)} instead
	 */
	@Deprecated
	public void setAND(boolean and) {
		properties.setAnd(and);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#isMandatory()} instead
	 */
	@Deprecated
	public boolean isMandatory() {
		return properties.isMandatory();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#hasInlineRule()} instead
	 */
	@Deprecated
	public boolean hasInlineRule() {
		return properties.hasInlineRune();
	}
	
	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setAnd()} instead
	 */
	@Deprecated
	public void setAnd() {
		properties.setAnd();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setOr()} instead
	 */
	@Deprecated
	public void setOr() {
		properties.setOr();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setAlternative()} instead
	 */
	@Deprecated
	public void setAlternative() {
		properties.setAlternative();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#hasHiddenParent()} instead
	 */
	@Deprecated
	public boolean hasHiddenParent() {
		return properties.hasHiddenParent();
	}
	
	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setDescription(String)} instead
	 */
	@Deprecated
	public void setDescription(String description) {
		this.properties.setDescription(description);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setMandatory(boolean} instead
	 */
	@Deprecated
	public void setMandatory(boolean mandatory) {
		this.properties.setMandatory(mandatory);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setHidden(boolean} instead
	 */
	@Deprecated
	public void setHidden(boolean hid) {
		this.properties.setHidden(hid);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setConstraintSelected(boolean} instead
	 */
	@Deprecated
	public void setConstraintSelected(boolean selection) {
		this.properties.setConstraintSelected(selection);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setAbstract(boolean} instead
	 */
	@Deprecated
	public void setAbstract(boolean value) {
		this.properties.setConcret(!value);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setFeatureStatus(FeatureStatus, boolean)} instead
	 */
	@Deprecated
	public void setFeatureStatus(FeatureStatus stat, boolean fire) {
		this.properties.setStatus(stat, fire);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setMultiple(boolean)} instead
	 */
	@Deprecated
	public void setMultiple(boolean multiple) {
		this.properties.setMultiple(multiple);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#setName(String)} instead
	 */
	@Deprecated
	public void setName(String name) {
		this.properties.setName(name);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------
	//	C O N T E X T
	// --------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public boolean isFirstChild(Feature child) {
		return context.isFirstChild(child);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#getChildrenCount()} instead
	 */
	@Deprecated
	public int getChildrenCount() {
		return context.getChildrenCount();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public Feature getFirstChild() {
		return context.getFirstChild();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public Feature getLastChild() {
		return context.getLastChild();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public int getChildIndex(Feature feature) {
		return context.getChildIndex(feature);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public boolean isANDPossible() {
		return context.isAndPossible();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#getSourceConnections()} instead
	 */
	@Deprecated
	public List<FeatureConnection> getSourceConnections() {
		return context.getSourceConnections();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public List<FeatureConnection> getTargetConnections() {
		return context.getTargetConnections();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public Feature getParent() {
		return context.getParent();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#isRoot()} instead
	 */
	@Deprecated
	public boolean isRoot() {
		return context.isRoot();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public LinkedList<Feature> getChildren() {
		return context.getChildren();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public Collection<Constraint> getRelevantConstraints() {
		return context.getPartOfConstraints();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()} instead
	 */
	@Deprecated
	public FeatureModel getFeatureModel() {
		return context.getFeatureModel();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#hasChildren()} instead
	 */
	@Deprecated
	public boolean hasChildren() {
		return context.hasChildren();
	}
	
	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#getRelevantConstraintsString()} instead
	 */
	@Deprecated
	public String getRelevantConstraintsString() {
		return context.getRelevantConstraintsString();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#setRelevantConstraints()} instead
	 */
	@Deprecated
	public void setRelevantConstraints() {
		context.setRelevantConstraints();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#setParent()} instead
	 */
	@Deprecated
	public void setParent(Feature newParent) {
		context.setParent(newParent);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#setChildren(LinkedList)} instead
	 */
	@Deprecated
	public void setChildren(LinkedList<Feature> children) {
		context.setChildren(children);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#addChild(Feature)} instead
	 */
	@Deprecated
	public void addChild(Feature newChild) {
		context.addChild(newChild);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#addChildAtPosition(int, Feature)} instead
	 */
	@Deprecated
	public void addChildAtPosition(int index, Feature newChild) {
		context.addChildAtPosition(index, newChild);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#replaceChild(Feature, Feature)} instead
	 */
	@Deprecated
	public void replaceChild(Feature oldChild, Feature newChild) {
		context.replaceChild(oldChild, newChild);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#removeChild(Feature)} instead
	 */
	@Deprecated
	public void removeChild(Feature child) {
		context.removeChild(child);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#removeLastChild()} instead
	 */
	@Deprecated
	public Feature removeLastChild() {
		return context.removeLastChild();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#addTargetConnection(FeatureConnection)} instead
	 */
	@Deprecated
	public void addTargetConnection(FeatureConnection connection) {
		context.addTargetConnection(connection);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#removeTargetConnection(FeatureConnection)} instead
	 */
	@Deprecated
	public boolean removeTargetConnection(FeatureConnection connection) {
		return context.removeTargetConnection(connection);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getContext()#isAncestorOf(Feature)} instead
	 */
	@Deprecated
	public boolean isAncestorOf(Feature next) {
		return context.isAncestorOf(next);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------
	//	G R A P H I C S
	// --------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @deprecated as of release 2.7.5, use {@link #getGraphicalRepresentation()} instead
	 */
	@Deprecated
	public ColorList getColorList() {
		return graphics.getColorList();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getGraphicalRepresentation()} instead
	 */
	@Deprecated
	@Override
	public GraphicItem getItemType() {
		return graphics.getItemType();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getGraphicalRepresentation()} instead
	 */
	@Deprecated
	public FMPoint getLocation() {
		return graphics.getLocation();
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getGraphicalRepresentation()#setLocation(FMPoint} instead
	 */
	@Deprecated
	public void setNewLocation(FMPoint newLocation) {
		graphics.setLocation(newLocation);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------
	// EVENT LISTENER
	// --------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#getEventListener()#addListener(PropertyChangeListener)} instead
	 */
	public void addListener(PropertyChangeListener listener) {
		properties.getEventListener().addListener(listener);
	}

	/**
	 * @deprecated as of release 2.7.5, use {@link #getProperties()#getEventListener()#removeListener(PropertyChangeListener)} instead
	 */
	public void removeListener(PropertyChangeListener listener) {
		properties.getEventListener().removeListener(listener);
	}

	
}
