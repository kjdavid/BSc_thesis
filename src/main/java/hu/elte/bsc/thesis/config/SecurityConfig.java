/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.bsc.thesis.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 *
 * @author kjdavid <kjdavid96 at gmail.com>
 */
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .jdbcAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, true as enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, role, true as enabled from users where username=?");
/*        auth
                .inMemoryAuthentication()
                .withUser("mm_ebed_elek")
                .password("{noop}asd")
                .roles("SELLER");
        auth
                .inMemoryAuthentication()
                .withUser("mm_unatko_zoltan")
                .password("{noop}asd")
                .roles("COMPANY_ADMIN");
        auth
                .inMemoryAuthentication()
                .withUser("mm_koaxk_abel")
                .password("{noop}asd")
                .roles("SELLER");
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}admin")
                .roles("ADMIN");
        auth
                .inMemoryAuthentication()
                .withUser("pcx_para_zita")
                .password("{noop}asd")
                .roles("COMPANY_ADMIN");
        auth
                .inMemoryAuthentication()
                .withUser("pcx_vincs_eszter")
                .password("{noop}asd")
                .roles("SELLER");
        auth
                .inMemoryAuthentication()
                .withUser("pcx_gep_elek")
                .password("{noop}asd")
                .roles("SELLER");
        */
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/api/**").permitAll()
                .antMatchers("/h2/**","/api/user/login","/api/user/register","/favicon.ico").permitAll()
                .antMatchers("/api/company/mycompany").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/company/addcompany").hasAuthority("ADMIN")
                .antMatchers("/api/company").hasAuthority("ADMIN")
                .antMatchers("/api/company/editcompany").hasAuthority("ADMIN")
                .antMatchers("/api/company/addstore").hasAuthority("COMPANY_ADMIN")
                .antMatchers("/api/company/editstore").hasAuthority("COMPANY_ADMIN")
                .antMatchers("/api/item/additemtocompany").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/additemtostore").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/editcompanyitem").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/editstoreitem").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/orderitem").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/addsale").hasAnyAuthority("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/getsales").hasAuthority("COMPANY_ADMIN")
                .antMatchers("/api/item/getsalesbystore").hasAuthority("COMPANY_ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(SecurityConfig::handleException)
                .and()
                .httpBasic();
        http.headers().frameOptions().disable(); //This is for h2 database access.
    }
    private static void handleException(HttpServletRequest req, HttpServletResponse rsp, AuthenticationException e)
            throws IOException {
        e.printStackTrace();
        PrintWriter writer = rsp.getWriter();
        writer.println(new ObjectMapper().writeValueAsString(ErrorResponse.UNAUTHORIZED));
        writer.close();
        rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}