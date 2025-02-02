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
package fr.insideapp.sonarqube.apple.xcode.coverage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insideapp.sonarqube.apple.xcode.coverage.mapper.XcodeCoverageMapper;
import fr.insideapp.sonarqube.apple.xcode.coverage.models.XcodeCodeCoverage;
import fr.insideapp.sonarqube.apple.xcode.coverage.models.XcodeCodeCoverageMetadata;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;

public final class XcodeCoverageMapperTest {

    private static final String BASE_DIR = "/xcode/coverage/mapper";
    private final File baseDir = FileUtils.toFile(getClass().getResource(BASE_DIR));

    private XcodeCoverageMapper mapper;
    private ObjectMapper objectMapper;

    @Before
    public void prepare() {
        mapper = new XcodeCoverageMapper();
        objectMapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    public void map_oneFile_manyCoverageData() throws IOException {
        // prepare
        File codeCoverageFile = new File(baseDir, "codeCoverage.json");
        String codeCoverageJSON = FileUtils.readFileToString(codeCoverageFile, Charset.defaultCharset());
        Map<String, List<XcodeCodeCoverageMetadata>> coverageDataPerFile = objectMapper.readValue(codeCoverageJSON, new TypeReference<Map<java.lang.String,java.util.List<fr.insideapp.sonarqube.apple.xcode.coverage.models.XcodeCodeCoverageMetadata>>>() {});
        // test
        final List<XcodeCodeCoverage> coverageData = new ArrayList<>(mapper.map(coverageDataPerFile));
        // assert
        assertThat(coverageData).hasSize(1);
        assertThat(coverageData.get(0).filePath).isEqualTo("file.swift");
        assertThat(coverageData.get(0).coverageMetadata).hasSize(4);
    }

}
