package com.blackducksoftware.integration.hub.detect.extraction.bomtool.pip;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackducksoftware.integration.hub.detect.DetectConfiguration;
import com.blackducksoftware.integration.hub.detect.extraction.requirement.evaluation.StrategyEnvironment;
import com.blackducksoftware.integration.hub.detect.extraction.result.ExecutableNotFoundStrategyResult;
import com.blackducksoftware.integration.hub.detect.extraction.result.FileNotFoundStrategyResult;
import com.blackducksoftware.integration.hub.detect.extraction.result.InspectorNotFoundStrategyResult;
import com.blackducksoftware.integration.hub.detect.extraction.result.PassedStrategyResult;
import com.blackducksoftware.integration.hub.detect.extraction.result.StrategyResult;
import com.blackducksoftware.integration.hub.detect.extraction.strategy.Strategy;
import com.blackducksoftware.integration.hub.detect.model.BomToolType;
import com.blackducksoftware.integration.hub.detect.util.DetectFileFinder;

@Component
public class PipInspectorStrategy extends Strategy<PipInspectorContext, PipInspectorExtractor> {
    public static final String SETUP_FILE_NAME= "setup.py";

    @Autowired
    public DetectFileFinder fileFinder;

    @Autowired
    public PipExecutableFinder pipExecutableFinder;

    @Autowired
    public PythonExecutableFinder pythonExecutableFinder;

    @Autowired
    public PipInspectorManager pipInspectorManager;


    @Autowired
    public DetectConfiguration detectConfiguration;

    public PipInspectorStrategy() {
        super("Pip Inspector", BomToolType.PIP, PipInspectorContext.class, PipInspectorExtractor.class);
    }

    @Override
    public StrategyResult applicable(final StrategyEnvironment environment, final PipInspectorContext context) {
        context.setupFile = fileFinder.findFile(environment.getDirectory(), SETUP_FILE_NAME);
        context.requirementFilePath = detectConfiguration.getRequirementsFilePath();

        final boolean hasSetups = context.setupFile != null;
        final boolean hasRequirements = context.requirementFilePath != null && StringUtils.isNotBlank(context.requirementFilePath);
        if (hasSetups || hasRequirements) {
            return new PassedStrategyResult();
        } else {
            return new FileNotFoundStrategyResult(SETUP_FILE_NAME);
        }

    }

    @Override
    public StrategyResult extractable(final StrategyEnvironment environment, final PipInspectorContext context){
        final String pipExe = pipExecutableFinder.findPip(environment);
        if (pipExe == null) {
            return new ExecutableNotFoundStrategyResult("pip");
        }

        context.pythonExe = pythonExecutableFinder.findPython(environment);
        if (context.pythonExe == null) {
            return new ExecutableNotFoundStrategyResult("python");
        }

        context.pipInspector = pipInspectorManager.findPipInspector(environment);
        if (context.pipInspector == null) {
            return new InspectorNotFoundStrategyResult("pip");
        }

        return new PassedStrategyResult();
    }


}
