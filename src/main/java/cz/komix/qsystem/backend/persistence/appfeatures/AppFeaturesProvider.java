package cz.komix.qsystem.backend.persistence.appfeatures;

import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.*;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory.AbstractMultiCategoryEntityFactory;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory.AbstractSingleCategoryEntityFactory;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author Jan Lejnar
 */
@Named
public class AppFeaturesProvider implements IAppFeatures {

    private final Logger logger = LoggerFactory.getLogger(AppFeaturesProvider.class);
    private AppFeaturesDataClass appFeaturesDataClass;

    @Inject
    public AppFeaturesProvider(ResourceLoader resourceLoader) {
        JAXBContext jaxbContext;
        try (BOMInputStream inputStreamWithoutBOM = new BOMInputStream(resourceLoader.getResource(JarResources.APP_FEATURES.getPath()).getInputStream())) {
            jaxbContext = JAXBContext.newInstance(AppFeaturesDataClass.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.appFeaturesDataClass = (AppFeaturesDataClass) jaxbUnmarshaller.unmarshal(inputStreamWithoutBOM);
        } catch (FileNotFoundException e) {
            logger.error("App features file not found", e);
        } catch (IOException e) {
            logger.error("IOException during reading app features file", e);
        } catch (JAXBException e) {
            logger.error("Exception during unmarshalling app features", e);
        }
    }

    @Override
    public SortedSet<JaxbQueryType> getAllQueryTypes() {
        return appFeaturesDataClass.getQueryTypes();
    }

    @Override
    public SortedSet<JaxbQueryModifier> getAllQueryModifiers() {
        return appFeaturesDataClass.getQueryModifiers();
    }

    private JaxbQueryEntityTypeCategory getCategory(String categoryName) {
        JaxbQueryEntityTypeCategory category = appFeaturesDataClass.getQueryEntityTypeCategoriesMap().get(categoryName);
        return category != null ? category : new JaxbQueryEntityTypeCategory();
    }

    private JaxbQueryEntityType getEntityType(String categoryName, String entityTypeName) {
        JaxbQueryEntityTypeCategory category = getCategory(categoryName);
        JaxbQueryEntityType entityType = category.getEntityTypesMap().get(entityTypeName);
        return entityType != null ? entityType : new JaxbQueryEntityType();
    }

    @Override
    public Map<String, JaxbQueryEntityType> getAllSingleCategoryQueryEntities() {
        return getCategory(AbstractSingleCategoryEntityFactory.CATEGORY_NAME).getEntityTypesMap();
    }

    @Override
    public Map<String, JaxbQueryEntityType> getAllMultiCategoryQueryEntities() {
        return getCategory(AbstractMultiCategoryEntityFactory.CATEGORY_NAME).getEntityTypesMap();
    }

    @Override
    public Map<String, JaxbSearchCriteria> getAllSearchCriteria(String queryEntityTypeCategory, String queryEntityType) {
        return getEntityType(queryEntityTypeCategory, queryEntityType).getSearchCriteriaMap();
    }

    @Override
    public List<JaxbQueryEntityType> sortQueryEntitiesByOrder(Map<String, JaxbQueryEntityType> map) {
        map.forEach((key, value) -> value.setName(key));
        List<JaxbQueryEntityType> list = new ArrayList<>(map.values());
        Collections.sort(list);
        return list;
    }

    @Override
    public List<JaxbSearchCriteria> sortSearchCriteriaByOrder(Map<String, JaxbSearchCriteria> map) {
        map.forEach((key, value) -> value.setName(key));
        List<JaxbSearchCriteria> list = new ArrayList<>(map.values());
        Collections.sort(list);
        return list;
    }

    @Override
    public Set<JaxbSearchCriteriaCombination> getAllAllowedSearchCriteriaCombinations(String queryEntityTypeCategory, String queryEntityType) {
        return getEntityType(queryEntityTypeCategory, queryEntityType).getAllowedSearchCriteriaCombinations();
    }

    @Override
    public boolean isAllowedSearchCriteriaCombination(JaxbSearchCriteriaCombination searchCriteriaCombination, String queryEntityTypeCategory, String queryEntityType) {
        return getAllAllowedSearchCriteriaCombinations(queryEntityTypeCategory, queryEntityType).contains(searchCriteriaCombination);
    }
}
