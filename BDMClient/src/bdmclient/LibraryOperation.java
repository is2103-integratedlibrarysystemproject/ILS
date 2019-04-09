package bdmclient;

import ws.client.MemberEntity;
import ws.client.SOAPWebServices;
import ws.client.SOAPWebServices_Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.BookEntity;
import ws.client.BookNotFoundException_Exception;
import ws.client.FineEntity;
import ws.client.FineNotFoundException_Exception;
import ws.client.LendingEntity;
import ws.client.MemberNotFoundException_Exception;
import ws.client.ReservationEntity;


public class LibraryOperation {

    private SOAPWebServices port;
    private SOAPWebServices_Service service;
    private MemberEntity currentMemberEntity;
    
    public LibraryOperation() {
    }

    public LibraryOperation(SOAPWebServices port, SOAPWebServices_Service service, MemberEntity currentMemberEntity) {
        this.port = port;
        this.service = service;
        this.currentMemberEntity = currentMemberEntity;
    }
    
    public void viewLentBooks() throws MemberNotFoundException_Exception, BookNotFoundException_Exception, DatatypeConfigurationException {
        Scanner scanner = new Scanner(System.in);
        List<LendingEntity> listOfCurrentLendings = new ArrayList<>();
        BookEntity thisBook = new BookEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** BDM Client :: View Lent Books ***\n");
        
        System.out.println("Currently Lent Books: ");
        listOfCurrentLendings = port.retrieveCurrentLendings(currentMemberEntity.getIdentityNumber());
        
        System.out.printf("%-12s%-50s%-25s\n", "Id", "Title", "Due Date");
        if (!listOfCurrentLendings.isEmpty()) {
            for (LendingEntity lendings : listOfCurrentLendings) {
                XMLGregorianCalendar dueDate = lendings.getDueDate();
                Date date = dueDate.toGregorianCalendar().getTime();
                System.out.printf("%-12s%-50s%-25s\n", (lendings.getBook().getBookId()).toString(), lendings.getBook().getTitle(), sdf.format(date));
            }
        } else { 
            System.out.println("You did not borrow any books.");
            return;
        }
    }
    
    public void returnBook() throws MemberNotFoundException_Exception, BookNotFoundException_Exception, DatatypeConfigurationException {
        Scanner scanner = new Scanner(System.in);
        List<LendingEntity> listOfCurrentLendings = new ArrayList<>();
        BookEntity thisBook = new BookEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** Self-Service Kiosk :: Return Book ***\n");
        
        System.out.println("Currently Lent Books: ");
        listOfCurrentLendings = port.retrieveCurrentLendings(currentMemberEntity.getIdentityNumber());
        
        System.out.printf("%-12s%-50s%-25s\n", "Id", "Title", "Due Date");
        if (!listOfCurrentLendings.isEmpty()) {
            for (LendingEntity lendings : listOfCurrentLendings) {
                XMLGregorianCalendar dueDate = lendings.getDueDate();
                Date date = dueDate.toGregorianCalendar().getTime();
                System.out.printf("%-12s%-50s%-25s\n", (lendings.getBook().getBookId()).toString(), lendings.getBook().getTitle(), sdf.format(date));
            }
        } else { 
            System.out.println("You did not borrow any books. \n");
            return;
        }
        
        LendingEntity thisLending = new LendingEntity();
        
        System.out.println("\n");
        System.out.print("Enter Book to Return> ");
        Long bookId = scanner.nextLong();
        
        thisBook = port.retrieveBookByBookId(bookId);
        
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
        
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        XMLGregorianCalendar returnDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        
        thisLending.setReturnDate(returnDate);
        thisBook.setAvailable(thisBook.getAvailable()+1);
        currentMemberEntity.setBookBorrowed(currentMemberEntity.getBookBorrowed()-1);
        
        port.updateLending(thisLending);
        port.updateBook(thisBook);
        port.updateMember(currentMemberEntity);
        System.out.println("Book successfully returned. \n");   
        
        XMLGregorianCalendar dueDate = thisLending.getDueDate();
        
        if (returnDate.compare(dueDate) <0) {
            return;
        }
        
        /*** Create Fine: If Today > Due Date. ***/
        FineEntity newFineEntity = new FineEntity();
        
        Integer daysLate = returnDate.compare(dueDate);
        
        newFineEntity.setAmount(BigDecimal.valueOf(daysLate));
        newFineEntity.setLending(thisLending);
        port.createNewFine(newFineEntity);
    }
    
