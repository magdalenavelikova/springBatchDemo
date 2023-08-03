package bg.demo.springBatchDemo.repository;

import bg.demo.springBatchDemo.model.entty.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
}
