package ejb.session.ws;

import ejb.session.stateless.BookEntityControllerLocal;
import ejb.session.stateless.FineEntityControllerLocal;
import ejb.session.stateless.LendingEntityControllerLocal;
import ejb.session.stateless.MemberEntityControllerLocal;
import ejb.session.stateless.ReservationEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.BookEntity;
import entity.FineEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.StaffNotFoundException;


@WebService(serviceName = "SOAPWebServices")
@Stateless()
public class SOAPWebServices {

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private ReservationEntityControllerLocal reservationEntityControllerLocal;
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;
    @EJB
    private LendingEntityControllerLocal lendingEntityControllerLocal;
    @EJB
    private FineEntityControllerLocal fineEntityControllerLocal;
    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;

    
    /*** Web Methods for BookEntityController. ***/
    
    @WebMethod(operationName = "createNewBook")
     public void createNewBook(@WebParam(name = "newBookEntity") BookEntity newBookEntity) 
    {
        bookEntityControllerLocal.createNewBook(newBookEntity);
    }
    
    @WebMethod(operationName = "retrieveAllBooks")
    public List<BookEntity> retrieveAllBooks()      
    {
        return bookEntityControllerLocal.retrieveAllBooks();
    }
    
    @WebMethod(operationName = "retrieveBookByBookId")
    public BookEntity retrieveBookByBookId(@WebParam(name = "bookId") Long bookId) throws BookNotFoundException      
    {
        return bookEntityControllerLocal.retrieveBookByBookId(bookId);
    }
    
    @WebMethod(operationName = "retrieveBookByIsbn")
    public BookEntity retrieveBookByBookIsbn(@WebParam(name = "isbn") String isbn) throws BookNotFoundException      
    {
        return bookEntityControllerLocal.retrieveBookByIsbn(isbn);
    }
    
    @WebMethod(operationName = "retrieveBookByTitle")
    public List<BookEntity> retrieveBookByTitle(@WebParam(name = "title") String title) throws BookNotFoundException
    {
        return bookEntityControllerLocal.retrieveBookByTitle(title);
    }
    
    @WebMethod(operationName = "updateBook")
    public void updateBook(@WebParam(name = "bookEntity") BookEntity bookEntity) 
    {
        bookEntityControllerLocal.updateBook(bookEntity);
    }
    
    
    @WebMethod(operationName = "deleteBook")
    public void deleteBook(@WebParam(name = "bookId") Long bookId) throws BookNotFoundException
    {
        bookEntityControllerLocal.deleteBook(bookId);
    }
    
    
    
    /*** Web Methods for FineEntityController. ***/
    
    
    
    @WebMethod(operationName = "createNewFine")
     public void createNewFine(@WebParam(name = "newFineEntity") FineEntity newFineEntity) 
    {
        fineEntityControllerLocal.createNewFine(newFineEntity);
    }
    
    @WebMethod(operationName = "retrieveAllFines")
    public List<FineEntity> retrieveAllFines()      
    {
        return fineEntityControllerLocal.retrieveAllFines();
    }
    
    @WebMethod(operationName = "retrieveBorrowerFines")
    public List<FineEntity> retrieveBorrowerFines(@WebParam(name = "ic") String ic)      
    {
        return fineEntityControllerLocal.retrieveBorrowerFines(ic);
    }
    
    @WebMethod(operationName = "retrieveFineByFineId")
    public FineEntity retrieveFineByFineId(@WebParam(name = "fineId") Long fineId) throws FineNotFoundException      
    {
        return fineEntityControllerLocal.retrieveFineByFineId(fineId);
    }
    
    @WebMethod(operationName = "retrieveOutstandingFines")
    public List<FineEntity> retrieveOutstandingFines(@WebParam(name = "ic") String ic) throws FineNotFoundException
    {
        return fineEntityControllerLocal.retrieveOutstandingFines(ic);
    }
    
