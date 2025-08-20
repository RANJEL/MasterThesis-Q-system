package cz.komix.qsystem.backend.logic.translationdials.entry;

/**
 * @author Jan Lejnar
 */
public class ICT021_CatalogEntry extends CatalogEntry {
    private String codeAlphabetic;
    private String codeNumeric;

    public ICT021_CatalogEntry(String code, String version, String label, String translatedLabel, String validFrom, String validUntil, String codeAlphabetic, String codeNumeric) {
        super(code, version, label, translatedLabel, validFrom, validUntil);
        this.codeAlphabetic = codeAlphabetic;
        this.codeNumeric = codeNumeric;
    }

    public String getCodeAlphabetic() {
        return codeAlphabetic;
    }

    public String getCodeNumeric() {
        return codeNumeric;
    }
}
