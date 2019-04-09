package selfservicekiosk;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.PaymentEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.MemberEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private FineEntityControllerRemote fineEntityControllerRemote;
    private ReservationEntityControllerRemote reservationEntityControllerRemote;
    private PaymentEntityControllerRemote paymentEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    
    private LibraryOperation libraryOperation;

    private MemberEntity currentMemberEntity;
    
    public MainApp() {
    }

    public MainApp(MemberEntityControllerRemote memberEntityControllerRemote, FineEntityControllerRemote fineEntityControllerRemote, ReservationEntityControllerRemote reservationEntityControllerRemote, PaymentEntityControllerRemote paymentEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, LendingEntityControllerRemote lendingEntityControllerRemote, LibraryOperation libraryOperation, MemberEntity currentMemberEntity) 
    {
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.fineEntityControllerRemote = fineEntityControllerRemote;
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;
        this.paymentEntityControllerRemote = paymentEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.libraryOperation = libraryOperation;
        this.currentMemberEntity = currentMemberEntity;
    }
    
      public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n");
            System.out.println("*** Welcome to Self-Service Kiosk ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit \n");
            response = 0;
            
            OUTER:
            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        doRegister();
                        break;
                    
                    case 2:
                        try {
                            doLogin();
                            libraryOperation = new LibraryOperation(fineEntityControllerRemote, reservationEntityControllerRemote, bookEntityControllerRemote, paymentEntityControllerRemote, memberEntityControllerRemote, lendingEntityControllerRemote);
                            menuMain();
                        } catch(InvalidLoginCredentialException ex) {
                            
                        }   break;
                    case 3:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");                
                        break;
                }
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    private void doRegister() {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Register ***\n");
        
        try {
            System.out.print("Enter Identity Number> ");
            newMemberEntity.setIdentityNumber(scanner.nextLine().trim());
            System.out.print("Enter Security Code> ");
            newMemberEntity.setSecurityCode(scanner.nextLine().trim());
            System.out.print("Enter First Name> ");
            newMemberEntity.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Last Name> ");
            newMemberEntity.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Gender> ");
            newMemberEntity.setGender(scanner.nextLine().trim());
            System.out.print("Enter Age> ");
            newMemberEntity.setAge(scanner.nextInt());
            scanner.nextLine();
            System.out.print("Enter Phone> ");
            newMemberEntity.setPhone(scanner.nextLine().trim());
            System.out.print("Enter Address> ");
            newMemberEntity.setAddress(scanner.nextLine().trim());
            newMemberEntity.setBookBorrowed(0);
            } 
            catch (InputMismatchException ex)
            {
                System.out.println("Please ensure all field input types are correct. Please try again.\n");
                return;
            }
        memberEntityControllerRemote.createNewMember(newMemberEntity);
        System.out.println("You have been registered successfully! \n");
    }
    

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String code = "";
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Login ***\n");
        System.out.print("Enter Identification Number> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        code = scanner.nextLine().trim();
        
        if(username.length() > 0 && code.length() > 0) {
            try {
                currentMemberEntity = memberEntityControllerRemote.memberLogin(username, code);
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
            System.out.println("*** Self-Service Kiosk :: Main ***\n");
            System.out.println("You are login as " + currentMemberEntity.getFirstName() + " " + currentMemberEntity.getLastName() + "\n");
            System.out.println("1: Borrow Book");
            System.out.println("2: View Lent Books");
            System.out.println("3: Return Book");
            System.out.println("4: Extend Book");
            System.out.println("5: Pay Fines");
            System.out.println("6: Search Book");
            System.out.println("7: Reserve Book");
            System.out.println("8: Log Out\n");
            response = 0;
            
            OUTER:
            while (response < 1 || response > 8) {
                System.out.print("> ");
                response = scanner.nextInt();
                switch (response) {
                    case 1:
                        libraryOperation.borrowBook(currentMemberEntity);
                        break;
                    case 2:
                        libraryOperation.viewLentBooks(currentMemberEntity);
                        break;
                    case 3:
                        libraryOperation.returnBook(currentMemberEntity);
                        break;
                    case 4:
                        libraryOperation.extendBook(currentMemberEntity);
                        break;
                   case 5:
                        libraryOperation.payFines(currentMemberEntity);
                        break;
                   case 6:
                        libraryOperation.searchBook(currentMemberEntity);
                        break;
                   case 7:
                        libraryOperation.reserveBook(currentMemberEntity);
                        break;
                    case 8:                
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
            
            if(response == 8) {
                break;
            }
        }
    }   
}