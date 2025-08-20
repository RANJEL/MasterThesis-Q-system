package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Jan Lejnar
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbSearchCriteriaCombination {
    @XmlElement(name = "searchCriteria")
    private Set<JaxbSearchCriteria> searchCriteriaCombination = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JaxbSearchCriteriaCombination that = (JaxbSearchCriteriaCombination) o;
        return Objects.equals(searchCriteriaCombination, that.searchCriteriaCombination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchCriteriaCombination);
    }
}
