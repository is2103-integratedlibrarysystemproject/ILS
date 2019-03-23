/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdmclient;

/**
 *
 * @author HP
 */
public class BDMClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    private static String hello(java.lang.String name) {
        ws.client.SOAPWebServices_Service service = new ws.client.SOAPWebServices_Service();
        ws.client.SOAPWebServices port = service.getSOAPWebServicesPort();
        return port.hello(name);
    }
    
}
