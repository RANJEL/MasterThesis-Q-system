package cz.komix.qsystem.frontend.controller.callService;

import cz.komix.qsystem.backend.logic.usecases.fewrapper.FEQueryEntityType;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FESearchCriteria;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryModifier;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryType;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbSearchCriteriaCombination;
import cz.komix.qsystem.backend.service.AppFeaturesService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Jan Lejnar
 */
@Named
@ApplicationScoped
public class AppFeaturesController {

    private AppFeaturesService appFeaturesService;

    @Inject
    public AppFeaturesController(AppFeaturesService appFeaturesService) {
        this.appFeaturesService = appFeaturesService;
    }

    public SortedSet<JaxbQueryType> getAllQueryTypes() {
        return appFeaturesService.getAllQueryTypes();
    }

    public SortedSet<JaxbQueryModifier> getAllQueryModifiers() {
        return appFeaturesService.getAllQueryModifiers();
    }

    public List<FEQueryEntityType> getAllSingleCategoryQueryEntities() {
        return appFeaturesService.getAllSingleCategoryQueryEntitiesWithSelectableInfo();
    }

    public List<FEQueryEntityType> getAllMultiCategoryQueryEntities() {
        return appFeaturesService.getAllMultiCategoryQueryEntitiesWithSelectableInfo();
    }

    public List<FESearchCriteria> getAllSearchCriteria(String queryEntityTypeCategory, String queryEntityType) {
        return appFeaturesService.getAllSearchCriteriaWithCheckableInfo(queryEntityTypeCategory, queryEntityType);
    }

    public Set<JaxbSearchCriteriaCombination> getAllAllowedSearchCriteriaCombinations(String queryEntityTypeCategory, String queryEntityType) {
        return appFeaturesService.getAllAllowedSearchCriteriaCombinations(queryEntityTypeCategory, queryEntityType);
    }

    public boolean isAllowedSearchCriteriaCombination(String queryEntityTypeCategory, String queryEntityType, String... searchCriteria) {
        return appFeaturesService.isAllowedSearchCriteriaCombination
                (queryEntityTypeCategory, queryEntityType, searchCriteria);
    } // TODO verify on submit

    public void testMethod() {
        System.out.println("appFeaturesControler testMethod");
    }

    public String getAppVersion() {
        return appFeaturesService.getAppVersion();
    }
}
