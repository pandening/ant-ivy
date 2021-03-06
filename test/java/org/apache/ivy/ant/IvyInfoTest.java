/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.ivy.ant;

import java.io.File;

import org.apache.ivy.TestHelper;

import org.apache.tools.ant.Project;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IvyInfoTest {
    private IvyInfo info;

    @Before
    public void setUp() {
        Project project = TestHelper.newProject();

        info = new IvyInfo();
        info.setProject(project);
    }

    @Test
    public void testSimple() {
        info.setFile(new File("test/java/org/apache/ivy/ant/ivy-simple.xml"));
        info.execute();

        assertEquals("apache", info.getProject().getProperty("ivy.organisation"));
        assertEquals("resolve-simple", info.getProject().getProperty("ivy.module"));
        assertEquals("1.0", info.getProject().getProperty("ivy.revision"));
        assertEquals("default", info.getProject().getProperty("ivy.configurations"));
        assertEquals("default", info.getProject().getProperty("ivy.public.configurations"));
    }

    @Test
    public void testAll() {
        info.setFile(new File("test/java/org/apache/ivy/ant/ivy-info-all.xml"));
        info.execute();

        assertEquals("apache", info.getProject().getProperty("ivy.organisation"));
        assertEquals("info-all", info.getProject().getProperty("ivy.module"));
        assertEquals("1.0", info.getProject().getProperty("ivy.revision"));
        assertEquals("release", info.getProject().getProperty("ivy.status"));
        assertEquals("default, test, private", info.getProject().getProperty("ivy.configurations"));
        assertEquals("default, test", info.getProject().getProperty("ivy.public.configurations"));
        assertEquals("trunk", info.getProject().getProperty("ivy.branch"));
        assertEquals("myvalue", info.getProject().getProperty("ivy.extra.myextraatt"));

        // test the configuration descriptions
        assertEquals("The default dependencies",
            info.getProject().getProperty("ivy.configuration.default.desc"));
        assertEquals("Dependencies used for testing",
            info.getProject().getProperty("ivy.configuration.test.desc"));
        assertNull(info.getProject().getProperty("ivy.configuration.private.desc"));
    }

    /**
     * Test case for IVY-726.
     *
     * @see <a href="https://issues.apache.org/jira/browse/IVY-726">IVY-726</a>
     */
    @Test
    public void testIVY726() {
        info.setFile(new File("test/java/org/apache/ivy/ant/ivy-info-all.xml"));
        info.execute();

        assertNull(info.getProject().getProperty("ivy.extra.branch"));
    }

    /**
     * Test case for IVY-395.
     *
     * @see <a href="https://issues.apache.org/jira/browse/IVY-395">IVY-395</a>
     */
    @Test
    public void testIVY395() {
        info.setFile(new File("test/java/org/apache/ivy/ant/ivy-artifact-info.xml"));
        info.execute();

        assertEquals("test", info.getProject().getProperty("ivy.artifact.1.name"));
        assertEquals("jar", info.getProject().getProperty("ivy.artifact.1.type"));
        assertEquals("jar", info.getProject().getProperty("ivy.artifact.1.ext"));
        assertEquals("master, alt", info.getProject().getProperty("ivy.artifact.1.conf"));
        assertEquals("main", info.getProject().getProperty("ivy.artifact.1.extra.data"));

        assertEquals("test-a", info.getProject().getProperty("ivy.artifact.2.name"));
        assertEquals("jar", info.getProject().getProperty("ivy.artifact.2.type"));
        assertEquals("jar", info.getProject().getProperty("ivy.artifact.2.ext"));
        assertEquals("alt", info.getProject().getProperty("ivy.artifact.2.conf"));
        assertEquals("client", info.getProject().getProperty("ivy.artifact.2.extra.data"));

        assertEquals("stuff", info.getProject().getProperty("ivy.artifact.3.name"));
        assertEquals("javadoc", info.getProject().getProperty("ivy.artifact.3.type"));
        assertEquals("zip", info.getProject().getProperty("ivy.artifact.3.ext"));
        assertEquals("doc", info.getProject().getProperty("ivy.artifact.3.conf"));
    }

}
