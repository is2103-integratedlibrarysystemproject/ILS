
package ejb.session.stateless;

import util.exception.InvalidLoginCredentialException;
import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.MemberNotFoundException;



public interface MemberEntityControllerLocal {
    
    
    public MemberEntity createNewMember(MemberEntity newMemberEntity);
    public MemberEntity retrieveMemberByMemberId(Long memberId) throws MemberNotFoundException;
    public MemberEntity retrieveMemberByIc(String ic) throws MemberNotFoundException;
    public List<MemberEntity> retrieveAllMembers();
    public MemberEntity memberLogin(String ic, String password) throws InvalidLoginCredentialException;
    public void updateMember(MemberEntity memberEntity);
    public void deleteMember(Long memberId) throws MemberNotFoundException;
   
}
