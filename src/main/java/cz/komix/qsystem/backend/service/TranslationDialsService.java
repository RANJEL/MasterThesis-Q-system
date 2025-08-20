package cz.komix.qsystem.backend.service;

import cz.komix.qsystem.backend.logic.translationdials.TranslationDialsTables;
import cz.komix.qsystem.backend.logic.translationdials.entry.CatalogEntry;
import cz.komix.qsystem.backend.logic.translationdials.index.IndexedAttributes;
import cz.komix.qsystem.backend.persistence.config.AbstractConfigurationProvider;
import cz.komix.qsystem.backend.persistence.config.part.translationdials.ITranslationDialsConfig;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * List of methods dealing with translation dials provided by backend.
 *
 * @author Jan Lejnar
 */
@RestController
@Named
public class TranslationDialsService {

    private ITranslationDialsConfig translationDialsConfig;

    @Inject
    public TranslationDialsService(@Named("tomlConfigurationProvider") AbstractConfigurationProvider config) {
        this.translationDialsConfig = config.getTranslationDialsConfiguration();
    }

    public List<CatalogEntry> getEntriesFrom(TranslationDialsTables table) {
        return translationDialsConfig.getTranslationDials().getEntriesFrom(table);
    }

    public CatalogEntry findEntryInTable(String key, IndexedAttributes keyType, TranslationDialsTables table) {
        return translationDialsConfig.getTranslationDials().findEntryInTable(key, keyType, table);
    }

    /**
     * UC PP0204 steps 7-8
     *
     * @param newTranslationDialsStream
     */
    public void replaceTranslationDialsConfigFile(InputStream newTranslationDialsStream) {
        translationDialsConfig.replaceTranslationDialsConfigFile(newTranslationDialsStream);
    }

    /**
     * Screen O09
     *
     * @return
     */
    public BasicFileAttributes getTranslationDialsConfigFileMetadata() {
        return translationDialsConfig.getTranslationDialsConfigFileMetadata();
    }
}
