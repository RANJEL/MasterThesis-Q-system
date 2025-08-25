package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single;

import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class SingleCategoryEntityGenerator {
    private final Map<String, AbstractSingleCategoryEntityFactory> entityFactoryMap;

    @Inject
    public SingleCategoryEntityGenerator(
            SingleCategoryEntityPersonFactory singleCategoryEntityPersonFactory,
            SingleCategoryEntityVehicleFactory singleCategoryEntityVehicleFactory) {
        entityFactoryMap = new HashMap<>();
        entityFactoryMap.put(SingleCategoryEntityPersonFactory.ENTITY_NAME, singleCategoryEntityPersonFactory);
        entityFactoryMap.put(SingleCategoryEntityVehicleFactory.ENTITY_NAME, singleCategoryEntityVehicleFactory);
    }

    public SingleCategoryQueryType getSingleCategoryEntity(Map<String, String> paramsMap, String entityName) {
        SingleCategoryQueryType singleCategoryEntity = new SingleCategoryQueryType();

        AbstractSingleCategoryEntityFactory abstractSingleCategoryEntityFactory = entityFactoryMap.get(entityName);
        if (abstractSingleCategoryEntityFactory != null) {
            abstractSingleCategoryEntityFactory.fillQueryEntity(singleCategoryEntity, paramsMap);
        }

        return singleCategoryEntity;
    }
}
