package cz.komix.qsystem.backend.persistence.appfeatures;

import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryEntityType;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbSearchCriteria;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbSearchCriteriaCombination;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Jan Lejnar
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppFeaturesProviderTest {
    @Inject
    AppFeaturesProvider appFeaturesProvider;

    @Test
    public void singleCategoryEntities() {
        String expectedResult = "person, vehicle, ";
        List<JaxbQueryEntityType> singleCategoryEntities = appFeaturesProvider.sortQueryEntitiesByOrder(
                appFeaturesProvider.getAllSingleCategoryQueryEntities());
        StringBuilder sortedCategories = new StringBuilder();
        singleCategoryEntities.forEach(category -> sortedCategories.append(category.getName() + ", "));
        assertEquals(expectedResult, sortedCategories.toString());
    }

    @Test
    public void personSearchCriteria() {
        String expectedResult = "family_names, first_names, date_of_birth, gender, nationality, formation, classification, ";

        List<JaxbSearchCriteria> personSearchCriteria = appFeaturesProvider.sortSearchCriteriaByOrder(
                appFeaturesProvider.getAllSearchCriteria("single_category", "person"));
        StringBuilder sortedCriteria = new StringBuilder();
        personSearchCriteria.forEach(criteria -> sortedCriteria.append(criteria.getName() + ", "));
        assertEquals(expectedResult, sortedCriteria.toString());
    }

    @Test
    public void personSearchCriteriaCombinations() {

        JaxbSearchCriteriaCombination searchCriteriaCombinationA = new JaxbSearchCriteriaCombination();
        Set<JaxbSearchCriteria> setA = new HashSet<>();
        Set<JaxbSearchCriteria> setB = new HashSet<>();

        JaxbSearchCriteria searchCriteria1 = new JaxbSearchCriteria();
        searchCriteria1.setName("family_names");

        JaxbSearchCriteria searchCriteria2 = new JaxbSearchCriteria();
        searchCriteria2.setName("date_of_birth");

        JaxbSearchCriteria searchCriteria3 = new JaxbSearchCriteria();
        searchCriteria3.setName("formation");

        setA.add(searchCriteria1);
        setA.add(searchCriteria2);

        setB.add(searchCriteria1);
        setB.add(searchCriteria3);

        JaxbSearchCriteriaCombination searchCriteriaCombinationB = new JaxbSearchCriteriaCombination();

        searchCriteriaCombinationA.setSearchCriteriaCombination(setA);
        searchCriteriaCombinationB.setSearchCriteriaCombination(setB);


        assertTrue(appFeaturesProvider.isAllowedSearchCriteriaCombination(searchCriteriaCombinationA, "single_category", "person"));
        assertFalse(appFeaturesProvider.isAllowedSearchCriteriaCombination(searchCriteriaCombinationB, "single_category", "person"));
    }
}