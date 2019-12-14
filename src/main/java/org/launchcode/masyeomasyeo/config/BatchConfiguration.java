package org.launchcode.masyeomasyeo.config;

import org.launchcode.masyeomasyeo.batch.DBWriter;
import org.launchcode.masyeomasyeo.batch.SongItemProcessor;
import org.launchcode.masyeomasyeo.models.Song;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.FileSystemResource;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /*Setting up our csv reader*/
    @Bean
    @Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public FlatFileItemReader<Song> importReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile){
        FlatFileItemReader<Song> reader = new FlatFileItemReader<Song>();
        reader.setResource(new FileSystemResource(pathToFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Song>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(",");
                setStrict(false);
                setNames(new String[] { "name", "tempo", "mkey", "artists", "genres" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Song>() {{
                setTargetType(Song.class);
            }});

        }});

        return reader;
    }

    /* Creating a new processor from the SongItemProcessor component */
    @Bean
    public SongItemProcessor processor(){
        return new SongItemProcessor();
    }

    @Bean
    public DBWriter writer(){
        return new DBWriter();
/*        JdbcBatchItemWriter<Song> writer = new JdbcBatchItemWriter<Song>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Song>());
        writer.setSql("INSERT INTO song(name) VALUES (:name)");
        writer.setDataSource(dataSource);

        return writer;*/
    }

    @Bean
    public Step step1(ItemReader<Song> importReader) {
        return stepBuilderFactory.get("step1").<Song, Song> chunk(10)
                .reader(importReader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(ItemReader<Song> importReader) {
        return jobBuilderFactory.get("importSongJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1(importReader))
                .end()
                .build();
    }

}
