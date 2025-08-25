package cz.komix.qsystem.backend.persistence.config.part.translationdials;

import com.moandjiezana.toml.Toml;
import cz.komix.qsystem.backend.logic.translationdials.TranslationDials;
import cz.komix.qsystem.backend.persistence.config.part.ConfigFiles;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.translationdials.TranslationDialsConfDataClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

/**
 * @author Jan Lejnar
 */
@Named
public class TranslationDialsConfigTomlImpl implements ITranslationDialsConfig {

    private final Logger logger = LoggerFactory.getLogger(ITranslationDialsConfig.class);
    private ConfigFiles configFiles;
    private Toml translationDialsConfigFile;
    private TranslationDialsConfDataClass translationDialsConfDataClass;
    private Optional<TranslationDials> deserializedTranslationDials;

    @Inject
    public TranslationDialsConfigTomlImpl(ConfigFiles configFiles) {
        this.configFiles = configFiles;

        try {
            this.translationDialsConfigFile = new Toml().read(this.configFiles.getTranslationDialsConfigFile());
        } catch (RuntimeException e) {
            logger.error("Error during parsing translationDialsConfigFile", e);
        }

        this.translationDialsConfDataClass = new TranslationDialsConfDataClass();
        this.deserializedTranslationDials = Optional.empty();
        loadTranslationDialsConfig();
    }

    private TranslationDials updateDeserializedTranslationDials() {
        deserializedTranslationDials = Optional.of(translationDialsConfDataClass.getDeserializedTranslationDials());
        return deserializedTranslationDials.get();
    }

    @Override
    public void loadTranslationDialsConfig() {
        try {
            this.translationDialsConfigFile = new Toml().read(configFiles.getTranslationDialsConfigFile());
        } catch (RuntimeException e) {
            logger.error("Error during parsing toml files", e);
        }
        translationDialsConfDataClass = translationDialsConfigFile.to(TranslationDialsConfDataClass.class);
        updateDeserializedTranslationDials(); // not @Lazy
    }

    @Override
    public TranslationDials getTranslationDials() {
        return deserializedTranslationDials.orElseGet(this::updateDeserializedTranslationDials);
    }

    @Override
    public synchronized void replaceTranslationDialsConfigFile(InputStream newTranslationDialsStream) {
        Path to = Paths.get(configFiles.getTranslationDialsConfigFile().toURI());
        try {
            Files.copy(newTranslationDialsStream, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.warn("Unable to replace translation dials file", e);
        }
        this.loadTranslationDialsConfig();
        logger.info("Translation dials replaced.");
    }

    @Override
    public BasicFileAttributes getTranslationDialsConfigFileMetadata() {
        Path translationDialsFile = Paths.get(configFiles.getTranslationDialsConfigFile().toURI());
        BasicFileAttributes fileAttributes = null;
        try {
            fileAttributes = Files.readAttributes(translationDialsFile, BasicFileAttributes.class);
        } catch (IOException ex) {
            logger.error("Unable to get translation dials file metadata", ex);
        }
        return fileAttributes;
    }

}
