package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.AnalyzerTestSamples.*;
import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContentTestSamples.*;
import static com.oconeco.courageous.domain.SearchConfigurationTestSamples.*;
import static com.oconeco.courageous.domain.SearchResultTestSamples.*;
import static com.oconeco.courageous.domain.SearchTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SearchResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchResult.class);
        SearchResult searchResult1 = getSearchResultSample1();
        SearchResult searchResult2 = new SearchResult();
        assertThat(searchResult1).isNotEqualTo(searchResult2);

        searchResult2.setId(searchResult1.getId());
        assertThat(searchResult1).isEqualTo(searchResult2);

        searchResult2 = getSearchResultSample2();
        assertThat(searchResult1).isNotEqualTo(searchResult2);
    }

    @Test
    void contentTest() {
        SearchResult searchResult = getSearchResultRandomSampleGenerator();
        Content contentBack = getContentRandomSampleGenerator();

        searchResult.addContent(contentBack);
        assertThat(searchResult.getContents()).containsOnly(contentBack);
        assertThat(contentBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.removeContent(contentBack);
        assertThat(searchResult.getContents()).doesNotContain(contentBack);
        assertThat(contentBack.getSearchResult()).isNull();

        searchResult.contents(new HashSet<>(Set.of(contentBack)));
        assertThat(searchResult.getContents()).containsOnly(contentBack);
        assertThat(contentBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.setContents(new HashSet<>());
        assertThat(searchResult.getContents()).doesNotContain(contentBack);
        assertThat(contentBack.getSearchResult()).isNull();
    }

    @Test
    void tagTest() {
        SearchResult searchResult = getSearchResultRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        searchResult.addTag(tagBack);
        assertThat(searchResult.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.removeTag(tagBack);
        assertThat(searchResult.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getSearchResult()).isNull();

        searchResult.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(searchResult.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.setTags(new HashSet<>());
        assertThat(searchResult.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getSearchResult()).isNull();
    }

    @Test
    void commentTest() {
        SearchResult searchResult = getSearchResultRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        searchResult.addComment(commentBack);
        assertThat(searchResult.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.removeComment(commentBack);
        assertThat(searchResult.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getSearchResult()).isNull();

        searchResult.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(searchResult.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.setComments(new HashSet<>());
        assertThat(searchResult.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getSearchResult()).isNull();
    }

    @Test
    void analyzerTest() {
        SearchResult searchResult = getSearchResultRandomSampleGenerator();
        Analyzer analyzerBack = getAnalyzerRandomSampleGenerator();

        searchResult.addAnalyzer(analyzerBack);
        assertThat(searchResult.getAnalyzers()).containsOnly(analyzerBack);
        assertThat(analyzerBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.removeAnalyzer(analyzerBack);
        assertThat(searchResult.getAnalyzers()).doesNotContain(analyzerBack);
        assertThat(analyzerBack.getSearchResult()).isNull();

        searchResult.analyzers(new HashSet<>(Set.of(analyzerBack)));
        assertThat(searchResult.getAnalyzers()).containsOnly(analyzerBack);
        assertThat(analyzerBack.getSearchResult()).isEqualTo(searchResult);

        searchResult.setAnalyzers(new HashSet<>());
        assertThat(searchResult.getAnalyzers()).doesNotContain(analyzerBack);
        assertThat(analyzerBack.getSearchResult()).isNull();
    }

    @Test
    void configTest() {
        SearchResult searchResult = getSearchResultRandomSampleGenerator();
        SearchConfiguration searchConfigurationBack = getSearchConfigurationRandomSampleGenerator();

        searchResult.setConfig(searchConfigurationBack);
        assertThat(searchResult.getConfig()).isEqualTo(searchConfigurationBack);

        searchResult.config(null);
        assertThat(searchResult.getConfig()).isNull();
    }

    @Test
    void searchTest() {
        SearchResult searchResult = getSearchResultRandomSampleGenerator();
        Search searchBack = getSearchRandomSampleGenerator();

        searchResult.setSearch(searchBack);
        assertThat(searchResult.getSearch()).isEqualTo(searchBack);

        searchResult.search(null);
        assertThat(searchResult.getSearch()).isNull();
    }
}
