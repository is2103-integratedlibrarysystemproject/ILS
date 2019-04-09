package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.StaffNotFoundException;


public class AdminOperation {
   
    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    
    public AdminOperation() {
    }

    public AdminOperation(BookEntityControllerRemote bookEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote) {
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.staffEntityControllerRemote = staffEntityControllerRemote;
    }

    
    public void menuAdmin() 
    {
       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("\n");
            System.out.println("*** ILS :: Administration Operation ***\n");
            System.out.println("1: Member Management");
            System.out.println("2: Book Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");

            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                switch (response) {
                    case 1:
                        manageMember();
                        break;
                    case 2:
                        manageBook();
                        break;
                    case 3:
                        manageStaff();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;                
                }
            }
        
            if(response == 4) {
                return;
            }
        }
    }
    
        private void manageMember() {
       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("\n");
            System.out.println("*** ILS :: Administration Operation :: Member Management ***\n");
            System.out.println("1: Add Member");
            System.out.println("2: View Member Details");
            System.out.println("3: Update Member");
            System.out.println("4: Delete Member");
            System.out.println("5: View All Members");
            System.out.println("6: Back\n");

            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();
                
                switch (response) {
                    case 1:
                        MemberEntity newMemberEntity = new MemberEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Add Member ***\n");
                        
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
                            memberEntityControllerRemote.createNewMember(newMemberEntity);
                            System.out.println("Member has been registered successfully!\n");
                        } catch (InputMismatchException ex) { 
                            System.out.println("Please ensure all field input types are correct. Please try again.\n");
                            return;
                        } break;
                        
                    case 2:
                        MemberEntity getMember = new MemberEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: View Member Details ***\n");
                        System.out.print("Enter Member Identity Number> ");
                        try {
                            String ic = scanner.nextLine().trim();
                            getMember = memberEntityControllerRemote.retrieveMemberByIc(ic);
                            
                            System.out.println("Serial Number: " + getMember.getMemberId());
                            System.out.println("Identity Number: " + getMember.getIdentityNumber());
                            System.out.println("First Name: " + getMember.getFirstName());
                            System.out.println("Last Name: " + getMember.getLastName());
                            System.out.println("Gender: " + getMember.getGender());
                            System.out.println("Age: " + getMember.getAge());
                            System.out.println("Phone: " + getMember.getPhone());
                            System.out.println("Address: " + getMember.getAddress() + "\n");
                            /*** Member security code should not be shown. ***/
                        } catch (MemberNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                            return;
                        }   break;
                    
                    case 3:
                        MemberEntity updatedMember = new MemberEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Update Member ***\n");
                        System.out.print("Enter Member Identity Number> ");
                        try {
                            
                            String ic = scanner.nextLine().trim();
                            updatedMember = memberEntityControllerRemote.retrieveMemberByIc(ic);
                            
                            try {
                                System.out.print("Enter New First Name> ");
                                updatedMember.setFirstName(scanner.nextLine().trim());
                                System.out.print("Enter New Last Name> ");
                                updatedMember.setLastName(scanner.nextLine().trim());
                                System.out.print("Enter New Age> ");
                                updatedMember.setAge(scanner.nextInt());
                                scanner.nextLine();
                                System.out.print("Enter Phone> ");
                                updatedMember.setPhone(scanner.nextLine().trim());
                                System.out.print("Enter Address> ");
                                updatedMember.setAddress(scanner.nextLine().trim());
                                /*** Staff cannot change Member's Security Code. ***/
                            } catch (InputMismatchException ex) { 
                                System.out.println("Please ensure all field input types are correct. Please try again.\n");
                                return;
                            }
                            
                            memberEntityControllerRemote.updateMember(updatedMember);
                            System.out.println("Member has been updated successfully!\n");
                            
                        } catch (MemberNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }   break;
                        
                    case 4:
                        MemberEntity toDeleteMember = new MemberEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Delete Member ***\n");
                        System.out.print("Enter Member Identity Number> ");
                        
                        try {  
                            String ic = scanner.nextLine().trim();
                            toDeleteMember = memberEntityControllerRemote.retrieveMemberByIc(ic);
                            memberEntityControllerRemote.deleteMember(toDeleteMember.getMemberId());
                            System.out.println("Member has been deleted successfully!\n");
                        } catch (MemberNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }   break;
                        
                    case 5:
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: View All Members ***\n");
                        List<MemberEntity> listOfMembers = memberEntityControllerRemote.retrieveAllMembers();
                        
                        System.out.printf("%-12s%-30s%-12s%-25s%-20s%-20s%-15s%-25s%-15s\n", "Member ID", "Address", "Age", "Books Borrowed", "First Name", "Last Name", "Gender", "Identity Number", "Phone");
                        
                        if (!listOfMembers.isEmpty()) {
                            for( MemberEntity member: listOfMembers) {
                                System.out.printf("%-12s%-30s%-12s%-25s%-20s%-20s%-15s%-25s%-15s\n", member.getMemberId().toString(), member.getAddress(), member.getAge(), member.getBookBorrowed(), member.getFirstName(), member.getLastName(), member.getGender(), member.getIdentityNumber(), member.getPhone());
                            } break;
                        } else {
                            System.out.println("No members found in database.\n");
                        }
                        
                    case 6:
                        return;
                        
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            } 
        }
    }
        
        private void manageBook() {
       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("\n");
            System.out.println("*** ILS :: Administration Operation :: Book Management ***\n");
            System.out.println("1: Add Book");
            System.out.println("2: View Book Details");
            System.out.println("3: Update Book");
            System.out.println("4: Delete Book");
            System.out.println("5: View All Books");
            System.out.println("6: Back\n");

            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();

                switch (response) {
                    case 1:
                        BookEntity newBookEntity = new BookEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Add Book ***\n");
                        
                        try {
                            System.out.print("Enter Title> ");
                            newBookEntity.setTitle(scanner.nextLine().trim());
                            System.out.print("Enter IBSN> ");
                            newBookEntity.setIsbn(scanner.nextLine().trim());
                            System.out.print("Enter Year> ");
                            newBookEntity.setYear(scanner.nextInt());
                            newBookEntity.setAvailable(1);
                            bookEntityControllerRemote.createNewBook(newBookEntity);
                            System.out.println("Book has been added successfully!\n");
                        } catch (InputMismatchException ex) { 
                            System.out.println("Please ensure all field input types are correct. Please try again.\n");
                            return;
                        } break;
                        
                    case 2:
                        BookEntity getBook = new BookEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: View Book Details ***\n");
                        System.out.print("Enter Book ISBN> ");
                        try {
                            String isbn = scanner.nextLine().trim();
                            getBook = bookEntityControllerRemote.retrieveBookByIsbn(isbn);
                            
                            System.out.println("Serial Number: " + getBook.getBookId());
                            System.out.println("Title: " + getBook.getTitle());
                            System.out.println("ISBN: " + getBook.getIsbn());
                            
                            String availability = "";
                            int quantity = getBook.getAvailable();
                            if (quantity == 1) {
                                availability = "Yes";
                            } else {
                                availability = "No";
                            }
                            System.out.println("Available> " + availability + "\n");
                        } catch (BookNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                            return;
                        }   break;
                        
                    case 3:
                        BookEntity updatedBook = new BookEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Update Book ***\n");
                        System.out.print("Enter Book ISBN> ");
                        
                        try {  
                            String isbn = scanner.nextLine().trim();
                            updatedBook = bookEntityControllerRemote.retrieveBookByIsbn(isbn);
                            
                            try {
                            System.out.print("Enter New Title> ");
                            updatedBook.setTitle(scanner.nextLine().trim());
                            System.out.print("Enter New ISBN> ");
                            updatedBook.setIsbn(scanner.nextLine().trim());
                            System.out.print("Enter New Year> ");
                            updatedBook.setYear(scanner.nextInt());
                            } catch (InputMismatchException ex) { 
                                System.out.println("Please ensure all field input types are correct. Please try again.\n");
                                return;
                            }
                            
                            bookEntityControllerRemote.updateBook(updatedBook);
                            System.out.println("Book has been updated successfully!\n");  
                            
                        } catch (BookNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }   break;
                        
                    case 4:
                        BookEntity toDeleteBook = new BookEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Delete Book ***\n");
                        System.out.print("Enter Book ISBN> ");
                        
                        try {
                            String isbn = scanner.nextLine().trim();
                            toDeleteBook = bookEntityControllerRemote.retrieveBookByIsbn(isbn);
                            bookEntityControllerRemote.deleteBook(toDeleteBook.getBookId());
                            System.out.println("Book has been deleted successfully!\n");
                        } catch (BookNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }   break;
                        
                    case 5:
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: View All Books ***\n");
                        List<BookEntity> listOfBooks = bookEntityControllerRemote.retrieveAllBooks();
                        
                        System.out.printf("%-12s%-50s%-20s%-15s%-8s\n", "Book ID", "Title", "ISBN", "Year", "Available");
                        
                        if (!listOfBooks.isEmpty()) {
                            for( BookEntity book: listOfBooks ) {
                                System.out.printf("%-12s%-50s%-20s%-15s%-8s\n", book.getBookId().toString(), book.getTitle(), book.getIsbn(), book.getYear(), book.getAvailable());
                            }   break;
                        } else {
                            System.out.println("No books found in database.\n");
                        }
                        
                    case 6:
                        return;
                        
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            } 
        }
    }
        
        private void manageStaff() {
       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("\n");
            System.out.println("*** ILS :: Administration Operation :: Staff Management ***\n");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back\n");

            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                switch (response) {
                    case 1:
                        StaffEntity newStaffEntity = new StaffEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Add Staff ***\n");
                        
                        try {
                            System.out.print("Enter First Name> ");
                            newStaffEntity.setFirstName(scanner.nextLine().trim());
                            System.out.print("Enter Last Name> ");
                            newStaffEntity.setLastName(scanner.nextLine().trim());
                            System.out.print("Enter Username> ");
                            newStaffEntity.setUsername(scanner.nextLine().trim());
                            System.out.print("Enter Password> ");
                            newStaffEntity.setPassword(scanner.nextLine().trim());
                            staffEntityControllerRemote.createNewStaff(newStaffEntity);
                            System.out.println("Staff has been added successfully!\n");
                        } catch (InputMismatchException ex) { 
                            System.out.println("Please ensure all field input types are correct. Please try again.\n");
                            return;
                        } break;
                        
                    case 2:
                        StaffEntity getStaff = new StaffEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: View Staff Details ***\n");
                        System.out.print("Enter Staff Username> ");
                        
                        try {
                            String username = scanner.nextLine().trim();
                            getStaff = staffEntityControllerRemote.retrieveStaffByUsername(username);
                            
                            System.out.println("Serial Number: " + getStaff.getStaffId());
                            System.out.println("First Name: " + getStaff.getFirstName());
                            System.out.println("Last Name: " + getStaff.getLastName());
                            System.out.println("Username: " + getStaff.getLastName() + "\n");
                            /*** Staff password should not be shown. ***/
                        } catch (StaffNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                            return;
                        }   break;
                       
                    case 3:
                        StaffEntity updatedStaff = new StaffEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Update Staff ***\n");
                        System.out.print("Enter Staff Username> ");
                        
                        try {
                            String username  = scanner.nextLine().trim();
                            updatedStaff = staffEntityControllerRemote.retrieveStaffByUsername(username);
                            
                            try {
                            System.out.print("Enter New First Name> ");
                            updatedStaff.setFirstName(scanner.nextLine().trim());
                            System.out.print("Enter New Last Name> ");
                            updatedStaff.setLastName(scanner.nextLine().trim());
                            System.out.print("Enter Username> ");
                            updatedStaff.setUsername(scanner.nextLine().trim());
                            System.out.print("Enter Password> ");
                            updatedStaff.setPassword(scanner.nextLine().trim());
                            } catch (InputMismatchException ex) { 
                                System.out.println("Please ensure all field input types are correct. Please try again.\n");
                                return;
                            }
                            
                            staffEntityControllerRemote.updateStaff(updatedStaff);
                            System.out.println("Staff has been updated successfully!\n"); 
                            
                        } catch (StaffNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }   break;
                        
                    case 4:
                        StaffEntity toDeleteStaff = new StaffEntity();
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: Delete Staff ***\n");
                        System.out.print("Enter Staff Username> ");
                        
                        try {
                            String username = scanner.nextLine().trim();
                            toDeleteStaff = staffEntityControllerRemote.retrieveStaffByUsername(username);
                            staffEntityControllerRemote.deleteStaff(toDeleteStaff.getStaffId());
                            System.out.println("Staff has been deleted successfully!\n");
                        } catch (StaffNotFoundException ex) {
                            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        }   break;

                    case 5:
                        System.out.println("\n");
                        System.out.println("*** ILS :: Administration Operation :: View All Staff ***\n");
                        List<StaffEntity> listOfStaff = staffEntityControllerRemote.retrieveAllStaffs();
                        
                        System.out.printf("%-12s%-20s%-20s%-20s\n", "Staff ID", "First Name", "Last Name", "Username");
                        
                        if (!listOfStaff.isEmpty()) {
                            for( StaffEntity staff: listOfStaff ) {
                                System.out.printf("%-12s%-20s%-20s%-20s\n", staff.getStaffId().toString(), staff.getFirstName(), staff.getLastName(), staff.getUsername());
                            }   break;
                        } else {
                            System.out.println("No staff found in database.\n");
                        }
           
                    case 6:
                        return;
                        
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            } 
        }
    }
}