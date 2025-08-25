package cz.komix.qsystem.backend.service;

import cz.komix.qsystem.backend.logic.usecases.IQSystemUseCases;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FEQueryEntityType;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FESearchCriteria;
import cz.komix.qsystem.backend.persistence.appfeatures.IAppFeatures;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * List of methods dealing with app features provided by backend.
 *
 * @author Jan Lejnar
 */
@RestController
@Named
public class AppFeaturesService {

    private IAppFeatures appFeatures;
    private IQSystemUseCases useCases;

    @Value("${app.version}")
    private String appVersion;

    @Inject
    public AppFeaturesService(IAppFeatures appFeatures, IQSystemUseCases useCases) {
        this.appFeatures = appFeatures;
        this.useCases = useCases;
    }

    /**
     * Get all query types that Q-system provides.
     *
     * @return Sorted query types
     */
    public SortedSet<JaxbQueryType> getAllQueryTypes() {
        return appFeatures.getAllQueryTypes();
    }

    /**
     * Get all query modifiers that Q-system provides.
     *
     * @return Sorted query modifiers
     */
    public SortedSet<JaxbQueryModifier> getAllQueryModifiers() {
        return appFeatures.getAllQueryModifiers();
    }

    /**
     * Get all single-category query entities that Q-system can query for.
     *
     * @return Map for quick entity search, e.g. keys: 'Person', 'Vehicle', ...
     */
    @Deprecated
    public Map<String, JaxbQueryEntityType> getAllSingleCategoryQueryEntities() {
        return appFeatures.getAllSingleCategoryQueryEntities();
    }

    public List<FEQueryEntityType> getAllSingleCategoryQueryEntitiesWithSelectableInfo() {
        return useCases.getAllSingleCategoryQueryEntities();
    }

    /**
     * Get all multi-category query entities that Q-system can query for.
     *
     * @return Map for quick entity search, e.g. keys: ...
     */
    @Deprecated
    public Map<String, JaxbQueryEntityType> getAllMultiCategoryQueryEntities() {
        return appFeatures.getAllMultiCategoryQueryEntities();
    }


    public List<FEQueryEntityType> getAllMultiCategoryQueryEntitiesWithSelectableInfo() {
        return useCases.getAllMultiCategoryQueryEntities();
    }

    /**
     * Get query entities sorted in order to be displayed on the frontend.
     *
     * @param map Map from method {@link #getAllSingleCategoryQueryEntities()} or {@link #getAllMultiCategoryQueryEntities()} ()}
     * @return Sorted query entities
     */
    @Deprecated
    public List<JaxbQueryEntityType> sortQueryEntitiesByOrder(Map<String, JaxbQueryEntityType> map) {
        return appFeatures.sortQueryEntitiesByOrder(map);
    }

    /**
     * Get all search criteria (with allowed modifiers for each, frontend types / validations) for concrete query entity.
     *
     * @param queryEntityTypeCategory Category of searched query entity. 'single_category' or 'multi_category'
     * @param queryEntityType         Name of query entity, e.g. 'Person'
     * @return Map, key is search criteria name, value is search criteria itself.
     */
    public Map<String, JaxbSearchCriteria> getAllSearchCriteria(String queryEntityTypeCategory, String queryEntityType) {
        return appFeatures.getAllSearchCriteria(queryEntityTypeCategory, queryEntityType);
    }

    public List<FESearchCriteria> getAllSearchCriteriaWithCheckableInfo(String queryEntityTypeCategory, String queryEntityType) {
        return useCases.getAllSearchCriteriaWithCheckedInfo(queryEntityTypeCategory, queryEntityType);
    }

    /**
     * Get search criteria sorted in order to be displayed on the frontend.
     *
     * @param map Map from method {@link #getAllSingleCategoryQueryEntities()}
     * @return Sorted query entities
     */
    @Deprecated
    public List<JaxbSearchCriteria> sortSearchCriteriaByOrder(Map<String, JaxbSearchCriteria> map) {
        return appFeatures.sortSearchCriteriaByOrder(map);
    }

    /**
     * Get all search criteria combinations, that concrete query entity can use. ={@literal >} FE validation.
     * E.g. single_category:person ={@literal >} [Family Names + Date of Birth] = 1 search criteria combination
     *
     * @param queryEntityTypeCategory Category of searched query entity. 'single_category' or 'multi_category'
     * @param queryEntityType         Name of query entity, e.g. 'Person'
     * @return Set of all combinations
     */
    public Set<JaxbSearchCriteriaCombination> getAllAllowedSearchCriteriaCombinations(String queryEntityTypeCategory, String queryEntityType) {
        return appFeatures.getAllAllowedSearchCriteriaCombinations(queryEntityTypeCategory, queryEntityType);
    }

    /**
     * Find out, if this search criteria combination is valid for specific query entity.
     * @param queryEntityTypeCategory
     * @param queryEntityType
     * @param searchCriteria
     * @return
     */
    public boolean isAllowedSearchCriteriaCombination(String queryEntityTypeCategory, String queryEntityType, String... searchCriteria) {
        JaxbSearchCriteriaCombination searchCriteriaCombination = new JaxbSearchCriteriaCombination();
        searchCriteriaCombination.setSearchCriteriaCombination(new HashSet<>());

        for (String searchCriteriaName :
                searchCriteria) {
            JaxbSearchCriteria jaxbSearchCriteria = new JaxbSearchCriteria();
            jaxbSearchCriteria.setName(searchCriteriaName);
            searchCriteriaCombination.getSearchCriteriaCombination().add(jaxbSearchCriteria);
        }

        return appFeatures.isAllowedSearchCriteriaCombination(searchCriteriaCombination, queryEntityTypeCategory, queryEntityType);
    }

    /**
     * O01
     *
     * @return version of application corresponding pom.xml
     */
    public String getAppVersion() {
        return appVersion;
    }
}
