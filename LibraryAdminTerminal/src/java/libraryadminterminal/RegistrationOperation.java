package libraryadminterminal;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.InputMismatchException;
import java.util.Scanner;


public class RegistrationOperation {
    private MemberEntityControllerRemote memberEntityControllerRemote;
    
    public RegistrationOperation() {
    }

    public RegistrationOperation(MemberEntityControllerRemote memberEntityControllerRemote) 
    {
        this();
        this.memberEntityControllerRemote = memberEntityControllerRemote;
   
    }
    
    public void menuRegistration() 
    {
       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n");
            System.out.println("*** ILS :: Registration Operation ***\n");
            System.out.println("1: Register New Member");
            System.out.println("2: Back\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                switch (response) {
                    case 1:
                        createNewMember();
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;                
                }
            }
            
            if(response == 2)
            {
                return;
            }
        }
    }
    
      private void createNewMember() {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();
        
        System.out.println("\n");
        System.out.println("*** ILS :: Registration Operation :: Register New Member ***\n");
        
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
        System.out.println("Member has been registered successfully! \n");
    }
}