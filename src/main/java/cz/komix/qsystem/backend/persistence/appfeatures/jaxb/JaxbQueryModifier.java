package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author Jan Lejnar
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbQueryModifier implements Comparable<JaxbQueryModifier> {
    private String name;
    @XmlAttribute(name = "order")
    private Integer elemOrder;

    @Override
    public int compareTo(JaxbQueryModifier o) {
        return ObjectUtils.compare(this.elemOrder, o.elemOrder, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JaxbQueryModifier that = (JaxbQueryModifier) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
