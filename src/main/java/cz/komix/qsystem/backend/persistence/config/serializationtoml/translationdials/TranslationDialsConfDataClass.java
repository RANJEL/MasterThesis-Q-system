package cz.komix.qsystem.backend.persistence.config.serializationtoml.translationdials;

import cz.komix.qsystem.backend.logic.translationdials.TranslationDials;
import cz.komix.qsystem.backend.logic.translationdials.TranslationDialsTables;
import cz.komix.qsystem.backend.logic.translationdials.entry.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Root class that is mapped to TOML structure of file with translation dials {see translationDials.toml}.
 *
 * @author Jan Lejnar
 */
@Data
public class TranslationDialsConfDataClass {
    private List<ICT001_CatalogEntry> ICT001;
    private List<ICT002_CatalogEntry> ICT002;
    private List<ICT006_CatalogEntry> ICT006;
    private List<CatalogEntry> ICT012;
    private List<CatalogEntry> ICT013;
    private List<CatalogEntry> ICT017;
    private List<CatalogEntry> ICT018;
    private List<ICT021_CatalogEntry> ICT021;
    private List<CatalogEntry> ICT022;
    private List<CatalogEntry> ICT024;
    private List<CatalogEntry> ICT025;
    private List<CatalogEntry> ICT026;
    private List<CatalogEntry> ICT029;
    private List<CatalogEntry> ICT031;
    private List<CatalogEntry> ICT108;
    private List<CatalogEntry> ICT129;
    private List<CatalogEntry> ICT130;

    public TranslationDialsConfDataClass() {
        this.ICT001 = new ArrayList<>();
        this.ICT002 = new ArrayList<>();
        this.ICT006 = new ArrayList<>();
        this.ICT012 = new ArrayList<>();
        this.ICT013 = new ArrayList<>();
        this.ICT017 = new ArrayList<>();
        this.ICT018 = new ArrayList<>();
        this.ICT021 = new ArrayList<>();
        this.ICT022 = new ArrayList<>();
        this.ICT024 = new ArrayList<>();
        this.ICT025 = new ArrayList<>();
        this.ICT026 = new ArrayList<>();
        this.ICT029 = new ArrayList<>();
        this.ICT031 = new ArrayList<>();
        this.ICT108 = new ArrayList<>();
        this.ICT129 = new ArrayList<>();
        this.ICT130 = new ArrayList<>();
    }

    public TranslationDials getDeserializedTranslationDials() {
        TranslationDials translationDials = new TranslationDials();
        ICT001.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypDotazovatele));
        ICT002.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypZaznamu));
        ICT006.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.Narodnost));
        ICT012.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.ZnackaVozu));
        ICT013.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.Barva));
        ICT017.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypTransakce));
        ICT018.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypEkonomickeJednotky));
        ICT021.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypOpatreni));
        ICT022.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypPlanu));
        ICT024.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypSubjektu));
        ICT025.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypSmlouvy));
        ICT026.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypSkladovePolozky));
        ICT029.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.TypVazby));
        ICT031.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.ZpusobTransakce));
        ICT108.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.KombinaceModifikatoru));
        ICT129.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.Pohlavi));
        ICT130.forEach(entry -> translationDials.putEntryInto(entry, TranslationDialsTables.ZpusobVazby));
        return translationDials;
    }
}
