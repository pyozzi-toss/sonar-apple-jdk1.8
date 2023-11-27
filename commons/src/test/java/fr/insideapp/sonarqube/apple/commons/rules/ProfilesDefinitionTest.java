package fr.insideapp.sonarqube.apple.commons.rules;

import fr.insideapp.sonarqube.apple.commons.ExceptionHelper;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Language;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ProfilesDefinitionTest {

    private static String LANGUAGE = "language";
    private static String REPOSITORY = "repository";

    private Language language;
    private RepositoryRuleParsable parser;
    private List<JSONRulesDefinition> rulesDefinitions;
    private ProfilesDefinition profilesDefinition;
    private BuiltInQualityProfilesDefinition.Context context;

    @Before
    public void prepare() {
        // objects
        language = mock(Language.class);
        parser = mock(RepositoryRuleParsable.class);
        rulesDefinitions = new ArrayList<>();
        profilesDefinition = new ProfilesDefinition(language, parser, rulesDefinitions) {};
        context = new BuiltInQualityProfilesDefinition.Context();
        // mocks
        when(language.getKey()).thenReturn(LANGUAGE);
        when(language.getName()).thenReturn(LANGUAGE);
    }

    @Test
    public void define_noJSONDefinition_empty() {
        // test
        profilesDefinition.define(context);
        // assert
        final BuiltInQualityProfilesDefinition.BuiltInQualityProfile builtProfile = context.profile(LANGUAGE, LANGUAGE);
        assertThat(builtProfile).isNotNull();
        assertThat(builtProfile.language()).isEqualTo(LANGUAGE);
        assertThat(builtProfile.name()).isEqualTo(LANGUAGE);
        assertThat(builtProfile.rules()).isEmpty();
    }

    @Test
    public void define_parserFailed_empty() throws Exception {
        // mock
        JSONRulesDefinition jsonRulesDefinition = mock(JSONRulesDefinition.class);
        when(jsonRulesDefinition.getRepositoryName()).thenReturn(REPOSITORY);
        when(jsonRulesDefinition.getJsonResourcePath()).thenReturn("/dummy/path/to/file");
        rulesDefinitions.add(jsonRulesDefinition);
        when(parser.parse(anyString())).thenThrow(ExceptionHelper.buildIO());
        // test
        profilesDefinition.define(context);
        // assert
        final BuiltInQualityProfilesDefinition.BuiltInQualityProfile builtProfile = context.profile(LANGUAGE, LANGUAGE);
        assertThat(builtProfile).isNotNull();
        assertThat(builtProfile.language()).isEqualTo(LANGUAGE);
        assertThat(builtProfile.name()).isEqualTo(LANGUAGE);
        assertThat(builtProfile.rules()).isEmpty();
    }

    @Test
    public void define_success() throws Exception {
        // mock
        JSONRulesDefinition jsonRulesDefinition = mock(JSONRulesDefinition.class);
        when(jsonRulesDefinition.getRepositoryName()).thenReturn(REPOSITORY);
        when(jsonRulesDefinition.getJsonResourcePath()).thenReturn("/dummy/path/to/file");
        rulesDefinitions.add(jsonRulesDefinition);
        RepositoryRule rule = new RepositoryRule(
                "rule_key",
                "Name of the Rule",
                RepositoryRule.Severity.MAJOR,
                "This is a description.",
                RepositoryRule.Type.CODE_SMELL,
                "5min"
        );
        ArrayList<RepositoryRule> ruls = new ArrayList<>();
        ruls.add(rule);
        when(parser.parse(anyString())).thenReturn(ruls);
        // test
        profilesDefinition.define(context);
        // assert
        final BuiltInQualityProfilesDefinition.BuiltInQualityProfile builtProfile = context.profile(LANGUAGE, LANGUAGE);
        assertThat(builtProfile).isNotNull();
        assertThat(builtProfile.language()).isEqualTo(LANGUAGE);
        assertThat(builtProfile.name()).isEqualTo(LANGUAGE);
        assertThat(builtProfile.rules()).hasSize(1);
        final BuiltInQualityProfilesDefinition.BuiltInActiveRule builtInActiveRule = builtProfile.rules().get(0);
        assertThat(builtInActiveRule.ruleKey()).isEqualTo(rule.key);
        assertThat(builtInActiveRule.repoKey()).isEqualTo(REPOSITORY);
        assertThat(builtInActiveRule.overriddenSeverity()).isEqualTo(rule.severity.name());
    }
}
