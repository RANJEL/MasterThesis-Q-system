package cz.komix.qsystem.backend.persistence.appfeatures;

/**
 * Class similar to {@link cz.komix.qsystem.backend.persistence.config.part.ConfigFiles} that provides path to resources wrapper in jar file.
 *
 * @author Jan Lejnar
 */
public enum JarResources {
    APP_FEATURES("classpath:app-features/app-features.xml");

    private final String path;

    JarResources(String resourcePath) {
        this.path = resourcePath;
    }

    public String getPath() {
        return path;
    }
}