    @WebMethod(operationName = "updateFine")
    public void updateFine(@WebParam(name = "fineEntity") FineEntity fineEntity) 
    {
        fineEntityControllerLocal.updateFine(fineEntity);
    }
    
    
    @WebMethod(operationName = "deleteFine")
    public void deleteFine(@WebParam(name = "fineId") Long fineId) throws FineNotFoundException
    {
        fineEntityControllerLocal.deleteFine(fineId);
    }
    
    
    
    /*** Web Methods for LendingEntityController. ***/
    
    
    
    @WebMethod(operationName = "createNewLending")
     public void createNewLending(@WebParam(name = "newLendingEntity") LendingEntity newLendingEntity) 
    {
        lendingEntityControllerLocal.createNewLending(newLendingEntity);
    }
    
    @WebMethod(operationName = "retrieveAllLendings")
    public List<LendingEntity> retrieveAllLendings()      
    {
        return lendingEntityControllerLocal.retrieveAllLendings();
    }
    
    @WebMethod(operationName = "retrieveCurrentLendings") 
    public List<LendingEntity> retrieveCurrentLendings(@WebParam(name = "ic") String ic) throws MemberNotFoundException   
    {
        return lendingEntityControllerLocal.retrieveCurrentLendings(ic);
    }
    
    @WebMethod(operationName = "retrieveCurrentLendingsByBookId")
    public List<LendingEntity> retrieveCurrentLendingsByBookId(@WebParam(name = "bookId") Long bookId) throws BookNotFoundException      
    {
        return lendingEntityControllerLocal.retrieveCurrentLendingsByBookId(bookId);
    }
    
    @WebMethod(operationName = "retrieveLendingByLendingId")
    public LendingEntity retrieveLendingByLendingId(@WebParam(name = "lendingId") Long lendingId) throws LendingNotFoundException      
    {
        return lendingEntityControllerLocal.retrieveLendingByLendingId(lendingId);
    }
    
    @WebMethod(operationName = "updateLending")
    public void updateLending(@WebParam(name = "lendingEntity") LendingEntity lendingEntity) 
    {
        lendingEntityControllerLocal.updateLending(lendingEntity);
    }
    
    @WebMethod(operationName = "deleteLending")
    public void deleteLending(@WebParam(name = "lendingId") Long lendingId) throws LendingNotFoundException
    {
        lendingEntityControllerLocal.deleteLending(lendingId);
    }
    
    

    /*** Web Methods for MemberEntityController. ***/
    

 
    @WebMethod(operationName = "retrieveAllMembers")
    public List<MemberEntity> retrieveAllMembers()      
    {
        return memberEntityControllerLocal.retrieveAllMembers();
    }
   
    @WebMethod(operationName = "createNewMember")
    public void createNewMember(@WebParam(name = "newMember") MemberEntity newMember) {
        memberEntityControllerLocal.createNewMember(newMember);
    }
    
    @WebMethod(operationName = "retrieveMemberByMemberId")
    public MemberEntity retrieveMemberByMemberId(@WebParam(name = "id") Long id) throws MemberNotFoundException      
    {
            return memberEntityControllerLocal.retrieveMemberByMemberId(id);
    }
    
    @WebMethod(operationName = "retrieveMemberByIc")
    public MemberEntity retrieveMemberByIc(@WebParam(name = "identityNumber") String ic) throws MemberNotFoundException      
    {
            return memberEntityControllerLocal.retrieveMemberByIc(ic);
    }

    @WebMethod(operationName = "memberLogin")
    public MemberEntity memberLogin(@WebParam(name = "ic") String ic,
            @WebParam(name = "securityCode") String securityCode) throws InvalidLoginCredentialException {
        MemberEntity currentMember;
        
        try {
            System.out.println(securityCode + "" + ic);
            currentMember = memberEntityControllerLocal.memberLogin(ic, securityCode);

        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");

            throw new InvalidLoginCredentialException();
        }
        return currentMember;
    }
    
