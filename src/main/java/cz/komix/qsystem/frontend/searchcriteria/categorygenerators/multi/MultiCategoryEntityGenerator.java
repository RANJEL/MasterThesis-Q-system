package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi;

import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class MultiCategoryEntityGenerator {
    private final Map<String, AbstractMultiCategoryEntityFactory> entityFactoryMap;

    @Inject
    public MultiCategoryEntityGenerator(
            MultiCategoryEntityDocumentFactory multiCategoryEntityDocumentFactory,
            MultiCategoryEntityOtherNumberFactory multiCategoryEntityOtherNumberFactory,
            MultiCategoryEntityPersonFactory multiCategoryEntityPersonFactory) {
        entityFactoryMap = new HashMap<>();
        entityFactoryMap.put(MultiCategoryEntityPersonFactory.ENTITY_NAME, multiCategoryEntityPersonFactory);
        entityFactoryMap.put(MultiCategoryEntityOtherNumberFactory.ENTITY_NAME, multiCategoryEntityOtherNumberFactory);
        entityFactoryMap.put(MultiCategoryEntityDocumentFactory.ENTITY_NAME, multiCategoryEntityDocumentFactory);
    }

    public MultiCategoryQueryType getMultiCategoryEntity(Map<String, String> paramsMap, String entityName) {
        MultiCategoryQueryType multiCategoryEntity = new MultiCategoryQueryType();

        AbstractMultiCategoryEntityFactory abstractMultiCategoryEntityFactory = entityFactoryMap.get(entityName);
        if (abstractMultiCategoryEntityFactory != null) {
            abstractMultiCategoryEntityFactory.fillQueryEntity(multiCategoryEntity, paramsMap);
        }

        return multiCategoryEntity;
    }
}
