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
package fr.insideapp.sonarqube.apple;

import fr.insideapp.sonarqube.apple.commons.ApplePluginExtensionProvider;
import fr.insideapp.sonarqube.apple.commons.ExtensionProvider;
import fr.insideapp.sonarqube.apple.commons.SonarProjectConfiguration;
import fr.insideapp.sonarqube.apple.commons.issues.ReportIssueRecorder;
import fr.insideapp.sonarqube.apple.commons.rules.RepositoryRuleParser;
import fr.insideapp.sonarqube.apple.mobsfscan.MobSFScanExtensionProvider;
import fr.insideapp.sonarqube.apple.xcode.coverage.XcodeCoverageExtensionProvider;
import fr.insideapp.sonarqube.apple.xcode.tests.XcodeTestsExtensionProvider;
import fr.insideapp.sonarqube.apple.xcode.warnings.XcodeWarningExtensionProvider;
import fr.insideapp.sonarqube.objc.issues.oclint.*;
import fr.insideapp.sonarqube.objc.ObjectiveCExtensionProvider;
import fr.insideapp.sonarqube.swift.SwiftExtensionProvider;
import fr.insideapp.sonarqube.swift.issues.periphery.PeripheryExtensionProvider;
import fr.insideapp.sonarqube.swift.issues.swiftlint.SwiftLintExtensionProvider;
import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class ApplePlugin implements Plugin {

    private static final Logger LOGGER = Loggers.get(ApplePlugin.class);

    @Override
    public void define(Context context) {

        // Project
        context.addExtension(SonarProjectConfiguration.class);

        // Rules
        context.addExtension(RepositoryRuleParser.class);

        // Issues reporter
        context.addExtension(ReportIssueRecorder.class);

        register(context,
                SwiftExtensionProvider.class, // Swift language
                ObjectiveCExtensionProvider.class, // Objective-C language
                ApplePluginExtensionProvider.class,
                XcodeResultExtensionProvider.class, // Xcode
                XcodeTestsExtensionProvider.class, // Tests
                XcodeCoverageExtensionProvider.class, // Coverage
                XcodeWarningExtensionProvider.class, // Warnings
                SwiftLintExtensionProvider.class, // SwiftLint
                PeripheryExtensionProvider.class, // Periphery
                MobSFScanExtensionProvider.class, // MobSFScan
                OCLintExtensionProvider.class  // OCLint
        );

    }

    @SafeVarargs
    private final void register(Context context, Class<? extends ExtensionProvider>... providersClazz) {
        for (Class<? extends ExtensionProvider> providerClazz : providersClazz) {
            try {
                // registering provider
                context.addExtension(providerClazz);
                // registering extensions
                ExtensionProvider provider = providerClazz.getDeclaredConstructor().newInstance();
                context.addExtensions(provider.extensions());
            } catch (Exception e) {
                LOGGER.info("An error occurred when trying to register '{}'", providerClazz.getCanonicalName());
                LOGGER.debug("Exception: {}", e);
            }
        }
    }
}