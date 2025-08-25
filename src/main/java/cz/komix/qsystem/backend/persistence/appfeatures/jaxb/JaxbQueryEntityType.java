package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jan Lejnar
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbQueryEntityType implements Comparable<JaxbQueryEntityType> {
    private String name;
    private Map<String, JaxbSearchCriteria> searchCriteriaMap = new HashMap<>();
    @XmlElementWrapper(name = "allowedSearchCriteriaCombinations")
    @XmlElement(name = "searchCriteriaCombination")
    private Set<JaxbSearchCriteriaCombination> allowedSearchCriteriaCombinations = new HashSet<>();
    @XmlAttribute(name = "order")
    private Integer elemOrder;

    @Override
    public int compareTo(JaxbQueryEntityType o) {
        return ObjectUtils.compare(this.elemOrder, o.elemOrder, true);
    }
}
