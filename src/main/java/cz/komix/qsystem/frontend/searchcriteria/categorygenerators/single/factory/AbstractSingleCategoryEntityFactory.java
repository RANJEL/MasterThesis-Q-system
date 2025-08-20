package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory;

import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.AbstractCategoryEntityFactory;

import java.util.Map;

/**
 * @author Jan Lejnar
 */
public abstract class AbstractSingleCategoryEntityFactory extends AbstractCategoryEntityFactory {
    public static final String CATEGORY_NAME = "single_category";

    public abstract void fillQueryEntity(SingleCategoryQueryType queryEntity, Map<String, String> paramsMap);
}