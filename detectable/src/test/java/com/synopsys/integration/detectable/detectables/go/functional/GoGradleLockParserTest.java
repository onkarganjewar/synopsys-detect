package com.synopsys.integration.detectable.detectables.go.functional;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.detectables.go.gogradle.GoGradleLockParser;
import com.synopsys.integration.detectable.util.FunctionalTestFiles;
import com.synopsys.integration.detectable.util.graph.GraphAssert;
import com.synopsys.integration.exception.IntegrationException;

class GoGradleLockParserTest {
    @Test
    void parseTest() throws IOException, IntegrationException {
        final File goGradleLockFile = FunctionalTestFiles.asFile("/go/gogradle/gogradle.lock");
        final ExternalIdFactory externalIdFactory = new ExternalIdFactory();
        final GoGradleLockParser goGradleLockParser = new GoGradleLockParser(externalIdFactory);
        final DependencyGraph dependencyGraph = goGradleLockParser.parse(goGradleLockFile);

        final GraphAssert graphAssert = new GraphAssert(Forge.GOLANG, dependencyGraph);
        graphAssert.hasRootDependency(externalIdFactory.createNameVersionExternalId(Forge.GOLANG, "github.com/golang/example", "0dea2d0bf90754ffa40e0cb2f23b638f3e3d7e09"));
        graphAssert.hasRootDependency(externalIdFactory.createNameVersionExternalId(Forge.GOLANG, "crypto", "9756ffdc24725223350eb3266ffb92590d28f278"));
        graphAssert.hasRootSize(2);
    }
}