package com.synopsys.integration.detectable.detectables.clang.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunner;
import com.synopsys.integration.detectable.detectable.file.FileFinder;
import com.synopsys.integration.detectable.detectables.clang.ClangDetectable;
import com.synopsys.integration.detectable.detectables.clang.ClangDetectableOptions;
import com.synopsys.integration.detectable.detectables.clang.ClangExtractor;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.ClangPackageManager;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.ClangPackageManagerRunner;
import com.synopsys.integration.detectable.util.MockDetectableEnvironment;
import com.synopsys.integration.detectable.util.MockFileFinder;

public class ClangDetectableTest {
    private static final String JSON_COMPILATION_DATABASE_FILENAME = "compile_commands.json";

    @Test
    public void testApplicable() {
        final ExecutableRunner executableRunner = null;
        final List<ClangPackageManager> availablePackageManagers = new ArrayList<>(0);
        final ClangExtractor clangExtractor = null;
        final ClangPackageManagerRunner packageManagerRunner = null;

        final ClangDetectableOptions options = new ClangDetectableOptions(false);
        final DetectableEnvironment environment = MockDetectableEnvironment.empty();
        final FileFinder fileFinder = MockFileFinder.withFileNamed(JSON_COMPILATION_DATABASE_FILENAME);

        final ClangDetectable detectable = new ClangDetectable(environment, executableRunner, fileFinder, availablePackageManagers, clangExtractor, options, packageManagerRunner);

        assertTrue(detectable.applicable().getPassed());
    }
}
