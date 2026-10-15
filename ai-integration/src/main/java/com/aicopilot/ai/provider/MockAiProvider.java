package com.aicopilot.ai.provider;

import com.aicopilot.ai.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("mockAiProvider")
public class MockAiProvider implements AiProvider {

    private final Map<String, String> translationDictionary = new HashMap<>();
    private final Map<String, List<String>> questionBank = new HashMap<>();

    public MockAiProvider() {
        initQuestionBanks();
        initTranslations();
    }

    @Override
    public AiResponse generateAnswer(AiRequest request) {
        String domain = request.getDomain() != null ? request.getDomain().toLowerCase() : "general";
        String difficulty = request.getDifficulty() != null ? request.getDifficulty() : "Mid-Level";
        boolean isDetailed = "DETAILED".equalsIgnoreCase(request.getFormat());
        String targetLang = request.getTargetLanguage() != null ? request.getTargetLanguage() : "English";

        AiResponse response = new AiResponse();
        response.setModeUsed(getProviderName());
        response.setLanguage(targetLang);

        if (domain.contains("java") || domain.contains("spring")) {
            populateJavaSpringResponse(request, response, isDetailed);
        } else if (domain.contains("python")) {
            populatePythonResponse(request, response, isDetailed);
        } else if (domain.contains("sql") || domain.contains("database")) {
            populateSqlResponse(request, response, isDetailed);
        } else if (domain.contains("javascript") || domain.contains("web")) {
            populateJsResponse(request, response, isDetailed);
        } else {
            populateGenericResponse(request, response, isDetailed);
        }

        // Apply translation if Hindi or Marathi is requested
        if ("Hindi".equalsIgnoreCase(targetLang)) {
            response.setAnswer(translate(response.getAnswer(), "Hindi"));
            response.setConceptSummary(translate(response.getConceptSummary(), "Hindi"));
        } else if ("Marathi".equalsIgnoreCase(targetLang)) {
            response.setAnswer(translate(response.getAnswer(), "Marathi"));
            response.setConceptSummary(translate(response.getConceptSummary(), "Marathi"));
        }

        return response;
    }

    private void populateJavaSpringResponse(AiRequest request, AiResponse response, boolean isDetailed) {
        String q = request.getQuestion() != null ? request.getQuestion().toLowerCase() : "";
        if (q.contains("hashmap") || q.contains("map")) {
            if (isDetailed) {
                response.setAnswer(
                    "Java HashMap is a hash table based implementation of the Map interface. " +
                    "It computes the bucket index via (n - 1) & hash(key). In Java 8+, when bucket entries exceed TREEIFY_THRESHOLD (8) " +
                    "and capacity >= 64, the linked list converts into a balanced Red-Black Tree (TreeNode), reducing worst-case lookup from O(N) to O(log N). " +
                    "Concurrent modifications require ConcurrentHashMap to avoid race conditions and resize loops."
                );
            } else {
                response.setAnswer(
                    "HashMap stores key-value pairs using buckets indexed by hashing. " +
                    "Java 8 optimizes collision handling by transforming linked lists into Red-Black Trees when bucket size exceeds 8, guaranteeing O(log N) worst-case time."
                );
            }
            response.setConceptSummary("Hashing, Bucket Array, Red-Black Trees, O(1) Average Lookup, Non-Thread-Safe.");
            response.setCodeSnippet(
                "Map<String, Integer> map = new HashMap<>();\n" +
                "map.put(\"candidateId\", 101);\n" +
                "int id = map.getOrDefault(\"candidateId\", -1);"
            );
            List<LineExplanation> lines = new ArrayList<>();
            lines.add(new LineExplanation(1, "Map<String, Integer> map = new HashMap<>();", "Initializes HashMap with default initial capacity 16 and load factor 0.75."));
            lines.add(new LineExplanation(2, "map.put(\"candidateId\", 101);", "Hashes key, computes bucket index, and places Node in the bucket."));
            lines.add(new LineExplanation(3, "int id = map.getOrDefault(\"candidateId\", -1);", "Safely retrieves value with fallback default, preventing NullPointerException."));
            response.setLineByLineExplanation(lines);
            response.setFollowUpQuestions(Arrays.asList(
                "Why must hashCode() and equals() contracts be strictly preserved for HashMap keys?",
                "How does ConcurrentHashMap achieve high concurrency without locking the entire map?",
                "What triggers the table resize (rehash) in HashMap?"
            ));
        } else {
            response.setAnswer(
                "Spring Boot provides convention-over-configuration, embedded servlet containers (Tomcat/Jetty), " +
                "and opinionated starter dependencies. Core architectural mechanisms include Dependency Injection (IoC), " +
                "Spring Bean Lifecycle (postProcessBeforeInitialization / postProcessAfterInitialization), and declarative transaction management (@Transactional)."
            );
            response.setConceptSummary("Spring IoC Container, ApplicationContext, Autowiring, Starters, Bean Scopes.");
            response.setCodeSnippet(
                "@Service\n" +
                "public class CandidateService {\n" +
                "    @Autowired\n" +
                "    private CandidateRepository repository;\n" +
                "}"
            );
            response.setFollowUpQuestions(Arrays.asList(
                "What is the difference between @Component, @Service, and @Repository annotations?",
                "How does Spring Boot auto-configuration evaluate @ConditionalOnClass and @ConditionalOnMissingBean?",
                "Explain the transactional isolation levels supported by Spring Data JPA."
            ));
        }
        response.setLearningSuggestions(Arrays.asList(
            "Review Java 17/21 Virtual Threads (Project Loom) for high-throughput concurrency.",
            "Practice Spring Data JPA projection interfaces for optimal SQL query performance."
        ));
    }

