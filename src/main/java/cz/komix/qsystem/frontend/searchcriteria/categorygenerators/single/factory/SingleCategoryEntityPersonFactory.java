package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory;

import javax.inject.Named;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class SingleCategoryEntityPersonFactory extends AbstractSingleCategoryEntityFactory {
    public static final String ENTITY_NAME = "person";

    @Override
    protected String getParam(String paramName, Map<String, String> paramsMap) {
        return getParam(paramName, paramsMap, CATEGORY_NAME, ENTITY_NAME);
    }

    @Override
    public void fillQueryEntity(SingleCategoryQueryType queryEntity, Map<String, String> paramsMap) {
        String family_names = getParam("family_names", paramsMap);
        String first_names = getParam("first_names", paramsMap);
        String date_of_birth = getParam("date_of_birth", paramsMap);
        String gender = getParam("gender", paramsMap);
        String nationality = getParam("nationality", paramsMap);
        String formation = getParam("formation", paramsMap);
        String classification = getParam("classification", paramsMap);

        StandardPersonQueryType person = new StandardPersonQueryType();

        if (isValidParamValue(family_names)) {
            person.setFamilyNames(new NameSearchType(
                    family_names,
                    getSearchMode("family_names", paramsMap)
            ));
        }
        if (isValidParamValue(first_names)) {
            person.setFirstNames(new NameSearchType(
                    first_names,
                    getSearchMode("first_names", paramsMap)
            ));
        }
        if (isValidParamValue(date_of_birth)) {
            person.setDateOfBirth(new StandardPersonQueryType.DateOfBirth(
                    date_of_birth,
                    getSearchMode("date_of_birth", paramsMap)
            ));
        }
        if (isValidParamValue(gender)) {
            person.setGender(gender);
        }
        if (isValidParamValue(nationality)) {
            person.setNationality(nationality);
        }
        if (isValidParamValue(formation)) {
            person.setformationKey(formation);
        }
        if (isValidParamValue(classification)) {
            person.setFinnishKey(classification);
        }

        queryEntity.setPerson(person);
    }
}
