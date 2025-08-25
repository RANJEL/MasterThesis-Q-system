package cz.komix.qsystem.backend.persistence.config.part.interfaces;

import cz.komix.qsystem.backend.logic.isaaa.AbstractISAAAAppInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * @author Jan Lejnar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "config.interface.filepath=src/test/java/cz/komix/qsystem/backend/persistence/config/part/interfaces/interface_test.toml",
})
public class InterfacesConfigTomlImplTest {
    @Inject
    private InterfacesConfigTomlImpl interfacesConfigTomlImpl;

    @Test
    public void saveAndLoadInterfacesDefaultConfig() throws Exception {
        interfacesConfigTomlImpl.loadInterfacesConfig();

        AbstractISAAAAppInterface fullInterface = interfacesConfigTomlImpl.getISAAAAppInterfaces().get("Plne rozhrani");
        AbstractISAAAAppInterface simplifiedInterface = interfacesConfigTomlImpl.getISAAAAppInterfaces().get("Zjednodusene rozhrani");
        if (fullInterface != null && simplifiedInterface != null) {
            interfacesConfigTomlImpl.setCurrentlyUsedISAAAInterface(simplifiedInterface);
            interfacesConfigTomlImpl.setPreferredISAAAInterface(simplifiedInterface);
            interfacesConfigTomlImpl.setPreferredISAAAInterface(fullInterface);

            assertEquals(simplifiedInterface, interfacesConfigTomlImpl.getCurrentlyUsedISAAAInterface());
            assertEquals(fullInterface, interfacesConfigTomlImpl.getPreferredISAAAInterface());
        }

        interfacesConfigTomlImpl.saveInterfacesConfig();
    }
}