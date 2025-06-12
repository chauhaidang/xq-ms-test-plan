package com.xq.testplan;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "TestPlan microservice",
                description = "TestPlan microservice api documentation",
                version = "v1",
                contact = @Contact(
                        name = "David Chau",
                        email = "service.testplan@xq.com",
                        url = "xq.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = ""
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "external documentation",
                url = "http://localhost:8080/swagger-ui/index.html"
        )
)
public class TestPlanApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestPlanApplication.class, args);
    }
}
