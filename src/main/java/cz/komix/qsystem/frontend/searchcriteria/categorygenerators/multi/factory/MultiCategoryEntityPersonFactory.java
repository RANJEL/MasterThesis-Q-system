package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory;

import javax.inject.Named;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class MultiCategoryEntityPersonFactory extends AbstractMultiCategoryEntityFactory {
    public static final String ENTITY_NAME = "person";

    @Override
    protected String getParam(String paramName, Map<String, String> paramsMap) {
        return getParam(paramName, paramsMap, CATEGORY_NAME, ENTITY_NAME);
    }

    @Override
    public void fillQueryEntity(MultiCategoryQueryType queryEntity, Map<String, String> paramsMap) {
        String first_names = getParam("first_names", paramsMap);
        String family_names = getParam("family_names", paramsMap);
        String date_of_birth = getParam("date_of_birth", paramsMap);
        String gender = getParam("gender", paramsMap);
        String document_number = getParam("document_number", paramsMap);
        String category_of_document = getParam("category_of_document", paramsMap);
        String nationality_person_and_or_document = getParam("nationality_person_and_or_document", paramsMap);

        PersonDataQueryType person = new PersonDataQueryType();

        if (isValidParamValue(first_names)) {
            person.setFirstNames(new NameSearchType(
                    first_names,
                    getSearchMode("first_names", paramsMap)
            ));
        }

        if (isValidParamValue(family_names)) {
            person.setFamilyNames(new NameSearchType(
                    family_names,
                    getSearchMode("family_names", paramsMap)
            ));
        }

        if (isValidParamValue(date_of_birth)) {
            person.setDateOfBirth(new PersonDataQueryType.DateOfBirth(
                    date_of_birth,
                    getSearchMode("date_of_birth", paramsMap)
            ));
        }

        if (isValidParamValue(gender)) {
            person.setGender(gender);
        }

        if (isValidParamValue(document_number)) {
            person.setDocNumber(new DocumentNumberSearchType(
                    document_number,
                    getSearchMode("document_number", paramsMap)
            ));
        }

        if (isValidParamValue(category_of_document)) {
            person.setCategoryOfDocument(category_of_document);
        }

        if (isValidParamValue(nationality_person_and_or_document)) {
            person.setNationality(nationality_person_and_or_document);
        }

        queryEntity.setPersonDataQuery(person);
    }
}
