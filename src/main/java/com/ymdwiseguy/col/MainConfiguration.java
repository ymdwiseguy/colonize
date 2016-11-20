package com.ymdwiseguy.col;


import com.github.jknack.handlebars.Handlebars;
import com.ymdwiseguy.col.views.WorldMapView;
import com.ymdwiseguy.col.worldmap.TileRepo;
import com.ymdwiseguy.col.worldmap.WorldMapRepo;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
public class MainConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    Handlebars handlebars(){
        return new Handlebars();
    }

    @Bean
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    WorldMapRepo worldMapRepo() {
        return new WorldMapRepo(jdbcTemplate());
    }

    @Bean
    TileRepo tileRepo() {
        return new TileRepo(jdbcTemplate());
    }

    @Bean
    public WorldMapService WorldMapService(){
        return new WorldMapService(worldMapRepo(), tileRepo());
    }

    @Bean
    public WorldMapView ServicesView(){
        return new WorldMapView(handlebars());
    }

}
