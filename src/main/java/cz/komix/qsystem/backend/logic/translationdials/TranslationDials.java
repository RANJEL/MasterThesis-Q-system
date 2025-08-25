package cz.komix.qsystem.backend.logic.translationdials;

import cz.komix.qsystem.backend.logic.translationdials.entry.CatalogEntry;
import cz.komix.qsystem.backend.logic.translationdials.index.IndexedAttributes;
import cz.komix.qsystem.backend.logic.translationdials.index.IndexedTranslationDials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * TranslationDials file (see translationDials.toml) was created as extraction of ICT file
 * Only tables that serves the purpose of Q-system were used.
 *
 * @author Jan Lejnar
 */
public class TranslationDials {
    private IndexedTranslationDials translationDialsIndexedById;
    private IndexedTranslationDials translationDialsIndexedByLabel;
    private IndexedTranslationDials translationDialsIndexedByTranslatedLabel;

    public TranslationDials() {
        this.translationDialsIndexedById = new IndexedTranslationDials();
        this.translationDialsIndexedByLabel = new IndexedTranslationDials();
        this.translationDialsIndexedByTranslatedLabel = new IndexedTranslationDials();
    }

    private IndexedTranslationDials getDialsIndexedByAttribute(IndexedAttributes attribute) {
        switch (attribute) {
            case ID:
                return translationDialsIndexedById;
            case LABEL:
                return translationDialsIndexedByLabel;
            case TRANSLATED_LABEL:
                return translationDialsIndexedByTranslatedLabel;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void putEntryInto(Map<String, CatalogEntry> where, CatalogEntry entry, IndexedAttributes entryType) {
        switch (entryType) {
            case ID:
                where.put(entry.getId(), entry);
                return;
            case LABEL:
                where.put(entry.getLabel(), entry);
                return;
            case TRANSLATED_LABEL:
                where.put(entry.getTranslatedLabel(), entry);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void putEntryInto(CatalogEntry catalogEntry, TranslationDialsTables table) {
        Stream.of(IndexedAttributes.values()).forEach(x ->
                putEntryInto(getDialsIndexedByAttribute(x).get(table), catalogEntry, x)
        );
    }

    public List<CatalogEntry> getEntriesFrom(TranslationDialsTables table) {
        List<CatalogEntry> list = new ArrayList<>(translationDialsIndexedById.get(table).values());
        Collections.sort(list);
        return list;
    }

    public CatalogEntry findEntryInTable(String key, IndexedAttributes keyType, TranslationDialsTables table) {
        return getDialsIndexedByAttribute(keyType).get(table).get(key);
    }
}
