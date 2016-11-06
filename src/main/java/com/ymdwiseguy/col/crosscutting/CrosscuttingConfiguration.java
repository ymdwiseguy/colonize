package com.ymdwiseguy.col.crosscutting;

import com.ymdwiseguy.col.crosscutting.correlation.CorrelationIdFilter;
import com.ymdwiseguy.col.crosscutting.security.LastLineOfDefenseErrorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
public class CrosscuttingConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public CorrelationIdFilter correlationIdFilter() {
        return new CorrelationIdFilter();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(correlationIdFilter(), ChannelProcessingFilter.class)
            .addFilterBefore(new LastLineOfDefenseErrorFilter(), ChannelProcessingFilter.class)
            .csrf().disable();
    }
}