    private void populatePythonResponse(AiRequest request, AiResponse response, boolean isDetailed) {
        response.setAnswer(
            "Python manages memory through reference counting and a generational cyclic garbage collector. " +
            "The Global Interpreter Lock (GIL) synchronizes thread execution in CPython, making multiprocessing or async I/O " +
            "preferred for CPU-bound or I/O-bound tasks respectively."
        );
        response.setConceptSummary("Reference Counting, Generational GC, GIL, Generators, Context Managers.");
        response.setCodeSnippet(
            "def fetch_data(items):\n" +
            "    for item in items:\n" +
            "        yield item.strip()\n"
        );
        response.setFollowUpQuestions(Arrays.asList(
            "How do Python generator expressions save memory compared to list comprehensions?",
            "What is the function of the __enter__ and __exit__ magic methods in context managers?"
        ));
    }

    private void populateSqlResponse(AiRequest request, AiResponse response, boolean isDetailed) {
        response.setAnswer(
            "SQL query optimization relies on cost-based query planners and indexing (B-Tree, Bitmap, Hash). " +
            "Clustered indexes define physical row ordering, whereas non-clustered indexes store pointers to table data. " +
            "ACID properties guarantee reliability across distributed transactions."
        );
        response.setConceptSummary("B-Tree Indexes, Query Execution Plans, ACID Transactions, Index SARGability.");
        response.setCodeSnippet(
            "SELECT u.full_name, COUNT(p.id) as sessions_count\n" +
            "FROM users u\n" +
            "JOIN practice_sessions p ON u.id = p.user_id\n" +
            "GROUP BY u.full_name\n" +
            "HAVING COUNT(p.id) >= 5;"
        );
        response.setFollowUpQuestions(Arrays.asList(
            "Explain index selectivity and why low-cardinality columns perform poorly with B-Tree indexes.",
            "What is the difference between READ COMMITTED and REPEATABLE READ isolation levels?"
        ));
    }

    private void populateJsResponse(AiRequest request, AiResponse response, boolean isDetailed) {
        response.setAnswer(
            "JavaScript executes on a single-threaded Event Loop comprising the Call Stack, Web APIs, Microtask Queue (Promises), " +
            "and Macrotask Queue (setTimeout, setImmediate). Microtasks always take precedence before the next event loop tick."
        );
        response.setConceptSummary("Event Loop, Call Stack, Microtasks vs Macrotasks, Prototypal Inheritance, Closures.");
        response.setCodeSnippet(
            "async function fetchSession(id) {\n" +
            "    const res = await fetch(`/api/practice/sessions/${id}`);\n" +
            "    return await res.json();\n" +
            "}"
        );
        response.setFollowUpQuestions(Arrays.asList(
            "How does JavaScript closure preserve outer lexical scope references?",
            "Explain how Promise.allSettled differs from Promise.all."
        ));
    }

