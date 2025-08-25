package cz.komix.qsystem.backend.logic.translationdials;

/**
 * @author Jan Lejnar
 */
public enum TranslationDialsTables {
    TypDotazovatele("ICT001"),
    TypZaznamu("ICT002"),
    Narodnost("ICT006"),
    ZnackaVozu("ICT012"),
    Barva("ICT013"),
    TypTransakce("ICT017"),
    TypEkonomickeJednotky("ICT018"),
    TypOpatreni("ICT021"),
    TypPlanu("ICT022"),
    TypSubjektu("ICT024"),
    TypSmlouvy("ICT025"),
    TypSkladovePolozky("ICT026"),
    TypVazby("ICT029"),
    ZpusobTransakce("ICT031"),
    KombinaceModifikatoru("ICT108"),
    Pohlavi("ICT129"),
    ZpusobVazby("ICT130");

    private String tableName;

    TranslationDialsTables(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
