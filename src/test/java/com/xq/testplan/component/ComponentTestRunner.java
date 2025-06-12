package com.xq.testplan.component;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentTestRunner {
    @Test
    void runComponentTests() throws IOException {
        String reportDir = "build/karate-reports";
        Results results = Runner.path("classpath:com/xq/testplan/component")
                .outputCucumberJson(true)
                .reportDir(reportDir)
                .parallel(5);

        // Rename the summary file for GitHub Actions
        Path source = Path.of(reportDir, "karate-summary-json.txt");
        Path target = Path.of(reportDir, "karate-summary.json");
        if (Files.exists(source)) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }

        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
