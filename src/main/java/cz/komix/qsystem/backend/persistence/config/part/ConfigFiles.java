package cz.komix.qsystem.backend.persistence.config.part;

import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * Class that provides File references to configuration files based on definition in application.properties.
 *
 * @author Jan Lejnar
 */
@Named
public class ConfigFiles {

    private File authorizationConfigFile;
    private File interfaceConfigFile;
    private File translationDialsConfigFile;

    @Inject
    public ConfigFiles(
            @Value("${config.authorization.filepath}") String authorizationPath,
            @Value("${config.interface.filepath}") String interfacePath,
            @Value("${config.translation-dials.filepath}") String translationDialsPath) {
        this.authorizationConfigFile = new File(authorizationPath);
        this.interfaceConfigFile = new File(interfacePath);
        this.translationDialsConfigFile = new File(translationDialsPath);
        if (!authorizationConfigFile.exists() || !interfaceConfigFile.exists() || !translationDialsConfigFile.exists()) {
            throw new IllegalStateException();
        }
    }

    public File getAuthorizationConfigFile() {
        return authorizationConfigFile;
    }

    public File getInterfaceConfigFile() {
        return interfaceConfigFile;
    }

    public File getTranslationDialsConfigFile() {
        return translationDialsConfigFile;
    }
}
