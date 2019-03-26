
package ejb.session.stateless;

import util.exception.StaffNotFoundException;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;


public interface StaffEntityControllerLocal {
   StaffEntity createNewStaff(StaffEntity newStaffEntity);
    
    List<StaffEntity> retrieveAllStaffs();
    
    StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    void updateStaff(StaffEntity staffEntity);
    
    void deleteStaff(Long staffId) throws StaffNotFoundException;  
}
