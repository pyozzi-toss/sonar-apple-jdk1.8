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
package fr.insideapp.sonarqube.swift.issues;

import fr.insideapp.sonarqube.apple.commons.rules.JSONRulesDefinition;
import fr.insideapp.sonarqube.apple.commons.rules.ProfilesDefinition;
import fr.insideapp.sonarqube.apple.commons.rules.RepositoryRuleParsable;
import fr.insideapp.sonarqube.swift.Swift;
import fr.insideapp.sonarqube.swift.issues.mobsfscan.MobSFScanSwiftRulesDefinition;
import fr.insideapp.sonarqube.swift.issues.periphery.PeripheryRulesDefinition;
import fr.insideapp.sonarqube.swift.issues.swiftlint.SwiftLintRulesDefinition;
import fr.insideapp.sonarqube.swift.issues.warnings.XcodeWarningSwiftRulesDefinition;

import java.util.ArrayList;
import java.util.List;

public class SwiftProfile extends ProfilesDefinition {

    public SwiftProfile(
            final Swift swift,
            final RepositoryRuleParsable parser,
            final SwiftLintRulesDefinition swiftLintRulesDefinition,
            final MobSFScanSwiftRulesDefinition mobSFScanSwiftRulesDefinition,
            final PeripheryRulesDefinition peripheryRulesDefinition,
            final XcodeWarningSwiftRulesDefinition xcodeWarningSwiftRulesDefinition
    ) {
        super(swift, parser, createRulesDefinitions(swiftLintRulesDefinition, mobSFScanSwiftRulesDefinition, peripheryRulesDefinition, xcodeWarningSwiftRulesDefinition));
    }

    private static List<JSONRulesDefinition> createRulesDefinitions(
            SwiftLintRulesDefinition swiftLintRulesDefinition,
            MobSFScanSwiftRulesDefinition mobSFScanSwiftRulesDefinition,
            PeripheryRulesDefinition peripheryRulesDefinition,
            XcodeWarningSwiftRulesDefinition xcodeWarningSwiftRulesDefinition
    ) {
        List<JSONRulesDefinition> rulesDefinitions = new ArrayList<>();
        rulesDefinitions.add(swiftLintRulesDefinition);
        rulesDefinitions.add(mobSFScanSwiftRulesDefinition);
        rulesDefinitions.add(peripheryRulesDefinition);
        rulesDefinitions.add(xcodeWarningSwiftRulesDefinition);
        return rulesDefinitions;
    }
}
