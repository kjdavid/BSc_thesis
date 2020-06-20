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

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        System.out.println(dataSource.getConnection().getClientInfo());
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username,password from users where username=?")
//                .authoritiesByUsernameQuery("select username, role from users where username=?");
        auth
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
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/api/**").permitAll()
                .antMatchers("/h2/**","/api/user/login","/api/user/register","/favicon.ico").permitAll()
                .anyRequest()
                .authenticated()
                .antMatchers("/api/company/mycompany").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/company/addcompany").hasRole("ADMIN")
                .antMatchers("/api/company/editcompany").hasRole("ADMIN")
                .antMatchers("/api/company/addstore").hasRole("COMPANY_ADMIN")
                .antMatchers("/api/company/editstore").hasRole("COMPANY_ADMIN")
                .antMatchers("/api/item/additemtocompany").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/additemtostore").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/editcompanyitem").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/editstoreitem").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/orderitem").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/addsale").hasAnyRole("COMPANY_ADMIN","SELLER")
                .antMatchers("/api/item/getsales").hasRole("COMPANY_ADMIN")
                .antMatchers("/api/item/getsalesbystore").hasRole("COMPANY_ADMIN")
                .and()
//                .formLogin().loginPage("/api/user/login")
//                .usernameParameter("username").passwordParameter("password")
//                .and()
//                .logout().logoutSuccessUrl("/login?logout")
//                .and()
                .exceptionHandling().authenticationEntryPoint(SecurityConfig::handleException)
                .and()
                .httpBasic();
        http.headers().frameOptions().disable(); //This is for h2 database access.
    }
    private static void handleException(HttpServletRequest req, HttpServletResponse rsp, AuthenticationException e)
            throws IOException {
        e.printStackTrace();
        System.out.println("asdasdasd");
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