      @WebMethod(operationName = "updateMember")
    public void updateMember(@WebParam(name = "memberEntity") MemberEntity memberEntity) 
    {
        memberEntityControllerLocal.updateMember(memberEntity);
    }
    @WebMethod(operationName = "deleteMember")
    public void deleteMember(@WebParam(name = "memberId") Long memberId) throws MemberNotFoundException
    {
        memberEntityControllerLocal.deleteMember(memberId);
    }

    
    
    /*** Web Methods for ReservationEntityController. ***/
    
    


    @WebMethod(operationName = "createNewReservation")
    public void createNewReservation(@WebParam(name = "newReservationEntity") ReservationEntity newReservationEntity) 
    {
        reservationEntityControllerLocal.createNewReservation(newReservationEntity);
    }
    
    @WebMethod(operationName = "retrieveAllReservations")
    public List<ReservationEntity> retrieveAllReservations()      
    {
        return reservationEntityControllerLocal.retrieveAllReservations();
    }
    
    @WebMethod(operationName = "retrieveBookReservations")
    public List<ReservationEntity> retrieveBookReservations(@WebParam(name = "bookId") Long bookId)    
    {
        return reservationEntityControllerLocal.retrieveBookReservations(bookId);
    }
    
    @WebMethod(operationName = "retrieveReservationByReservationId")
    public ReservationEntity retrieveReservationByReservationId(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException      
    {
        return reservationEntityControllerLocal.retrieveReservationByReservationId(reservationId);
    }
    
    @WebMethod(operationName = "updateReservation")
    public void updateReservation(@WebParam(name = "reservationEntity") ReservationEntity reservationEntity) 
    {
        reservationEntityControllerLocal.updateReservation(reservationEntity);
    }
    
    @WebMethod(operationName = "deleteReservation")
    public void deleteReservation(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException
    {
        reservationEntityControllerLocal.deleteReservation(reservationId);
    }
    
    
    
   
    /*** Web Methods for StaffEntityController. ***/

    
    
    @WebMethod(operationName = "createNewStaff")
     public void createNewStaff(@WebParam(name = "newStaffEntity") StaffEntity newStaffEntity) 
    {
        staffEntityControllerLocal.createNewStaff(newStaffEntity);
    }
    
    @WebMethod(operationName = "retrieveAllStaffs")
    public List<StaffEntity> retrieveAllStaffs()      
    {
        return staffEntityControllerLocal.retrieveAllStaffs();
    }
    
    @WebMethod(operationName = "retrieveStaffByStaffId")
    public StaffEntity retrieveStaffByStaffId(@WebParam(name = "staffId") Long staffId) throws StaffNotFoundException      
    {
        return staffEntityControllerLocal.retrieveStaffByStaffId(staffId);
    }
    
    @WebMethod(operationName = "retrieveStaffByUsername")
    public StaffEntity retrieveStaffByUsername(@WebParam(name = "username") String username) throws StaffNotFoundException      
    {
        return staffEntityControllerLocal.retrieveStaffByUsername(username);
    }
    
    @WebMethod(operationName = "staffLogin")
    public StaffEntity staffLogin(@WebParam(name = "username") String username,
        @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        StaffEntity currentStaff;
        
        try {
            System.out.println(password + "" + username);
            currentStaff = staffEntityControllerLocal.staffLogin(username, password);

        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");

            throw new InvalidLoginCredentialException();
        }
        return currentStaff;
    }
    
    @WebMethod(operationName = "updateStaff")
    public void updateStaff(@WebParam(name = "staffEntity") StaffEntity staffEntity) 
    {
        staffEntityControllerLocal.updateStaff(staffEntity);
    }
    
    @WebMethod(operationName = "deleteStaff")
    public void deleteStaff(@WebParam(name = "staffId") Long staffId) throws StaffNotFoundException
    {
        staffEntityControllerLocal.deleteStaff(staffId);
    }
}
