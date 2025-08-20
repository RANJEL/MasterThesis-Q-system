package cz.komix.qsystem.backend.logic.translationdials.entry;

/**
 * @author Jan Lejnar
 */
public class ICT001_CatalogEntry extends CatalogEntry {
    private final String alphaCode;

    public ICT001_CatalogEntry(String code, String version, String label, String translatedLabel, String validFrom, String validUntil, String alphaCode) {
        super(code, version, label, translatedLabel, validFrom, validUntil);
        this.alphaCode = alphaCode;
    }

    public String getAlphaCode() {
        return alphaCode;
    }
}
