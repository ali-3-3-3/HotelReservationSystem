
package ws.partner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for roomAllocation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="roomAllocation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allocationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="allocationId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="exception" type="{http://ws.session.ejb/}allocationException" minOccurs="0"/>
 *         &lt;element name="isException" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="reservation" type="{http://ws.session.ejb/}reservation" minOccurs="0"/>
 *         &lt;element name="room" type="{http://ws.session.ejb/}room" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roomAllocation", propOrder = {
    "allocationDate",
    "allocationId",
    "exception",
    "isException",
    "reservation",
    "room"
})
public class RoomAllocation {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar allocationDate;
    protected Long allocationId;
    protected AllocationException exception;
    protected boolean isException;
    protected Reservation reservation;
    protected Room room;

    /**
     * Gets the value of the allocationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAllocationDate() {
        return allocationDate;
    }

    /**
     * Sets the value of the allocationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAllocationDate(XMLGregorianCalendar value) {
        this.allocationDate = value;
    }

    /**
     * Gets the value of the allocationId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAllocationId() {
        return allocationId;
    }

    /**
     * Sets the value of the allocationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAllocationId(Long value) {
        this.allocationId = value;
    }

    /**
     * Gets the value of the exception property.
     * 
     * @return
     *     possible object is
     *     {@link AllocationException }
     *     
     */
    public AllocationException getException() {
        return exception;
    }

    /**
     * Sets the value of the exception property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllocationException }
     *     
     */
    public void setException(AllocationException value) {
        this.exception = value;
    }

    /**
     * Gets the value of the isException property.
     * 
     */
    public boolean isIsException() {
        return isException;
    }

    /**
     * Sets the value of the isException property.
     * 
     */
    public void setIsException(boolean value) {
        this.isException = value;
    }

    /**
     * Gets the value of the reservation property.
     * 
     * @return
     *     possible object is
     *     {@link Reservation }
     *     
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Sets the value of the reservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reservation }
     *     
     */
    public void setReservation(Reservation value) {
        this.reservation = value;
    }

    /**
     * Gets the value of the room property.
     * 
     * @return
     *     possible object is
     *     {@link Room }
     *     
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the value of the room property.
     * 
     * @param value
     *     allowed object is
     *     {@link Room }
     *     
     */
    public void setRoom(Room value) {
        this.room = value;
    }

}
