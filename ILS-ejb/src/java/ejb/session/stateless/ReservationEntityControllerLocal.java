
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;

public interface ReservationEntityControllerLocal {
      public ReservationEntity createNewReservation(ReservationEntity newReservationEntity);
     public List<ReservationEntity> retrieveAllReservations();
     public ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
     public void updateReservation(ReservationEntity reservationEntity);
     public void deleteReservation(Long reservationId) throws ReservationNotFoundException;
}
