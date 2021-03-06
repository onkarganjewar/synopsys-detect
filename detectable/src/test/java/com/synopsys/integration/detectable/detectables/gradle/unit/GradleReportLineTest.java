package com.synopsys.integration.detectable.detectables.gradle.unit;

import org.junit.Assert;
import org.junit.Test;

import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.detectables.gradle.inspection.model.GradleGav;
import com.synopsys.integration.detectable.detectables.gradle.inspection.model.GradleTreeNode;
import com.synopsys.integration.detectable.detectables.gradle.inspection.parse.GradleReportLineParser;

public class GradleReportLineTest {
    @Test
    public void testMiscWinningIndicators() {
        String line = "|    |    |    |    +--- commons-codec:commons-codec:1.7 -> 1.9";
        String[] expected = new String[] {
            "commons-codec",
            "1.9",
            "commons-codec:commons-codec:1.9"
        };
        assertDependency(line, expected);

        line = "|    |    |    |    +--- joda-time:joda-time:2.2 -> 2.7";
        expected = new String[] {
            "joda-time",
            "2.7",
            "joda-time:joda-time:2.7"
        };
        assertDependency(line, expected);
    }

    @Test
    public void testParsingWinningIndicatorIn4_6() {
        final String line = "+--- org.springframework.boot:spring-boot-starter -> 2.0.0.RELEASE";
        final String[] expected = new String[] {
            "spring-boot-starter",
            "2.0.0.RELEASE",
            "org.springframework.boot:spring-boot-starter:2.0.0.RELEASE"
        };
        assertDependency(line, expected);
    }

    @Test
    public void testParsingWinningIndicator() {
        final String line = "+--- org.springframework.boot:spring-boot-starter: -> 1.4.3.RELEASE";
        final String[] expected = new String[] {
            "spring-boot-starter",
            "1.4.3.RELEASE",
            "org.springframework.boot:spring-boot-starter:1.4.3.RELEASE"
        };
        assertDependency(line, expected);
    }

    @Test
    public void testParsingWinningIndcatorWithFullGav() {
        final String line = "|    |    |    |    +--- org.bouncycastle:bcprov-jdk15:1.46 -> org.bouncycastle:bcprov-jdk15on:1.47";
        final String[] expected = new String[] {
            "bcprov-jdk15on",
            "1.47",
            "org.bouncycastle:bcprov-jdk15on:1.47"
        };
        assertDependency(line, expected);
    }

    @Test
    public void testParsingWinningIndicatorLine() {
        final String line = "|    \\--- com.squareup.okhttp3:okhttp-urlconnection:3.4.2 (*)";
        final String[] expected = new String[] {
            "okhttp-urlconnection",
            "3.4.2",
            "com.squareup.okhttp3:okhttp-urlconnection:3.4.2"
        };
        assertDependency(line, expected);
    }

    @Test
    public void testParsingStandardLine() {
        final String line = "|    +--- com.blackducksoftware.integration:integration-bdio:12.1.0";
        final String[] expected = new String[] {
            "integration-bdio",
            "12.1.0",
            "com.blackducksoftware.integration:integration-bdio:12.1.0"
        };
        assertDependency(line, expected);
    }

    private void assertDependency(final String line, final String[] expectedResults) {
        final ExternalIdFactory externalIdFactory = new ExternalIdFactory();
        final GradleReportLineParser gradleReportLineParser = new GradleReportLineParser();
        final GradleTreeNode gradleTreeNode = gradleReportLineParser.parseLine(line);
        final GradleGav gav = gradleTreeNode.getGav().get();
        final ExternalId externalId = externalIdFactory.createMavenExternalId(gav.getName(), gav.getArtifact(), gav.getVersion());
        final Dependency dependency = new Dependency(gav.getArtifact(), gav.getVersion(), externalId);

        Assert.assertEquals(expectedResults[0], dependency.getName());
        Assert.assertEquals(expectedResults[1], dependency.getVersion());
        Assert.assertEquals(expectedResults[2], dependency.getExternalId().createExternalId());
    }
}
