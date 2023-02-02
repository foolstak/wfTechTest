package org.wf.sreesainath.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.wf.sreesainath.domain.Transaction;
import org.wf.sreesainath.domain.output.OMSAAA;
import org.wf.sreesainath.service.TransactionOMSAAConverter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    //TODO: Move config bits to application.properties
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("input/transactions.csv")
    private Resource[] inputResources;

    private final Resource outputResource = new FileSystemResource("output/OMSOutput.aaa");

    public MultiResourceItemReader multiResourceItemReader()
    {
        MultiResourceItemReader<Transaction> resourceItemReader = new MultiResourceItemReader();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(transactionReader());
        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<Transaction> transactionReader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<Transaction>();
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Transaction>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "SecurityId","PortfolioId","Nominal","OMS","TransactionType"});
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {
                    {
                        setTargetType(Transaction.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public TransactionOMSAAConverter transactionProcessor() {
        return new TransactionOMSAAConverter();
    }

    @Bean
    public FlatFileItemWriter<OMSAAA> writer()
    {
        FlatFileItemWriter<OMSAAA> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<OMSAAA>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<OMSAAA>() {
                    {
                        setNames(new String[] { "ISIN", "PortfolioCode", "Nominal", "TransactionType"});
                    }
                });
            }
        });
        return writer;
    }
    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob").incrementer(
                new RunIdIncrementer()).flow(step1()).end().build();
    }
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<Transaction, OMSAAA>chunk(10)
                .reader(multiResourceItemReader())
                .processor(transactionProcessor())
                .writer(writer())
                .build();
    }
}