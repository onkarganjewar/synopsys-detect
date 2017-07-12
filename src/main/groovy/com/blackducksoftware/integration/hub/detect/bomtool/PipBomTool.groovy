/*
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
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
package com.blackducksoftware.integration.hub.detect.bomtool

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.bdio.simple.model.DependencyNode
import com.blackducksoftware.integration.hub.detect.bomtool.output.DetectCodeLocation
import com.blackducksoftware.integration.hub.detect.bomtool.pip.PipPackager
import com.blackducksoftware.integration.hub.detect.bomtool.pip.VirtualEnvironment
import com.blackducksoftware.integration.hub.detect.bomtool.pip.VirtualEnvironmentHandler
import com.blackducksoftware.integration.hub.detect.type.BomToolType

@Component
class PipBomTool extends BomTool {
    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    static final String SETUP_FILENAME = 'setup.py'

    @Autowired
    PipPackager pipPackager

    @Autowired
    VirtualEnvironmentHandler virtualEnvironmentHandler

    BomToolType getBomToolType() {
        BomToolType.PIP
    }

    boolean isBomToolApplicable() {
        def matchingSourcePath = detectFileManager.findFile(sourcePath, SETUP_FILENAME)
        def definedRequirements = detectConfiguration.requirementsFilePath

        def foundExecutables
        if (matchingSourcePath || definedRequirements) {
            virtualEnvironmentHandler.init()
            VirtualEnvironment systemEnvironment = virtualEnvironmentHandler.getSystemEnvironment()
            foundExecutables = systemEnvironment.pipPath && systemEnvironment.pythonPath
        }

        foundExecutables && (matchingSourcePath || definedRequirements)
    }

    List<DetectCodeLocation> extractDetectCodeLocations() {
        def outputDirectory = detectFileManager.createDirectory(BomToolType.PIP)
        def sourcePath = sourcePath

        VirtualEnvironment virtualEnv = virtualEnvironmentHandler.getVirtualEnvironment(outputDirectory)
        DependencyNode projectNode = pipPackager.makeDependencyNode(virtualEnv)
        def codeLocation = new DetectCodeLocation(BomToolType.PIP, sourcePath, projectNode)

        [codeLocation]
    }
}