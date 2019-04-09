package ejb.session.singleton;

import ejb.session.stateless.BookEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import ejb.session.stateless.MemberEntityControllerLocal;
import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.StaffNotFoundException;

@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean {
    @PersistenceContext(unitName = "ILS-ejbPU")
    private EntityManager entityManager;
    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;
    
    public DataInitializationSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("manager");
        } catch(StaffNotFoundException ex) {
            initializeData();
        }
    }
    
    private void initializeData() {
        staffEntityControllerLocal.createNewStaff(new StaffEntity("Linda", "Chua", "manager", "password"));
        staffEntityControllerLocal.createNewStaff(new StaffEntity("Barbara", "Durham", "assistant", "password"));
        
        bookEntityControllerLocal.createNewBook(new BookEntity("The Lord of the Rings", "S18018", 1954, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        bookEntityControllerLocal.createNewBook(new BookEntity("Le Petit Prince", "S64921", 1943, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        bookEntityControllerLocal.createNewBook(new BookEntity("Harry Potter and the Philosopher's Stone", "S38101", 1997, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        bookEntityControllerLocal.createNewBook(new BookEntity("The Hobbit", "S19527", 1937, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        bookEntityControllerLocal.createNewBook(new BookEntity("And Then There Were None", "S63288", 1939, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        bookEntityControllerLocal.createNewBook(new BookEntity("Dream of the Red Chamber", "S32187", 1791, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        bookEntityControllerLocal.createNewBook(new BookEntity("The Lion, the Witch and the Wardrobe", "S74569", 1950, 1, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        
        memberEntityControllerLocal.createNewMember(new MemberEntity("S7483027A", "1", "Tony", "Teo", "Male", 44, "87297373", "11 Tampines Ave 3", 0, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
        memberEntityControllerLocal.createNewMember(new MemberEntity("S8381028X", "2", "Wendy", "Tan", "Female", 35, "97502837", "15 Computing Dr", 0, new ArrayList<LendingEntity>(), new ArrayList<ReservationEntity>()));
    }
}
