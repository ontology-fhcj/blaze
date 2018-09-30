package pers.ontology.blaze.packet.xml;

import org.apache.commons.collections4.CollectionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "protobuf")
public class ProtobufXmlConfig {


    @XmlElement(name = "type-mapping")
    private List<TypeMapping> typeMappings;


    public List<TypeMapping> getTypeMappings () {
        return typeMappings;
    }

    public void setTypeMappings (List<TypeMapping> typeMappings) {
        this.typeMappings = typeMappings;
    }


    public byte findHexadecimal (Class<?> clazz) throws ClassNotFoundException {

        if (!CollectionUtils.isEmpty(typeMappings)) {
            for (TypeMapping typeMapping : typeMappings) {
                if (clazz == typeMapping.findClass()) {
                    String hexadecimal = typeMapping.getHexadecimal();
                    return (byte)Integer.parseInt(hexadecimal,16);
                }

            }
        }

        return 0;

    }

    public Class<?> findClass (byte hexadecimal) throws ClassNotFoundException {
        if (!CollectionUtils.isEmpty(typeMappings)) {
            for (TypeMapping typeMapping : typeMappings) {
                if (hexadecimal == (byte)Integer.parseInt(typeMapping.getHexadecimal(),16)) {
                    return typeMapping.findClass();
                }

            }
        }

        return null;
    }


}
