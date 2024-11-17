
package ws.partner;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws.partner package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RoomTypeUnavailableException_QNAME = new QName("http://ws.session.ejb/", "RoomTypeUnavailableException");
    private final static QName _RetrieveGuestByGuestId_QNAME = new QName("http://ws.session.ejb/", "retrieveGuestByGuestId");
    private final static QName _InputDataValidationException_QNAME = new QName("http://ws.session.ejb/", "InputDataValidationException");
    private final static QName _ReserveNewReservation_QNAME = new QName("http://ws.session.ejb/", "reserveNewReservation");
    private final static QName _CalculatePre_QNAME = new QName("http://ws.session.ejb/", "calculatePre");
    private final static QName _RetrievePartnerReservationsByReservationIdResponse_QNAME = new QName("http://ws.session.ejb/", "retrievePartnerReservationsByReservationIdResponse");
    private final static QName _ReservationNotFoundException_QNAME = new QName("http://ws.session.ejb/", "ReservationNotFoundException");
    private final static QName _DoLoginResponse_QNAME = new QName("http://ws.session.ejb/", "doLoginResponse");
    private final static QName _ViewReservationsByPartnerIdResponse_QNAME = new QName("http://ws.session.ejb/", "viewReservationsByPartnerIdResponse");
    private final static QName _ReserveNewReservationResponse_QNAME = new QName("http://ws.session.ejb/", "reserveNewReservationResponse");
    private final static QName _ViewReservationsByPartnerId_QNAME = new QName("http://ws.session.ejb/", "viewReservationsByPartnerId");
    private final static QName _GuestNotFoundException_QNAME = new QName("http://ws.session.ejb/", "GuestNotFoundException");
    private final static QName _UnknownPersistenceException_QNAME = new QName("http://ws.session.ejb/", "UnknownPersistenceException");
    private final static QName _SearchRoomResponse_QNAME = new QName("http://ws.session.ejb/", "searchRoomResponse");
    private final static QName _InvalidLoginCredentialException_QNAME = new QName("http://ws.session.ejb/", "InvalidLoginCredentialException");
    private final static QName _DoLogin_QNAME = new QName("http://ws.session.ejb/", "doLogin");
    private final static QName _RoomTypeNotFoundException_QNAME = new QName("http://ws.session.ejb/", "RoomTypeNotFoundException");
    private final static QName _InvalidRoomCountException_QNAME = new QName("http://ws.session.ejb/", "InvalidRoomCountException");
    private final static QName _RetrieveGuestByGuestIdResponse_QNAME = new QName("http://ws.session.ejb/", "retrieveGuestByGuestIdResponse");
    private final static QName _SearchRoom_QNAME = new QName("http://ws.session.ejb/", "searchRoom");
    private final static QName _CalculatePreResponse_QNAME = new QName("http://ws.session.ejb/", "calculatePreResponse");
    private final static QName _RetrievePartnerReservationsByReservationId_QNAME = new QName("http://ws.session.ejb/", "retrievePartnerReservationsByReservationId");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws.partner
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchRoomResponse }
     * 
     */
    public SearchRoomResponse createSearchRoomResponse() {
        return new SearchRoomResponse();
    }

    /**
     * Create an instance of {@link InvalidLoginCredentialException }
     * 
     */
    public InvalidLoginCredentialException createInvalidLoginCredentialException() {
        return new InvalidLoginCredentialException();
    }

    /**
     * Create an instance of {@link DoLogin }
     * 
     */
    public DoLogin createDoLogin() {
        return new DoLogin();
    }

    /**
     * Create an instance of {@link RoomTypeNotFoundException }
     * 
     */
    public RoomTypeNotFoundException createRoomTypeNotFoundException() {
        return new RoomTypeNotFoundException();
    }

    /**
     * Create an instance of {@link InvalidRoomCountException }
     * 
     */
    public InvalidRoomCountException createInvalidRoomCountException() {
        return new InvalidRoomCountException();
    }

    /**
     * Create an instance of {@link RetrieveGuestByGuestIdResponse }
     * 
     */
    public RetrieveGuestByGuestIdResponse createRetrieveGuestByGuestIdResponse() {
        return new RetrieveGuestByGuestIdResponse();
    }

    /**
     * Create an instance of {@link SearchRoom }
     * 
     */
    public SearchRoom createSearchRoom() {
        return new SearchRoom();
    }

    /**
     * Create an instance of {@link CalculatePreResponse }
     * 
     */
    public CalculatePreResponse createCalculatePreResponse() {
        return new CalculatePreResponse();
    }

    /**
     * Create an instance of {@link RetrievePartnerReservationsByReservationId }
     * 
     */
    public RetrievePartnerReservationsByReservationId createRetrievePartnerReservationsByReservationId() {
        return new RetrievePartnerReservationsByReservationId();
    }

    /**
     * Create an instance of {@link ReserveNewReservationResponse }
     * 
     */
    public ReserveNewReservationResponse createReserveNewReservationResponse() {
        return new ReserveNewReservationResponse();
    }

    /**
     * Create an instance of {@link ViewReservationsByPartnerId }
     * 
     */
    public ViewReservationsByPartnerId createViewReservationsByPartnerId() {
        return new ViewReservationsByPartnerId();
    }

    /**
     * Create an instance of {@link GuestNotFoundException }
     * 
     */
    public GuestNotFoundException createGuestNotFoundException() {
        return new GuestNotFoundException();
    }

    /**
     * Create an instance of {@link UnknownPersistenceException }
     * 
     */
    public UnknownPersistenceException createUnknownPersistenceException() {
        return new UnknownPersistenceException();
    }

    /**
     * Create an instance of {@link ReserveNewReservation }
     * 
     */
    public ReserveNewReservation createReserveNewReservation() {
        return new ReserveNewReservation();
    }

    /**
     * Create an instance of {@link CalculatePre }
     * 
     */
    public CalculatePre createCalculatePre() {
        return new CalculatePre();
    }

    /**
     * Create an instance of {@link RetrievePartnerReservationsByReservationIdResponse }
     * 
     */
    public RetrievePartnerReservationsByReservationIdResponse createRetrievePartnerReservationsByReservationIdResponse() {
        return new RetrievePartnerReservationsByReservationIdResponse();
    }

    /**
     * Create an instance of {@link ReservationNotFoundException }
     * 
     */
    public ReservationNotFoundException createReservationNotFoundException() {
        return new ReservationNotFoundException();
    }

    /**
     * Create an instance of {@link DoLoginResponse }
     * 
     */
    public DoLoginResponse createDoLoginResponse() {
        return new DoLoginResponse();
    }

    /**
     * Create an instance of {@link ViewReservationsByPartnerIdResponse }
     * 
     */
    public ViewReservationsByPartnerIdResponse createViewReservationsByPartnerIdResponse() {
        return new ViewReservationsByPartnerIdResponse();
    }

    /**
     * Create an instance of {@link RoomTypeUnavailableException }
     * 
     */
    public RoomTypeUnavailableException createRoomTypeUnavailableException() {
        return new RoomTypeUnavailableException();
    }

    /**
     * Create an instance of {@link RetrieveGuestByGuestId }
     * 
     */
    public RetrieveGuestByGuestId createRetrieveGuestByGuestId() {
        return new RetrieveGuestByGuestId();
    }

    /**
     * Create an instance of {@link InputDataValidationException }
     * 
     */
    public InputDataValidationException createInputDataValidationException() {
        return new InputDataValidationException();
    }

    /**
     * Create an instance of {@link AllocationException }
     * 
     */
    public AllocationException createAllocationException() {
        return new AllocationException();
    }

    /**
     * Create an instance of {@link RoomAllocation }
     * 
     */
    public RoomAllocation createRoomAllocation() {
        return new RoomAllocation();
    }

    /**
     * Create an instance of {@link Reservation }
     * 
     */
    public Reservation createReservation() {
        return new Reservation();
    }

    /**
     * Create an instance of {@link RoomType }
     * 
     */
    public RoomType createRoomType() {
        return new RoomType();
    }

    /**
     * Create an instance of {@link Room }
     * 
     */
    public Room createRoom() {
        return new Room();
    }

    /**
     * Create an instance of {@link LocalTime }
     * 
     */
    public LocalTime createLocalTime() {
        return new LocalTime();
    }

    /**
     * Create an instance of {@link RoomRate }
     * 
     */
    public RoomRate createRoomRate() {
        return new RoomRate();
    }

    /**
     * Create an instance of {@link Partner }
     * 
     */
    public Partner createPartner() {
        return new Partner();
    }

    /**
     * Create an instance of {@link Guest }
     * 
     */
    public Guest createGuest() {
        return new Guest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoomTypeUnavailableException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "RoomTypeUnavailableException")
    public JAXBElement<RoomTypeUnavailableException> createRoomTypeUnavailableException(RoomTypeUnavailableException value) {
        return new JAXBElement<RoomTypeUnavailableException>(_RoomTypeUnavailableException_QNAME, RoomTypeUnavailableException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveGuestByGuestId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrieveGuestByGuestId")
    public JAXBElement<RetrieveGuestByGuestId> createRetrieveGuestByGuestId(RetrieveGuestByGuestId value) {
        return new JAXBElement<RetrieveGuestByGuestId>(_RetrieveGuestByGuestId_QNAME, RetrieveGuestByGuestId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InputDataValidationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "InputDataValidationException")
    public JAXBElement<InputDataValidationException> createInputDataValidationException(InputDataValidationException value) {
        return new JAXBElement<InputDataValidationException>(_InputDataValidationException_QNAME, InputDataValidationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveNewReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "reserveNewReservation")
    public JAXBElement<ReserveNewReservation> createReserveNewReservation(ReserveNewReservation value) {
        return new JAXBElement<ReserveNewReservation>(_ReserveNewReservation_QNAME, ReserveNewReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculatePre }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "calculatePre")
    public JAXBElement<CalculatePre> createCalculatePre(CalculatePre value) {
        return new JAXBElement<CalculatePre>(_CalculatePre_QNAME, CalculatePre.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrievePartnerReservationsByReservationIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrievePartnerReservationsByReservationIdResponse")
    public JAXBElement<RetrievePartnerReservationsByReservationIdResponse> createRetrievePartnerReservationsByReservationIdResponse(RetrievePartnerReservationsByReservationIdResponse value) {
        return new JAXBElement<RetrievePartnerReservationsByReservationIdResponse>(_RetrievePartnerReservationsByReservationIdResponse_QNAME, RetrievePartnerReservationsByReservationIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReservationNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "ReservationNotFoundException")
    public JAXBElement<ReservationNotFoundException> createReservationNotFoundException(ReservationNotFoundException value) {
        return new JAXBElement<ReservationNotFoundException>(_ReservationNotFoundException_QNAME, ReservationNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "doLoginResponse")
    public JAXBElement<DoLoginResponse> createDoLoginResponse(DoLoginResponse value) {
        return new JAXBElement<DoLoginResponse>(_DoLoginResponse_QNAME, DoLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewReservationsByPartnerIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "viewReservationsByPartnerIdResponse")
    public JAXBElement<ViewReservationsByPartnerIdResponse> createViewReservationsByPartnerIdResponse(ViewReservationsByPartnerIdResponse value) {
        return new JAXBElement<ViewReservationsByPartnerIdResponse>(_ViewReservationsByPartnerIdResponse_QNAME, ViewReservationsByPartnerIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveNewReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "reserveNewReservationResponse")
    public JAXBElement<ReserveNewReservationResponse> createReserveNewReservationResponse(ReserveNewReservationResponse value) {
        return new JAXBElement<ReserveNewReservationResponse>(_ReserveNewReservationResponse_QNAME, ReserveNewReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewReservationsByPartnerId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "viewReservationsByPartnerId")
    public JAXBElement<ViewReservationsByPartnerId> createViewReservationsByPartnerId(ViewReservationsByPartnerId value) {
        return new JAXBElement<ViewReservationsByPartnerId>(_ViewReservationsByPartnerId_QNAME, ViewReservationsByPartnerId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuestNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "GuestNotFoundException")
    public JAXBElement<GuestNotFoundException> createGuestNotFoundException(GuestNotFoundException value) {
        return new JAXBElement<GuestNotFoundException>(_GuestNotFoundException_QNAME, GuestNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownPersistenceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "UnknownPersistenceException")
    public JAXBElement<UnknownPersistenceException> createUnknownPersistenceException(UnknownPersistenceException value) {
        return new JAXBElement<UnknownPersistenceException>(_UnknownPersistenceException_QNAME, UnknownPersistenceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "searchRoomResponse")
    public JAXBElement<SearchRoomResponse> createSearchRoomResponse(SearchRoomResponse value) {
        return new JAXBElement<SearchRoomResponse>(_SearchRoomResponse_QNAME, SearchRoomResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidLoginCredentialException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "InvalidLoginCredentialException")
    public JAXBElement<InvalidLoginCredentialException> createInvalidLoginCredentialException(InvalidLoginCredentialException value) {
        return new JAXBElement<InvalidLoginCredentialException>(_InvalidLoginCredentialException_QNAME, InvalidLoginCredentialException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "doLogin")
    public JAXBElement<DoLogin> createDoLogin(DoLogin value) {
        return new JAXBElement<DoLogin>(_DoLogin_QNAME, DoLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoomTypeNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "RoomTypeNotFoundException")
    public JAXBElement<RoomTypeNotFoundException> createRoomTypeNotFoundException(RoomTypeNotFoundException value) {
        return new JAXBElement<RoomTypeNotFoundException>(_RoomTypeNotFoundException_QNAME, RoomTypeNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidRoomCountException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "InvalidRoomCountException")
    public JAXBElement<InvalidRoomCountException> createInvalidRoomCountException(InvalidRoomCountException value) {
        return new JAXBElement<InvalidRoomCountException>(_InvalidRoomCountException_QNAME, InvalidRoomCountException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveGuestByGuestIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrieveGuestByGuestIdResponse")
    public JAXBElement<RetrieveGuestByGuestIdResponse> createRetrieveGuestByGuestIdResponse(RetrieveGuestByGuestIdResponse value) {
        return new JAXBElement<RetrieveGuestByGuestIdResponse>(_RetrieveGuestByGuestIdResponse_QNAME, RetrieveGuestByGuestIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "searchRoom")
    public JAXBElement<SearchRoom> createSearchRoom(SearchRoom value) {
        return new JAXBElement<SearchRoom>(_SearchRoom_QNAME, SearchRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculatePreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "calculatePreResponse")
    public JAXBElement<CalculatePreResponse> createCalculatePreResponse(CalculatePreResponse value) {
        return new JAXBElement<CalculatePreResponse>(_CalculatePreResponse_QNAME, CalculatePreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrievePartnerReservationsByReservationId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.session.ejb/", name = "retrievePartnerReservationsByReservationId")
    public JAXBElement<RetrievePartnerReservationsByReservationId> createRetrievePartnerReservationsByReservationId(RetrievePartnerReservationsByReservationId value) {
        return new JAXBElement<RetrievePartnerReservationsByReservationId>(_RetrievePartnerReservationsByReservationId_QNAME, RetrievePartnerReservationsByReservationId.class, null, value);
    }

}
