package fr.insideapp.sonarqube.apple.commons.mapper;

import fr.insideapp.sonarqube.apple.commons.ExceptionHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class AbstractReportMapperTest {

    private AbstractReportMapper<String, String> mapper;

    @Before
    public void prepare() {
        mapper = mock(AbstractReportMapper.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void map_failed() throws Exception {
        // prepare
        when(mapper.perform(anyString())).thenThrow(ExceptionHelper.build());
        // test
        Set<?> outputs = mapper.map(anyString());
        // assert
        assertThat(outputs).isEmpty();
    }

    @Test
    public void map_succeed() throws Exception {
        // prepare
        when(mapper.perform(anyString())).thenReturn(createSet("value_mapped"));
        // test
        Set<?> outputs = mapper.map(anyString());
        // assert
        assertThat(outputs).hasSize(1).isEqualTo(createSet("value_mapped"));
    }

    // Java 1.8에서 Set.of()을 대체하기 위한 메서드
    private <T> Set<T> createSet(T... elements) {
        Set<T> set = new HashSet<>();
        for (T element : elements) {
            set.add(element);
        }
        return set;
    }
}
