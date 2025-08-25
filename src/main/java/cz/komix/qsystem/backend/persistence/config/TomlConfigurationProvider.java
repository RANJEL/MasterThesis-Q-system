package cz.komix.qsystem.backend.persistence.config;

import cz.komix.qsystem.backend.persistence.config.part.authorization.IAuthorizationConfig;
import cz.komix.qsystem.backend.persistence.config.part.interfaces.IInterfacesConfig;
import cz.komix.qsystem.backend.persistence.config.part.translationdials.ITranslationDialsConfig;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Implementation of {@link AbstractConfigurationProvider} that parses configuration files as .TOML files.
 *
 * @author Jan Lejnar
 */
@Named
public class TomlConfigurationProvider extends AbstractConfigurationProvider {
    @Inject
    public TomlConfigurationProvider(@Named("authorizationConfigTomlImpl") IAuthorizationConfig authorizationConfig,
                                     @Named("interfacesConfigTomlImpl") IInterfacesConfig interfacesConfig,
                                     @Named("translationDialsConfigTomlImpl") ITranslationDialsConfig translationDialsConfig) {
        super(authorizationConfig, interfacesConfig, translationDialsConfig);
    }
}