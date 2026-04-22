/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public class CucumberConfiguration {
}
