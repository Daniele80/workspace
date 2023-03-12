package it.rock.rock_app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("it.rock.rock_app.domain")
@EnableJpaRepositories("it.rock.rock_app.repos")
@EnableTransactionManagement
public class DomainConfig {
}
