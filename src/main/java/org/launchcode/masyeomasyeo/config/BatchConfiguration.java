package org.launchcode.masyeomasyeo.config;

import org.launchcode.masyeomasyeo.batch.SongItemProcessor;
import org.launchcode.masyeomasyeo.models.Genre;
import org.launchcode.masyeomasyeo.models.Song;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

/*    private static final String QUERY_INSERT_SONG = "INSERT " +
    "IF EXISTS (SELECT * FROM Bookings WHERE FLightID = @Id) " +
    "BEGIN " +
    --UPDATE HERE
    END
            "ELSE " +
    "BEGIN " +
            "INSERT " +
                    "INTO songs(mkey, name, tempo) " +
                    "VALUES (:mkey, :name, :tempo)";

                    INSERT INTO songs ( name, mkey, tempo )
(
    SELECT "dvgr", "sg", 234
      WHERE NOT EXISTS (
        SELECT 1
          FROM songs
          WHERE name = "gho"
    )
)
                    */

private static final String QUERY_INSERT_SONG_COND = "INSERT " +
        "INTO songs(mkey, name, tempo) " +
        "( " +
        "SELECT :mkey, :name, :tempo " +
        "WHERE NOT EXISTS ( " +
        "SELECT * " +
        "FROM songs " +
        "WHERE name = :name " +
        ")" +
        ")";
    /*TODO: Figure out how to handle duplicates without skipping entire batch*/
    private static final String QUERY_INSERT_SONG = "INSERT IGNORE " +
            "INTO songs(mkey, name, tempo) " +
            "VALUES (:mkey, :name, :tempo) ";

    private static final String QUERY_INSERT_GENRE = "INSERT " +
            "INTO genres(name) " +
            "VALUES (:genres)";

    private static final String QUERY_INSERT_ARTIST = "INSERT " +
            "INTO artists(name) " +
            "VALUES (:artists)";

    //TODO: Figure out how this reader is used in the job process in BatchConfiguration.
    @Bean
    @Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public FlatFileItemReader<Song> importReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile){
    //public FlatFileItemReader<Song> importReader() throws IOException {
        FlatFileItemReader<Song> reader = new FlatFileItemReader<Song>();
        reader.setResource(new FileSystemResource(pathToFile));
        reader.setLinesToSkip(1);
/*        reader.setLineMapper(new DefaultLineMapper<Song>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(",");
                setStrict(false);
                setNames(new String[] { "name", "tempo", "mkey", "artists", "genres" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Song>() {{
                setTargetType(Song.class);
            }});

        }});*/
        LineMapper<Song> songLineMapper = createSongLineMapper();
        reader.setLineMapper(songLineMapper);

        return reader;
    }

    private LineMapper<Song> createSongLineMapper() {
        DefaultLineMapper<Song> songLineMapper = new DefaultLineMapper<>();

        LineTokenizer songLineTokenizer = createSongLineTokenizer();
        songLineMapper.setLineTokenizer(songLineTokenizer);

        FieldSetMapper<Song> songInformationMapper = createSongInformationMapper();
        songLineMapper.setFieldSetMapper(songInformationMapper);

        return songLineMapper;
    }

    private LineTokenizer createSongLineTokenizer() {
        DelimitedLineTokenizer songLineTokenizer = new DelimitedLineTokenizer();
        songLineTokenizer.setDelimiter(",");
        songLineTokenizer.setNames(new String[]{ "name", "tempo", "mkey", "artists", "genres" });
        return songLineTokenizer;
    }

    private FieldSetMapper<Song> createSongInformationMapper() {
        BeanWrapperFieldSetMapper<Song> songInformationMapper = new BeanWrapperFieldSetMapper<>();
        songInformationMapper.setTargetType(Song.class);
        return songInformationMapper;
    }

    /* Creating a new processor from the SongItemProcessor component.  If we comment this out, our Step will find
    the SongItemProcessor class and use that.  Similarly, if we changed the name of this instance and didn't
    make the same change in our Step, Step would not know whether to use this instance of ItemProcessor or the one
    defined in SongItemProcessor.
     */
    @Bean
    public SongItemProcessor processor(){
        return new SongItemProcessor();
    }


    @Bean
    ItemWriter<Song> csvFileDatabaseItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
            JdbcBatchItemWriter<Song> databaseItemWriter = new JdbcBatchItemWriter<>();
            databaseItemWriter.setDataSource(dataSource);
            databaseItemWriter.setJdbcTemplate(jdbcTemplate);

            databaseItemWriter.setSql(QUERY_INSERT_SONG);

            ItemSqlParameterSourceProvider<Song> sqlParameterSourceProvider = songSqlParameterSourceProvider();
            databaseItemWriter.setItemSqlParameterSourceProvider(sqlParameterSourceProvider);

            return databaseItemWriter;
        }

        private ItemSqlParameterSourceProvider<Song> songSqlParameterSourceProvider() {
            return new BeanPropertyItemSqlParameterSourceProvider<>();
        }

    @Bean
    ItemWriter<Genre> csvFileDatabaseGenreWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<Genre> genreDatabaseItemWriter = new JdbcBatchItemWriter<>();
        genreDatabaseItemWriter.setDataSource(dataSource);
        genreDatabaseItemWriter.setJdbcTemplate(jdbcTemplate);

        genreDatabaseItemWriter.setSql(QUERY_INSERT_GENRE);

        ItemSqlParameterSourceProvider<Genre> sqlGenreParameterSourceProvider = genreSqlParameterSourceProvider();
        genreDatabaseItemWriter.setItemSqlParameterSourceProvider(sqlGenreParameterSourceProvider);

        return genreDatabaseItemWriter;
    }

    private ItemSqlParameterSourceProvider<Genre> genreSqlParameterSourceProvider() {
        return new BeanPropertyItemSqlParameterSourceProvider<>();
    }

    private ItemSqlParameterSourceProvider<Song> studentSqlParameterSourceProvider() {
        return new BeanPropertyItemSqlParameterSourceProvider<>();
    }

/*    @Bean
    public Step step1(ItemReader<Song> importReader) {
        //public Step step1() throws IOException {
        return stepBuilderFactory.get("step1").<Song, Song> chunk(10)
                .reader(importReader)
                .processor(processor())
                *//*.writer((ItemWriter<? super Song>) writer())*//*
                .writer(SongWriter())
                .build();
    }

        @Bean
    public Job job(ItemReader<Song> importReader) {
    //public Job job() throws IOException {
        return jobBuilderFactory.get("importSongJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1(importReader))
                .end()
                .build();
    }*/

    @Bean
    Step csvFileToDatabaseStep(ItemReader<Song> csvFileItemReader,
                               SongItemProcessor processor,
                               ItemWriter<Song> csvFileDatabaseItemWriter,
                               StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("csvFileToDatabaseStep")
                .<Song, Song>chunk(1)
                .reader(csvFileItemReader)
                .processor(processor)
                .writer(csvFileDatabaseItemWriter)
                .build();
    }

    @Bean
    Job csvFileToDatabaseJob(JobBuilderFactory jobBuilderFactory,
                             @Qualifier("csvFileToDatabaseStep") Step csvStudentStep) {
        return jobBuilderFactory.get("csvFileToDatabaseJob")
                .incrementer(new RunIdIncrementer())
                .flow(csvStudentStep)
                .end()
                .build();
    }

}
