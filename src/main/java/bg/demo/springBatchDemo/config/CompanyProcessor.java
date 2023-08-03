package bg.demo.springBatchDemo.config;

import bg.demo.springBatchDemo.model.entty.Company;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;


public class CompanyProcessor implements ItemProcessor<Company, Company> {
    @Override
    public Company process(Company company) throws Exception {
        return company;
    }
}
