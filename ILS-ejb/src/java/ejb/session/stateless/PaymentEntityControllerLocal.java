
package ejb.session.stateless;

import util.exception.PaymentNotFoundException;
import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Local;


public interface PaymentEntityControllerLocal {
     public PaymentEntity createNewPayment(PaymentEntity newPaymentEntity);
     public List<PaymentEntity> retrieveAllPayments();
     public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException;
     public void updatePayment(PaymentEntity paymentEntity);
     public void deletePayment(Long paymentId) throws PaymentNotFoundException;
}
