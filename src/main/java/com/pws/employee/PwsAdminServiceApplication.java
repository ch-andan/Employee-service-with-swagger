package com.pws.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pws.employee.utility.AuditAwareImpl;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.pws.employee.*"})
public class PwsAdminServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(PwsAdminServiceApplication.class, args);

	}
	@Bean
    public AuditorAware<String> auditorAware() {
        return new AuditAwareImpl();
    }


}
