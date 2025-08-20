package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Root class that is mapped to XML structure of app features file {see app-features.xml} using JAXB.
 *
 * @author Jan Lejnar
 */
@Data
@XmlRootElement(name = "appFeatures")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppFeaturesDataClass {
    @XmlElementWrapper(name = "queryTypes")
    @XmlElement(name = "queryType")
    private SortedSet<JaxbQueryType> queryTypes = new TreeSet<>();
    @XmlElementWrapper(name = "queryModifiers")
    @XmlElement(name = "queryModifier")
    private SortedSet<JaxbQueryModifier> queryModifiers = new TreeSet<>();
    private Map<String, JaxbQueryEntityTypeCategory> queryEntityTypeCategoriesMap = new HashMap<>();
}
