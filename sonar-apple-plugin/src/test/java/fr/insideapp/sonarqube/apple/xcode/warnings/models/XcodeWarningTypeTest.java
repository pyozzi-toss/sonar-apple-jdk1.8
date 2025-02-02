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
package fr.insideapp.sonarqube.apple.xcode.warnings.models;

import fr.insideapp.sonarqube.apple.xcode.warnings.parser.XcodeWarningParser;
import fr.insideapp.sonarqube.apple.xcode.warnings.parser.models.WarningSummary;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.insideapp.sonarqube.apple.xcode.warnings.models.XcodeWarningType.*;
import static org.assertj.core.api.Assertions.assertThat;

public final class XcodeWarningTypeTest {

    private static Map<String, XcodeWarningType> data;

    @Before
    public void prepare() {
        data = new HashMap<>();
        data.put("Swift Compiler Warning", SWIFT_COMPILER);

        data.put("Format String Issue", CLANG_COMPILER);
        data.put("Semantic Issue", CLANG_COMPILER);
                data.put("Value Conversion Issue", CLANG_COMPILER);
                data.put("Lexical or Preprocessor Issue", CLANG_COMPILER);
                data.put("Nullability Issue", CLANG_COMPILER);
                data.put("Deprecations", DEPRECATION);
                data.put("Deprecation", DEPRECATION);
                data.put("No-usage", UNUSED);
                data.put("Warning", PROJECT);
                data.put("Target Integrity", PROJECT);
                data.put("dummy random data", NOTE);
    }


    @Test
    public void build() {
        data.forEach( (rawValue, expected) -> {
            // test
            final XcodeWarningType type = XcodeWarningType.builder(rawValue);
            // assert
            assertThat(type).isEqualTo(expected);
        });
    }
    @Test
    public void count() {
        // test
        final XcodeWarningType[] types = XcodeWarningType.values();
        // assert
        assertThat(types).hasSize(6);
    }

}
