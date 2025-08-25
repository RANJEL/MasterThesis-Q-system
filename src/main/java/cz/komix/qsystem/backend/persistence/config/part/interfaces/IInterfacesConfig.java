package cz.komix.qsystem.backend.persistence.config.part.interfaces;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.logic.isaaa.AbstractISAAAAppInterface;

import java.util.Map;

/**
 * All methods we need for choosing IS AAAA interface.
 *
 * @author Jan Lejnar
 */
public interface IInterfacesConfig {
    void loadInterfacesConfig();

    void saveInterfacesConfig();

    AbstractISAAAAppInterface getCurrentlyUsedISAAAInterface() throws ISAAAInterfaceNotFound;

    void setCurrentlyUsedISAAAInterface(AbstractISAAAAppInterface interfaceToSet) throws ISAAAInterfaceNotFound;

    AbstractISAAAAppInterface getPreferredISAAAInterface() throws ISAAAInterfaceNotFound;

    void setPreferredISAAAInterface(AbstractISAAAAppInterface interfaceToSet) throws ISAAAInterfaceNotFound;

    Map<String, AbstractISAAAAppInterface> getISAAAAppInterfaces();

    AbstractISAAAAppInterface getFullInterface();

    AbstractISAAAAppInterface getSimplifiedInterface();
}
