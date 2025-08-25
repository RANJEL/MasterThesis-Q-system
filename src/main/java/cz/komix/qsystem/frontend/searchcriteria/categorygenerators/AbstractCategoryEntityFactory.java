package cz.komix.qsystem.frontend.searchcriteria.categorygenerators;

import cz.komix.qsystem.backend.exception.SearchModeNotFound;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbSearchCriteria;
import cz.komix.qsystem.backend.persistence.config.part.translationdials.searchmode.QueryModifiers2SearchModeConvertor;
import cz.komix.qsystem.backend.service.AppFeaturesService;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jan Lejnar
 */
public abstract class AbstractCategoryEntityFactory {
    public static final String PARAM_KEY_PREFIX = "app-form:search-criteria-form:";

    @Inject
    private AppFeaturesService appFeaturesService;

    @Inject
    private QueryModifiers2SearchModeConvertor queryModifiers2SearchModeConvertor;

    protected abstract String getParam(String paramName, Map<String, String> paramsMap);

    protected String getParam(String paramName, Map<String, String> paramsMap, String queryCategory, String queryEntity) {
        String[] paramNameParts = paramName.split(":");

        JaxbSearchCriteria searchCriteria = appFeaturesService
                .getAllSearchCriteria(queryCategory, queryEntity)
                .get(paramNameParts[0]);

        if (searchCriteria == null) {
            return null;
        }

        String frontendElemType = searchCriteria.getFrontendElemType();

        if (paramNameParts.length > 1) {
            return getParam(paramName, paramsMap, frontendElemType);
        } else {
            return getParam(paramNameParts[0], paramsMap, frontendElemType);
        }
    }

    protected String getParam(
            String paramName,
            Map<String, String> paramsMap,
            String frontendElemType) {
        String paramKey = AbstractCategoryEntityFactory.PARAM_KEY_PREFIX;

        if (frontendElemType.contains("textbox")) {
            paramKey += paramName;
            return paramsMap.get(paramKey);
        } else if (frontendElemType.contains("calendar") || frontendElemType.equals("range")) {
            paramKey += paramName + "_input";
            return paramsMap.get(paramKey);
        } else if (frontendElemType.contains("selectbox")) {
            paramKey += paramName + "_input";
            String fullLabel = paramsMap.get(paramKey);
            return parseIdFromSelectBoxValue(fullLabel);
        } else {
            return null;
        }
    }

    protected String parseIdFromSelectBoxValue(String value) {
        Matcher matcher = Pattern.compile(".*id:'(\\d*.\\d*)'.*").matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    protected String getSearchMode(String paramName, Map<String, String> paramsMap) {
        Set<QueryModifier> checkedQueryModifiers = new HashSet<>();

        appFeaturesService.getAllQueryModifiers()
                .forEach(queryModifier -> {
                    String paramKey = AbstractCategoryEntityFactory.PARAM_KEY_PREFIX
                            + paramName + '-' + queryModifier.getName() + "_input";
                    String queryModifierCheckboxValue = paramsMap.get(paramKey);
                    if (isCheckedCheckbox(queryModifierCheckboxValue)) {
                        checkedQueryModifiers.add(new QueryModifier(queryModifier.getName()));
                    }
                });

        String searchMode;
        try {
            searchMode = queryModifiers2SearchModeConvertor.getCorrespondingSearchMode(checkedQueryModifiers);
        } catch (SearchModeNotFound ex) {
            searchMode = null; // Exact modifier
        }

        return searchMode;
    }

    public static boolean isValidParamValue(String paramValue) {
        return paramValue != null && !paramValue.trim().equals("");
    }

    public static boolean isCheckedCheckbox(String checkboxValue) {
        return checkboxValue != null && checkboxValue.equals("on");
    }
}
