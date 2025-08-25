package cz.komix.qsystem.backend.persistence.config;

import cz.komix.qsystem.backend.persistence.config.part.authorization.IAuthorizationConfig;
import cz.komix.qsystem.backend.persistence.config.part.interfaces.IInterfacesConfig;
import cz.komix.qsystem.backend.persistence.config.part.translationdials.ITranslationDialsConfig;

/**
 * Q-system bean that provides access (read/write/modify) to all available configurations.
 *
 * @author Jan Lejnar
 */
public abstract class AbstractConfigurationProvider {
    protected final IAuthorizationConfig authorizationConfig;
    protected final IInterfacesConfig interfacesConfig;
    protected final ITranslationDialsConfig translationDialsConfig;

    public AbstractConfigurationProvider(IAuthorizationConfig authorizationConfig,
                                         IInterfacesConfig interfacesConfig,
                                         ITranslationDialsConfig translationDialsConfig) {
        this.authorizationConfig = authorizationConfig;
        this.interfacesConfig = interfacesConfig;
        this.translationDialsConfig = translationDialsConfig;
    }

    public IAuthorizationConfig getAuthorizationConfiguration() {
        return this.authorizationConfig;
    }

    public IInterfacesConfig getInterfacesConfiguration() {
        return this.interfacesConfig;
    }

    public ITranslationDialsConfig getTranslationDialsConfiguration() {
        return translationDialsConfig;
    }
}
