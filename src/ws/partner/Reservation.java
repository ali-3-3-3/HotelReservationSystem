
package ws.partner;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for reservation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reservation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="checkInDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="checkInTime" type="{http://ws.session.ejb/}localTime" minOccurs="0"/>
 *         &lt;element name="checkOutDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="checkOutTime" type="{http://ws.session.ejb/}localTime" minOccurs="0"/>
 *         &lt;element name="guest" type="{http://ws.session.ejb/}guest" minOccurs="0"/>
 *         &lt;element name="hasCheckedIn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="hasCheckedOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="numOfRooms" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="partner" type="{http://ws.session.ejb/}partner" minOccurs="0"/>
 *         &lt;element name="reservationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="reservationId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="roomAllocations" type="{http://ws.session.ejb/}roomAllocation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="roomRates" type="{http://ws.session.ejb/}roomRate" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="roomType" type="{http://ws.session.ejb/}roomType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reservation", propOrder = {
    "checkInDate",
    "checkInTime",
    "checkOutDate",
    "checkOutTime",
    "guest",
    "hasCheckedIn",
    "hasCheckedOut",
    "numOfRooms",
    "partner",
    "reservationDate",
    "reservationId",
    "roomAllocations",
    "roomRates",
    "roomType"
})
public class Reservation {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar checkInDate;
    protected LocalTime checkInTime;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar checkOutDate;
    protected LocalTime checkOutTime;
    protected Guest guest;
    protected boolean hasCheckedIn;
    protected boolean hasCheckedOut;
    protected int numOfRooms;
    protected Partner partner;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reservationDate;
    protected Long reservationId;
    @XmlElement(nillable = true)
    protected List<RoomAllocation> roomAllocations;
    @XmlElement(nillable = true)
    protected List<RoomRate> roomRates;
    protected RoomType roomType;

    /**
     * Gets the value of the checkInDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCheckInDate() {
        return checkInDate;
    }

    /**
     * Sets the value of the checkInDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCheckInDate(XMLGregorianCalendar value) {
        this.checkInDate = value;
    }

    /**
     * Gets the value of the checkInTime property.
     * 
     * @return
     *     possible object is
     *     {@link LocalTime }
     *     
     */
    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    /**
     * Sets the value of the checkInTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalTime }
     *     
     */
    public void setCheckInTime(LocalTime value) {
        this.checkInTime = value;
    }

    /**
     * Gets the value of the checkOutDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Sets the value of the checkOutDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCheckOutDate(XMLGregorianCalendar value) {
        this.checkOutDate = value;
    }

    /**
     * Gets the value of the checkOutTime property.
     * 
     * @return
     *     possible object is
     *     {@link LocalTime }
     *     
     */
    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    /**
     * Sets the value of the checkOutTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalTime }
     *     
     */
    public void setCheckOutTime(LocalTime value) {
        this.checkOutTime = value;
    }

    /**
     * Gets the value of the guest property.
     * 
     * @return
     *     possible object is
     *     {@link Guest }
     *     
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * Sets the value of the guest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Guest }
     *     
     */
    public void setGuest(Guest value) {
        this.guest = value;
    }

    /**
     * Gets the value of the hasCheckedIn property.
     * 
     */
    public boolean isHasCheckedIn() {
        return hasCheckedIn;
    }

    /**
     * Sets the value of the hasCheckedIn property.
     * 
     */
    public void setHasCheckedIn(boolean value) {
        this.hasCheckedIn = value;
    }

    /**
     * Gets the value of the hasCheckedOut property.
     * 
     */
    public boolean isHasCheckedOut() {
        return hasCheckedOut;
    }

    /**
     * Sets the value of the hasCheckedOut property.
     * 
     */
    public void setHasCheckedOut(boolean value) {
        this.hasCheckedOut = value;
    }

    /**
     * Gets the value of the numOfRooms property.
     * 
     */
    public int getNumOfRooms() {
        return numOfRooms;
    }

    /**
     * Sets the value of the numOfRooms property.
     * 
     */
    public void setNumOfRooms(int value) {
        this.numOfRooms = value;
    }

    /**
     * Gets the value of the partner property.
     * 
     * @return
     *     possible object is
     *     {@link Partner }
     *     
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * Sets the value of the partner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Partner }
     *     
     */
    public void setPartner(Partner value) {
        this.partner = value;
    }

    /**
     * Gets the value of the reservationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReservationDate() {
        return reservationDate;
    }

    /**
     * Sets the value of the reservationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReservationDate(XMLGregorianCalendar value) {
        this.reservationDate = value;
    }

    /**
     * Gets the value of the reservationId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReservationId() {
        return reservationId;
    }

    /**
     * Sets the value of the reservationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReservationId(Long value) {
        this.reservationId = value;
    }

    /**
     * Gets the value of the roomAllocations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roomAllocations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoomAllocations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoomAllocation }
     * 
     * 
     */
    public List<RoomAllocation> getRoomAllocations() {
        if (roomAllocations == null) {
            roomAllocations = new ArrayList<RoomAllocation>();
        }
        return this.roomAllocations;
    }

    /**
     * Gets the value of the roomRates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roomRates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoomRates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoomRate }
     * 
     * 
     */
    public List<RoomRate> getRoomRates() {
        if (roomRates == null) {
            roomRates = new ArrayList<RoomRate>();
        }
        return this.roomRates;
    }

    /**
     * Gets the value of the roomType property.
     * 
     * @return
     *     possible object is
     *     {@link RoomType }
     *     
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * Sets the value of the roomType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoomType }
     *     
     */
    public void setRoomType(RoomType value) {
        this.roomType = value;
    }

}
