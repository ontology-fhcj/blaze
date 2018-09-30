package pers.ontology.blaze.packet.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "type-mapping")
public class TypeMapping {


    @XmlAttribute
    private String name;

    @XmlAttribute
    private String hexadecimal;

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getHexadecimal () {
        return hexadecimal;
    }

    public void setHexadecimal (String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    public Class<?> findClass () throws ClassNotFoundException {
        return Class.forName(name);
    }

}
