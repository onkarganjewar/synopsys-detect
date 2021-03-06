package com.synopsys.integration.detectable.detectables.npm.lockfile.functional;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.NpmParseResult;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.PackageLock;
import com.synopsys.integration.detectable.detectables.npm.lockfile.parse.NpmLockfileParser;
import com.synopsys.integration.detectable.detectables.npm.packagejson.model.PackageJson;
import com.synopsys.integration.detectable.util.FunctionalTestFiles;
import com.synopsys.integration.detectable.util.GraphCompare;

public class NpmLockfileParserTest {
    private NpmLockfileParser npmLockfileParser;

    @Before
    public void init() {
        npmLockfileParser = new NpmLockfileParser(new GsonBuilder().setPrettyPrinting().create(), new ExternalIdFactory());
    }

    @Test
    public void parseLockFileWithRecreatedJsonTest() {
        final String lockFileText = FunctionalTestFiles.asString("/npm/package-lock.json");

        final NpmParseResult result = npmLockfileParser.parse(recreatePackageJsonFromLock(lockFileText), lockFileText, true);

        Assert.assertEquals("knockout-tournament", result.getProjectName());
        Assert.assertEquals("1.0.0", result.getProjectVersion());
        GraphCompare.assertEqualsResource("/npm/packageLockExpected_graph.json", result.getCodeLocation().getDependencyGraph());
    }

    @Test
    public void parseLockFileTest() {
        final String lockFileText = FunctionalTestFiles.asString("/npm/package-lock.json");

        final NpmParseResult result = npmLockfileParser.parse(Optional.empty(), lockFileText, true);

        Assert.assertEquals("knockout-tournament", result.getProjectName());
        Assert.assertEquals("1.0.0", result.getProjectVersion());
        GraphCompare.assertEqualsResource("/npm/packageLockExpected_graph.json", result.getCodeLocation().getDependencyGraph());
    }

    private Optional<String> recreatePackageJsonFromLock(final String lockFileText) {
        //These tests were written before we needed a package json.
        //So we replicate a package json with every package as root.
        final PackageJson packageJson = new PackageJson();
        final Gson gson = new Gson();
        final PackageLock packageLock = gson.fromJson(lockFileText, PackageLock.class);
        packageLock.dependencies.forEach((key, value) -> packageJson.dependencies.put(key, key));
        final String text = gson.toJson(packageJson);
        return Optional.of(text);
    }

    @Test
    public void parseShrinkwrapWithRecreatedJsonTest() {
        final String shrinkwrapText = FunctionalTestFiles.asString("/npm/npm-shrinkwrap.json");
        final NpmParseResult result = npmLockfileParser.parse(recreatePackageJsonFromLock(shrinkwrapText), shrinkwrapText, true);

        Assert.assertEquals("fec-builder", result.getProjectName());
        Assert.assertEquals("1.3.7", result.getProjectVersion());
        GraphCompare.assertEqualsResource("/npm/shrinkwrapExpected_graph.json", result.getCodeLocation().getDependencyGraph());
    }

    @Test
    public void parseShrinkwrapTest() {
        final String shrinkwrapText = FunctionalTestFiles.asString("/npm/npm-shrinkwrap.json");
        final NpmParseResult result = npmLockfileParser.parse(Optional.empty(), shrinkwrapText, true);

        Assert.assertEquals("fec-builder", result.getProjectName());
        Assert.assertEquals("1.3.7", result.getProjectVersion());
        GraphCompare.assertEqualsResource("/npm/shrinkwrapExpected_graph.json", result.getCodeLocation().getDependencyGraph());
    }
}
