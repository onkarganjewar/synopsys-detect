/**
 * synopsys-detect
 *
 * Copyright (c) 2020 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.detect.tool.detector;

import com.synopsys.integration.detectable.detectables.bitbake.BitbakeDetectable;
import com.synopsys.integration.detectable.detectables.clang.ClangDetectable;
import com.synopsys.integration.detectable.detectables.cocoapods.PodlockDetectable;
import com.synopsys.integration.detectable.detectables.conda.CondaCliDetectable;
import com.synopsys.integration.detectable.detectables.cpan.CpanCliDetectable;
import com.synopsys.integration.detectable.detectables.cran.PackratLockDetectable;
import com.synopsys.integration.detectable.detectables.git.cli.GitCliDetectable;
import com.synopsys.integration.detectable.detectables.git.parsing.GitParseDetectable;
import com.synopsys.integration.detectable.detectables.go.godep.GoDepLockDetectable;
import com.synopsys.integration.detectable.detectables.go.gogradle.GoGradleDetectable;
import com.synopsys.integration.detectable.detectables.go.gomod.GoModCliDetectable;
import com.synopsys.integration.detectable.detectables.go.vendor.GoVendorDetectable;
import com.synopsys.integration.detectable.detectables.go.vendr.GoVndrDetectable;
import com.synopsys.integration.detectable.detectables.gradle.inspection.GradleDetectable;
import com.synopsys.integration.detectable.detectables.gradle.parsing.GradleParseDetectable;
import com.synopsys.integration.detectable.detectables.hex.RebarDetectable;
import com.synopsys.integration.detectable.detectables.maven.cli.MavenPomDetectable;
import com.synopsys.integration.detectable.detectables.maven.cli.MavenPomWrapperDetectable;
import com.synopsys.integration.detectable.detectables.maven.parsing.MavenParseDetectable;
import com.synopsys.integration.detectable.detectables.npm.cli.NpmCliDetectable;
import com.synopsys.integration.detectable.detectables.npm.lockfile.NpmPackageLockDetectable;
import com.synopsys.integration.detectable.detectables.npm.lockfile.NpmShrinkwrapDetectable;
import com.synopsys.integration.detectable.detectables.npm.packagejson.NpmPackageJsonParseDetectable;
import com.synopsys.integration.detectable.detectables.nuget.NugetProjectDetectable;
import com.synopsys.integration.detectable.detectables.nuget.NugetSolutionDetectable;
import com.synopsys.integration.detectable.detectables.packagist.ComposerLockDetectable;
import com.synopsys.integration.detectable.detectables.pear.PearCliDetectable;
import com.synopsys.integration.detectable.detectables.pip.PipInspectorDetectable;
import com.synopsys.integration.detectable.detectables.pip.PipenvDetectable;
import com.synopsys.integration.detectable.detectables.rubygems.gemlock.GemlockDetectable;
import com.synopsys.integration.detectable.detectables.rubygems.gemspec.GemspecParseDetectable;
import com.synopsys.integration.detectable.detectables.sbt.SbtResolutionCacheDetectable;
import com.synopsys.integration.detectable.detectables.swift.SwiftCliDetectable;
import com.synopsys.integration.detectable.detectables.yarn.YarnLockDetectable;
import com.synopsys.integration.detector.base.DetectorType;
import com.synopsys.integration.detector.rule.DetectorRule;
import com.synopsys.integration.detector.rule.DetectorRuleSet;

public class DetectorRuleFactory {
    public DetectorRuleSet createRules(final DetectableFactory detectableFactory, final boolean buildless) {
        if (buildless) {
            return createBuildlessRules(detectableFactory);
        } else {
            return createRules(detectableFactory);
        }
    }

    //TODO: It would just be nice not to have to call 'build' after each of the addDetectors.
    private DetectorRuleSet createRules(final DetectableFactory detectableFactory) {
        final DetectableFactoryDetectorRuleSetBuilder ruleSet = new DetectableFactoryDetectorRuleSetBuilder(detectableFactory);

        //TODO: Verify we still need to pass detector name here. We may now be able to get it from the detectable class - before we could not as it was not instantiated.
        ruleSet.addDetector(DetectorType.BITBAKE, "Bitbake", BitbakeDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.COCOAPODS, "Pod Lock", PodlockDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.CONDA, "Conda Cli", CondaCliDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.CPAN, "Cpan Cli", CpanCliDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.CRAN, "Packrat Lock", PackratLockDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.GO_MOD, "Go Mod Cli", GoModCliDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_GRADLE, "Go Gradle", GoGradleDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_DEP, "Go Lock", GoDepLockDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_VNDR, "Go Vndr", GoVndrDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_VENDOR, "Go Vendor", GoVendorDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.GRADLE, "Gradle Inspector", GradleDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.HEX, "Rebar", RebarDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.MAVEN, "Maven Pom", MavenPomDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.MAVEN, "Maven Wrapper", MavenPomWrapperDetectable.class).defaults().build();

        final DetectorRule yarnLock = ruleSet.addDetector(DetectorType.YARN, "Yarn Lock", YarnLockDetectable.class).defaultLock().build();
        final DetectorRule npmPackageLock = ruleSet.addDetector(DetectorType.NPM, "Package Lock", NpmPackageLockDetectable.class).defaultLock().build();
        final DetectorRule npmShrinkwrap = ruleSet.addDetector(DetectorType.NPM, "Shrinkwrap", NpmShrinkwrapDetectable.class).defaultLock().build();
        final DetectorRule npmCli = ruleSet.addDetector(DetectorType.NPM, "Npm Cli", NpmCliDetectable.class).defaults().build();

        ruleSet.yield(npmShrinkwrap).to(npmPackageLock);
        ruleSet.yield(npmCli).to(npmPackageLock);
        ruleSet.yield(npmCli).to(npmShrinkwrap);

        ruleSet.yield(npmCli).to(yarnLock);
        ruleSet.yield(npmPackageLock).to(yarnLock);
        ruleSet.yield(npmShrinkwrap).to(yarnLock);

        final DetectorRule nugetSolution = ruleSet.addDetector(DetectorType.NUGET, "Solution", NugetSolutionDetectable.class).defaults().build();
        //The Project detectable is "notNestable" because it will falsely apply under a solution (the solution includes all of the projects).
        final DetectorRule nugetProject = ruleSet.addDetector(DetectorType.NUGET, "Project", NugetProjectDetectable.class).notNestable().noMaxDepth().build();

        ruleSet.yield(nugetProject).to(nugetSolution);

        ruleSet.addDetector(DetectorType.PACKAGIST, "Composer", ComposerLockDetectable.class).defaults().build();

        final DetectorRule pipEnv = ruleSet.addDetector(DetectorType.PIP, "Pip Env", PipenvDetectable.class).defaults().build();
        final DetectorRule pipInspector = ruleSet.addDetector(DetectorType.PIP, "Pip Inspector", PipInspectorDetectable.class).defaults().build();

        ruleSet.yield(pipInspector).to(pipEnv);

        ruleSet.addDetector(DetectorType.RUBYGEMS, "Gemlock", GemlockDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.SBT, "Sbt Resolution Cache", SbtResolutionCacheDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.PEAR, "Pear", PearCliDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.CLANG, "Clang", ClangDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.SWIFT, "Swift", SwiftCliDetectable.class).defaults().build();

        final DetectorRule gitParse = ruleSet.addDetector(DetectorType.GIT, "Git Parse", GitParseDetectable.class).defaults().build();
        final DetectorRule gitCli = ruleSet.addDetector(DetectorType.GIT, "Git Cli", GitCliDetectable.class).defaults().build();
        ruleSet.fallback(gitCli).to(gitParse);

        return ruleSet.build();
    }

    private DetectorRuleSet createBuildlessRules(final DetectableFactory detectableFactory) {
        final DetectableFactoryDetectorRuleSetBuilder ruleSet = new DetectableFactoryDetectorRuleSetBuilder(detectableFactory);

        ruleSet.addDetector(DetectorType.COCOAPODS, "Pod Lock", PodlockDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.PACKAGIST, "Packrat Lock", PackratLockDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.GO_DEP, "Go Lock", GoDepLockDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_VNDR, "Go Vndr", GoVndrDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_VENDOR, "Go Vendor", GoVendorDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.GRADLE, "Gradle Parse", GradleParseDetectable.class).defaults().build();
        ruleSet.addDetector(DetectorType.GO_GRADLE, "Go Gradle", GoGradleDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.MAVEN, "Maven Pom Parse", MavenParseDetectable.class).defaults().build();

        final DetectorRule yarnLock = ruleSet.addDetector(DetectorType.YARN, "Yarn Lock", YarnLockDetectable.class).defaults().build();
        final DetectorRule npmPackageLock = ruleSet.addDetector(DetectorType.NPM, "Package Lock", NpmPackageLockDetectable.class).defaults().build();
        final DetectorRule npmShrinkwrap = ruleSet.addDetector(DetectorType.NPM, "Shrinkwrap", NpmShrinkwrapDetectable.class).defaults().build();
        final DetectorRule npmPackageJsonParse = ruleSet.addDetector(DetectorType.NPM, "Package Json Parse", NpmPackageJsonParseDetectable.class).defaults().build();

        ruleSet.yield(npmShrinkwrap).to(npmPackageLock);
        ruleSet.yield(npmPackageJsonParse).to(npmPackageLock);
        ruleSet.yield(npmPackageJsonParse).to(npmShrinkwrap);

        ruleSet.yield(npmPackageJsonParse).to(yarnLock);
        ruleSet.yield(npmPackageLock).to(yarnLock);
        ruleSet.yield(npmShrinkwrap).to(yarnLock);

        ruleSet.addDetector(DetectorType.PACKAGIST, "Composer", ComposerLockDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.PIP, "Pip Env", PipenvDetectable.class).defaults().build();

        final DetectorRule gemlock = ruleSet.addDetector(DetectorType.RUBYGEMS, "Gemlock", GemlockDetectable.class).defaults().build();
        final DetectorRule gemspec = ruleSet.addDetector(DetectorType.RUBYGEMS, "Gemspec", GemspecParseDetectable.class).defaults().build();

        ruleSet.yield(gemspec).to(gemlock);

        ruleSet.addDetector(DetectorType.SBT, "Sbt Resolution Cache", SbtResolutionCacheDetectable.class).defaults().build();

        ruleSet.addDetector(DetectorType.GIT, "Git Parse", GitParseDetectable.class).defaults().invisibleToNesting().build();

        return ruleSet.build();
    }
}