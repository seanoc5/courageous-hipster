package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.AnalyzerTestSamples.*;
import static com.oconeco.courageous.domain.ContextTestSamples.*;
import static com.oconeco.courageous.domain.SearchConfigurationTestSamples.*;
import static com.oconeco.courageous.domain.SearchResultTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AnalyzerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Analyzer.class);
        Analyzer analyzer1 = getAnalyzerSample1();
        Analyzer analyzer2 = new Analyzer();
        assertThat(analyzer1).isNotEqualTo(analyzer2);

        analyzer2.setId(analyzer1.getId());
        assertThat(analyzer1).isEqualTo(analyzer2);

        analyzer2 = getAnalyzerSample2();
        assertThat(analyzer1).isNotEqualTo(analyzer2);
    }

    @Test
    void tagTest() {
        Analyzer analyzer = getAnalyzerRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        analyzer.addTag(tagBack);
        assertThat(analyzer.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getAnalyzer()).isEqualTo(analyzer);

        analyzer.removeTag(tagBack);
        assertThat(analyzer.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getAnalyzer()).isNull();

        analyzer.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(analyzer.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getAnalyzer()).isEqualTo(analyzer);

        analyzer.setTags(new HashSet<>());
        assertThat(analyzer.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getAnalyzer()).isNull();
    }

    @Test
    void contextTest() {
        Analyzer analyzer = getAnalyzerRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        analyzer.setContext(contextBack);
        assertThat(analyzer.getContext()).isEqualTo(contextBack);

        analyzer.context(null);
        assertThat(analyzer.getContext()).isNull();
    }

    @Test
    void searchConfigurationTest() {
        Analyzer analyzer = getAnalyzerRandomSampleGenerator();
        SearchConfiguration searchConfigurationBack = getSearchConfigurationRandomSampleGenerator();

        analyzer.setSearchConfiguration(searchConfigurationBack);
        assertThat(analyzer.getSearchConfiguration()).isEqualTo(searchConfigurationBack);

        analyzer.searchConfiguration(null);
        assertThat(analyzer.getSearchConfiguration()).isNull();
    }

    @Test
    void searchResultTest() {
        Analyzer analyzer = getAnalyzerRandomSampleGenerator();
        SearchResult searchResultBack = getSearchResultRandomSampleGenerator();

        analyzer.setSearchResult(searchResultBack);
        assertThat(analyzer.getSearchResult()).isEqualTo(searchResultBack);

        analyzer.searchResult(null);
        assertThat(analyzer.getSearchResult()).isNull();
    }
}
