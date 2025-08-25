package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory;

import javax.inject.Named;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class MultiCategoryEntityDocumentFactory extends AbstractMultiCategoryEntityFactory {
    public static final String ENTITY_NAME = "document";

    @Override
    protected String getParam(String paramName, Map<String, String> paramsMap) {
        return getParam(paramName, paramsMap, CATEGORY_NAME, ENTITY_NAME);
    }

    @Override
    public void fillQueryEntity(MultiCategoryQueryType queryEntity, Map<String, String> paramsMap) {
        String nationality_of_document = getParam("nationality_of_document", paramsMap);
        String category_of_document = getParam("category_of_document", paramsMap);
        String document_number = getParam("document_number", paramsMap);
        String family_names = getParam("family_names", paramsMap);

        CheckDocumentType document = new CheckDocumentType();

        if (isValidParamValue(nationality_of_document)) {
            document.setNationalityOfDocument(nationality_of_document);
        }

        if (isValidParamValue(category_of_document)) {
            document.setCategoryOfDocument(category_of_document);
        }

        if (isValidParamValue(document_number)) {
            document.setDocumentNumber(new DocumentNumberSearchType(
                    document_number,
                    getSearchMode("document_number", paramsMap)
            ));
        }

        if (isValidParamValue(family_names)) {
            document.setFamilyNames(new NameSearchType(
                    family_names,
                    getSearchMode("family_names", paramsMap)
            ));
        }

        queryEntity.setCheckDocument(document);
    }
}
