
package ejb.session.stateless;

import entity.BookEntity;
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
import util.exception.BookNotFoundException;
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
        Query query = entityManager.createQuery("SELECT s FROM LendingEntity s WHERE s.returnDate IS NULL AND s.member.identityNumber = :inIc");
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
    public List<LendingEntity> retrieveCurrentLendingsByBookId(Long bookId) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM LendingEntity s WHERE s.returnDate IS NULL AND s.book.bookId = :inId");
        query.setParameter("inId", bookId);
        
        try
        {
            return query.getResultList();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("Book Id " + bookId + " does not exist!");
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
        LendingEntity lending = entityManager.find(LendingEntity.class, lendingEntity.getLendId());
        lending.setDueDate(lendingEntity.getDueDate());
        lending.setFine(lendingEntity.getFine());
        lending.setMember(lendingEntity.getMember());
        lending.setLendDate(lendingEntity.getLendDate());
        lending.setReturnDate(lendingEntity.getReturnDate());
        lending.setBook(lendingEntity.getBook());
    }
    
    
    
    @Override
    public void deleteLending(Long lendingId) throws LendingNotFoundException
    {
        LendingEntity lendingEntityToRemove = retrieveLendingByLendingId(lendingId);
        entityManager.remove(lendingEntityToRemove);
    }

   
}
