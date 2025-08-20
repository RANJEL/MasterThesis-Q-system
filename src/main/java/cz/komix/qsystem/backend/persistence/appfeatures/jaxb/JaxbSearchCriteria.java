package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * @author Jan Lejnar
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbSearchCriteria implements Comparable<JaxbSearchCriteria> {
    private String name;
    private String frontendElemType;
    @XmlList
    private List<String> frontendValidations = new ArrayList<>();
    @XmlElementWrapper(name = "allowedQueryModifiers")
    @XmlElement(name = "queryModifier")
    private Set<JaxbQueryModifier> allowedQueryModifiers = new HashSet<>();
    @XmlAttribute(name = "order")
    private Integer elemOrder;

    @Override
    public int compareTo(JaxbSearchCriteria o) {
        return ObjectUtils.compare(this.elemOrder, o.elemOrder, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JaxbSearchCriteria that = (JaxbSearchCriteria) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
