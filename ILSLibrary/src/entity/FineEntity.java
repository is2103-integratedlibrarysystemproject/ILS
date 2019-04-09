package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class FineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;
    @Column(nullable = false)
    private BigDecimal amount;
    
    @OneToOne 
    private LendingEntity lending;
    
    @OneToOne(cascade=ALL, mappedBy = "fine")
    private PaymentEntity payment;

    public FineEntity() {
    }

    public FineEntity(BigDecimal amount, LendingEntity lending, PaymentEntity payment) {
        this.amount = amount;
        this.lending = lending;
        this.payment = payment;
    }
    
    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LendingEntity getLending() {
        return lending;
    }

    public void setLending(LendingEntity lending) {
        this.lending = lending;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fineId != null ? fineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FineEntity)) {
            return false;
        }
        FineEntity other = (FineEntity) object;
        if ((this.fineId == null && other.fineId != null) || (this.fineId != null && !this.fineId.equals(other.fineId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FineEntity[ id=" + fineId + " ]";
    }
    
}
