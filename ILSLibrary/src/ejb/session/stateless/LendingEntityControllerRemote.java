
package ejb.session.stateless;

import entity.LendingEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;


public interface LendingEntityControllerRemote {
    public LendingEntity createNewLending(LendingEntity newLendingEntity);
     public List<LendingEntity> retrieveAllLendings();
     public List<LendingEntity> retrieveCurrentLendings(String ic) throws MemberNotFoundException;
     public LendingEntity retrieveLendingByLendingId(Long lendingId) throws LendingNotFoundException;
     public void updateLending(LendingEntity lendingEntity);
     public void deleteLending(Long lendingId) throws LendingNotFoundException;
}
