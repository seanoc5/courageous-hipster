package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.AnalyzerTestSamples.*;
import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.SearchConfigurationTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SearchConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchConfiguration.class);
        SearchConfiguration searchConfiguration1 = getSearchConfigurationSample1();
        SearchConfiguration searchConfiguration2 = new SearchConfiguration();
        assertThat(searchConfiguration1).isNotEqualTo(searchConfiguration2);

        searchConfiguration2.setId(searchConfiguration1.getId());
        assertThat(searchConfiguration1).isEqualTo(searchConfiguration2);

        searchConfiguration2 = getSearchConfigurationSample2();
        assertThat(searchConfiguration1).isNotEqualTo(searchConfiguration2);
    }

    @Test
    void tagTest() {
        SearchConfiguration searchConfiguration = getSearchConfigurationRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        searchConfiguration.addTag(tagBack);
        assertThat(searchConfiguration.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getSearchConfiguration()).isEqualTo(searchConfiguration);

        searchConfiguration.removeTag(tagBack);
        assertThat(searchConfiguration.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getSearchConfiguration()).isNull();

        searchConfiguration.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(searchConfiguration.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getSearchConfiguration()).isEqualTo(searchConfiguration);

        searchConfiguration.setTags(new HashSet<>());
        assertThat(searchConfiguration.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getSearchConfiguration()).isNull();
    }

    @Test
    void commentTest() {
        SearchConfiguration searchConfiguration = getSearchConfigurationRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        searchConfiguration.addComment(commentBack);
        assertThat(searchConfiguration.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getSearchConfiguration()).isEqualTo(searchConfiguration);

        searchConfiguration.removeComment(commentBack);
        assertThat(searchConfiguration.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getSearchConfiguration()).isNull();

        searchConfiguration.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(searchConfiguration.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getSearchConfiguration()).isEqualTo(searchConfiguration);

        searchConfiguration.setComments(new HashSet<>());
        assertThat(searchConfiguration.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getSearchConfiguration()).isNull();
    }

    @Test
    void analyzerTest() {
        SearchConfiguration searchConfiguration = getSearchConfigurationRandomSampleGenerator();
        Analyzer analyzerBack = getAnalyzerRandomSampleGenerator();

        searchConfiguration.addAnalyzer(analyzerBack);
        assertThat(searchConfiguration.getAnalyzers()).containsOnly(analyzerBack);
        assertThat(analyzerBack.getSearchConfiguration()).isEqualTo(searchConfiguration);

        searchConfiguration.removeAnalyzer(analyzerBack);
        assertThat(searchConfiguration.getAnalyzers()).doesNotContain(analyzerBack);
        assertThat(analyzerBack.getSearchConfiguration()).isNull();

        searchConfiguration.analyzers(new HashSet<>(Set.of(analyzerBack)));
        assertThat(searchConfiguration.getAnalyzers()).containsOnly(analyzerBack);
        assertThat(analyzerBack.getSearchConfiguration()).isEqualTo(searchConfiguration);

        searchConfiguration.setAnalyzers(new HashSet<>());
        assertThat(searchConfiguration.getAnalyzers()).doesNotContain(analyzerBack);
        assertThat(analyzerBack.getSearchConfiguration()).isNull();
    }
}
