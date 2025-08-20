package cz.komix.qsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.lang.System;
import java.lang.RuntimeException;
import java.io.File;

/**
 * @author Jan Lejnar
 */
@SpringBootApplication
public class QSystemApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        validateNecessaryStuff();
        SpringApplication.run(QSystemApplication.class, args);
    }

    private static void validateNecessaryStuff() throws RuntimeException {
        validatePKIStuff("javax.net.ssl.keyStore", "javax.net.ssl.keyStorePassword");
        validatePKIStuff("javax.net.ssl.trustStore", "javax.net.ssl.trustStorePassword");
    }

    private static void validatePKIStuff(String keystoreEnvVarFileFileArg, String keystoreEnvVarFilePasswordArg) throws RuntimeException {
        String keystoreEnvVarFile = System.getProperty(keystoreEnvVarFileFileArg);
        if (keystoreEnvVarFile == null) {
            throw new RuntimeException("The necessary environment variable for locating the keystore file \"" + keystoreEnvVarFileFileArg + "\" is not defined.");
        } else {
            File keystoreFile = new File(keystoreEnvVarFile);
            if (!keystoreFile.exists() && !keystoreFile.canRead()) {
                throw new RuntimeException("The keystore file \"" + keystoreEnvVarFile + "\" defined by the environment variable \"" + keystoreEnvVarFileFileArg + "\" does not exist or cannot be read by this process.");
            }

            String keystoreEnvVarPassword = System.getProperty(keystoreEnvVarFilePasswordArg);
            if (keystoreEnvVarPassword == null) {
                throw new RuntimeException("The necessary environment variable \"" + keystoreEnvVarFilePasswordArg + "\" containing the password for the keystore file \"" + keystoreEnvVarFile + "\" is not defined.");
            }
        }
    }
}