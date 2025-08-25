package cz.komix.qsystem.backend.persistence.appfeatures.jaxb;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jan Lejnar
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbQueryType implements Comparable<JaxbQueryType> {
    private String name;
    @XmlAttribute(name = "order")
    private Integer elemOrder;

    @Override
    public int compareTo(JaxbQueryType o) {
        return ObjectUtils.compare(this.elemOrder, o.elemOrder, true);
    }
}
