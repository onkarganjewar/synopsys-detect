package com.blackducksoftware.integration.hub.detect.bomtool;

import com.blackducksoftware.integration.hub.bdio.graph.DependencyGraph;
import com.blackducksoftware.integration.hub.bdio.model.externalid.ExternalId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class YarnBomToolTest {
    private YarnBomTool yarnBomTool;
    private DependencyGraph dependencyGraph;
    private List<String> testLines;

    @Before
    public void setup() {
        yarnBomTool = new YarnBomTool();
    }

    @Test
    public void testThatYarnListWithOnlyTopLevelDependenciesIsParsedCorrectly() {
        testLines = new ArrayList<>();
        testLines.add("├─ esprima@3.1.3");
        testLines.add("└─ extsprintf@1.3.0");

        dependencyGraph = yarnBomTool.extractGraphFromYarnListFile(testLines);

        List<ExternalId> tempList = new ArrayList<>(dependencyGraph.getRootDependencyExternalIds());

        assertEquals(tempList.get(1).name, "esprima");
        assertEquals(tempList.get(2).name, "extsprintf");

    }

    @Test
    public void testThatYarnListWithGrandchildIsParsedCorrectly() {
        testLines = new ArrayList<>();
        testLines.add("├─ yargs-parser@4.2.1");
        testLines.add("│  └─ camelcase@^3.0.0");

        dependencyGraph = yarnBomTool.extractGraphFromYarnListFile(testLines);

        List<ExternalId> tempList = new ArrayList<>(dependencyGraph.getRootDependencyExternalIds());
        List<ExternalId> kidsList = new ArrayList<>(dependencyGraph.getChildrenExternalIdsForParent(tempList.get(1)));

        assertEquals(tempList.get(1).name, "yargs-parser");
        assertEquals(kidsList.get(0).name, "camelcase");
    }

    @Test
    public void testThatYarnListWithGreatGrandchildrenIsParsedCorrectly() {
        testLines = new ArrayList<>();
        testLines.add("├─ yargs-parser@4.2.1");
        testLines.add("│  └─ camelcase@^3.0.0");
        testLines.add("│  │  └─ ms@0.7.2");

        dependencyGraph = yarnBomTool.extractGraphFromYarnListFile(testLines);

        List<ExternalId> tempList = new ArrayList<>(dependencyGraph.getRootDependencyExternalIds());
        List<ExternalId> kidsList = new ArrayList<>(dependencyGraph.getChildrenExternalIdsForParent(tempList.get(1)));

        assertEquals(tempList.get(1).name, "yargs-parser");
        assertEquals(kidsList.get(0).name, "camelcase");
        assertEquals(kidsList.get(1).name, "ms");
    }

    @Test
    public void testThatYarnListRegexParsesTheCorrectText() {
        String input = "│  │  ├─ engine.io-client@~1.8.4";
        assertEquals(yarnBomTool.grabFuzzyName(input), "engine.io-client@~1.8.4");

        input = "│  ├─ test-fixture@PolymerElements/test-fixture";
        assertEquals(yarnBomTool.grabFuzzyName(input), "test-fixture@PolymerElements/test-fixture");

        input = "│  │  ├─ tough-cookie@>=0.12.0";
        assertEquals(yarnBomTool.grabFuzzyName(input), "tough-cookie@>=0.12.0");

        input = "│  │  ├─ cryptiles@2.x.x";
        assertEquals(yarnBomTool.grabFuzzyName(input), "cryptiles@2.x.x");

    }
}