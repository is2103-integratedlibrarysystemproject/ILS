/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Remote;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
@Local(BookEntityControllerLocal.class)
@Remote(BookEntityControllerRemote.class)
public class BookEntityController implements BookEntityControllerRemote, BookEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
