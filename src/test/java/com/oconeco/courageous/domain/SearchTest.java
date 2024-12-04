package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContextTestSamples.*;
import static com.oconeco.courageous.domain.SearchConfigurationTestSamples.*;
import static com.oconeco.courageous.domain.SearchResultTestSamples.*;
import static com.oconeco.courageous.domain.SearchTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static com.oconeco.courageous.domain.ThingTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SearchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Search.class);
        Search search1 = getSearchSample1();
        Search search2 = new Search();
        assertThat(search1).isNotEqualTo(search2);

        search2.setId(search1.getId());
        assertThat(search1).isEqualTo(search2);

        search2 = getSearchSample2();
        assertThat(search1).isNotEqualTo(search2);
    }

    @Test
    void searchResultTest() {
        Search search = getSearchRandomSampleGenerator();
        SearchResult searchResultBack = getSearchResultRandomSampleGenerator();

        search.addSearchResult(searchResultBack);
        assertThat(search.getSearchResults()).containsOnly(searchResultBack);
        assertThat(searchResultBack.getSearch()).isEqualTo(search);

        search.removeSearchResult(searchResultBack);
        assertThat(search.getSearchResults()).doesNotContain(searchResultBack);
        assertThat(searchResultBack.getSearch()).isNull();

        search.searchResults(new HashSet<>(Set.of(searchResultBack)));
        assertThat(search.getSearchResults()).containsOnly(searchResultBack);
        assertThat(searchResultBack.getSearch()).isEqualTo(search);

        search.setSearchResults(new HashSet<>());
        assertThat(search.getSearchResults()).doesNotContain(searchResultBack);
        assertThat(searchResultBack.getSearch()).isNull();
    }

    @Test
    void tagsTest() {
        Search search = getSearchRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        search.addTags(tagBack);
        assertThat(search.getTags()).containsOnly(tagBack);

        search.removeTags(tagBack);
        assertThat(search.getTags()).doesNotContain(tagBack);

        search.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(search.getTags()).containsOnly(tagBack);

        search.setTags(new HashSet<>());
        assertThat(search.getTags()).doesNotContain(tagBack);
    }

    @Test
    void commentTest() {
        Search search = getSearchRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        search.addComment(commentBack);
        assertThat(search.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getSearch()).isEqualTo(search);

        search.removeComment(commentBack);
        assertThat(search.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getSearch()).isNull();

        search.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(search.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getSearch()).isEqualTo(search);

        search.setComments(new HashSet<>());
        assertThat(search.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getSearch()).isNull();
    }

    @Test
    void configurationsTest() {
        Search search = getSearchRandomSampleGenerator();
        SearchConfiguration searchConfigurationBack = getSearchConfigurationRandomSampleGenerator();

        search.addConfigurations(searchConfigurationBack);
        assertThat(search.getConfigurations()).containsOnly(searchConfigurationBack);

        search.removeConfigurations(searchConfigurationBack);
        assertThat(search.getConfigurations()).doesNotContain(searchConfigurationBack);

        search.configurations(new HashSet<>(Set.of(searchConfigurationBack)));
        assertThat(search.getConfigurations()).containsOnly(searchConfigurationBack);

        search.setConfigurations(new HashSet<>());
        assertThat(search.getConfigurations()).doesNotContain(searchConfigurationBack);
    }

    @Test
    void contextTest() {
        Search search = getSearchRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        search.setContext(contextBack);
        assertThat(search.getContext()).isEqualTo(contextBack);

        search.context(null);
        assertThat(search.getContext()).isNull();
    }

    @Test
    void typeTest() {
        Search search = getSearchRandomSampleGenerator();
        ThingType thingTypeBack = getThingTypeRandomSampleGenerator();

        search.setType(thingTypeBack);
        assertThat(search.getType()).isEqualTo(thingTypeBack);

        search.type(null);
        assertThat(search.getType()).isNull();
    }
}
