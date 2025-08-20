package cz.komix.qsystem.backend.persistence.appfeatures;

import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * All methods we need to work with app features.
 * App features {see app-features.xml} provides information gained from ICT document
 *
 * @author Jan Lejnar
 */
public interface IAppFeatures {
    SortedSet<JaxbQueryType> getAllQueryTypes();

    SortedSet<JaxbQueryModifier> getAllQueryModifiers();

    Map<String, JaxbQueryEntityType> getAllSingleCategoryQueryEntities();

    Map<String, JaxbQueryEntityType> getAllMultiCategoryQueryEntities();

    List<JaxbQueryEntityType> sortQueryEntitiesByOrder(Map<String, JaxbQueryEntityType> map);

    Map<String, JaxbSearchCriteria> getAllSearchCriteria(String queryEntityTypeCategory, String queryEntityType);

    List<JaxbSearchCriteria> sortSearchCriteriaByOrder(Map<String, JaxbSearchCriteria> map);

    Set<JaxbSearchCriteriaCombination> getAllAllowedSearchCriteriaCombinations(String queryEntityTypeCategory, String queryEntityType);

    boolean isAllowedSearchCriteriaCombination(JaxbSearchCriteriaCombination searchCriteriaCombination, String queryEntityTypeCategory, String queryEntityType);


}
