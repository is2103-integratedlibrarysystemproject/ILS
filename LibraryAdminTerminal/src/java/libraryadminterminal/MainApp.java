package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.PaymentEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import java.util.Scanner;
import util.exception.FineNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;

public class MainApp {
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private FineEntityControllerRemote fineEntityControllerRemote;
    private ReservationEntityControllerRemote reservationEntityControllerRemote;
    private PaymentEntityControllerRemote paymentEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    
    private RegistrationOperation registrationOperation;
    private LibraryOperation libraryOperation;
    private AdminOperation adminOperation;
    private StaffEntity currentStaffEntity;
    
    public MainApp() {
    }

    public MainApp(StaffEntityControllerRemote staffEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, FineEntityControllerRemote fineEntityControllerRemote, ReservationEntityControllerRemote reservationEntityControllerRemote, PaymentEntityControllerRemote paymentEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, LendingEntityControllerRemote lendingEntityControllerRemote, RegistrationOperation registrationOperation, LibraryOperation libraryOperation, AdminOperation adminOperation, StaffEntity currentStaffEntity) {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.fineEntityControllerRemote = fineEntityControllerRemote;
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;
        this.paymentEntityControllerRemote = paymentEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.registrationOperation = registrationOperation;
        this.libraryOperation = libraryOperation;
        this.adminOperation = adminOperation;
        this.currentStaffEntity = currentStaffEntity;
    }
    
      public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n");
            System.out.println("*** Welcome to Library Admin Terminal ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            OUTER:
            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        try {
                            doLogin();
                            registrationOperation = new RegistrationOperation(memberEntityControllerRemote);
                            libraryOperation = new LibraryOperation(fineEntityControllerRemote, reservationEntityControllerRemote, bookEntityControllerRemote, paymentEntityControllerRemote, memberEntityControllerRemote, lendingEntityControllerRemote);
                            adminOperation = new AdminOperation(bookEntityControllerRemote, memberEntityControllerRemote, staffEntityControllerRemote);
                            menuMain();
                        } catch(InvalidLoginCredentialException ex) {
                            
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
    
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("\n");
        System.out.println("*** ILS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            try {
                currentStaffEntity = staffEntityControllerRemote.staffLogin(username, password);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginCredentialException ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                
                throw new InvalidLoginCredentialException();
            }           
        } else {
            System.out.println("Invalid login credential!");
            return;
        }
    }
    
    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n");
            System.out.println("*** ILS :: Main ***\n");
            System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + "\n");
            System.out.println("1: Registration Operation");
            System.out.println("2: Library Operation");
            System.out.println("3: Administration Operation");
            System.out.println("4: Logout\n");
            response = 0;
            
            OUTER:
            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        registrationOperation.menuRegistration();
                        break;
                    case 2:
                        libraryOperation.menuLibrary();
                        break;
                    case 3:
                        adminOperation.menuAdmin();
                        break;
                    case 4:                
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
            
            if(response == 4) {
                break;
            }
        }
    }   
}