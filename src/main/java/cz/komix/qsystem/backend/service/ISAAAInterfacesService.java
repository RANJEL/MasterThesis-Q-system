package cz.komix.qsystem.backend.service;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.logic.isaaa.AbstractISAAAAppInterface;
import cz.komix.qsystem.backend.logic.usecases.IQSystemUseCases;
import cz.komix.qsystem.backend.persistence.config.AbstractConfigurationProvider;
import cz.komix.qsystem.backend.persistence.config.part.interfaces.IInterfacesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List of methods dealing with choosing IS AAA interface.
 *
 * @author Jan Lejnar
 */
@RestController
@Named
public class ISAAAInterfacesService {

    private final Logger logger = LoggerFactory.getLogger(ISAAAInterfacesService.class);
    private IInterfacesConfig interfacesConfig;
    private IQSystemUseCases useCases;

    @Inject
    public ISAAAInterfacesService(AbstractConfigurationProvider configuration, IQSystemUseCases useCases) {
        this.interfacesConfig = configuration.getInterfacesConfiguration();
        this.useCases = useCases;
    }

    /**
     * Screen O11
     *
     * @return
     */
    public List<String> getISAAAInterfaceNames() {
        return interfacesConfig.getISAAAAppInterfaces().values().stream()
                .map(AbstractISAAAAppInterface::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * UC PP0205 step 3
     *
     * @return
     */
    public String getCurrentlyUsedISAAAInterface() {
        try {
            return interfacesConfig.getCurrentlyUsedISAAAInterface().getName();
        } catch (ISAAAInterfaceNotFound ISAAAInterfaceNotFound) {
            logger.error("Could not find currently used IS AAA interface", ISAAAInterfaceNotFound);
            return "invalid";
        }
    }

    /**
     * Screen O11
     *
     * @return
     */
    public String getPreferredISAAAInterface() {
        try {
            return interfacesConfig.getPreferredISAAAInterface().getName();
        } catch (ISAAAInterfaceNotFound ISAAAInterfaceNotFound) {
            logger.error("Could not find preferred IS AAA interface", ISAAAInterfaceNotFound);
            return "invalid";
        }
    }

    public void setPreferredISAAAInterface(String isAAAInterfaceName) {
        useCases.setPreferredISAAAInterface(isAAAInterfaceName);
    }
}
