package bg.demo.springBatchDemo.config;


import bg.demo.springBatchDemo.model.entty.Company;
import bg.demo.springBatchDemo.repository.CompanyRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {


    private final CompanyRepository companyRepository;

    public SpringBatchConfiguration(CompanyRepository companyRepository) {

        this.companyRepository = companyRepository;
    }


    @Bean
    public FlatFileItemReader<Company> reader() {
        FlatFileItemReader<Company> itemReader = new FlatFileItemReader<Company>();
        itemReader.setResource(new FileSystemResource(("scr/main/resources/static/test.csv")));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Company> lineMapper() {
        DefaultLineMapper<Company> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "NACE", "NAME");
        BeanWrapperFieldSetMapper<Company> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Company.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CompanyProcessor companyProcessor() {
        return new CompanyProcessor();
    }

    @Bean
    public RepositoryItemWriter<Company> writer() {
        RepositoryItemWriter<Company> writer = new RepositoryItemWriter<>();
        writer.setRepository(companyRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(PlatformTransactionManager transactionManager,JobRepository jobRepository) {
        return new StepBuilder("step1", jobRepository)
                .<Company, Company>chunk(10, transactionManager)
                .reader(reader())
                .processor(companyProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(PlatformTransactionManager transactionManager,JobRepository jobRepository) {
        return new JobBuilder("importCompany")
                .preventRestart().start(step1(transactionManager,jobRepository)).build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }
}
