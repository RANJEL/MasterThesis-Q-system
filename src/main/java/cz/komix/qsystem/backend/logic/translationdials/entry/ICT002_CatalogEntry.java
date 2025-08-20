package cz.komix.qsystem.backend.logic.translationdials.entry;

/**
 * @author Jan Lejnar
 */
public class ICT002_CatalogEntry extends CatalogEntry {
    private final String oldCode;

    public ICT002_CatalogEntry(String code, String version, String label, String translatedLabel, String validFrom, String validUntil, String oldCode) {
        super(code, version, label, translatedLabel, validFrom, validUntil);
        this.oldCode = oldCode;
    }

    public String getOldCode() {
        return oldCode;
    }
}
