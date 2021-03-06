package com.synopsys.integration.detect.integration;

import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfigBuilder;
import com.synopsys.integration.blackduck.service.BlackDuckService;
import com.synopsys.integration.blackduck.service.BlackDuckServicesFactory;
import com.synopsys.integration.blackduck.service.ProjectService;
import com.synopsys.integration.blackduck.service.ReportService;
import com.synopsys.integration.blackduck.service.model.ProjectSyncModel;
import com.synopsys.integration.blackduck.service.model.ProjectVersionWrapper;
import com.synopsys.integration.detect.Application;
import com.synopsys.integration.log.BufferedIntLogger;
import com.synopsys.integration.log.IntLogger;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class CompleteRiskReportTest extends BlackDuckIntegrationTest {
    @Test
    public void testRiskReportIsPopulated() throws Exception {
        Path tempReportDirectoryPath = Files.createTempDirectory("junit_report");
        File tempReportDirectory = tempReportDirectoryPath.toFile();

        String projectName = "synopsys-detect-junit";
        String projectVersionName = "risk-report";
        ProjectVersionWrapper projectVersionWrapper = assertProjectVersionReady(projectName, projectVersionName);

        List<File> pdfFiles = getPdfFiles(tempReportDirectory);
        assertEquals(0, pdfFiles.size());

        reportService.createReportPdfFile(tempReportDirectory, projectVersionWrapper.getProjectView(), projectVersionWrapper.getProjectVersionView());
        pdfFiles = getPdfFiles(tempReportDirectory);
        assertEquals(1, pdfFiles.size());

        long initialFileLength = pdfFiles.get(0).length();
        assertTrue(initialFileLength > 0);
        FileUtils.deleteQuietly(pdfFiles.get(0));

        pdfFiles = getPdfFiles(tempReportDirectory);
        assertEquals(0, pdfFiles.size());

        List<String> detectArgs = getInitialArgs(projectName, projectVersionName);
        detectArgs.add("--detect.risk.report.pdf=true");
        detectArgs.add("--detect.risk.report.pdf.path=" + tempReportDirectory.toString());
        Application.main(detectArgs.toArray(new String[detectArgs.size()]));

        pdfFiles = getPdfFiles(tempReportDirectory);
        assertEquals(1, pdfFiles.size());
        long postLength = pdfFiles.get(0).length();
        assertTrue(postLength > initialFileLength);
    }

    private List<File> getPdfFiles(File directory) {
        return Arrays.stream(directory.listFiles())
                .filter(file -> file.getName().endsWith(".pdf"))
                .collect(Collectors.toList());
    }

}
