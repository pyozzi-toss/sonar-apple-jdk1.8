/*
 * SonarQube Apple Plugin - Enables analysis of Swift and Objective-C projects into SonarQube.
 * Copyright © 2022 inside|app (contact@insideapp.fr)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insideapp.sonarqube.objc.tests;

import fr.insideapp.sonarqube.objc.ObjectiveC;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public final class ObjectiveCLanguageTestFileFinderTest {

    private static final String BASE_DIR = "/objc";
    private final File baseDir = FileUtils.toFile(getClass().getResource(BASE_DIR));

    private ObjectiveCLanguageTestFileFinder fileFinder;
    private DefaultFileSystem fileSystem;

    private ObjectiveC objectiveC;

    @Before
    public void prepare() {
        objectiveC = new ObjectiveC();
        fileFinder = new ObjectiveCLanguageTestFileFinder(objectiveC);
        fileSystem = new DefaultFileSystem(baseDir);
    }

    @Test
    public void getUnitTestResource_found() {
        // prepare
        DefaultInputFile testFile = new TestInputFileBuilder("", "Greeting.m")
                .setModuleBaseDir(baseDir.toPath())
                .setLanguage(objectiveC.getKey())
                .build();
        fileSystem.add(testFile);
        // test
        InputFile fileRetrieved = fileFinder.getUnitTestResource(fileSystem, baseDir.getName(), "Greeting");
        // assert
        assertThat(fileRetrieved).isNotNull();
        assertThat(fileRetrieved.filename()).isEqualTo("Greeting.m");

    }

    @Test
    public void getUnitTestResource_notFound() {
        // prepare
        // test
        InputFile fileRetrieved = fileFinder.getUnitTestResource(fileSystem, baseDir.getName(), "Greeting");
        // assert
        assertThat(fileRetrieved).isNull();
    }
}
