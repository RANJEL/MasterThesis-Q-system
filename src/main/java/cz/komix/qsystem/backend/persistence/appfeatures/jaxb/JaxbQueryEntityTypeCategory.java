package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Lejnar
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbQueryEntityTypeCategory {
    private Map<String, JaxbQueryEntityType> entityTypesMap = new HashMap<>();
}
