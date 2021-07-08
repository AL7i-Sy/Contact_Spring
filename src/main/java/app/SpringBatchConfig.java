package app;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	//reader
	//read from the database
	@Bean
	public JdbcCursorItemReader<Contact> reader() {
		System.out.println("reader============================");
		JdbcCursorItemReader<Contact> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT * from contact");
		reader.setRowMapper(new ContactRowMapper());
		
		return reader;
	}
	
	//processor
	
	@Bean
	public ContactItemProcessor processor() {
		return new ContactItemProcessor();
	}
	
	//writer
	//write to the CSV file
	@Bean
	public FlatFileItemWriter<Contact> writer(){
		FlatFileItemWriter<Contact> writer = new FlatFileItemWriter<>();
		System.out.println("Writer Method()==============================================");
		writer.setResource(new FileSystemResource("c:\\contacts.csv"));
	
		writer.setLineAggregator(new DelimitedLineAggregator<Contact>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Contact>() {
					{
						setNames(new String[] {"id","email","firstname","lastname","address","phone"});
					}
				});
			}
		});
		return writer;
	}
	
	//Step
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Contact, Contact> chunk(100)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	//Job
	@Bean
	public Job job() {
		return jobBuilderFactory.get("exportContactJob")
				//.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}
}
