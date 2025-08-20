package cz.komix.qsystem.backend.persistence.config.part.translationdials;

import cz.komix.qsystem.backend.logic.translationdials.TranslationDials;
import cz.komix.qsystem.backend.logic.translationdials.TranslationDialsTables;
import cz.komix.qsystem.backend.logic.translationdials.entry.CatalogEntry;
import cz.komix.qsystem.backend.logic.translationdials.index.IndexedAttributes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * @author Jan Lejnar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TranslationDialsConfigTomlImplTest {
    @Inject
    private TranslationDialsConfigTomlImpl translationDialsConfigTomlImpl;

    private TranslationDials translationDials;

    @Before
    public void setUp() {
        translationDialsConfigTomlImpl.loadTranslationDialsConfig();
        translationDials = translationDialsConfigTomlImpl.getTranslationDials();
    }

    @Test
    public void getTranslationDialsTestEntry() {
        CatalogEntry expectedCatalogEntry = new CatalogEntry(
                "0010",
                "01",
                "Red",
                "Red",
                "20060101",
                "");

        CatalogEntry catalogEntry = translationDials.findEntryInTable(
                "0010.01",
                IndexedAttributes.ID,
                TranslationDialsTables.Barva);

        assertEquals(expectedCatalogEntry, catalogEntry);
    }

    @Test
    public void getTranslationDialsTestCount() {
        int expectedTranslationCount = 1_882;

        int translationCount = 0;
        for (TranslationDialsTables table : TranslationDialsTables.values()) {
            translationCount += translationDials.getEntriesFrom(table).size();
        }

        assertEquals(expectedTranslationCount, translationCount);
    }
}