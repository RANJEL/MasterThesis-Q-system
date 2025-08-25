package cz.komix.qsystem.backend.logic.isaaaquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

import static cz.komix.qsystem.backend.logic.isaaaquery.MarshallingHelper.createJaxbContext;
import static cz.komix.qsystem.backend.logic.isaaaquery.MarshallingHelper.createUnmarshaller;

/**
 * Class providing JAXB unmarshalling (= deserialization of inner body response into object class).
 *
 * @author Jan Lejnar
 */
@Named
public class ISAAAMessagesDeserializer implements IDeserializeToResponseData {

    private final Logger logger = LoggerFactory.getLogger(ISAAAMessagesDeserializer.class);
    private JAXBContext standardQueryJaxbContext;
    private JAXBContext complementQueryJaxbContext;

    public ISAAAMessagesDeserializer() {
        this.standardQueryJaxbContext = createJaxbContext(StandardQueryResponseData.class);
        this.complementQueryJaxbContext = createJaxbContext(ComplementQueryResponseData.class);
    }

    private String fixNamespace(String originalResponse) {
        return originalResponse.replace
                ("http://www.komix.cz/aaa/query/external/types",
                        "http://www.komix.cz/aaa/query/types");
    }

    @Override
    public StandardQueryResponseData deserializeStandardQuery(String serializedResponse) {
        if (serializedResponse == null) {
            return new StandardQueryResponseData();
        }
        StandardQueryResponseData standardQueryResponseData = null;
        try {
            Unmarshaller unmarshaller = createUnmarshaller(standardQueryJaxbContext);
            standardQueryResponseData = (StandardQueryResponseData) unmarshaller.unmarshal
                    (new StringReader(fixNamespace(serializedResponse)));
        } catch (JAXBException e) {
            logger.error("Standard Query unmarshalling failed", e);
        }
        return standardQueryResponseData;
    }

    @Override
    public ComplementQueryResponseData deserializeComplementQuery(String serializedResponse) {
        if (serializedResponse == null) {
            return new ComplementQueryResponseData();
        }
        ComplementQueryResponseData complementQueryResponseData = null;
        try {
            Unmarshaller unmarshaller = createUnmarshaller(complementQueryJaxbContext);
            complementQueryResponseData = (ComplementQueryResponseData) unmarshaller.unmarshal
                    (new StringReader(fixNamespace(serializedResponse)));
        } catch (JAXBException e) {
            logger.error("Complement Query unmarshalling failed", e);
        }
        return complementQueryResponseData;
    }
}
