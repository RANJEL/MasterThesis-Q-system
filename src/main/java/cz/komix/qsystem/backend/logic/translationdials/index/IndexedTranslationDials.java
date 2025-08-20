package cz.komix.qsystem.backend.logic.translationdials.index;

import cz.komix.qsystem.backend.logic.translationdials.TranslationDialsTables;
import cz.komix.qsystem.backend.logic.translationdials.entry.CatalogEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Fast table retrieval only based on table name.
 * Contains tables indexed (string key) by specific IndexedAttribute {@link IndexedAttributes}
 *
 * @author Jan Lejnar
 */
public class IndexedTranslationDials {
    public IndexedTranslationDials() {
        this.ICT001 = new HashMap<>();
        this.ICT002 = new HashMap<>();
        this.ICT006 = new HashMap<>();
        this.ICT012 = new HashMap<>();
        this.ICT013 = new HashMap<>();
        this.ICT017 = new HashMap<>();
        this.ICT018 = new HashMap<>();
        this.ICT021 = new HashMap<>();
        this.ICT022 = new HashMap<>();
        this.ICT024 = new HashMap<>();
        this.ICT025 = new HashMap<>();
        this.ICT026 = new HashMap<>();
        this.ICT029 = new HashMap<>();
        this.ICT031 = new HashMap<>();
        this.ICT108 = new HashMap<>();
        this.ICT129 = new HashMap<>();
        this.ICT130 = new HashMap<>();
    }

    public Map<String, CatalogEntry> get(TranslationDialsTables table) {
        switch (table) {
            case TypDotazovatele:
                return ICT001;
            case TypZaznamu:
                return ICT002;
            case Narodnost:
                return ICT006;
            case ZnackaVozu:
                return ICT012;
            case Barva:
                return ICT013;
            case TypTransakce:
                return ICT017;
            case TypEkonomickeJednotky:
                return ICT018;
            case TypOpatreni:
                return ICT021;
            case TypPlanu:
                return ICT022;
            case TypSubjektu:
                return ICT024;
            case TypSmlouvy:
                return ICT025;
            case TypSkladovePolozky:
                return ICT026;
            case TypVazby:
                return ICT029;
            case ZpusobTransakce:
                return ICT031;
            case KombinaceModifikatoru:
                return ICT108;
            case Pohlavi:
                return ICT129;
            case ZpusobVazby:
                return ICT130;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Map<String, CatalogEntry> ICT001;
    private Map<String, CatalogEntry> ICT002;
    private Map<String, CatalogEntry> ICT006;
    private Map<String, CatalogEntry> ICT012;
    private Map<String, CatalogEntry> ICT013;
    private Map<String, CatalogEntry> ICT017;
    private Map<String, CatalogEntry> ICT018;
    private Map<String, CatalogEntry> ICT021;
    private Map<String, CatalogEntry> ICT022;
    private Map<String, CatalogEntry> ICT024;
    private Map<String, CatalogEntry> ICT025;
    private Map<String, CatalogEntry> ICT026;
    private Map<String, CatalogEntry> ICT029;
    private Map<String, CatalogEntry> ICT031;
    private Map<String, CatalogEntry> ICT108;
    private Map<String, CatalogEntry> ICT129;
    private Map<String, CatalogEntry> ICT130;
}