    private void populateGenericResponse(AiRequest request, AiResponse response, boolean isDetailed) {
        response.setAnswer(
            "In modern software engineering, clean architecture separates business logic from infrastructure frameworks. " +
            "Key principles include Single Responsibility, Dependency Inversion, comprehensive automated unit testing, and modular packaging."
        );
        response.setConceptSummary("Clean Architecture, SOLID Principles, Loose Coupling, High Cohesion.");
        response.setFollowUpQuestions(Arrays.asList(
            "How do you approach refactoring monolithic systems into event-driven microservices?",
            "What metrics do you monitor to ensure distributed system reliability (SLIs, SLOs, SLAs)?"
        ));
    }

    @Override
    public List<String> generateQuestions(List<String> skills, String domain, String difficulty, int count) {
        List<String> result = new ArrayList<>();
        List<String> pool = new ArrayList<>();

        if (skills != null) {
            for (String s : skills) {
                String key = s.trim().toLowerCase();
                if (questionBank.containsKey(key)) {
                    pool.addAll(questionBank.get(key));
                }
            }
        }

        if (domain != null && questionBank.containsKey(domain.toLowerCase())) {
            pool.addAll(questionBank.get(domain.toLowerCase()));
        }

        if (pool.isEmpty()) {
            pool.addAll(questionBank.get("general"));
        }

        Collections.shuffle(pool);
        int take = Math.min(count > 0 ? count : 5, pool.size());
        for (int i = 0; i < take; i++) {
            result.add(pool.get(i));
        }
        return result;
    }

    @Override
    public PracticeEvaluationResponse evaluateAnswer(PracticeEvaluationRequest request) {
        String answer = request.getCandidateAnswer() != null ? request.getCandidateAnswer().trim() : "";
        int wordCount = answer.split("\\s+").length;

        int accuracy = Math.min(95, Math.max(50, 60 + (wordCount > 30 ? 25 : wordCount)));
        int completeness = Math.min(92, Math.max(45, 55 + (wordCount > 50 ? 30 : wordCount / 2)));
        int communication = Math.min(96, Math.max(60, 70 + (answer.contains("because") || answer.contains("specifically") ? 20 : 10)));

        String feedback = "Your response demonstrates strong fundamental understanding. " +
            (wordCount < 25 ? "Consider providing more architectural depth and real-world trade-off examples." : "Excellent technical phrasing and structured breakdown.");

        String idealAnswer = "A comprehensive answer should start with the core concept, discuss internal mechanics and edge cases, quantify performance complexities, and cite production experience.";

        PracticeEvaluationResponse response = new PracticeEvaluationResponse(accuracy, completeness, communication, feedback, idealAnswer);
        response.setSuggestions(Arrays.asList(
            "Elaborate on production failure modes and disaster recovery.",
            "Mention time and space Big-O complexities explicitly."
        ));
        response.setFollowUpQuestion("Can you discuss how this design behaves under sudden traffic spikes of 10x normal load?");
        return response;
    }

    @Override
    public CodeAnalysisResponse analyzeCode(CodeAnalysisRequest request) {
        CodeAnalysisResponse res = new CodeAnalysisResponse();
        String code = request.getSourceCode() != null ? request.getSourceCode() : "";

        res.setExecutionTimeMs(48);
        res.setMemoryUsedMb(22.4);
        res.setTestsPassed(3);
        res.setTotalTests(3);
        res.setStatus("PASSED");
        res.setOutput("All 3 test cases passed successfully in 48ms.");
        res.setTimeComplexity("O(N)");
        res.setSpaceComplexity("O(N)");
        res.setCritique("Algorithm is efficient and cleanly structured. Edge cases for null/empty collections are handled.");
        res.setSuggestions(Arrays.asList(
            "Consider pre-allocating collection capacity to eliminate internal resizing overhead.",
            "Add JavaDoc comments explaining method invariants."
        ));
        res.setRefactoredCode("// Optimized clean version\n" + code);
        return res;
    }

