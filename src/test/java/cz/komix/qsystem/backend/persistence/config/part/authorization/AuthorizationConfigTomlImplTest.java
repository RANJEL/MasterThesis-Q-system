package cz.komix.qsystem.backend.persistence.config.part.authorization;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.queryform.QueryFormTest;
import cz.komix.qsystem.backend.logic.role.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author Jan Lejnar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "config.authorization.filepath=src/test/java/cz/komix/qsystem/backend/persistence/config/part/authorization/authorization_test.toml",
})
public class AuthorizationConfigTomlImplTest {
    @Inject
    private AuthorizationConfigTomlImpl authorizationConfigTomlImpl;

    private QueryForm testQueryForm1;
    private QueryForm testQueryForm2;
    private QueryForm testQueryForm3;
    private UserRole userRole1;
    private UserRole userRole2;

    @Before
    public void setUp() {
        QueryFormTest queryFormTest = new QueryFormTest();
        queryFormTest.setUp();

        this.testQueryForm1 = queryFormTest.testQueryForm1;
        this.testQueryForm2 = queryFormTest.testQueryForm2;
        this.testQueryForm3 = queryFormTest.testQueryForm3;

        Set<QueryForm> mappedQueryForms1 = new HashSet<>();
        mappedQueryForms1.add(testQueryForm1);

        Set<QueryForm> mappedQueryForms2 = new HashSet<>();
        mappedQueryForms2.add(testQueryForm1);
        mappedQueryForms2.add(testQueryForm2);
        mappedQueryForms2.add(testQueryForm3);

        userRole1 = new UserRole("default", "Výchozí uživatelská role", mappedQueryForms1);
        userRole2 = new UserRole("all", "Role povolující všechny formy dotazu", mappedQueryForms2);
    }

    @Test
    public void contextLoads() {
        assertThat(authorizationConfigTomlImpl).isNotNull();
    }

    @Test
    public void saveAndLoadAuthorizationConfig() {
        Map<String, QueryForm> referenceQueryForms = new HashMap<>();
        referenceQueryForms.put(testQueryForm1.getName(), testQueryForm1);
        referenceQueryForms.put(testQueryForm2.getName(), testQueryForm2);
        referenceQueryForms.put(testQueryForm3.getName(), testQueryForm3);

        Map<String, UserRole> referenceUserRoles = new HashMap<>();
        referenceUserRoles.put(userRole1.getName(), userRole1);
        referenceUserRoles.put(userRole2.getName(), userRole2);

        authorizationConfigTomlImpl.addQueryForm(testQueryForm1);
        authorizationConfigTomlImpl.addQueryForm(testQueryForm2);
        authorizationConfigTomlImpl.addQueryForm(testQueryForm3);
        authorizationConfigTomlImpl.addQueryForm(testQueryForm3);
        authorizationConfigTomlImpl.removeQueryForm(testQueryForm2.getName());
        authorizationConfigTomlImpl.addQueryForm(testQueryForm2);

        authorizationConfigTomlImpl.addUserRole(userRole1);
        authorizationConfigTomlImpl.addUserRole(userRole2);

        authorizationConfigTomlImpl.saveAuthorizationConfig();

        authorizationConfigTomlImpl.loadAuthorizationConfig();
        Map<String, QueryForm> definedQueryForms = authorizationConfigTomlImpl.getQueryForms();
        Map<String, UserRole> definedUserRoles = authorizationConfigTomlImpl.getUserRoles();

        assertEquals(referenceQueryForms, definedQueryForms);
        assertEquals(referenceUserRoles, definedUserRoles);
    }
}