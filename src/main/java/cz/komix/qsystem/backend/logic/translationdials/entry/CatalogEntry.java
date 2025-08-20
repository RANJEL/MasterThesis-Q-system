package cz.komix.qsystem.backend.logic.translationdials.entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class CatalogEntry implements Serializable, Comparable<CatalogEntry> {

    private final String code;

    private final String version;

    private final String validFrom;

    private final String validUntil;

    private final String label;

    private final String translatedLabel;

    private final Logger logger = LoggerFactory.getLogger(CatalogEntry.class);

    public CatalogEntry(
            String code,
            String version,
            String label,
            String translatedLabel,
            String validFrom,
            String validUntil) {
        this.code = code;
        this.version = version;
        this.label = label;
        this.translatedLabel = translatedLabel;
        this.validFrom = validFrom;
        this.validUntil = validUntil == null ? "" : validUntil;
    }

    protected static LocalDate parseDate(String dateAsString) {
        return LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String getId() {
        return code + '.' + version;
    }

    public int getCode() {
        return Integer.parseInt(code);
    }

    public int getVersion() {
        return Integer.parseInt(version);
    }

    public String getLabel() {
        return label;
    }

    public String getTranslatedLabel() {
        return translatedLabel;
    }

    public Optional<LocalDate> getValidFrom() {
        try {
            LocalDate date = CatalogEntry.parseDate(this.validFrom);
            return Optional.of(date);
        } catch (DateTimeParseException e) {
            logger.debug("Field validFrom = {} contains non-parseable date", validFrom, e);
            return Optional.empty();
        }
    }

    public Optional<LocalDate> getValidUntil() {
        try {
            LocalDate date = CatalogEntry.parseDate(this.validUntil);
            return Optional.of(date);
        } catch (DateTimeParseException e) {
            logger.debug("Field validUntil = {} contains non-parseable date", validUntil, e);
            return Optional.empty();
        }
    }

    public boolean isValidNow() {
        Optional<LocalDate> validUntil = getValidUntil();
        if (validUntil.isEmpty()) {
            return true;
        } else {
            return LocalDate.now().isBefore(validUntil.get());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatalogEntry that = (CatalogEntry) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(version, that.version) &&
                Objects.equals(validFrom, that.validFrom) &&
                Objects.equals(validUntil, that.validUntil) &&
                Objects.equals(label, that.label) &&
                Objects.equals(translatedLabel, that.translatedLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, version, validFrom, validUntil, label, translatedLabel);
    }

    @Override
    public String toString() {
        return "CatalogEntry{" +
                "id='" + getId() + '\'' +
                ", label='" + label + '\'' +
                ", translatedLabel='" + translatedLabel + '\'' +
                '}';
    }

    @Override
    public int compareTo(CatalogEntry o) {
        return this.getTranslatedLabel().compareTo(o.getTranslatedLabel());
    }
}