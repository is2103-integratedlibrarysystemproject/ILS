
package ejb.session.stateless;

import entity.FineEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FineNotFoundException;


public interface FineEntityControllerRemote {
     public FineEntity createNewFine(FineEntity newFineEntity);
     public List<FineEntity> retrieveAllFines();
     public FineEntity retrieveFineByFineId(Long fineId) throws FineNotFoundException;
     public void updateFine(FineEntity fineEntity);
     public void deleteFine(Long fineId) throws FineNotFoundException;
}
