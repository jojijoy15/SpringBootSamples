/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.example.demo.steps")
public class CucumberRunnerTest {
}
