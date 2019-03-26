
package ejb.session.stateless;

import entity.FineEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FineNotFoundException;


@Stateless
@Local(FineEntityControllerLocal.class)
@Remote(FineEntityControllerRemote.class)
public class FineEntityController implements FineEntityControllerRemote, FineEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;

   @Override
    public FineEntity createNewFine(FineEntity newFineEntity){
        entityManager.persist(newFineEntity);
        entityManager.flush();
        
        return newFineEntity;
    
    }
    
     @Override
    public List<FineEntity> retrieveAllFines()
    {
         Query query = entityManager.createQuery("SELECT s FROM FineEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public FineEntity retrieveFineByFineId(Long fineId) throws FineNotFoundException
    {
        FineEntity fineEntity = entityManager.find(FineEntity.class, fineId);
        
        if(fineEntity != null)
        {
            return fineEntity;
        }
        else
        {
            throw new FineNotFoundException("Fine ID " + fineId + " does not exist!");
        }
   
    }   
    
    @Override
    public void updateFine(FineEntity fineEntity)
    {
        entityManager.merge(fineEntity);
    }
    
    
    
    @Override
    public void deleteFine(Long fineId) throws FineNotFoundException
    {
        FineEntity fineEntityToRemove = retrieveFineByFineId(fineId);
        entityManager.remove(fineEntityToRemove);
    }

   
    
}
