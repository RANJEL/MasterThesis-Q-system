package cz.komix.qsystem.backend.logic.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Named;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Class that provides mapping of ComplementQueryResponseData into known structure of StandardQueryResponse.
 * (string -{@literal >} string -{@literal >} deserialization later)
 * Thread safe class because it's stateless.
 *
 * @author Jan Lejnar
 */
@Named
public class ComplementQueryResponseDataMapper {

    private final Logger logger = LoggerFactory.getLogger(ComplementQueryResponseDataMapper.class);

    private final String querydt = "http://www.komix.cz/aaa/query/types";
    private final String aaarecorddt = "http://www.siteone.com/aaa/xsd/v100/interface_types/record";
    private final String aaadt = "http://www.siteone.com/aaa/xsd/v100/interface_types/common";

    private Node deepCopy(Node complementNodeToCopy, TransformationContext context) {
        return context.documentStandard.importNode(complementNodeToCopy, true);
    }

    private Node deepCopyWithRename(Node complementNodeToCopy,
                                    String namespaceURL,
                                    String fullTagName,
                                    TransformationContext context) {
        Node standardNodeCopy = deepCopy(complementNodeToCopy, context);
        return context.documentStandard.renameNode(standardNodeCopy,
                namespaceURL,
                fullTagName);
    }

    private Node createHitCandidateWrapper(TransformationContext context) {
	// ANONYMIZED
    }

    private void addFlag(Node hitCandidate, Node complementData, TransformationContext context) throws XPathExpressionException {
        Node attributeComplement = (Node) context.xPath.compile(
                "Flags/Flag")
                .evaluate(complementData, XPathConstants.NODE);
        if (attributeComplement != null) {
            hitCandidate.appendChild(deepCopy(attributeComplement, context));
        }
    }

    private void addLinks(Node hitCandidate, Node complementData, TransformationContext context) throws XPathExpressionException {
        Node linksComplement = (Node) context.xPath.compile(
                "Links")
                .evaluate(complementData, XPathConstants.NODE);
        if (linksComplement != null) {
            hitCandidate.appendChild(deepCopy(linksComplement, context));
        }
    }

    private CoreEntities addMetaData(Node hitCandidate,
                                     Node complementData,
                                     TransformationContext context) throws XPathExpressionException {
	// ANONYMIZED
    }

    private void addObject(Node hitCandidate, Node complementData, TransformationContext context, Node objectNodeComplement) throws XPathExpressionException {
 	// ANONYMIZED
    }

    private void addPersonBinary(String binaryXpath, Node personRoot, Node complementData, TransformationContext context) throws XPathExpressionException {
        Node personBinaryComplement = (Node) context.xPath.compile(
                binaryXpath)
                .evaluate(complementData, XPathConstants.NODE);
        if (personBinaryComplement != null) {
            personRoot.appendChild(deepCopy(personBinaryComplement, context));
        }
    }

    private void addPersonIdentities(Node personCoreStandard, Node complementData, TransformationContext context) throws XPathExpressionException {
	// ANONYMIZED
    }

    private void addPerson(Node hitCandidate, Node complementData, TransformationContext context, Node personNodeComplement) throws XPathExpressionException {
	// ANONYMIZED
    }

    private void transformComplementQueryResponse2StandardQueryResponse(Document documentComplement, Document
            documentStandard) throws XPathExpressionException {
	// ANONYMIZED
    }

    private String findAndReplaceNamespaces(String response) {
        return response
                .replaceAll("querydt", "aaarecorddt")
                .replaceAll("aaaattributedt", "aaarecorddt")
                .replaceAll("aaalinkdt", "aaarecorddt")
                .replaceFirst("xmlns:aaarecorddt=\"http://www.komix.cz/aaa/query/types\"", "")
                .replaceFirst("xmlns:aaarecorddt=\"http://www.siteone.com/aaa/xsd/v100/interface_types/link\"", "")
                .replaceFirst("xmlns:aaarecorddt=\"http://www.siteone.com/aaa/xsd/v100/interface_types/attribute\"", "");
    }

    public String transformComplementQueryResponse2StandardQueryResponse(String complementQueryResponseData) {
        Document standardQueryDocument = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document complementQueryDocument = docBuilder.parse(new ByteArrayInputStream(
                    findAndReplaceNamespaces(complementQueryResponseData).getBytes(Charset.forName("UTF-8"))));
            standardQueryDocument = docBuilder.newDocument();
            transformComplementQueryResponse2StandardQueryResponse(complementQueryDocument, standardQueryDocument);
        } catch (ParserConfigurationException e) {
            logger.error("Unable to create DocumentBuilder", e);
        } catch (SAXException | IOException e) {
            logger.error("DocumentBuilder was unable to parse modified complementQueryResponseData", e);
        } catch (XPathExpressionException e) {
            logger.error("XPath error during transformation ComplementQueryResponseData to StandardQueryResponseData", e);
        }

        StringWriter output = new StringWriter();

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.transform(new DOMSource(standardQueryDocument), new StreamResult(output));
        } catch (TransformerException e) {
            logger.error("Unable to convert XML-DOM back to String", e);
        }

        return output.toString();
    }

    private class TransformationContext {
        public Document documentComplement;
        public Document documentStandard;
        public XPath xPath;

        public TransformationContext(Document documentComplement, Document documentStandard, XPath xPath) {
            this.documentComplement = documentComplement;
            this.documentStandard = documentStandard;
            this.xPath = xPath;
        }
    }

    private class CoreEntities {
        public Node objectNode;
        public Node personNode;

        public CoreEntities() {
        }

        public CoreEntities(Node objectNode, Node personNode) {
            this.objectNode = objectNode;
            this.personNode = personNode;
        }
    }

}
