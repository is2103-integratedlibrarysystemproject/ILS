
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;


public interface ReservationEntityControllerRemote {
      public ReservationEntity createNewReservation(ReservationEntity newReservationEntity);
     public List<ReservationEntity> retrieveAllReservations();
     public ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
     public void updateReservation(ReservationEntity reservationEntity);
     public void deleteReservation(Long reservationId) throws ReservationNotFoundException;
}
