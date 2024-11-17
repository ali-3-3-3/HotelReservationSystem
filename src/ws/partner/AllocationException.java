
package ws.partner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for allocationException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="allocationException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exceptionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="resolutionStatus" type="{http://ws.session.ejb/}resolutionStatusEnum" minOccurs="0"/>
 *         &lt;element name="roomAllocation" type="{http://ws.session.ejb/}roomAllocation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "allocationException", propOrder = {
    "errorDescription",
    "exceptionId",
    "resolutionStatus",
    "roomAllocation"
})
public class AllocationException {

    protected String errorDescription;
    protected Long exceptionId;
    @XmlSchemaType(name = "string")
    protected ResolutionStatusEnum resolutionStatus;
    protected RoomAllocation roomAllocation;

    /**
     * Gets the value of the errorDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Sets the value of the errorDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorDescription(String value) {
        this.errorDescription = value;
    }

    /**
     * Gets the value of the exceptionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExceptionId() {
        return exceptionId;
    }

    /**
     * Sets the value of the exceptionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExceptionId(Long value) {
        this.exceptionId = value;
    }

    /**
     * Gets the value of the resolutionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionStatusEnum }
     *     
     */
    public ResolutionStatusEnum getResolutionStatus() {
        return resolutionStatus;
    }

    /**
     * Sets the value of the resolutionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionStatusEnum }
     *     
     */
    public void setResolutionStatus(ResolutionStatusEnum value) {
        this.resolutionStatus = value;
    }

    /**
     * Gets the value of the roomAllocation property.
     * 
     * @return
     *     possible object is
     *     {@link RoomAllocation }
     *     
     */
    public RoomAllocation getRoomAllocation() {
        return roomAllocation;
    }

    /**
     * Sets the value of the roomAllocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoomAllocation }
     *     
     */
    public void setRoomAllocation(RoomAllocation value) {
        this.roomAllocation = value;
    }

}
