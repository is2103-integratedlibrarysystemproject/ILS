
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;


public interface StaffEntityControllerRemote {
    StaffEntity createNewStaff(StaffEntity newStaffEntity);
    
    List<StaffEntity> retrieveAllStaffs();
    
    StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    void updateStaff(StaffEntity staffEntity);
    
    void deleteStaff(Long staffId) throws StaffNotFoundException;  
}
