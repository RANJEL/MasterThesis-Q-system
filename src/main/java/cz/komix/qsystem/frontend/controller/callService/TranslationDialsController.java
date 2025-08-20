package cz.komix.qsystem.frontend.controller.callService;

import cz.komix.qsystem.backend.logic.translationdials.TranslationDialsTables;
import cz.komix.qsystem.backend.logic.translationdials.entry.CatalogEntry;
import cz.komix.qsystem.backend.logic.translationdials.index.IndexedAttributes;
import cz.komix.qsystem.backend.service.TranslationDialsService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jan Lejnar
 */
@Named
@ApplicationScoped
public class TranslationDialsController {

    private final Logger logger = LoggerFactory.getLogger(TranslationDialsController.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private TranslationDialsService translationDialsService;

    @Inject
    public TranslationDialsController(TranslationDialsService translationDialsService) {
        this.translationDialsService = translationDialsService;
    }

    public List<String> getEntriesFrom(String frontendElementType) {
        TranslationDialsTables translationDialsTable = getSelectboxType(frontendElementType);

        List<CatalogEntry> catalogEntries = translationDialsService.getEntriesFrom(translationDialsTable);
        List<String> catalogEntriesValues = catalogEntries.stream()
                .map(catalogEntry -> catalogEntry.getTranslatedLabel() +
                        " (id:\'" +
                        catalogEntry.getId() +
                        "\', en:\'" +
                        catalogEntry.getLabel() +
                        "\')")
                .collect(Collectors.toList());
        catalogEntriesValues.add(0, ""); // default = nothing is selected

        return catalogEntriesValues;
    }

    public CatalogEntry findEntryInTable(String key, IndexedAttributes keyType, TranslationDialsTables table) {
        return translationDialsService.findEntryInTable(key, keyType, table);
    }

    public void replaceTranslationDialsConfigFile(FileUploadEvent event) {
        try {
            UploadedFile uploadedFile = event.getFile();
            translationDialsService.replaceTranslationDialsConfigFile(uploadedFile.getInputstream());
        } catch (IOException e) {
            logger.error("Unable to get replace translation dials", e);
        }
    }

    public String getTranslationDialsCreationTime() {
        long modifiedTimeInMilis = translationDialsService.getTranslationDialsConfigFileMetadata().lastModifiedTime().toMillis();
        LocalDateTime creationTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(modifiedTimeInMilis), ZoneId.systemDefault());
        return creationTime.format(formatter);
    }

    private TranslationDialsTables getSelectboxType(String frontendElemType) {
        Matcher matcher = Pattern.compile(".*selectbox\\((.*)\\).*").matcher(frontendElemType);
        if (matcher.find()) {
            return TranslationDialsTables.valueOf(matcher.group(1));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
