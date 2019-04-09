package bdmclient;

import javax.xml.datatype.DatatypeConfigurationException;
import ws.client.BookNotFoundException_Exception;
import ws.client.FineNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.MemberNotFoundException_Exception;

public class Main {
    
    public static void main(String[] args) throws InvalidLoginCredentialException_Exception, MemberNotFoundException_Exception, BookNotFoundException_Exception, DatatypeConfigurationException, FineNotFoundException_Exception {
        
        MainApp mainApp = new MainApp();
        mainApp.runApp();
        
    }  
}
