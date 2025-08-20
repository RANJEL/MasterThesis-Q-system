package cz.komix.qsystem.backend.logic.isaaa;

import com.sun.xml.ws.client.ClientTransportException;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.backend.logic.isaaaquery.ISerializeRequestData;
import cz.komix.qsystem.backend.logic.isaaaquery.ISAAAMessagesDeserializer;
import cz.komix.qsystem.backend.logic.transformation.ComplementQueryResponseDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import java.util.Objects;

/**
 * Abstract parent class of any IS AAA interfaces.
 *
 * @author Jan Lejnar
 */
public abstract class AbstractISAAAAppInterface implements
        ISerializeRequestData, IIsaaaMessagesDispatcher, Comparable<AbstractISAAAAppInterface> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractISAAAAppInterface.class);
    protected final JAXBContext standardQueryJaxbContext;
    protected final JAXBContext complementQueryJaxbContext;
    private final ISAAAMessagesDeserializer ISAAAMessagesDeserializer;
    private final ComplementQueryResponseDataMapper complementQueryResponseDataMapper;
    protected String name;

    public AbstractISAAAAppInterface(
            JAXBContext standardQueryJaxbContext,
            JAXBContext complementQueryJaxbContext,
            ISAAAMessagesDeserializer ISAAAMessagesDeserializer,
            ComplementQueryResponseDataMapper complementQueryResponseDataMapper) {
        this.standardQueryJaxbContext = standardQueryJaxbContext;
        this.complementQueryJaxbContext = complementQueryJaxbContext;
        this.ISAAAMessagesDeserializer = ISAAAMessagesDeserializer;
        this.complementQueryResponseDataMapper = complementQueryResponseDataMapper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractISAAAAppInterface that = (AbstractISAAAAppInterface) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "AbstractISAAAAppInterface{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(AbstractISAAAAppInterface o) {
        return this.name.compareTo(o.name);
    }

    protected void handleClientTransportException(ClientTransportException ex) throws ISAAARequestFailed {
        String errorMessage;

        if (ex.getMessage().contains("javax.net.ssl.SSLHandshakeException: PKIX path building failed")) {
            errorMessage = "You have to add IS AAA server certificate to your truststore => " +
                    "set system properties \"javax.net.ssl.trustStore\" and \"javax.net.ssl.trustStorePassword\"";
        } else if (ex.getMessage().contains("javax.net.ssl.SSLHandshakeException: Received fatal record: bad_certificate")) {
            errorMessage = "You have to provide your client certificate to IS AAA server => " +
                    "set system properties \"javax.net.ssl.keyStore\" and \"javax.net.ssl.keyStorePassword\"";
        } else {
            errorMessage = "Error during sending request to IS AAA";
        }

        logger.error(errorMessage, ex);
        throw new ISAAARequestFailed(errorMessage, ex);
    }

    protected abstract Object defineDefaultHeader();

    protected abstract Object createHeader(String reason);

    protected abstract String performStandardQueryCall(Object header, String xmlData) throws ISAAARequestFailed;

    protected abstract String performComplementQueryCall(Object header, String xmlData) throws ISAAARequestFailed;

    protected abstract String performLoadAttachmentsQueryCall(Object header, LoadAttachmentsOrLinksData loadAttachmentsOrLinksData) throws ISAAARequestFailed;

    protected abstract String performLoadLinksCall(Object header, LoadAttachmentsOrLinksData loadAttachmentsOrLinksData) throws ISAAARequestFailed;

    public StandardQueryResponseData dispatchStandardQuery(String reason, StandardQueryType searchCriteria) throws ISAAARequestFailed {
        Object header = createHeader(reason);
        String xmlData = serializeStandardQueryRequestData(searchCriteria);
        logger.info("Dispatching StandardQuery, StandardQueryRequestData = {}", xmlData);
        String internalResponse = performStandardQueryCall(header, xmlData);
        logger.info("Received response = {}", internalResponse);
        return ISAAAMessagesDeserializer.deserializeStandardQuery(internalResponse);
    }

    @Override
    public StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID) throws ISAAARequestFailed {
        SubsetCriteriaType subsetCriteriaType = new SubsetCriteriaType();
        subsetCriteriaType.setTextualRecord(true);
        subsetCriteriaType.setBinaryInfos(true);
        return dispatchComplementQuery(reason, AAAID, subsetCriteriaType);
    }

    @Override
    public StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID, SubsetCriteriaType subsetCriteriaType) throws ISAAARequestFailed {
        Object header = createHeader(reason);
        String xmlData = serializeComplementQueryRequestData(AAAID, subsetCriteriaType);
        logger.info("Dispatching ComplementQuery, ComplementQueryRequestData = {}", xmlData);
        String internalResponse = performComplementQueryCall(header, xmlData);
        logger.info("Received response = {}", internalResponse);
        internalResponse = complementQueryResponseDataMapper
                .transformComplementQueryResponse2StandardQueryResponse(internalResponse);
        return ISAAAMessagesDeserializer.deserializeStandardQuery(internalResponse);
    }

    private LoadAttachmentsOrLinksData wrap(AAAIDType AAAID, long dataId) {
        LoadAttachmentsOrLinksData loadAttachmentsOrLinksData = new LoadAttachmentsOrLinksData();
        loadAttachmentsOrLinksData.setAAAID(AAAID);
        loadAttachmentsOrLinksData.setDataId(dataId);
        return loadAttachmentsOrLinksData;
    }

    @Override
    public String dispatchLoadAttachments(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed {
        Object header = createHeader(reason);
        logger.info("Dispatching LoadAttachments, AAAID = {}, dataId = {}", AAAID, dataId);
        LoadAttachmentsOrLinksData loadAttachmentsOrLinksData = wrap(AAAID, dataId);
        String internalResponse = performLoadAttachmentsQueryCall(header, loadAttachmentsOrLinksData);
        logger.info("Received response = {}", internalResponse);
        return internalResponse;
    }

    @Override
    public String dispatchLoadLinks(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed {
        Object header = createHeader(reason);
        logger.info("Dispatching LoadLinks, AAAID = {}, dataId = {}", AAAID, dataId);
        LoadAttachmentsOrLinksData loadAttachmentsOrLinksData = wrap(AAAID, dataId);
        String internalResponse = performLoadLinksCall(header, loadAttachmentsOrLinksData);
        logger.info("Received response = {}", internalResponse);
        return internalResponse;
    }
}
