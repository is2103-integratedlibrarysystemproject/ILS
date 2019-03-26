
package ejb.session.stateless;

import util.exception.PaymentNotFoundException;
import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(PaymentEntityControllerLocal.class)
@Remote(PaymentEntityControllerRemote.class)
public class PaymentEntityController implements PaymentEntityControllerRemote, PaymentEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;

    @Override
    public PaymentEntity createNewPayment(PaymentEntity newPaymentEntity){
        entityManager.persist(newPaymentEntity);
        entityManager.flush();
        
        return newPaymentEntity;
    
    }
    
     @Override
    public List<PaymentEntity> retrieveAllPayments()
    {
         Query query = entityManager.createQuery("SELECT s FROM PaymentEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException
    {
        PaymentEntity paymentEntity = entityManager.find(PaymentEntity.class, paymentId);
        
        if(paymentEntity != null)
        {
            return paymentEntity;
        }
        else
        {
            throw new PaymentNotFoundException("Payment ID " + paymentId + " does not exist!");
        }
   
    }   
    
    @Override
    public void updatePayment(PaymentEntity paymentEntity)
    {
        entityManager.merge(paymentEntity);
    }
    
    
    
    @Override
    public void deletePayment(Long paymentId) throws PaymentNotFoundException
    {
        PaymentEntity paymentEntityToRemove = retrievePaymentByPaymentId(paymentId);
        entityManager.remove(paymentEntityToRemove);
    }

   
}