     public void extendBook() throws MemberNotFoundException_Exception, BookNotFoundException_Exception, DatatypeConfigurationException {
        Scanner scanner = new Scanner(System.in);
        List<LendingEntity> listOfCurrentLendings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** BDM Client :: Extend Book ***\n");

        System.out.println("Currently Lent Books: ");
        listOfCurrentLendings = port.retrieveCurrentLendings(currentMemberEntity.getIdentityNumber());
        
        System.out.printf("%-12s%-50s%-25s\n", "Id", "Title", "Due Date");
        if (!listOfCurrentLendings.isEmpty()) {
            for (LendingEntity lendings : listOfCurrentLendings) {
                XMLGregorianCalendar dueDate = lendings.getDueDate();
                Date date = dueDate.toGregorianCalendar().getTime();
                System.out.printf("%-12s%-50s%-25s\n", (lendings.getBook().getBookId()).toString(), lendings.getBook().getTitle(), sdf.format(date));
            }
        } else { 
            System.out.println("You did not borrow any books. \n");
            return;
        }
        
        BookEntity thisBook = new BookEntity();
        LendingEntity thisLending = new LendingEntity();
        
        System.out.println("\n");
        System.out.print("Enter Book to Extend> ");
        Long bookId = scanner.nextLong();
        
        thisBook = port.retrieveBookByBookId(bookId);
             
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
        
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        XMLGregorianCalendar returnDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar); 
        
        /*** Reject Extend (Quit Method): Book is already overdue. ***/
        if (thisLending.getDueDate().compare(returnDate) < 0) {
            System.out.println("Unable to extend due to overdue loan.");
            return;
        }   
        
        /*** Reject Extend (Quit Method): Member has outstanding fines. ***/
        if (!(port.retrieveBorrowerFines(currentMemberEntity.getIdentityNumber())).isEmpty()) { 
            System.out.println("Unable to extend due to outstanding fines.");
            return;
        }
        
        /*** Reject Extend (Quit Method): Book is already reserved by a member. ***/
        if (!(port.retrieveBookReservations(bookId)).isEmpty()) {
            System.out.println("Unable to extend as book has already been reserved by a member.\n");
            return;
        }
        
        GregorianCalendar dueDate = thisLending.getDueDate().toGregorianCalendar();
        dueDate.add((GregorianCalendar.DATE),14);
        XMLGregorianCalendar extendedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(dueDate);
        
        thisLending.setDueDate(extendedDate);
        
        port.updateLending(thisLending);

