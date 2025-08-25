package cz.komix.qsystem.frontend.searchcriteria;

import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.MultiCategoryEntityGenerator;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory.AbstractMultiCategoryEntityFactory;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.SingleCategoryEntityGenerator;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory.AbstractSingleCategoryEntityFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class SearchCriteriaGenerator {
    private final SingleCategoryEntityGenerator singleCategoryEntityGenerator;
    private final MultiCategoryEntityGenerator multiCategoryEntityGenerator;

    @Inject
    public SearchCriteriaGenerator(SingleCategoryEntityGenerator singleCategoryEntityGenerator, MultiCategoryEntityGenerator multiCategoryEntityGenerator) {
        this.singleCategoryEntityGenerator = singleCategoryEntityGenerator;
        this.multiCategoryEntityGenerator = multiCategoryEntityGenerator;
    }

    private StandardQueryType createDefaultSearchCriteriaObject() {
        StandardQueryType searchCriteria = new StandardQueryType();
        searchCriteria.setSendWarning(false);
        searchCriteria.setUserRank(BigDecimal.valueOf(0.001));
        searchCriteria.setUserDefinedHardCap(Long.valueOf(100));
        return searchCriteria;
    }

    public StandardQueryType getSearchCriteria(Map<String, String> paramsMap, String entityCategoryName, String entityName) {
        StandardQueryType searchCriteria = createDefaultSearchCriteriaObject();
        if (entityCategoryName.equals(AbstractSingleCategoryEntityFactory.CATEGORY_NAME)) {
            searchCriteria.setSingleCategoryQueryType(singleCategoryEntityGenerator.getSingleCategoryEntity(paramsMap, entityName));
        } else if (entityCategoryName.equals(AbstractMultiCategoryEntityFactory.CATEGORY_NAME)) {
            searchCriteria.setMultiCategoryQueryType(multiCategoryEntityGenerator.getMultiCategoryEntity(paramsMap, entityName));
        }
        return searchCriteria;
    }
}
