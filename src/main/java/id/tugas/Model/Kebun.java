package id.tugas.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Kebun extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;

    public String komoditas;

    public Integer total;

    public LocalDateTime created_at;

    public LocalDateTime updated_at;



    public String getKomoditas() {
        return komoditas;
    }

    public void setKomoditas(String komoditas) {
        this.komoditas = komoditas;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }


    public void preUpdate() {
        this.updated_at = LocalDateTime.now();
    }


}
