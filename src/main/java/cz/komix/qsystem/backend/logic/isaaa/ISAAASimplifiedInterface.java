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
 * Representation of "Zjednodusene rozhrani"
 *
 * @author Jan Lejnar
 */
@Named
public class ISAAASimplifiedInterface extends AbstractISAAAAppInterface {
    @Inject
    private Provider<ISAAAQueryServicesProxy> isAAAProxyProvider;

    @Inject
    public ISAAASimplifiedInterface(ISAAAMessagesDeserializer ISAAAMessagesDeserializer,
                                         ComplementQueryResponseDataMapper complementQueryResponseDataMapper) {
        super(createJaxbContext(StandardQueryRequestData.class),
                null,
                ISAAAMessagesDeserializer,
                complementQueryResponseDataMapper);
    }

    @Override
    protected Object defineDefaultHeader() {
        QueryHeader header = new QueryHeader();
        header.setExtSystemUserId("TestovaciExterniSystem");
        header.setDuvod("dotaz_za_ucelem_testu");
        return header;
    }

    @Override
    protected Object createHeader(String reason) {
        QueryHeader header = (QueryHeader) defineDefaultHeader();
        header.setDuvod(reason);
        return header;
    }

    @Override
    protected String performStandardQueryCall(Object header, String xmlData) throws ISAAARequestFailed {
        StandardQuery standardQueryExternalParams = new StandardQuery();
        standardQueryExternalParams.setHeader((QueryHeader) header);
        standardQueryExternalParams.setXmlData(xmlData);

        String internalResponse = null;
        try {
            ResponseWithXmlResult response = isAAAProxyProvider.get().getISAAAQuerySimplifiedService()
                    .standardQueryExternal(standardQueryExternalParams).getReturn();
            internalResponse = inspectISAAASimplifiedResponse(response);
        } catch (ClientTransportException ex) {
            handleClientTransportException(ex);
        }
        return internalResponse;
    }

    @Override
    protected String performComplementQueryCall(Object header, String xmlData) throws ISAAARequestFailed {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String performLoadAttachmentsQueryCall(Object header, LoadAttachmentsOrLinksData loadAttachmentsOrLinksData) throws ISAAARequestFailed {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String performLoadLinksCall(Object header, LoadAttachmentsOrLinksData loadAttachmentsOrLinksData) throws ISAAARequestFailed {
        throw new UnsupportedOperationException();
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
            logger.error("Standard Query External marshalling failed", e);
        }
        return stringWriter.toString();
    }

    @Override
    public String serializeComplementQueryRequestData(AAAIDType AAAID, SubsetCriteriaType subsetCriteriaType) {
        throw new UnsupportedOperationException();
    }

    private String inspectISAAASimplifiedResponse(ResponseWithXmlResult response) throws ISAAARequestFailed {
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
