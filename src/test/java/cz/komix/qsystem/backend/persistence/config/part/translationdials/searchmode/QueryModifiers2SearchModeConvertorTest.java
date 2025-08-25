package cz.komix.qsystem.backend.persistence.config.part.translationdials.searchmode;

import cz.komix.qsystem.backend.exception.SearchModeNotFound;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Jan Lejnar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryModifiers2SearchModeConvertorTest {

    @Inject
    private QueryModifiers2SearchModeConvertor queryModifiers2SearchModeConvertor;

    private Set<QueryModifier> queryModifiers1;
    private Set<QueryModifier> queryModifiers2;
    private Set<QueryModifier> queryModifiers3;
    private Set<QueryModifier> queryModifiers1b;
    private Set<QueryModifier> queryModifiersFail;

    @Before
    public void setUp() {
        queryModifiers1 = new HashSet<>();
        queryModifiers1.add(new QueryModifier("fuzzy_text"));
        queryModifiers1.add(new QueryModifier("zastupne_znaky"));
        queryModifiers1.add(new QueryModifier("kombinace_jmen"));

        queryModifiers2 = new HashSet<>();
        queryModifiers2.add(new QueryModifier("fuzzy_text"));
        queryModifiers2.add(new QueryModifier("zastupne_znaky"));

        queryModifiers3 = new HashSet<>();
        queryModifiers3.add(new QueryModifier("fuzzy_text"));

        queryModifiers1b = new HashSet<>();
        queryModifiers1b.add(new QueryModifier("   fuzzy_text"));
        queryModifiers1b.add(new QueryModifier(" zastupne_znaky "));
        queryModifiers1b.add(new QueryModifier("AnY_nAMe"));

        queryModifiersFail = new HashSet<>();
        queryModifiersFail.add(new QueryModifier("exact"));
    }

    @Test
    public void getCorrespondingSearchMode() throws SearchModeNotFound {
        assertEquals("0005.01", this.queryModifiers2SearchModeConvertor.getCorrespondingSearchMode(queryModifiers1));
        assertEquals("0004.01", this.queryModifiers2SearchModeConvertor.getCorrespondingSearchMode(queryModifiers2));
        assertEquals("0001.01", this.queryModifiers2SearchModeConvertor.getCorrespondingSearchMode(queryModifiers3));
    }

    @Test
    public void getCorrespondingSearchModeCaseInsensitivity() throws SearchModeNotFound {
        assertEquals("0005.01", this.queryModifiers2SearchModeConvertor.getCorrespondingSearchMode(queryModifiers1b));
    }

    @Test(expected = SearchModeNotFound.class)
    public void getCorrespondingSearchModeFail() throws SearchModeNotFound {
        this.queryModifiers2SearchModeConvertor.getCorrespondingSearchMode(queryModifiersFail);
    }
}