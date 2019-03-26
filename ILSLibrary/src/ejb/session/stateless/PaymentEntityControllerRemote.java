
package ejb.session.stateless;

import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.PaymentNotFoundException;

public interface PaymentEntityControllerRemote {
    public PaymentEntity createNewPayment(PaymentEntity newPaymentEntity);
     public List<PaymentEntity> retrieveAllPayments();
     public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException;
     public void updatePayment(PaymentEntity paymentEntity);
     public void deletePayment(Long paymentId) throws PaymentNotFoundException;
}
