
package ejb.session.stateless;

import entity.BookEntity;
import entity.LendingEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.BookNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;


public interface LendingEntityControllerLocal {
    
     public LendingEntity createNewLending(LendingEntity newLendingEntity);
     public List<LendingEntity> retrieveAllLendings();
     public List<LendingEntity> retrieveCurrentLendings(String ic) throws MemberNotFoundException;
     public LendingEntity retrieveLendingByLendingId(Long lendingId) throws LendingNotFoundException;
     public List<LendingEntity> retrieveCurrentLendingsByBookId(Long bookId) throws BookNotFoundException;
     public void updateLending(LendingEntity lendingEntity);
     public void deleteLending(Long lendingId) throws LendingNotFoundException;
}
