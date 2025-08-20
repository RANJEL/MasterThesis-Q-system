package cz.komix.qsystem.backend.persistence.config.part.interfaces;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.logic.isaaa.AbstractISAAAAppInterface;
import cz.komix.qsystem.backend.persistence.config.part.ConfigFiles;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.interfaces.InterfacesConfDataClass;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.interfaces.ISAAAInterfacesCaretaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Named
public class InterfacesConfigTomlImpl implements IInterfacesConfig {

    private final Logger logger = LoggerFactory.getLogger(IInterfacesConfig.class);
    private TomlWriter tomlWriter;
    private ConfigFiles configFiles;
    private Toml interfacesConfigFile;
    private InterfacesConfDataClass interfacesConfDataClass;
    private ISAAAInterfacesCaretaker ISAAAInterfacesCaretaker;

    @Inject
    public InterfacesConfigTomlImpl(ConfigFiles configFiles, ISAAAInterfacesCaretaker ISAAAInterfacesCaretaker) {
        this.configFiles = configFiles;
        this.tomlWriter = new TomlWriter();

        try {
            this.interfacesConfigFile = new Toml().read(this.configFiles.getInterfaceConfigFile());
        } catch (RuntimeException e) {
            logger.error("Error during parsing interfacesConfigFile", e);
        }

        this.interfacesConfDataClass = new InterfacesConfDataClass();
        this.ISAAAInterfacesCaretaker = ISAAAInterfacesCaretaker;

        loadInterfacesConfig();
    }

    @Override
    public void loadInterfacesConfig() {
        interfacesConfDataClass = interfacesConfigFile.to(InterfacesConfDataClass.class);
    }

    @Override
    public void saveInterfacesConfig() {
        try {
            tomlWriter.write(interfacesConfDataClass, configFiles.getInterfaceConfigFile());
        } catch (IOException e) {
            logger.warn("Unable to save interfaces configuration", e);
        }
    }

    @Override
    public AbstractISAAAAppInterface getCurrentlyUsedISAAAInterface() throws ISAAAInterfaceNotFound {
        AbstractISAAAAppInterface currentlyUsedISAAAInterface =
                getISAAAAppInterfaces().get(
                        interfacesConfDataClass.getCurrentlyUsedISAAAInterface()
                );

        if (currentlyUsedISAAAInterface == null) {
            throw new ISAAAInterfaceNotFound();
        } else {
            return currentlyUsedISAAAInterface;
        }
    }

    @Override
    public synchronized void setCurrentlyUsedISAAAInterface(AbstractISAAAAppInterface interfaceToSet) throws ISAAAInterfaceNotFound {
        checkISAAAInterfaceIsDefined(interfaceToSet);
        interfacesConfDataClass.setCurrentlyUsedISAAAInterface(interfaceToSet.getName());
        saveInterfacesConfig();
        logger.info("Currently used IS AAA interface changed to {}", interfaceToSet.getName());
    }

    private void checkISAAAInterfaceIsDefined(AbstractISAAAAppInterface isAAAInterface) throws ISAAAInterfaceNotFound {
        if (isAAAInterface == null || !getISAAAAppInterfaces().containsKey(isAAAInterface.getName())) {
            throw new ISAAAInterfaceNotFound();
        }
    }

    @Override
    public AbstractISAAAAppInterface getPreferredISAAAInterface() throws ISAAAInterfaceNotFound {
        AbstractISAAAAppInterface preferredISAAAInterface =
                getISAAAAppInterfaces().get(
                        interfacesConfDataClass.getPreferredISAAAInterface()
                );

        if (preferredISAAAInterface == null) {
            throw new ISAAAInterfaceNotFound();
        } else {
            return preferredISAAAInterface;
        }
    }

    @Override
    public synchronized void setPreferredISAAAInterface(AbstractISAAAAppInterface interfaceToSet) throws ISAAAInterfaceNotFound {
        checkISAAAInterfaceIsDefined(interfaceToSet);
        interfacesConfDataClass.setPreferredISAAAInterface(interfaceToSet.getName());
        saveInterfacesConfig();
        logger.info("Preferred IS AAA interface changed to {}", interfaceToSet.getName());
    }

    @Override
    public Map<String, AbstractISAAAAppInterface> getISAAAAppInterfaces() {
        if (ISAAAInterfacesCaretaker.getDeserializedISAAAInterfaces().size() == 0) {
            ISAAAInterfacesCaretaker.addFullInterface(interfacesConfDataClass.getISAAAFullInterfaceName());
            ISAAAInterfacesCaretaker.addSimplifiedInterface(interfacesConfDataClass.getISAAASimplifiedInterfaceName());
        }
        return ISAAAInterfacesCaretaker.getDeserializedISAAAInterfaces();
    }

    @Override
    public AbstractISAAAAppInterface getFullInterface() {
        return getISAAAAppInterfaces().get(interfacesConfDataClass.getISAAAFullInterfaceName());
    }

    @Override
    public AbstractISAAAAppInterface getSimplifiedInterface() {
        return getISAAAAppInterfaces().get(interfacesConfDataClass.getISAAASimplifiedInterfaceName());
    }
}