    @Override
    public String translate(String text, String targetLanguage) {
        if (text == null || text.isBlank()) return text;
        if ("English".equalsIgnoreCase(targetLanguage)) return text;

        if ("Hindi".equalsIgnoreCase(targetLanguage)) {
            // Contextual technical translation
            String result = text;
            for (Map.Entry<String, String> entry : translationDictionary.entrySet()) {
                if (entry.getKey().startsWith("hi_")) {
                    String englishPhrase = entry.getKey().substring(3);
                    result = result.replace(englishPhrase, entry.getValue());
                }
            }
            return "[हिंदी भाषांतर] " + result;
        }

        if ("Marathi".equalsIgnoreCase(targetLanguage)) {
            String result = text;
            for (Map.Entry<String, String> entry : translationDictionary.entrySet()) {
                if (entry.getKey().startsWith("mr_")) {
                    String englishPhrase = entry.getKey().substring(3);
                    result = result.replace(englishPhrase, entry.getValue());
                }
            }
            return "[मराठी अनुवाद] " + result;
        }

        return text;
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public String getProviderName() {
        return "OFFLINE_KNOWLEDGE_BASE";
    }

    private void initTranslations() {
        translationDictionary.put("hi_Java", "जावा");
        translationDictionary.put("hi_Spring Boot", "स्प्रिंग बूट");
        translationDictionary.put("hi_memory", "मेमरी (स्मृति)");
        translationDictionary.put("hi_database", "डेटाबेस");
        translationDictionary.put("hi_architecture", "आर्किटेक्चर (रचना)");
        translationDictionary.put("hi_optimization", "ऑप्टिमाइजेशन (सुधार)");

        translationDictionary.put("mr_Java", "जावा");
        translationDictionary.put("mr_Spring Boot", "स्प्रिंग बूट");
        translationDictionary.put("mr_memory", "स्मृती (मेमरी)");
        translationDictionary.put("mr_database", "डेटाबेस");
        translationDictionary.put("mr_architecture", "रचना");
    }

    private void initQuestionBanks() {
        questionBank.put("java", Arrays.asList(
            "How does the Java Garbage Collector detect and eliminate circular memory references?",
            "Explain the Java Memory Model (JMM), volatile semantics, and happens-before guarantees.",
            "Compare CompletableFuture vs reactive streams (Project Reactor) for asynchronous workflows.",
            "What is the difference between String pool, interned strings, and heap allocation in Java?"
        ));

        questionBank.put("spring boot", Arrays.asList(
            "How does Spring Boot resolve circular bean dependencies, and why should they be avoided?",
            "Explain Spring Security filter chains and JWT authentication mechanics.",
            "How do you implement distributed tracing using Spring Cloud Sleuth/Micrometer and Zipkin?",
            "What is the difference between JPA persist(), merge(), and saveOrUpdate()?"
        ));

        questionBank.put("python", Arrays.asList(
            "How does Python's GIL impact multithreaded applications and what are common workarounds?",
            "Explain Python decorators and how functools.wraps preserves metadata.",
            "What are Python metaclasses and when are they appropriately used in production?"
        ));

        questionBank.put("sql", Arrays.asList(
            "What is the difference between clustered and non-clustered indexes in relational databases?",
            "Explain database isolation levels and phenomena like dirty reads, non-repeatable reads, and phantom reads.",
            "How do window functions (ROW_NUMBER, RANK, DENSE_RANK) differ from aggregate functions?"
        ));

        questionBank.put("docker", Arrays.asList(
            "How do multi-stage Docker builds reduce container image footprints and vulnerabilities?",
            "Explain Docker network drivers: bridge, host, and overlay networks."
        ));

        questionBank.put("general", Arrays.asList(
            "Describe a complex technical challenge you resolved and how you diagnosed the root cause.",
            "How do you design a high-throughput, low-latency microservice handling 50k requests per second?",
            "Explain the CAP theorem and how you choose between consistency and availability in distributed storage."
        ));
    }
}
