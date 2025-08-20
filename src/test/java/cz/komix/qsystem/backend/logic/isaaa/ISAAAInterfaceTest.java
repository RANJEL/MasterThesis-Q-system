package cz.komix.qsystem.backend.logic.isaaa;

import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests: you need to have connection to service urls specified in application.properties
 * @author Jan Lejnar
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class ISAAAInterfaceTest {
    @Inject
    private ISAAAFullInterface fullInterface;
    @Inject
    private ISAAASimplifiedInterface simplifiedInterface;

    private String reason;
    private StandardQueryType searchCriteria;
    private AAAIDType AAAID;
    private long dataId;

    @Before
    public void setUp() {
        reason = "Q-system test";

        dataId = 1;

        searchCriteria = new StandardQueryType();
        searchCriteria.setUserDefinedHardCap(Long.valueOf(100));
        searchCriteria.setSendWarning(false);
        searchCriteria.setUserRank(BigDecimal.valueOf(0.001));

        SingleCategoryQueryType singleCategoryQueryType = new SingleCategoryQueryType();
        StandardPersonQueryType person = new StandardPersonQueryType();
        NameSearchType familyName = new NameSearchType();
        familyName.setValue("Želman"); // exact
        person.setFamilyNames(familyName);
        singleCategoryQueryType.setPerson(person);

        searchCriteria.setSingleCategoryQueryType(singleCategoryQueryType);

        AAAID = new AAAIDType();
        AAAID.setRequestingUser("0016.02");
        AAAID.setRecordType("0001.01");
        AAAID.setNationalIDNumber(new NationalIDNumberType());
        AAAID.getNationalIDNumber().setIdNumber("0000000000006");
        AAAID.getNationalIDNumber().setAliasNumber("0000");

        System.setProperty("javax.net.ssl.trustStore", "./qsystem-security/isaaa-server-certificate.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "kokosak");
        System.setProperty("javax.net.ssl.keyStore", "./qsystem-security/qsystem-certificate.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "kokosak");
    }

    @Test
    public void dispatchStandardQuery() {
        StandardQueryResponseData standardQueryResponseData = null;
        try {
            standardQueryResponseData = fullInterface.dispatchStandardQuery(reason, searchCriteria);
            assertNotNull(standardQueryResponseData);
        } catch (ISAAARequestFailed ISAAARequestFailed) {
            assertTrue(ISAAARequestFailed.getMessage().contains(ResponseStatus.KO_INVALID.value()));
        }
    }

    @Test
    public void dispatchStandardQueryExternal() {
        StandardQueryResponseData standardQueryResponseData = null;
        try {
            standardQueryResponseData = simplifiedInterface.dispatchStandardQuery(reason, searchCriteria);
            assertNotNull(standardQueryResponseData);
        } catch (ISAAARequestFailed ISAAARequestFailed) {
            assertTrue(ISAAARequestFailed.getMessage().contains(ResponseStatus.KO_INVALID.value()));
        }
    }

    @Test
    public void dispatchComplementQuery() {
        StandardQueryResponseData standardQueryResponseData = null;
        try {
            standardQueryResponseData = fullInterface.dispatchComplementQuery(reason, AAAID);
            assertNotNull(standardQueryResponseData);
        } catch (ISAAARequestFailed ISAAARequestFailed) {
            assertTrue(ISAAARequestFailed.getMessage().contains(ResponseStatus.KO_INVALID.value()));
        }
    }

    @Test
    public void dispatchLoadAttachments() {
        String responseData = null;
        try {
            responseData = fullInterface.dispatchLoadAttachments(reason, AAAID, dataId);
            assertNotNull(responseData);
        } catch (ISAAARequestFailed ISAAARequestFailed) {
            assertTrue(ISAAARequestFailed.getMessage().contains(ResponseStatus.KO_INVALID.value()));
        }
    }

    @Test
    public void dispatchLoadLinks() {
        String responseData = null;
        try {
            responseData = fullInterface.dispatchLoadLinks(reason, AAAID, dataId);
            assertNotNull(responseData);
        } catch (ISAAARequestFailed ISAAARequestFailed) {
            assertTrue(ISAAARequestFailed.getMessage().contains(ResponseStatus.KO_INVALID.value()));
        }
    }
}