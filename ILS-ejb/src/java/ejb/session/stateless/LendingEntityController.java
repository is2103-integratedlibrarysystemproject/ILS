
package ejb.session.stateless;

import util.exception.LendingNotFoundException;
import entity.LendingEntity;
import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberNotFoundException;


@Stateless
@Local(LendingEntityControllerLocal.class)
@Remote(LendingEntityControllerRemote.class)
public class LendingEntityController implements LendingEntityControllerRemote, LendingEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;

  @Override
    public LendingEntity createNewLending(LendingEntity newLendingEntity){
        entityManager.persist(newLendingEntity);
        entityManager.flush();
        
        return newLendingEntity;
    
    }
    
     @Override
    public List<LendingEntity> retrieveAllLendings()
    {
         Query query = entityManager.createQuery("SELECT s FROM LendingEntity s");
        
        return query.getResultList();
    }
    
    @Override
    public List<LendingEntity> retrieveCurrentLendings(String ic) throws MemberNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s.book.bookId, s.book.title, s.dueDate FROM LendingEntity s WHERE s.returnDate IS NULL AND s.member.identityNumber = :inIc");
        query.setParameter("inIc", ic);
        
        try
        {
            return query.getResultList();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MemberNotFoundException("Member Ic " + ic + " does not exist!");
        }
    }
    
    @Override
    public LendingEntity retrieveLendingByLendingId(Long lendingId) throws LendingNotFoundException
    {
        LendingEntity lendingEntity = entityManager.find(LendingEntity.class, lendingId);
        
        if(lendingEntity != null)
        {
            return lendingEntity;
        }
        else
        {
            throw new LendingNotFoundException("Lending ID " + lendingId + " does not exist!");
        }
   
    }   
    
    @Override
    public void updateLending(LendingEntity lendingEntity)
    {
        entityManager.merge(lendingEntity);
    }
    
    
    
    @Override
    public void deleteLending(Long lendingId) throws LendingNotFoundException
    {
        LendingEntity lendingEntityToRemove = retrieveLendingByLendingId(lendingId);
        entityManager.remove(lendingEntityToRemove);
    }

   
}