        Date newDueDate = extendedDate.toGregorianCalendar().getTime();
        System.out.println("Book successfully extended. New due date: " + sdf.format(newDueDate));
    }
    
    public void payFines() throws FineNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        List<FineEntity> outstandingFines = new ArrayList<>();
 
        System.out.println("\n");
        System.out.println("*** BDM Client :: Pay Fines ***\n");
        
        System.out.println("Unpaid Fines for Member: ");
        outstandingFines = port.retrieveOutstandingFines(currentMemberEntity.getIdentityNumber());
        
        System.out.printf("%-8s%-20s\n", "Id", "Amount");
        if (!outstandingFines.isEmpty()) {
            for (FineEntity fines : outstandingFines) {
                System.out.printf("%-8s%-20s\n", fines.getFineId(), NumberFormat.getCurrencyInstance().format(fines.getAmount()));
            }
        } else {
            System.out.println("You do not have any outstanding fines. \n"); 
            return;
        }
        
        System.out.print("\n");
        System.out.print("Enter Fine to Settle> ");
        Long fineId = scanner.nextLong();
        scanner.nextLine();
        
        FineEntity thisFine = new FineEntity();
        thisFine = port.retrieveFineByFineId(fineId);
        
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
       port.deleteFine(fineId);
       System.out.println("Fine successfully paid.");
    }
    
    public void reserveBook() throws BookNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        List<BookEntity> allBooks = new ArrayList<>();
        List<LendingEntity> thisBookLendings = new ArrayList<>();
        List<ReservationEntity> thisBookReservations = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("\n");
        System.out.println("*** BDM Client :: Search Book ***\n");
        
        System.out.print("Enter Title to Search> ");
        String title = scanner.nextLine();
        System.out.println("\n");
        
        allBooks = port.retrieveBookByTitle(title);
        System.out.printf("%-8s%-50s%-20s\n", "Book Id", "Title", "Availability");    
        String status = "";
        
        if (!allBooks.isEmpty()) {
            for (BookEntity book : allBooks) {
                thisBookReservations = port.retrieveBookReservations(book.getBookId());
                thisBookLendings = port.retrieveCurrentLendingsByBookId(book.getBookId());
                
                if (book.getAvailable() == 1 && thisBookReservations.isEmpty()) {
                    status = "Currently Available";    
                } else if (!(thisBookReservations.isEmpty())) {
                    status = "Reserved";    
                } else if (!(thisBookLendings.isEmpty()) && thisBookReservations.isEmpty()) {
                    XMLGregorianCalendar dueDate = thisBookLendings.get(thisBookLendings.size()-1).getDueDate();
                    Date date = dueDate.toGregorianCalendar().getTime();
                    status = "Due On " + sdf.format(date);
                }
                System.out.printf("%-8s%-50s%-20s\n", book.getBookId(), book.getTitle(), status);
            }
        } else {
            System.out.println("There are no books available. \n"); 
            return;
        }
    
        System.out.println("\n");
        System.out.print("Enter Book ID to Reserve> ");
        
        BookEntity thisBook = new BookEntity();
        ReservationEntity newReservationEntity = new ReservationEntity();
        Long bookId = scanner.nextLong();     
        
        thisBook = port.retrieveBookByBookId(bookId);
        
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
       
        if (!(port.retrieveBorrowerFines(currentMemberEntity.getIdentityNumber())).isEmpty()) { 
            System.out.println("Unable to reserve due to outstanding fines.");
            return;
        }
        
        if (thisBook.getAvailable() == 1) {
            System.out.println("Book is available and cannot be reserved.");
            return;
        }
        
        if (!thisBookLendings.isEmpty()) {
            if (thisBookLendings.get(0).getMember().getIdentityNumber().equals(currentMemberEntity.getIdentityNumber())) {
            System.out.println("You have borrowed this book.");
            return;
            }
        }
       
        Integer latestQueueNumber = 0;
        for (ReservationEntity reservation : thisBookReservations) {
            if (reservation.getQueueNumber() > latestQueueNumber) {
                latestQueueNumber = reservation.getQueueNumber();
            }
            
            if (reservation.getMember().getIdentityNumber().equals(currentMemberEntity.getIdentityNumber())) {
                System.out.println("You have already reserved this book.\n");
                return;
            }
        }
        newReservationEntity.setBook(thisBook);
        newReservationEntity.setMember(currentMemberEntity);
        newReservationEntity.setFulfilled(Boolean.FALSE);
        newReservationEntity.setQueueNumber(latestQueueNumber+1);
        port.createNewReservation(newReservationEntity);
        System.out.println("Book successfully reserved.");
    }
}
