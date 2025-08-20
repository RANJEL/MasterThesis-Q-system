package cz.komix.qsystem.backend.logic.queryform;

import cz.komix.qsystem.backend.logic.queryform.element.ElementaryQueryForm;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryEntityType;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Jan Lejnar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryFormTest {

    public QueryForm testQueryForm1;
    public QueryForm testQueryForm2;
    public QueryForm testQueryForm3;

    @Before
    public void setUp() {
        testQueryForm1 = new QueryForm("testQueryForm1", "Testovací forma dotazu 1");
        testQueryForm1.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:vehicle"),
                new QueryModifier("zastupne_znaky"),
                new QueryType("standard_query")
        ));
        testQueryForm1.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:person"),
                new QueryModifier("kombinace_jmen"),
                new QueryType("standard_query")
        ));
        testQueryForm1.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:person"),
                new QueryModifier("fuzzy_cislo"),
                new QueryType("load_attachments")
        ));
        testQueryForm1.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:person"),
                new QueryModifier("fuzzy_cislo"),
                new QueryType("load_links")
        ));

        testQueryForm2 = new QueryForm("testQueryForm2", "Testovací forma dotazu 2");
        testQueryForm2.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:vehicle"),
                new QueryModifier("zastupne_znaky"),
                new QueryType("standard_query")
        ));
        testQueryForm2.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:vehicle"),
                new QueryModifier("zastupne_znaky"),
                new QueryType("standard_query")
        ));
        testQueryForm2.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:vehicle"),
                new QueryModifier("zastupne_znaky"),
                new QueryType("standard_query")
        ));

        testQueryForm3 = new QueryForm("testQueryForm3", "Testovací forma dotazu 3");
        testQueryForm3.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:vehicle"),
                new QueryModifier("zastupne_znaky"),
                new QueryType("standard_query")
        ));
        testQueryForm3.addElementaryQueryForm(new ElementaryQueryForm(
                new QueryEntityType("single_category:vehicle"),
                new QueryModifier("zastupne_znaky"),
                new QueryType("complement_query")
        ));
    }

    @Test
    public void getAllElementaryQueryForms() {
        String referenceToString = "QueryForm{name='testQueryForm1', queryEntityTypeVertices=[single_category:person, single_category:vehicle], queryModifierVertices=[kombinace_jmen, fuzzy_cislo, zastupne_znaky], queryTypeVertices=[load_attachments, load_links, standard_query], queryFormGraph=\n" +
                "single_category:person & kombinace_jmen & standard_query\n" +
                "single_category:person & fuzzy_cislo & load_attachments\n" +
                "single_category:person & fuzzy_cislo & load_links\n" +
                "single_category:vehicle & zastupne_znaky & standard_query\n" +
                "}";

        assertEquals(referenceToString, testQueryForm1.toString());
    }

    @Test
    public void duplicityTest() {
        String referenceToString = "QueryForm{name='testQueryForm2', queryEntityTypeVertices=[single_category:vehicle], queryModifierVertices=[zastupne_znaky], queryTypeVertices=[standard_query], queryFormGraph=\n" +
                "single_category:vehicle & zastupne_znaky & standard_query\n" +
                "}";

        assertEquals(referenceToString, testQueryForm2.toString());
    }

    @Test
    public void mergeQueryForms() {
        String referenceToString = "QueryForm{name='mergedQueryForm', queryEntityTypeVertices=[single_category:aircraft, single_category:person, single_category:vehicle], queryModifierVertices=[kombinace_jmen, fuzzy_cislo, zastupne_znaky], queryTypeVertices=[complement_query, load_attachments, load_links, standard_query], queryFormGraph=\n" +
                "single_category:aircraft & zastupne_znaky & standard_query\n" +
                "single_category:person & kombinace_jmen & standard_query\n" +
                "single_category:person & fuzzy_cislo & load_attachments\n" +
                "single_category:person & fuzzy_cislo & load_links\n" +
                "single_category:vehicle & zastupne_znaky & complement_query\n" +
                "single_category:vehicle & zastupne_znaky & standard_query\n" +
                "}";

        QueryForm mergedQueryForm = QueryForm.mergeQueryForms(testQueryForm1, testQueryForm2, testQueryForm3);
        mergedQueryForm.setName("mergedQueryForm");
        assertEquals(referenceToString, mergedQueryForm.toString());
    }
}