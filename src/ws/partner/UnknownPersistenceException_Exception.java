
package ws.partner;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "UnknownPersistenceException", targetNamespace = "http://ws.session.ejb/")
public class UnknownPersistenceException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private UnknownPersistenceException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public UnknownPersistenceException_Exception(String message, UnknownPersistenceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public UnknownPersistenceException_Exception(String message, UnknownPersistenceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: ws.partner.UnknownPersistenceException
     */
    public UnknownPersistenceException getFaultInfo() {
        return faultInfo;
    }

}
