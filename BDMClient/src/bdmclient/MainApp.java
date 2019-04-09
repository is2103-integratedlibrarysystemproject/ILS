package bdmclient;

import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import util.exception.InvalidLoginCredentialException;
import ws.client.BookNotFoundException_Exception;
import ws.client.FineNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.MemberEntity;
import ws.client.MemberNotFoundException_Exception;

public class MainApp {

    private final ws.client.SOAPWebServices_Service service = new ws.client.SOAPWebServices_Service();
    private final ws.client.SOAPWebServices port = service.getSOAPWebServicesPort();
    private MemberEntity currentMemberEntity;
    private LibraryOperation libraryOperation;
    
    public MainApp() {
    }
    
    public void runApp() throws InvalidLoginCredentialException_Exception, MemberNotFoundException_Exception, BookNotFoundException_Exception, DatatypeConfigurationException, FineNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n");
            System.out.println("*** Welcome to BDM Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit");
            response = 0;
            
            OUTER:
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {

                    case 1:
                        try {
                            doLogin();
                            libraryOperation = new LibraryOperation(port, service, currentMemberEntity);
                            menuMain();
                        } catch(InvalidLoginCredentialException ex) 
                        {
                        }   break;
                    case 2:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");                
                        break;
                }
            }
            
            if (response == 2) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException, InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
       
        System.out.println("\n");
        System.out.println("*** BDM Client :: Login ***\n");
        System.out.print("Enter Identification Number> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        String code = scanner.nextLine().trim();
        
        if(username.length() > 0 && code.length() > 0) {
            currentMemberEntity = port.memberLogin(username, code);
            System.out.println("Login successful!\n");           
        } else {
            System.out.println("Invalid login credential!");
            return;
        }
    }
    
    private void menuMain() throws MemberNotFoundException_Exception, BookNotFoundException_Exception, DatatypeConfigurationException, FineNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n");
            System.out.println("*** BDM Client :: Main ***\n");
            System.out.println("You are login as " + currentMemberEntity.getFirstName() + " " + currentMemberEntity.getLastName() + "\n");
            System.out.println("1: View Lent Books");
            System.out.println("2: Return Book");
            System.out.println("3: Extend Book");
            System.out.println("4: Pay Fines");
            System.out.println("5: Reserve Book");
            System.out.println("6: Log Out\n");
            response = 0;
            
            OUTER:
            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        libraryOperation.viewLentBooks();
                        break;
                    case 2:
                        libraryOperation.returnBook();
                        break;
                    case 3:
                        libraryOperation.extendBook();
                        break;
                   case 4:
                        libraryOperation.payFines();
                        break;
                   case 5:
                        libraryOperation.reserveBook();
                        break;
                    case 6:                
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
            
            if(response == 6) {
                break;
            }
        }
    }   
    
}


