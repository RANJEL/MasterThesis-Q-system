package cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory;

import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.AbstractCategoryEntityFactory;

import java.util.Map;

/**
 * @author Jan Lejnar
 */
public abstract class AbstractMultiCategoryEntityFactory extends AbstractCategoryEntityFactory {
    public static final String CATEGORY_NAME = "multi_category";

    public abstract void fillQueryEntity(MultiCategoryQueryType queryEntity, Map<String, String> paramsMap);
}
