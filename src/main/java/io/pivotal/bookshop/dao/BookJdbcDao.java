package io.pivotal.bookshop.dao;

import io.pivotal.bookshop.domain.BookMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BookJdbcDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookJdbcDao(@Qualifier("dataSource") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public BookMaster getBook(Integer id) {
        return jdbcTemplate.queryForObject("select * from books where item_number = ?",
                new BookMapper(),
                id
        );
    }

    public int countBooks() {
        return jdbcTemplate.queryForObject("select count(*) from books", Integer.class);
    }

    public class BookMapper implements RowMapper<BookMaster> {

        @Override
        public BookMaster mapRow(ResultSet rs, int i) throws SQLException {
            return new BookMaster(rs.getInt("item_number"),rs.getString("description"),rs.getFloat("retail_cost"),
                    rs.getInt("publication_date"),rs.getString("author"),rs.getString("title"));
        }
    }
}
