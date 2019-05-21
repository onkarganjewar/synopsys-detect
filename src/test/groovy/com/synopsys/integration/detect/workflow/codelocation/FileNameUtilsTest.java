package com.synopsys.integration.detect.workflow.codelocation;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class FileNameUtilsTest {

    @Test
    public void testStandard() {
        final String relativized = FileNameUtils.relativize("/a/b/c", "/a/b/d");
        assertEquals("d", relativized);
    }
}
