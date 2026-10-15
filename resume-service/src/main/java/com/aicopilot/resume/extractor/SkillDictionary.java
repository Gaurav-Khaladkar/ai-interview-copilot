package com.aicopilot.resume.extractor;

import java.util.*;

public class SkillDictionary {

    public static final Map<String, String> SKILL_CATEGORY_MAP = new LinkedHashMap<>();

    static {
        // Programming Languages
        register("Java", "Languages");
        register("Python", "Languages");
        register("JavaScript", "Languages");
        register("TypeScript", "Languages");
        register("C++", "Languages");
        register("C#", "Languages");
        register("Go", "Languages");
        register("Golang", "Languages");
        register("Rust", "Languages");
        register("Kotlin", "Languages");
        register("Swift", "Languages");
        register("Scala", "Languages");
        register("SQL", "Languages");
        register("HTML5", "Languages");
        register("CSS3", "Languages");
        register("Bash", "Languages");
        register("Shell", "Languages");
        register("PHP", "Languages");
        register("Ruby", "Languages");

        // Frameworks & Libraries
        register("Spring Boot", "Frameworks");
        register("Spring Framework", "Frameworks");
        register("Spring Security", "Frameworks");
        register("Spring Cloud", "Frameworks");
        register("Spring Data", "Frameworks");
        register("Hibernate", "Frameworks");
        register("JPA", "Frameworks");
        register("React", "Frameworks");
        register("React.js", "Frameworks");
        register("Angular", "Frameworks");
        register("Vue.js", "Frameworks");
        register("Node.js", "Frameworks");
        register("Express.js", "Frameworks");
        register("Next.js", "Frameworks");
        register("Django", "Frameworks");
        register("Flask", "Frameworks");
        register("FastAPI", "Frameworks");
        register("ASP.NET", "Frameworks");
        register(".NET Core", "Frameworks");
        register("Android SDK", "Frameworks");
        register("JavaFX", "Frameworks");
        register("Flutter", "Frameworks");

        // Databases & Storage
        register("PostgreSQL", "Databases");
        register("MySQL", "Databases");
        register("Oracle", "Databases");
        register("SQL Server", "Databases");
        register("MongoDB", "Databases");
        register("Redis", "Databases");
        register("Cassandra", "Databases");
        register("SQLite", "Databases");
        register("Elasticsearch", "Databases");
        register("DynamoDB", "Databases");
        register("Neo4j", "Databases");

        // Cloud & DevOps
        register("AWS", "Cloud & DevOps");
        register("Amazon Web Services", "Cloud & DevOps");
        register("Azure", "Cloud & DevOps");
        register("GCP", "Cloud & DevOps");
        register("Google Cloud Platform", "Cloud & DevOps");
        register("Docker", "Cloud & DevOps");
        register("Kubernetes", "Cloud & DevOps");
        register("Jenkins", "Cloud & DevOps");
        register("GitLab CI", "Cloud & DevOps");
        register("GitHub Actions", "Cloud & DevOps");
        register("Terraform", "Cloud & DevOps");
        register("Ansible", "Cloud & DevOps");
        register("Linux", "Cloud & DevOps");
        register("Prometheus", "Cloud & DevOps");
        register("Grafana", "Cloud & DevOps");
        register("Maven", "Cloud & DevOps");
        register("Gradle", "Cloud & DevOps");

        // Architecture & Messaging
        register("Microservices", "Architecture");
        register("REST API", "Architecture");
        register("RESTful", "Architecture");
        register("GraphQL", "Architecture");
        register("Kafka", "Architecture");
        register("Apache Kafka", "Architecture");
        register("RabbitMQ", "Architecture");
        register("gRPC", "Architecture");
        register("WebSocket", "Architecture");
        register("Event-Driven Architecture", "Architecture");
        register("OAuth2", "Architecture");
        register("JWT", "Architecture");
        register("JSON Web Token", "Architecture");
        register("Distributed Systems", "Architecture");

        // Testing & Quality
        register("JUnit", "Testing");
        register("JUnit 5", "Testing");
        register("Mockito", "Testing");
        register("Selenium", "Testing");
        register("Cypress", "Testing");
        register("TestNG", "Testing");
        register("Postman", "Testing");
        register("Cucumber", "Testing");
        register("SonarQube", "Testing");
    }

    private static void register(String skill, String category) {
        SKILL_CATEGORY_MAP.put(skill, category);
    }

    public static Set<String> getAllSkills() {
        return SKILL_CATEGORY_MAP.keySet();
    }

    public static String getCategory(String skill) {
        return SKILL_CATEGORY_MAP.getOrDefault(skill, "General Technical");
    }
}
