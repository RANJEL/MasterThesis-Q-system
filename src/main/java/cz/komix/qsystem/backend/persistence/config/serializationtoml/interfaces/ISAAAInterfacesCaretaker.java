package cz.komix.qsystem.backend.persistence.config.serializationtoml.interfaces;

import cz.komix.qsystem.backend.logic.isaaa.AbstractISAAAAppInterface;
import cz.komix.qsystem.backend.logic.isaaa.ISAAAFullInterface;
import cz.komix.qsystem.backend.logic.isaaa.ISAAASimplifiedInterface;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles deserialization based on interfaces configuration file into {@link AbstractISAAAAppInterface}
 *
 * @author Jan Lejnar
 */
@Named
public class ISAAAInterfacesCaretaker {
    private final ISAAAFullInterface isAAAFullInterface;
    private final ISAAASimplifiedInterface isAAASimplifiedInterface;
    private Map<String, AbstractISAAAAppInterface> isAAAAppInterfaceMap;

    @Inject
    public ISAAAInterfacesCaretaker(ISAAAFullInterface isAAAFullInterface, ISAAASimplifiedInterface isAAASimplifiedInterface) {
        this.isAAAAppInterfaceMap = new HashMap<>();
        this.isAAAFullInterface = isAAAFullInterface;
        this.isAAASimplifiedInterface = isAAASimplifiedInterface;
    }

    public void addFullInterface(String name) {
        isAAAFullInterface.setName(name);
        isAAAAppInterfaceMap.put(name, isAAAFullInterface);
    }

    public void addSimplifiedInterface(String name) {
        isAAASimplifiedInterface.setName(name);
        isAAAAppInterfaceMap.put(name, isAAASimplifiedInterface);
    }

    public Map<String, AbstractISAAAAppInterface> getDeserializedISAAAInterfaces() {
        return isAAAAppInterfaceMap;
    }
}
