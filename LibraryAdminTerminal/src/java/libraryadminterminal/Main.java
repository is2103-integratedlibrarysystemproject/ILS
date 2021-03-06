package libraryadminterminal;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.PaymentEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import javax.ejb.EJB;


public class Main {
    @EJB
    private static PaymentEntityControllerRemote paymentEntityControllerRemote;
    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    @EJB
    private static ReservationEntityControllerRemote reservationEntityControllerRemote;
    @EJB
    private static FineEntityControllerRemote fineEntityControllerRemote;
    @EJB
    private static LendingEntityControllerRemote lendingEntityControllerRemote;
    @EJB
    private static MemberEntityControllerRemote memberEntityControllerRemote;
    @EJB
    private static BookEntityControllerRemote bookEntityControllerRemote;
    
    private static StaffEntity currentStaffEntity;
    private static RegistrationOperation registrationOperation = new RegistrationOperation();
    private static LibraryOperation libraryOperation = new LibraryOperation();
    private static AdminOperation adminOperation = new AdminOperation();
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(staffEntityControllerRemote, memberEntityControllerRemote, fineEntityControllerRemote, reservationEntityControllerRemote, paymentEntityControllerRemote, bookEntityControllerRemote, lendingEntityControllerRemote, registrationOperation, libraryOperation, adminOperation, currentStaffEntity);
        mainApp.runApp();
    }
    
}