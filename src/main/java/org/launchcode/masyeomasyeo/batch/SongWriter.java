package org.launchcode.masyeomasyeo.batch;

import org.launchcode.masyeomasyeo.models.Song;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

//TODO: Do I need to add implements ItemWriter<Song> (see SimpleSongWriter) ?
@Component
public class SongWriter implements ItemWriter<Song> {

    private static final String QUERY_INSERT_SONG = "INSERT " +
            "INTO songs(mkey, name, tempo) " +
            "VALUES (:mkey, :name, :tempo)";

    ItemWriter<Song> csvFileDatabaseItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<Song> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);

        databaseItemWriter.setSql(QUERY_INSERT_SONG);

        ItemSqlParameterSourceProvider<Song> sqlParameterSourceProvider = studentSqlParameterSourceProvider();
        databaseItemWriter.setItemSqlParameterSourceProvider(sqlParameterSourceProvider);

        return databaseItemWriter;
    }

    private ItemSqlParameterSourceProvider<Song> studentSqlParameterSourceProvider() {
        return new BeanPropertyItemSqlParameterSourceProvider<>();
    }

    @Override
    public void write(List<? extends Song> items) throws Exception {

    }
}
