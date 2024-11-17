
package ws.partner;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for room complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="room">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="floorNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="isClean" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="roomAllocations" type="{http://ws.session.ejb/}roomAllocation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="roomId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="roomNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roomStatus" type="{http://ws.session.ejb/}roomStatusEnum" minOccurs="0"/>
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
@XmlType(name = "room", propOrder = {
    "floorNumber",
    "isClean",
    "roomAllocations",
    "roomId",
    "roomNumber",
    "roomStatus",
    "roomType"
})
public class Room {

    protected int floorNumber;
    protected boolean isClean;
    @XmlElement(nillable = true)
    protected List<RoomAllocation> roomAllocations;
    protected Long roomId;
    protected String roomNumber;
    @XmlSchemaType(name = "string")
    protected RoomStatusEnum roomStatus;
    protected RoomType roomType;

    /**
     * Gets the value of the floorNumber property.
     * 
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Sets the value of the floorNumber property.
     * 
     */
    public void setFloorNumber(int value) {
        this.floorNumber = value;
    }

    /**
     * Gets the value of the isClean property.
     * 
     */
    public boolean isIsClean() {
        return isClean;
    }

    /**
     * Sets the value of the isClean property.
     * 
     */
    public void setIsClean(boolean value) {
        this.isClean = value;
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
     * Gets the value of the roomId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * Sets the value of the roomId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRoomId(Long value) {
        this.roomId = value;
    }

    /**
     * Gets the value of the roomNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the value of the roomNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoomNumber(String value) {
        this.roomNumber = value;
    }

    /**
     * Gets the value of the roomStatus property.
     * 
     * @return
     *     possible object is
     *     {@link RoomStatusEnum }
     *     
     */
    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    /**
     * Sets the value of the roomStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoomStatusEnum }
     *     
     */
    public void setRoomStatus(RoomStatusEnum value) {
        this.roomStatus = value;
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
