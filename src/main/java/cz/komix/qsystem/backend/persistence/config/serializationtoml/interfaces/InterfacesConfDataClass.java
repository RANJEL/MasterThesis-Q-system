package cz.komix.qsystem.backend.persistence.config.serializationtoml.interfaces;

import lombok.Data;

/**
 * Root class that is mapped to TOML structure of interfaces config file {see interface.toml}.
 *
 * @author Jan Lejnar
 */
@Data
public class InterfacesConfDataClass {
    private String currentlyUsedISAAAInterface;
    private String preferredISAAAInterface;
    private String iSAAAFullInterfaceName;
    private String iSAAASimplifiedInterfaceName;

    public InterfacesConfDataClass() {
        this.iSAAAFullInterfaceName = "";
        this.iSAAASimplifiedInterfaceName = "";
        this.currentlyUsedISAAAInterface = "";
        this.preferredISAAAInterface = "";
    }
}
