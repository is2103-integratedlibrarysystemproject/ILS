
package ejb.session.stateless;


import util.exception.InvalidLoginCredentialException;
import util.exception.MemberNotFoundException;
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

@Stateless
@Local(MemberEntityControllerLocal.class)
@Remote(MemberEntityControllerRemote.class)
public class MemberEntityController implements MemberEntityControllerRemote, MemberEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;

    
    @Override
    public MemberEntity createNewMember(MemberEntity newMemberEntity){
        entityManager.persist(newMemberEntity);
        entityManager.flush();
        
        return newMemberEntity;
    
    }
    
     @Override
    public List<MemberEntity> retrieveAllMembers()
    {
        Query query = entityManager.createQuery("SELECT s FROM MemberEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public MemberEntity retrieveMemberByMemberId(Long memberId) throws MemberNotFoundException
    {
        MemberEntity memberEntity = entityManager.find(MemberEntity.class, memberId);
        
        if(memberEntity != null)
        {
            return memberEntity;
        }
        else
        {
            throw new MemberNotFoundException("Member ID " + memberId + " does not exist!");
        }
   
    }   
     @Override
    public MemberEntity retrieveMemberByIc(String ic) throws MemberNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM MemberEntity s WHERE s.ic = :inIc");
        query.setParameter("inIc", ic);
        
        try
        {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MemberNotFoundException("Member Ic " + ic + " does not exist!");
        }
    }
    
    @Override
    public MemberEntity memberLogin(String ic, String securityCode) throws InvalidLoginCredentialException
    {
        try
        {
            MemberEntity memberEntity = retrieveMemberByIc(ic);
            
            if(memberEntity.getSecurityCode().equals(securityCode))
            {
                return memberEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Ic does not exist or invalid security code!");
            }
        }
        catch(MemberNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Ic does not exist or invalid security code!");
        }
    }
    
    
    
    @Override
    public void updateMember(MemberEntity memberEntity)
    {
        entityManager.merge(memberEntity);
    }
    
    
    
    @Override
    public void deleteMember(Long memberId) throws MemberNotFoundException
    {
        MemberEntity memberEntityToRemove = retrieveMemberByMemberId(memberId);
        entityManager.remove(memberEntityToRemove);
    }
    
    
}