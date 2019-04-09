package entity;

import java.io.Serializable;
import java.util.Date;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class LendingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lendId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lendDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dueDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date returnDate;
    
    @ManyToOne
    private MemberEntity member;
    
    @ManyToOne
    private BookEntity book;
   
    @OneToOne(cascade=ALL, mappedBy = "lending")
    private FineEntity fine;

    
    public LendingEntity() {
    }

    public LendingEntity(Date lendDate, Date dueDate, Date returnDate, MemberEntity member, BookEntity book, FineEntity fine) {
        this.lendDate = lendDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.member = member;
        this.book = book;
        this.fine = fine;
    }

    public Long getLendId() {
        return lendId;
    }

    public void setLendId(Long lendId) {
        this.lendId = lendId;
    }

    public Date getLendDate() {
        return lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public FineEntity getFine() {
        return fine;
    }

    public void setFine(FineEntity fine) {
        this.fine = fine;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lendId != null ? lendId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LendingEntity)) {
            return false;
        }
        LendingEntity other = (LendingEntity) object;
        if ((this.lendId == null && other.lendId != null) || (this.lendId != null && !this.lendId.equals(other.lendId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LendingEntity[ id=" + lendId + " ]";
    }
    
}
