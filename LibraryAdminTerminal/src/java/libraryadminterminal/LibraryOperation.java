
package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.PaymentEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import entity.BookEntity;
import entity.FineEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.List;
import util.exception.BookNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;


public class LibraryOperation {
    private FineEntityControllerRemote fineEntityControllerRemote;
    private ReservationEntityControllerRemote reservationEntityControllerRemote;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private PaymentEntityControllerRemote paymentEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    
    public LibraryOperation() {
    }

    public LibraryOperation(FineEntityControllerRemote fineEntityControllerRemote, ReservationEntityControllerRemote reservationEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, PaymentEntityControllerRemote paymentEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, LendingEntityControllerRemote lendingEntityControllerRemote) {
        this.fineEntityControllerRemote = fineEntityControllerRemote;
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.paymentEntityControllerRemote = paymentEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
    }

   
    public void menuLibrary() throws LendingNotFoundException, BookNotFoundException 
    {
       
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** ILS System :: Library Operation ***\n");
            System.out.println("1: Lend Book");
            System.out.println("2: View Lent Books");
            System.out.println("3: Return Book");
            System.out.println("4: Extend Book");
            System.out.println("5: Pay Fines");
            System.out.println("6: Manage Reservations");
            System.out.println("7: Back\n");
            response = 0;
            
             while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    lendBook();
                }
                else if(response == 2)
                {
                    viewLentBooks();
                }
                else if(response == 3)
                {
                    returnBook();
                }
                else if(response == 4)
                {
                    //extendBook();
                }
                else if(response == 5)
                {
                    //payFines();
                }
                else if(response == 6)
                {
                    //manageReservations();
                }
                else if (response == 7)
                {
                    return;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
             
            if(response == 7)
            {
                return;
            }
        }
    }
    
    
    private void lendBook() 
    {
        Scanner scanner = new Scanner(System.in);
        LendingEntity newLendingEntity = new LendingEntity();
        MemberEntity thisMember = new MemberEntity();
        BookEntity thisBook = new BookEntity();
        
        
        System.out.println("*** ILS :: Library Operation :: Lend Book ***\n");   
        System.out.println("Enter Member Identity Number> ");
        try{
           thisMember = memberEntityControllerRemote.retrieveMemberByIc(scanner.nextLine().trim());
        }
        catch(MemberNotFoundException ex)
        {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
            return;
        }
     
        System.out.println("Enter Book ID: ");
       
        try{
            
            thisBook = bookEntityControllerRemote.retrieveBookByBookId(scanner.nextLong());
            
            if(thisBook.getAvailable() > 0 && thisMember.getBookBorrowed() < 3 ){
                newLendingEntity.setBooks(thisBook);
                newLendingEntity.setMember(thisMember);        
            
                Calendar cal = Calendar.getInstance();
                Date today = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, 14);
                Date due = cal.getTime();
                
                newLendingEntity.setLendDate(today);
                newLendingEntity.setDueDate(due);
                newLendingEntity.setFine(null);
                
                newLendingEntity = lendingEntityControllerRemote.createNewLending(newLendingEntity);
                thisBook.setAvailable(thisBook.getAvailable()-1);
                thisMember.setBookBorrowed(thisMember.getBookBorrowed()+1);
                
                System.out.println("Successfully lent book to member. Due Date: " + due +".\n");

            } else {
                System.out.println("Please check book borrowing limit or book availability.");
            }
        }
          catch(BookNotFoundException ex)
        {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return;  
        }
    }
    
    private void viewLentBooks() 
    {
        Scanner scanner = new Scanner(System.in);
        MemberEntity thisMember = new MemberEntity();
        List<LendingEntity> currentLendings = new ArrayList<LendingEntity>();
        
        System.out.println("*** ILS :: Library Operation :: View Lent Books ***\n");
        System.out.println("Enter Member Identity Number> ");
        try {
            String ic = scanner.nextLine().trim();
            thisMember = memberEntityControllerRemote.retrieveMemberByIc(ic);
            currentLendings = lendingEntityControllerRemote.retrieveCurrentLendings(ic);
            
            System.out.print("Currently Lent Books: ");
            for( LendingEntity l: currentLendings )
            {
                System.out.println(l);
            }
        } 
        catch (MemberNotFoundException ex)
        {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
    }
    
    private void returnBook() throws LendingNotFoundException, BookNotFoundException 
    {
        Scanner scanner = new Scanner(System.in);
        List<LendingEntity> currentLendings = new ArrayList<LendingEntity>();
        MemberEntity thisMember = new MemberEntity();
        
        System.out.println("Enter Member Identity Number> ");
        try {
            String ic = scanner.nextLine().trim();
            thisMember = memberEntityControllerRemote.retrieveMemberByIc(ic);
            currentLendings = lendingEntityControllerRemote.retrieveCurrentLendings(ic);
            
            System.out.print("Currently Lent Books: ");
            for( LendingEntity l: currentLendings )
            {
                System.out.println(l);
            }
        } 
        catch (MemberNotFoundException ex)
        {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
        
        System.out.println("Enter Book to Return> ");
        Long bookId = scanner.nextLong();
        BookEntity thisBook = new BookEntity();
        LendingEntity thisLending = new LendingEntity();
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        
        FineEntity newFineEntity = new FineEntity();
        for( LendingEntity l: currentLendings )
            {
                if(l.getBook().getBookId()== bookId){
                    thisMember.setBookBorrowed(thisMember.getBookBorrowed()-1);
                    thisBook = bookEntityControllerRemote.retrieveBookByBookId(bookId);
                    thisBook.setAvailable(thisBook.getAvailable()+1);
                    thisLending = lendingEntityControllerRemote.retrieveLendingByLendingId(l.getLendId());
                    thisLending.setReturnDate(today);
                    
            ///difference for fines
                    System.out.println("Book successfully returned.");
                  
                }else{
                    System.out.println("Book not found");
                }
        
            }   
        
    
    }
}
