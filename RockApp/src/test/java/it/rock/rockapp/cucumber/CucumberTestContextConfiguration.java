package it.rock.rockapp.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import it.rock.rockapp.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
