package selfservicekiosk;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.PaymentEntityControllerRemote;
import ejb.session.stateless.ReservationEntityControllerRemote;
import entity.MemberEntity;
import javax.ejb.EJB;


public class Main {
    @EJB
    private static PaymentEntityControllerRemote paymentEntityControllerRemote;
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
    
    private static MemberEntity currentMemberEntity;
    private static LibraryOperation libraryOperation = new LibraryOperation();

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(memberEntityControllerRemote, fineEntityControllerRemote, reservationEntityControllerRemote, paymentEntityControllerRemote, bookEntityControllerRemote, lendingEntityControllerRemote, libraryOperation, currentMemberEntity);
        mainApp.runApp();
    }
    
}