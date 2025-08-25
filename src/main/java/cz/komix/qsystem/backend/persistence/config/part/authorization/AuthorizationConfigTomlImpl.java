package cz.komix.qsystem.backend.persistence.config.part.authorization;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;
import cz.komix.qsystem.backend.persistence.config.part.ConfigFiles;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization.AuthorizationConfDataClass;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization.SerializedQueryForm;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization.SerializedUserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jan Lejnar
 */
@Named
public class AuthorizationConfigTomlImpl implements IAuthorizationConfig {

    private final Logger logger = LoggerFactory.getLogger(IAuthorizationConfig.class);
    private TomlWriter tomlWriter;
    private ConfigFiles configFiles;
    private Toml authorizationConfigFile;
    private AuthorizationConfDataClass authorizationConfDataClass;
    private Optional<Map<String, QueryForm>> deserializedQueryForms; // @Lazy
    private Optional<Map<String, UserRole>> deserializedUserRoles; // @Lazy

    @Inject
    public AuthorizationConfigTomlImpl(ConfigFiles configFiles) {
        this.configFiles = configFiles;
        this.tomlWriter = new TomlWriter();

        try {
            this.authorizationConfigFile = new Toml().read(this.configFiles.getAuthorizationConfigFile());
        } catch (RuntimeException e) {
            logger.error("Error during parsing authorizationConfigFile", e);
        }

        this.authorizationConfDataClass = new AuthorizationConfDataClass();
        this.deserializedQueryForms = Optional.empty();
        this.deserializedUserRoles = Optional.empty();

        loadAuthorizationConfig();
    }

    @Override
    public void loadAuthorizationConfig() {
        authorizationConfDataClass = authorizationConfigFile.to(AuthorizationConfDataClass.class);
    }

    @Override
    public void saveAuthorizationConfig() {
        try {
            tomlWriter.write(authorizationConfDataClass, configFiles.getAuthorizationConfigFile());
        } catch (IOException e) {
            logger.warn("Unable to save authorization configuration", e);
        }
    }

    @Override
    public Map<String, QueryForm> getQueryForms() {
        return deserializedQueryForms.orElseGet(this::updateQueryForms);
    }

    @Override
    public synchronized void addQueryForm(QueryForm queryForm) {
        if (isQueryFormNameUnique(queryForm.getName())) {
            authorizationConfDataClass.getDefinedQueryForms().add(queryForm.serialize());
            saveAuthorizationConfig();
            updateQueryForms();
            logger.info("New query form \"{}\" defined.", queryForm.getName());
        } else {
            logger.warn("New query form \"{}\" was not created, name is not unique.", queryForm.getName());
        }
    }

    @Override
    public synchronized void removeQueryForm(String queryFormName) {
        SerializedQueryForm serializedQueryFormToRemove = new SerializedQueryForm(queryFormName, null);
        authorizationConfDataClass.getDefinedQueryForms().remove(serializedQueryFormToRemove);
        saveAuthorizationConfig();
        updateQueryForms();
        updateUserRoles();
        logger.info("Query form \"{}\" deleted.", queryFormName);
    }

    @Override
    public synchronized void updateQueryForm(QueryForm queryForm) {
        removeQueryForm(queryForm.getName());
        addQueryForm(queryForm);
        saveAuthorizationConfig();
        updateQueryForms();
        logger.info("Query form \"{}\" has been modified. Current status = {}",
                queryForm.getName(), queryForm);
    }

    private Map<String, QueryForm> updateQueryForms() {
        deserializedQueryForms = Optional.of(authorizationConfDataClass.getDeserializedQueryForms());
        return deserializedQueryForms.get();
    }

    @Override
    public Map<String, UserRole> getUserRoles() {
        return deserializedUserRoles.orElseGet(this::updateUserRoles);
    }

    @Override
    public synchronized void addUserRole(UserRole userRole) {
        if (isUserRoleNameUnique(userRole.getName())) {
            authorizationConfDataClass.getDefinedUserRoles().add(userRole.serialize());
            saveAuthorizationConfig();
            updateUserRoles();
            logger.info("New user role \"{}\" defined.", userRole.getName());
        } else {
            logger.warn("New user role \"{}\" was not created, name is not unique.", userRole.getName());
        }
    }

    @Override
    public synchronized void removeUserRole(String userRoleName) {
        SerializedUserRole serializedUserRoleToRemove = new SerializedUserRole(userRoleName, null);
        authorizationConfDataClass.getDefinedUserRoles().remove(serializedUserRoleToRemove);
        saveAuthorizationConfig();
        updateUserRoles();
        logger.info("User role \"{}\" deleted.", userRoleName);
    }

    @Override
    public void updateUserRole(UserRole userRole) {
        removeUserRole(userRole.getName());
        addUserRole(userRole);
        saveAuthorizationConfig();
        updateUserRoles();
        logger.info("User role \"{}\" has been modified. Current status = {}",
                userRole.getName(), userRole);
    }

    private Map<String, UserRole> updateUserRoles() {
        deserializedUserRoles = Optional.of(authorizationConfDataClass.getDeserializedUserRoles(this.getQueryForms()));
        return deserializedUserRoles.get();
    }
}
