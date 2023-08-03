package bg.demo.springBatchDemo.model.entty;

import jakarta.persistence.*;


@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(nullable = false)
    private String NACE;

    @Column(nullable = false)
    private String name;

    public String getNACE() {
        return NACE;
    }

    public void setNACE(String NACE) {
        this.NACE = NACE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}