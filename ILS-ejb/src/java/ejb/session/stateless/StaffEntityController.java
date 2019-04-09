
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

@Stateless
@Local(StaffEntityControllerLocal.class)
@Remote(StaffEntityControllerRemote.class)

public class StaffEntityController implements StaffEntityControllerRemote, StaffEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;
    
    public StaffEntityController()
    {
    }
    
    @Override
    public StaffEntity createNewStaff(StaffEntity newStaffEntity)
    {
        entityManager.persist(newStaffEntity);
        entityManager.flush();
        
        return newStaffEntity;
    }
    
    
    
    @Override
    public List<StaffEntity> retrieveAllStaffs()
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        
        if(staffEntity != null)
        {
            return staffEntity;
        }
        else
        {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }
    
    
    
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    
    
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            
            if(staffEntity.getPassword().equals(password))
            {
                return staffEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    
    
    @Override
    public void updateStaff(StaffEntity staffEntity)
    {
        StaffEntity staff = entityManager.find(StaffEntity.class, staffEntity.getStaffId());
        staff.setFirstName(staffEntity.getFirstName());
        staff.setLastName(staffEntity.getLastName());
        staff.setUsername(staffEntity.getUsername());
        staff.setPassword(staffEntity.getPassword());
    }
    
    
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntityToRemove = entityManager.find(StaffEntity.class, staffId);
        entityManager.remove(staffEntityToRemove);
    }  
}
