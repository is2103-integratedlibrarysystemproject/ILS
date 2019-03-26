
package ejb.session.stateless;

import util.exception.ReservationNotFoundException;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@Local(ReservationEntityControllerLocal.class)
@Remote(ReservationEntityControllerRemote.class)
public class ReservationEntityController implements ReservationEntityControllerRemote, ReservationEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;

    
    @Override
    public ReservationEntity createNewReservation(ReservationEntity newReservationEntity){
        entityManager.persist(newReservationEntity);
        entityManager.flush();
        
        return newReservationEntity;
    
    }
    
     @Override
    public List<ReservationEntity> retrieveAllReservations()
    {
         Query query = entityManager.createQuery("SELECT s FROM ReservationEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException
    {
        ReservationEntity reservationEntity = entityManager.find(ReservationEntity.class, reservationId);
        
        if(reservationEntity != null)
        {
            return reservationEntity;
        }
        else
        {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }
   
    }   
    
    @Override
    public void updateReservation(ReservationEntity reservationEntity)
    {
        entityManager.merge(reservationEntity);
    }
    
    
    
    @Override
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException
    {
        ReservationEntity reservationEntityToRemove = retrieveReservationByReservationId(reservationId);
        entityManager.remove(reservationEntityToRemove);
    }


   
}
