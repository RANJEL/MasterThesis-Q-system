package cz.komix.qsystem.backend.logic.isaaaquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Thread-safe class providing JAXB marshalling (= serialization of object type into inner body XML request).
 *
 * @author Jan Lejnar
 */
public class MarshallingHelper {

    private final static Logger logger = LoggerFactory.getLogger(MarshallingHelper.class);

    /**
     * @param classToBeMarshalled
     * @return Thread-safe JAXB context
     */
    public static JAXBContext createJaxbContext(Class classToBeMarshalled) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(classToBeMarshalled);
        } catch (JAXBException e) {
            logger.error("Creating JAXB context failed", e);
        }
        return jaxbContext;
    }

    /**
     * @param jaxbContext
     * @return Not thread-safe marshaller, that should be used only by 1 thread
     */
    public static Marshaller createMarshaller(JAXBContext jaxbContext) {
        Marshaller jaxbMarshaller = null;
        try {
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); // without xml header
        } catch (JAXBException e) {
            logger.error("Creating JAXB marshaller failed", e);
        }
        return jaxbMarshaller;
    }

    /**
     *
     * @param jaxbContext
     * @return Not thread-safe unmarshaller, that should be used only by 1 thread
     */
    public static Unmarshaller createUnmarshaller(JAXBContext jaxbContext) {
        Unmarshaller jaxbUnmarshaller = null;
        try {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("Creating JAXB unmarshaller failed", e);
        }
        return jaxbUnmarshaller;
    }
}
