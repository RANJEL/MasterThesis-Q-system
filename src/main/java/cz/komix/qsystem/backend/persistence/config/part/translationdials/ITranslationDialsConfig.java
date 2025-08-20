package cz.komix.qsystem.backend.persistence.config.part.translationdials;

import cz.komix.qsystem.backend.logic.translationdials.TranslationDials;

import java.io.InputStream;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * All methods we need to work with translation dials.
 *
 * @author Jan Lejnar
 */
public interface ITranslationDialsConfig {
    void loadTranslationDialsConfig();

    TranslationDials getTranslationDials();

    void replaceTranslationDialsConfigFile(InputStream newTranslationDialsStream);

    BasicFileAttributes getTranslationDialsConfigFileMetadata();
}
