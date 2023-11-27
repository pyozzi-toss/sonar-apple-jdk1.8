package fr.insideapp.sonarqube.apple.commons.parser;

import fr.insideapp.sonarqube.apple.commons.ExceptionHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ReportMapParserTest {

    private ReportMapParser parser;

    @Before
    public void prepare() {
        parser = mock(ReportMapParser.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void parse_failed() throws Exception {
        // prepare
        when(parser.perform(anyString())).thenThrow(ExceptionHelper.build());
        // test
        Map<?, ?> outputs = parser.parse(anyString());
        // assert
        assertThat(outputs).isEmpty();
    }

    @Test
    public void parse_succeed() throws Exception {
        // prepare
        when(parser.perform(anyString())).thenReturn(createMap("key_parsed", "value_parsed"));
        // test
        Map<?, ?> outputs = parser.parse(anyString());
        // assert
        assertThat(outputs).hasSize(1).isEqualTo(createMap("key_parsed", "value_parsed"));
    }

    // Java 1.8에서 Map.of()를 대체하기 위한 메서드
    private <K, V> Map<K, V> createMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
