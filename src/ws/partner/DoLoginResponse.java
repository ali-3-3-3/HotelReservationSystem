
package ws.partner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for doLoginResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="doLoginResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="doLoginResponse" type="{http://ws.session.ejb/}partner" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "doLoginResponse", propOrder = {
    "doLoginResponse"
})
public class DoLoginResponse {

    protected Partner doLoginResponse;

    /**
     * Gets the value of the doLoginResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Partner }
     *     
     */
    public Partner getDoLoginResponse() {
        return doLoginResponse;
    }

    /**
     * Sets the value of the doLoginResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Partner }
     *     
     */
    public void setDoLoginResponse(Partner value) {
        this.doLoginResponse = value;
    }

}
