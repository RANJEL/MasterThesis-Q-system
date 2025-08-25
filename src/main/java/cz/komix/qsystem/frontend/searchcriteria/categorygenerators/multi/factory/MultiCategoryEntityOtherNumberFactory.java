package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory;

import javax.inject.Named;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class MultiCategoryEntityOtherNumberFactory extends AbstractMultiCategoryEntityFactory {
    public static final String ENTITY_NAME = "other_number";

    @Override
    protected String getParam(String paramName, Map<String, String> paramsMap) {
        return getParam(paramName, paramsMap, CATEGORY_NAME, ENTITY_NAME);
    }

    @Override
    public void fillQueryEntity(MultiCategoryQueryType queryEntity, Map<String, String> paramsMap) {
        String number = getParam("number", paramsMap);
        String type_of_object = getParam("type_of_object", paramsMap);

        OtherNumberQueryType otherNumber = new OtherNumberQueryType();

        if (isValidParamValue(number)) {
            otherNumber.setNumber(new OtherNumberSearchType(
                    number,
                    getSearchMode("number", paramsMap)
            ));
        }

        if (isValidParamValue(type_of_object)) {
            otherNumber.setTypeOfObject(type_of_object);
        }

        queryEntity.setOtherNumberQuery(otherNumber);
    }
}
