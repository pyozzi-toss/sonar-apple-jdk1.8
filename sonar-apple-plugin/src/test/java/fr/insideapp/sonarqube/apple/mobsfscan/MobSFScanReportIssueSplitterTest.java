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
package fr.insideapp.sonarqube.apple.mobsfscan;

import fr.insideapp.sonarqube.apple.commons.rules.MobSFScanRulesDefinition;
import fr.insideapp.sonarqube.apple.commons.issues.ReportIssue;
import fr.insideapp.sonarqube.apple.mobsfscan.splitter.MobSFScanReportIssueSplitter;
import fr.insideapp.sonarqube.objc.ObjectiveC;
import fr.insideapp.sonarqube.swift.Swift;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.resources.Language;
import org.sonar.api.rule.RuleKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public final class MobSFScanReportIssueSplitterTest {

    private MobSFScanReportIssueSplitter splitter;

    private ActiveRules activeRules;

    private MobSFScanRulesDefinition swiftRulesDefinition;
    private MobSFScanRulesDefinition objcRulesDefinition;

    private Language swift;
    private Language objc;


    @Before
    public void prepare() {
        swift = new Swift();
        objc = new ObjectiveC();
        swiftRulesDefinition = new MobSFScanRulesDefinition(swift) {};
        objcRulesDefinition = new MobSFScanRulesDefinition(objc) {};
        ActiveRulesBuilder builder = new ActiveRulesBuilder();
        Stream.of(swiftRulesDefinition, objcRulesDefinition)
                .map(rulesDefinition -> new NewActiveRule.Builder()
                        .setRuleKey(RuleKey.of(rulesDefinition.getRepositoryKey(), buildRule(rulesDefinition.getLanguage())))
                        .setLanguage(rulesDefinition.getLanguage().getKey())
                        .build()
                )
                .forEach(builder::addRule);
        activeRules = builder.build();
        splitter = new MobSFScanReportIssueSplitter(Arrays.asList(swiftRulesDefinition, objcRulesDefinition));
    }

    private static String buildRule(Language language) {
        return "RULE" + language.getKey();
    }

    @Test
    public void no_issue() {
        // prepare
        // test
        Map<MobSFScanRulesDefinition, List<ReportIssue>> issuesSplit = splitter.split(new ArrayList<>(), activeRules);
        // assert
        assertThat(issuesSplit).hasSize(2);
        assertThat(issuesSplit.get(swiftRulesDefinition)).isEmpty();
        assertThat(issuesSplit.get(objcRulesDefinition)).isEmpty();
    }

    @Test
    public void one_issue_no_file_path() {
        // prepare
        ReportIssue reportIssue = new ReportIssue(buildRule(swiftRulesDefinition.getLanguage()), "message", null, 15);
        // test
        Map<MobSFScanRulesDefinition, List<ReportIssue>> issuesSplit = splitter.split(Arrays.asList(reportIssue), activeRules);
        // assert
        assertThat(issuesSplit).hasSize(2);
        assertThat(issuesSplit.get(swiftRulesDefinition)).hasSize(1);
        assertThat(issuesSplit.get(objcRulesDefinition)).isEmpty();
    }

    @Test
    public void one_issue_file_path_wrong_extension() {
        // prepare
        ReportIssue reportIssue = new ReportIssue(buildRule(swiftRulesDefinition.getLanguage()), "message", "path/to/file.ext", 15);
        // test
        Map<MobSFScanRulesDefinition, List<ReportIssue>> issuesSplit = splitter.split(Arrays.asList(reportIssue), activeRules);
        // assert
        assertThat(issuesSplit).hasSize(2);
        assertThat(issuesSplit.get(swiftRulesDefinition)).isEmpty();
        assertThat(issuesSplit.get(objcRulesDefinition)).isEmpty();
    }

    @Test
    public void one_issue_file_path_valid_extension() {
        // prepare
        ReportIssue reportIssue = new ReportIssue(buildRule(swiftRulesDefinition.getLanguage()), "message", "path/to/file.swift", 15);
        // test
        Map<MobSFScanRulesDefinition, List<ReportIssue>> issuesSplit = splitter.split(Arrays.asList(reportIssue), activeRules);
        // assert
        assertThat(issuesSplit).hasSize(2);
        assertThat(issuesSplit.get(swiftRulesDefinition)).hasSize(1);
        assertThat(issuesSplit.get(objcRulesDefinition)).isEmpty();
    }

    @Test
    public void two_issue_file_path_valid_extension() {
        // prepare
        ReportIssue reportIssue1 = new ReportIssue(buildRule(swiftRulesDefinition.getLanguage()), "message", "path/to/file.swift", 15);
        ReportIssue reportIssue2 = new ReportIssue(buildRule(objcRulesDefinition.getLanguage()), "message", "path/to/file.m", 15);
        // test
        Map<MobSFScanRulesDefinition, List<ReportIssue>> issuesSplit = splitter.split(Arrays.asList(reportIssue1, reportIssue2), activeRules);
        // assert
        assertThat(issuesSplit).hasSize(2);
        assertThat(issuesSplit.get(swiftRulesDefinition)).hasSize(1);
        assertThat(issuesSplit.get(objcRulesDefinition)).hasSize(1);
    }

}
