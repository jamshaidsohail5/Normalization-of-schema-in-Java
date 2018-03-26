package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Contains static utility methods
 * 
 * @author Raymond Cho
 * 
 */
public class RDTUtils {
	
	public static final String functionalDependencyArrow = "\u2192";
	public static final String multivaliedDependencyArrow = "\u21A0";
	public static final String LONG_LEFTWARDS_ARROW = "<---";

	/**
	 * @param attributeList
	 * @param attribute
	 * @return True if the attribute is contained in the attributeList and false
	 *         otherwise.
	 */
	protected static boolean attributeListContainsAttribute(final List<Attribute> attributeList,
			final Attribute attribute) {
		for (Attribute a : attributeList) {
			if (a.getName().equals(attribute.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param attributeList
	 * @return True if the given attribute list does not contain duplicate attributes and false otherwise.
	 */
	protected static boolean attributeListContainsUniqueAttributes(final List<Attribute> attributeList) {
		for (int i = 0; i < attributeList.size(); i++) {
			for (int j = 0; j < attributeList.size(); j++) {
				if (i != j && attributeList.get(i).getName().equals(attributeList.get(j).getName())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param firstAttributeList
	 * @param secondAttributeList
	 * @return True if second list's attributes are all found in the first list
	 *         and false otherwise.
	 */
	protected static boolean isAttributeListSubsetOfOtherAttributeList(final List<Attribute> firstAttributeList,
			final List<Attribute> secondAttributeList) {
		if (firstAttributeList.size() < secondAttributeList.size()) {
			return false;
		}
		for (Attribute a : secondAttributeList) {
			if (!attributeListContainsAttribute(firstAttributeList, a)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param closureList
	 * @param attribute
	 * @return True if the attribute is contained in a closure that is in the
	 *         closureList and false otherwise.
	 */
	protected static boolean closureListContainsAttribute(final List<Closure> closureList, final Attribute attribute) {
		for (Closure c : closureList) {
			for (Attribute a : c.getClosureOf()) {
				if (a.getName().equals(attribute.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param firstClosure
	 * @param secondClosure
	 * @return True if second closure is a proper subset of the first closure
	 *         and false otherwise.
	 */
	protected static boolean isClosureProperSubsetOfOtherClosure(final Closure firstClosure, final Closure secondClosure) {
		if (firstClosure.getClosureOf().size() <= secondClosure.getClosureOf().size()) {
			return false;
		}
		for (Attribute a : secondClosure.getClosureOf()) {
			if (!attributeListContainsAttribute(firstClosure.getClosureOf(), a)) {
				return false;
			}
		}
		return true;
	}
	
	protected static boolean isFunctionalDependencyAlreadyInFDList(final FunctionalDependency fd, final List<FunctionalDependency> fdList) {
		if (fd == null || fdList == null) {
			throw new IllegalArgumentException("Input null functional dependency or list of functional dependencies.");
		}
		if (fdList.isEmpty()) {
			return false;
		}
		HashMap<String, HashSet<String>> dependencyMap = new HashMap<>();
		for (FunctionalDependency f : fdList) {
			String fKey = f.getLeftHandNameKey();
			HashSet<String> fdAttrs = dependencyMap.get(fKey);
			if (fdAttrs == null) {
				fdAttrs = new HashSet<>();
			}
			for (Attribute a : f.getRightHandAttributes()) {
				fdAttrs.add(a.getName());
			}
			dependencyMap.put(fKey, fdAttrs);
		}
		HashSet<String> fdCompareAttrs = dependencyMap.get(fd.getLeftHandNameKey());
		if (fdCompareAttrs != null) {
			for (Attribute fdCompareAttr : fd.getRightHandAttributes()) {
				if (!fdCompareAttrs.contains(fdCompareAttr.getName())) {
					return false;
				}
			}
			return true;
		}

		return false;
	}
		
	/**
	 * 
	 * @param leftHand
	 * @param closureList
	 * @return Closure object in input closureList of which the left-hand side attributes match input list of attributes, or null
	 * if such Closure object does not exist in input closureList.
	 */
	protected static Closure findClosureWithLeftHandAttributes(final List<Attribute> leftHand, final List<Closure> closureList) {
		Closure closure = null;
		for (Closure c : closureList) {
			if (c.getClosureOf().size() == leftHand.size()) {
				boolean containsAll = true;
				for (Attribute attr : leftHand) {
					if (!attributeListContainsAttribute(c.getClosureOf(), attr)) {
						containsAll = false;
						break;
					}
				}
				if (containsAll) {
					closure = c;
					break;
				}
			}
		}
		return closure;
	}

	/**
	 * 
	 * @param fdList
	 * @param decomposedRAttrs
	 * @return List of all functional dependencies from input FD list that have
	 *         attributes that match the input list of attributes.
	 */
	protected static List<FunctionalDependency> fetchFDsOfDecomposedR(final List<FunctionalDependency> fdList,
			final List<Attribute> decomposedRAttrs) {
		List<FunctionalDependency> result = new ArrayList<>();
		if (fdList == null || decomposedRAttrs == null || fdList.isEmpty() || decomposedRAttrs.isEmpty()) {
			return result;
		}
		for (FunctionalDependency f : fdList) {
			List<Attribute> fdAttrs = new ArrayList<>();
			for (Attribute a : f.getLeftHandAttributes()) {
				if (!attributeListContainsAttribute(fdAttrs, a)) {
					fdAttrs.add(a);
				}
			}
			for (Attribute a : f.getRightHandAttributes()) {
				if (!attributeListContainsAttribute(fdAttrs, a)) {
					fdAttrs.add(a);
				}
			}
			if (isAttributeListSubsetOfOtherAttributeList(decomposedRAttrs, fdAttrs)) {
				result.add(f);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param fdList
	 * @param relation
	 * @return List of minimum canonical cover of functional dependencies each with single right hand side attributes.
	 */
	protected static List<FunctionalDependency> getSingleAttributeMinimalCoverList(final List<FunctionalDependency> fdList, final Relation relation) {
		List<FunctionalDependency> result = new ArrayList<>();
		for (FunctionalDependency fd : fdList) {
			if (fd.getRightHandAttributes().size() > 1) {
				for (Attribute a : fd.getRightHandAttributes()) {
					List<Attribute> singleRightAttribute = new ArrayList<>();
					singleRightAttribute.add(a);
					FunctionalDependency split = new FunctionalDependency(fd.getLeftHandAttributes(), singleRightAttribute, relation);
					result.add(split);
				}
			} else {
				result.add(fd);
			}
		}
		return result;
	}
}
