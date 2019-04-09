
package ejb.session.stateless;

import util.exception.FineNotFoundException;
import entity.FineEntity;
import java.util.List;
import javax.ejb.Local;


public interface FineEntityControllerLocal {
     public FineEntity createNewFine(FineEntity newFineEntity);
     public List<FineEntity> retrieveAllFines();
     public FineEntity retrieveFineByFineId(Long fineId) throws FineNotFoundException;
     public List<FineEntity> retrieveBorrowerFines(String ic);
     public List<FineEntity> retrieveOutstandingFines(String ic) throws FineNotFoundException;
     public void updateFine(FineEntity fineEntity);
     public void deleteFine(Long fineId) throws FineNotFoundException;
}
