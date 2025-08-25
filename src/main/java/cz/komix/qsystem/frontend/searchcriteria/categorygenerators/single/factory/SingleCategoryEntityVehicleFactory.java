package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory;

import javax.inject.Named;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class SingleCategoryEntityVehicleFactory extends AbstractSingleCategoryEntityFactory {
    public static final String ENTITY_NAME = "vehicle";

    @Override
    protected String getParam(String paramName, Map<String, String> paramsMap) {
        return getParam(paramName, paramsMap, CATEGORY_NAME, ENTITY_NAME);
    }

    @Override
    public void fillQueryEntity(SingleCategoryQueryType queryEntity, Map<String, String> paramsMap) {
		// ANONYMIZED
        queryEntity.setVehicle(vehicle);
    }
}
