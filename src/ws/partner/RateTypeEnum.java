
package ws.partner;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rateTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="rateTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PUBLISHED"/>
 *     &lt;enumeration value="NORMAL"/>
 *     &lt;enumeration value="PEAK"/>
 *     &lt;enumeration value="PROMOTION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "rateTypeEnum")
@XmlEnum
public enum RateTypeEnum {

    PUBLISHED,
    NORMAL,
    PEAK,
    PROMOTION;

    public String value() {
        return name();
    }

    public static RateTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
