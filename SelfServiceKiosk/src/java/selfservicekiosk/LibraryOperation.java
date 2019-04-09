package selfservicekiosk;

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
import entity.ReservationEntity;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.TimeUnit;
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.ReservationNotFoundException;


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

    public void borrowBook(MemberEntity currentMemberEntity) {
        Scanner scanner = new Scanner(System.in);
        LendingEntity newLendingEntity = new LendingEntity();
        MemberEntity thisMember = currentMemberEntity;
        BookEntity thisBook = new BookEntity();
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");   
        
        /*** Restrict Borrowing (Quit Method): Outstanding Fines. ***/
        if (!(fineEntityControllerRemote.retrieveBorrowerFines(thisMember.getIdentityNumber())).isEmpty()) {
            System.out.println("You are restricted from borrowing due to outstanding fines.\n");
            return;
        }
        
        System.out.print("Enter Book ID> ");
        Long bookId = scanner.nextLong();
        
        try {
            thisBook = bookEntityControllerRemote.retrieveBookByBookId(bookId);
            
            /*** Case: Book has Reservations & Book is Available After Previous Borrower Return. ***/
            if (!(reservationEntityControllerRemote.retrieveBookReservations(bookId)).isEmpty() && thisBook.getAvailable() == 1) {
                List<ReservationEntity> listOfReservations = reservationEntityControllerRemote.retrieveBookReservations(bookId);
                ReservationEntity firstReservation = new ReservationEntity();
                
                if (!listOfReservations.isEmpty() && listOfReservations.size() > 0) {
                    firstReservation = listOfReservations.get(0);
                }
                
                /*** Reject Borrowing (Quit Method): First in Reservation Line for Book is not Current Member. ***/
                if  (!(firstReservation.getMember().getIdentityNumber()).equals(thisMember.getIdentityNumber())) {
                    System.out.println("Book has already been reserved by a member.\n");
                    return; 
                }
                
                /*** Accept Borrowing: First in Reservation Line for Book is Current Member. ***/
                if  (firstReservation.getMember().getIdentityNumber().equals(thisMember.getIdentityNumber())) {
                    /*** Delete Reservation and Continue Flow. ***/
                    try {
                    reservationEntityControllerRemote.deleteReservation(firstReservation.getReservationId());
                    } catch (ReservationNotFoundException ex) {
                        System.out.println("An error has occurred: " + ex.getMessage() + "\n");
                        return;    
                    }
                }  
            }
            /*** Case: Book has No Reservations & Book is Not Available. ***/
            else if (reservationEntityControllerRemote.retrieveBookReservations(bookId).isEmpty() && thisBook.getAvailable() == 0) {
                System.out.println("Book is not available for lending.\n");
                return;
            } /*** Case: Book has Reservations & Book is Not Available. ***/
            else if (!(reservationEntityControllerRemote.retrieveBookReservations(bookId).isEmpty()) && thisBook.getAvailable() == 0) {
                System.out.println("Book has already been reserved by a member.\n");
                return;
            }
            
            /*** Reject Borrowing (Quit Method): Book Unavailable. ***/
            if (thisBook.getAvailable() < 1) {
                System.out.println("Book is not available for lending.\n");
                return;
            }
            
            if (thisMember.getBookBorrowed() < 3){    
                
                newLendingEntity.setBook(thisBook);
                newLendingEntity.setMember(thisMember);

                Calendar cal = Calendar.getInstance();
                Date today = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, 14);
                Date due = cal.getTime();

                newLendingEntity.setLendDate(today);
                newLendingEntity.setDueDate(due);
                newLendingEntity.setFine(null);

                lendingEntityControllerRemote.createNewLending(newLendingEntity);

                thisBook.setAvailable(thisBook.getAvailable()-1);
                thisMember.setBookBorrowed(thisMember.getBookBorrowed()+1);
                bookEntityControllerRemote.updateBook(thisBook);
                memberEntityControllerRemote.updateMember(thisMember);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("Successfully lent book. Due Date: " + sdf.format(due) +".\n");  
            } else {
                /*** Reject Lend (Quit Method): Exceed Borrow Count. ***/
                System.out.println("You have exceeded your borrowing limit.\n");
            }
        } catch (BookNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
            return;
        }
    }
    
    public void viewLentBooks(MemberEntity currentMemberEntity) 
    {
        Scanner scanner = new Scanner(System.in);
        MemberEntity thisMember = currentMemberEntity;
        List<LendingEntity> listOfCurrentLendings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");
        
        try {
            System.out.println("Currently Lent Books: ");
            
            listOfCurrentLendings = lendingEntityControllerRemote.retrieveCurrentLendings(thisMember.getIdentityNumber());
            System.out.printf("%-12s%-50s%-25s\n", "Id", "Title", "Due Date");
            if (!listOfCurrentLendings.isEmpty()) {
                for (LendingEntity lendings : listOfCurrentLendings) {
                System.out.printf("%-12s%-50s%-25s\n", lendings.getBook().getBookId().toString(), lendings.getBook().getTitle(), sdf.format(lendings.getDueDate()));
                }
            } else {
                System.out.println("You did not borrow any books.");
                return;
            }
        } catch (MemberNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
    }
    
    public void returnBook(MemberEntity currentMemberEntity) {
        Scanner scanner = new Scanner(System.in);
        MemberEntity thisMember = currentMemberEntity;
        List<LendingEntity> listOfCurrentLendings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Return Book ***\n");
        
        try {    
            System.out.println("Currently Lent Books: ");
            listOfCurrentLendings = lendingEntityControllerRemote.retrieveCurrentLendings(thisMember.getIdentityNumber());
            
            System.out.printf("%-12s%-50s%-25s\n", "Id", "Title", "Due Date");
            if (!listOfCurrentLendings.isEmpty()) {
                for (LendingEntity lendings : listOfCurrentLendings) {
                System.out.printf("%-12s%-50s%-25s\n", lendings.getBook().getBookId().toString(), lendings.getBook().getTitle(), sdf.format(lendings.getDueDate()));
                }
            } else {
                System.out.println("You did not borrow any books. \n");
                return;
            }
        } catch (MemberNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
        
        BookEntity thisBook = new BookEntity();
        LendingEntity thisLending = new LendingEntity();
        
        System.out.println("\n");
        System.out.print("Enter Book to Return> ");
        Long bookId = scanner.nextLong();
        
        try {
            thisBook = bookEntityControllerRemote.retrieveBookByBookId(bookId);   
        } catch (BookNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
             
        Boolean containsBook = false;
        
        for (LendingEntity lending : listOfCurrentLendings) {
            if (!lending.getBook().getBookId().equals(bookId)) {
                containsBook =  false;
            } else {
                thisLending = lending;
                containsBook = true;
                break;
            }
        }
        
        if (!containsBook) {
            System.out.println("You did not borrow this book.");
            return;
        }
        
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        thisLending.setReturnDate(today);
        
        thisBook.setAvailable(thisBook.getAvailable()+1);
        thisMember.setBookBorrowed(thisMember.getBookBorrowed()-1);
        lendingEntityControllerRemote.updateLending(thisLending);
        bookEntityControllerRemote.updateBook(thisBook);
        memberEntityControllerRemote.updateMember(thisMember);
        
        System.out.println("Book successfully returned. \n");

        if (today.compareTo(thisLending.getDueDate()) < 0) {
            return;
        }
        
        /*** Create Fine: If Today > Due Date. ***/
        FineEntity newFineEntity = new FineEntity();
        
        TimeUnit timeUnit = TimeUnit.DAYS;
        long millis = today.getTime() - (thisLending.getDueDate()).getTime();
        long days = timeUnit.convert(millis,TimeUnit.MILLISECONDS);
        
        newFineEntity.setAmount(BigDecimal.valueOf(days));
        newFineEntity.setLending(thisLending);
        fineEntityControllerRemote.createNewFine(newFineEntity);
    }
    
    public void extendBook(MemberEntity currentMemberEntity) {
    
        Scanner scanner = new Scanner(System.in);
        MemberEntity thisMember = currentMemberEntity;
        List<LendingEntity> listOfCurrentLendings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");

        try {
            System.out.println("Currently Lent Books: ");
            
            listOfCurrentLendings = lendingEntityControllerRemote.retrieveCurrentLendings(thisMember.getIdentityNumber());
            System.out.printf("%-12s%-50s%-25s\n", "Id", "Title", "Due Date");
            if (!listOfCurrentLendings.isEmpty()) {
                for (LendingEntity lendings : listOfCurrentLendings) {
                    System.out.printf("%-12s%-50s%-25s\n", lendings.getBook().getBookId().toString(), lendings.getBook().getTitle(), sdf.format(lendings.getDueDate()));
                }
            } else {
                System.out.println("You did not borrow any books. \n");
                return;
            }
        } catch (MemberNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
        
        BookEntity thisBook = new BookEntity();
        LendingEntity thisLending = new LendingEntity();
        
        System.out.println("\n");
        System.out.print("Enter Book to Extend> ");
        Long bookId = scanner.nextLong();
        
        try {
            thisBook = bookEntityControllerRemote.retrieveBookByBookId(bookId);   
        } catch (BookNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
             
        Boolean containsBook = false;
        
        for (LendingEntity lending : listOfCurrentLendings) {
            if (!lending.getBook().getBookId().equals(bookId)) {
                containsBook =  false;
            } else {
                thisLending = lending;
                containsBook = true;
                break;
            }
        }
        
        if (!containsBook) {
            System.out.println("You did not borrow this book.");
            return;
        }
        
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        
        /*** Reject Extend (Quit Method): Book is already overdue. ***/
        if (thisLending.getDueDate().before(today)){
            System.out.println("Unable to extend due to overdue loan.");
            return;
        }   
        
        /*** Reject Extend (Quit Method): Member has outstanding fines. ***/
        if (!(fineEntityControllerRemote.retrieveBorrowerFines(thisMember.getIdentityNumber())).isEmpty())
        { 
            System.out.println("Unable to extend due to outstanding fines.");
            return;
        }
        
        /*** Reject Extend (Quit Method): Book is already reserved by a member. ***/
        if (!(reservationEntityControllerRemote.retrieveBookReservations(bookId)).isEmpty()) 
        {
            System.out.println("Unable to extend as book has already been reserved by a member.\n");
            return;
        } 
        
        Long due = thisLending.getDueDate().getTime();
        due+= 1209600000; 
        Date newDueDate = new Date(due);
        thisLending.setDueDate(newDueDate);
        
        lendingEntityControllerRemote.updateLending(thisLending);

        System.out.println("Book successfully extended. New due date: " + sdf.format(newDueDate));
    }
    
    public void payFines(MemberEntity currentMemberEntity) {
        Scanner scanner = new Scanner(System.in);
        List<FineEntity> outstandingFines = new ArrayList<>();
        MemberEntity thisMember = currentMemberEntity;
 
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Pay Fines ***\n");
        
        try {    
            System.out.println("Unpaid Fines for Member: ");
            
            outstandingFines = fineEntityControllerRemote.retrieveOutstandingFines(thisMember.getIdentityNumber());
            System.out.printf("%-8s%-20s\n", "Id", "Amount");
            
            if (!outstandingFines.isEmpty()) {  
                for (FineEntity fines : outstandingFines) {
                System.out.printf("%-8s%-20s\n", fines.getFineId(), NumberFormat.getCurrencyInstance().format(fines.getAmount()));
                }
            } else {
                System.out.println("You do not have any outstanding fines. \n");
                return;
            }
        } catch (FineNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
        
        System.out.print("\n");
        System.out.print("Enter Fine to Settle> ");
        Long fineId = scanner.nextLong();
        scanner.nextLine();
        FineEntity thisFine = new FineEntity();
        
        try {
            thisFine = fineEntityControllerRemote.retrieveFineByFineId(fineId);
        } catch(FineNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
            return; 
        }
        
       Boolean containsFine = false;
        
        for (FineEntity fine : outstandingFines) {
            if (!fine.getFineId().equals(fineId)) {
                containsFine =  false;
            } else {
                thisFine = fine;
                containsFine = true;
                break;
            }
        }
        
        if (!containsFine) {
            System.out.println("Fine does not exist.");
            return;
        }
        
       try {
            System.out.print("Enter Name of Card> ");
            String name = scanner.nextLine();
            System.out.print("Enter Card Number> ");
            Long card = scanner.nextLong();
            System.out.print("Enter Card Expiry (MMYYYY)> ");
            Integer date = scanner.nextInt();
            System.out.print("Enter PIN> ");
            Integer pin = scanner.nextInt();
       } catch (InputMismatchException ex) {
            System.out.println("Please ensure all field input types are correct. Please try again.\n");
       }
       
        try {
            fineEntityControllerRemote.deleteFine(fineId);
            System.out.println("Fine successfully paid.");
        } catch (FineNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
            return;
        }
    }
    
    public void searchBook(MemberEntity currentMemberEntity) 
    {
        Scanner scanner = new Scanner(System.in);
        List<BookEntity> allBooks = new ArrayList<>();
        MemberEntity thisMember = currentMemberEntity;
        List<LendingEntity> thisBookLendings = new ArrayList<>();
        List<ReservationEntity> thisBookReservations = new ArrayList<>(); 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Search Book ***\n");
        System.out.print("Enter Title to Search> ");
        String title = scanner.nextLine();
        
        try {
            allBooks = bookEntityControllerRemote.retrieveBookByTitle(title);
            System.out.printf("%-8s%-50s%-20s\n", "Book Id", "Title", "Availability");
            String status = "";
            
            if (!(allBooks.isEmpty())) {   
                for (BookEntity book : allBooks) {
                    thisBookReservations = reservationEntityControllerRemote.retrieveBookReservations(book.getBookId());
                    thisBookLendings = lendingEntityControllerRemote.retrieveCurrentLendingsByBookId(book.getBookId());
                    
                    if (book.getAvailable() == 1 && thisBookReservations.isEmpty()) {
                        status = "Currently Available";
                    } else if (!(thisBookReservations.isEmpty())) {
                        status = "Reserved";    
                    } else if (!(thisBookLendings.isEmpty()) && thisBookReservations.isEmpty()) {
                        status = "Due On " + sdf.format(thisBookLendings.get(thisBookLendings.size()-1).getDueDate());
                    }
                System.out.printf("%-8s%-50s%-20s\n", book.getBookId(), book.getTitle(), status);
                }
            } else {
                System.out.println("There are no books available. \n");
                return;
            }
        } catch (BookNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
    }
    
    public void reserveBook(MemberEntity currentMemberEntity) {
        Scanner scanner = new Scanner(System.in);
        List<BookEntity> allBooks = new ArrayList<>();
        MemberEntity thisMember = currentMemberEntity;
        List<LendingEntity> thisBookLendings = new ArrayList<>();
        List<ReservationEntity> thisBookReservations = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Search Book ***\n");
        System.out.print("Enter Title to Search> ");
        String title = scanner.nextLine();
        System.out.println("\n");
        
        try {
            allBooks = bookEntityControllerRemote.retrieveBookByTitle(title);
            System.out.printf("%-8s%-50s%-20s\n", "Book Id", "Title", "Availability");
            String status = "";
            
            if (!allBooks.isEmpty()) {
                for (BookEntity book : allBooks) {
                    thisBookReservations = reservationEntityControllerRemote.retrieveBookReservations(book.getBookId());
                    thisBookLendings = lendingEntityControllerRemote.retrieveCurrentLendingsByBookId(book.getBookId());
                    
                    if (book.getAvailable() == 1 && thisBookReservations.isEmpty()) {
                        status = "Currently Available";   
                    } else if (!(thisBookReservations.isEmpty())) {
                        status = "Reserved";
                    } else if (!(thisBookLendings.isEmpty()) && thisBookReservations.isEmpty()) {
                        status = "Due On " + sdf.format(thisBookLendings.get(thisBookLendings.size()-1).getDueDate());
                    }
                System.out.printf("%-8s%-50s%-20s\n", book.getBookId(), book.getTitle(), status);
                }
            } else {
                System.out.println("There are no books available. \n");
                return;
            }
        } catch (BookNotFoundException ex) {
           System.out.println("An error has occurred: " + ex.getMessage() + "\n");
           return; 
        }
    
        System.out.println("\n");
        System.out.print("Enter Book ID to Reserve> ");
        
        BookEntity thisBook = new BookEntity();
        ReservationEntity newReservationEntity = new ReservationEntity();
        Long bookId = scanner.nextLong();     
        
        try {
            thisBook = bookEntityControllerRemote.retrieveBookByBookId(bookId);
        } catch(BookNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage() + "\n");
            return; 
        }
        
       Boolean containsBook = false;
       
       for (BookEntity book : allBooks) {
            if (!book.getBookId().equals(bookId)) {
                containsBook =  false;
            } else {
                thisBook = book;
                containsBook = true;
                break;
            }
        }
        
        if (!containsBook) {
            System.out.println("Book does not exist.");
            return;
        }
       

        if (!(fineEntityControllerRemote.retrieveBorrowerFines(thisMember.getIdentityNumber())).isEmpty()) { 
            System.out.println("Unable to reserve due to outstanding fines.");
            return;
        }
        
        if (thisBook.getAvailable() == 1) {
            System.out.println("Book is available and cannot be reserved.");
            return;
        }
        
        if (!thisBookLendings.isEmpty()){
            if (thisBookLendings.get(0).getMember().getIdentityNumber().equals(thisMember.getIdentityNumber())) {
            System.out.println("You have borrowed this book.");
            return;
            }
        }
       
        Integer latestQueueNumber = 0;
        for (ReservationEntity reservation : thisBookReservations) {
            if (reservation.getQueueNumber() > latestQueueNumber) {
                latestQueueNumber = reservation.getQueueNumber();
            }
            
            if (reservation.getMember().getIdentityNumber().equals(thisMember.getIdentityNumber())) {
                System.out.println("You have already reserved this book.\n");
                return;
            }
        }
        newReservationEntity.setBook(thisBook);
        newReservationEntity.setMember(thisMember);
        newReservationEntity.setFulfilled(Boolean.FALSE);
        newReservationEntity.setQueueNumber(latestQueueNumber+1);
        reservationEntityControllerRemote.createNewReservation(newReservationEntity);
        System.out.println("Book successfully reserved.");
    } 
}