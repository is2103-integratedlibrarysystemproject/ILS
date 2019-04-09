package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String method;
    
    @OneToOne
    private FineEntity fine;

    public PaymentEntity() {
    }

    public PaymentEntity(BigDecimal amount, String method, FineEntity fine) {
        this.amount = amount;
        this.method = method;
        this.fine = fine;
    }
    
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentEntity)) {
            return false;
        }
        PaymentEntity other = (PaymentEntity) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PaymentEntity[ id=" + paymentId + " ]";
    }
    
}
