package cz.komix.qsystem.backend.logic.isaaa;

import com.sun.xml.ws.client.ClientTransportException;
import cz.komix.qsystem.backend.exception.ISAAARequestError;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.backend.logic.isaaaquery.ISAAAMessagesDeserializer;
import cz.komix.qsystem.backend.logic.transformation.ComplementQueryResponseDataMapper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static cz.komix.qsystem.backend.logic.isaaaquery.MarshallingHelper.createJaxbContext;
import static cz.komix.qsystem.backend.logic.isaaaquery.MarshallingHelper.createMarshaller;

/**
 * Representation of "Plne rozhrani"
 *
 * @author Jan Lejnar
 */
@Named
public class ISAAAFullInterface extends AbstractISAAAAppInterface {
    @Inject
    private Provider<ISAAAQueryServicesProxy> isAAAProxyProvider;

    @Inject
    public ISAAAFullInterface(ISAAAMessagesDeserializer ISAAAMessagesDeserializer,
                              ComplementQueryResponseDataMapper complementQueryResponseDataMapper) {
        super(createJaxbContext(StandardQueryRequestData.class),
                createJaxbContext(ComplementQueryRequestData.class),
                ISAAAMessagesDeserializer,
                complementQueryResponseDataMapper);
    }

    @Override
    protected Object defineDefaultHeader() {
        QueryCsHeader header = new QueryCsHeader();
        header.setUid(4414);
        header.setQSyst("ADM_ISAAA2");
        header.setQServer("testQServer");
        header.setTermId("testTerm");
        header.setLogin("testLogin");
        header.setSystem("administraceISAAA");
        header.setRole("0004.01");
        header.setDuvod("dotaz_za_ucelem_testu");
        return header;
    }

    @Override
    protected Object createHeader(String reason) {
        QueryCsHeader header = (QueryCsHeader) defineDefaultHeader();
        header.setDuvod(reason);
        return header;
    }

    @Override
    protected String performStandardQueryCall(Object header, String xmlData) throws ISAAARequestFailed {
        String internalResponse = null;
        try {
            ResponseWithXmlResult response = isAAAProxyProvider.get().getISAAAQueryService()
                    .standardQuery((QueryCsHeader) header, xmlData);
            internalResponse = inspectISAAAResponse(response);
        } catch (ClientTransportException ex) {
            handleClientTransportException(ex);
        }
        return internalResponse;
    }

    @Override
    protected String performComplementQueryCall(Object header, String xmlData) throws ISAAARequestFailed {
        String internalResponse = null;
        try {
            ResponseWithXmlResult response = isAAAProxyProvider.get().getISAAAQueryService()
                    .complementQuery((QueryCsHeader) header, xmlData);
            internalResponse = inspectISAAAResponse(response);
        } catch (ClientTransportException ex) {
            handleClientTransportException(ex);
        }
        return internalResponse;
    }

    @Override
    protected String performLoadAttachmentsQueryCall(Object header, LoadAttachmentsOrLinksData loadAttachmentsOrLinksData) throws
            ISAAARequestFailed {
        String internalResponse = null;
        try {
            ResponseWithXmlResult response = isAAAProxyProvider.get().getISAAAQueryService()
                    .loadAttachments((QueryCsHeader) header, loadAttachmentsOrLinksData);
            internalResponse = inspectISAAAResponse(response);
        } catch (ClientTransportException ex) {
            handleClientTransportException(ex);
        }
        return internalResponse;
    }

    @Override
    protected String performLoadLinksCall(Object header, LoadAttachmentsOrLinksData loadAttachmentsOrLinksData) throws
            ISAAARequestFailed {
        String internalResponse = null;
        try {
            ResponseWithXmlResult response = isAAAProxyProvider.get().getISAAAQueryService()
                    .loadLinks((QueryCsHeader) header, loadAttachmentsOrLinksData);
            internalResponse = inspectISAAAResponse(response);
        } catch (ClientTransportException ex) {
            handleClientTransportException(ex);
        }
        return internalResponse;
    }

    @Override
    public String serializeStandardQueryRequestData(StandardQueryType searchCriteria) {
        StandardQueryRequestData standardQueryRequestData = new StandardQueryRequestData();
        standardQueryRequestData.setSearchCriteria(searchCriteria);

        StringWriter stringWriter = new StringWriter();
        try {
            Marshaller marshaller = createMarshaller(standardQueryJaxbContext);
            marshaller.marshal(standardQueryRequestData, stringWriter);
        } catch (JAXBException e) {
            logger.error("Standard Query marshalling failed", e);
        }
        return stringWriter.toString();
    }

    @Override
    public String serializeComplementQueryRequestData(AAAIDType AAAID, SubsetCriteriaType
            subsetCriteriaType) {
        ComplementQueryRequestData complementQueryRequestData = new ComplementQueryRequestData();
        complementQueryRequestData.setAAAID(AAAID);
        complementQueryRequestData.setSubsetCriteriaType(subsetCriteriaType);

        StringWriter stringWriter = new StringWriter();
        try {
            Marshaller marshaller = createMarshaller(complementQueryJaxbContext);
            marshaller.marshal(complementQueryRequestData, stringWriter);
        } catch (JAXBException e) {
            logger.error("Complement Query marshalling failed", e);
        }
        return stringWriter.toString();
    }

    private String inspectISAAAResponse(ResponseWithXmlResult response) throws ISAAARequestFailed {
        if (response == null) {
            throw new ISAAARequestFailed("Null response");
        } else {
            if (!response.getStatus().equals(ResponseStatus.OK)) {
                List<ISAAARequestError> requestErrors = new ArrayList<>();
                response.getMessages().forEach(codeExplained -> requestErrors.add(new ISAAARequestError(codeExplained.getName(), codeExplained.getRecomended())));
                throw new ISAAARequestFailed(response.getStatus().value(), requestErrors);
            } else {
                return response.getResult();
            }
        }
    }
}
