package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.AnalyzerTestSamples.*;
import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContentFragmentTestSamples.*;
import static com.oconeco.courageous.domain.ContentTestSamples.*;
import static com.oconeco.courageous.domain.ContextTestSamples.*;
import static com.oconeco.courageous.domain.OrganizationTestSamples.*;
import static com.oconeco.courageous.domain.SearchConfigurationTestSamples.*;
import static com.oconeco.courageous.domain.SearchResultTestSamples.*;
import static com.oconeco.courageous.domain.SearchTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static com.oconeco.courageous.domain.ThingTypeTestSamples.*;
import static com.oconeco.courageous.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void commentTest() {
        Tag tag = getTagRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        tag.addComment(commentBack);
        assertThat(tag.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getTag()).isEqualTo(tag);

        tag.removeComment(commentBack);
        assertThat(tag.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getTag()).isNull();

        tag.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(tag.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getTag()).isEqualTo(tag);

        tag.setComments(new HashSet<>());
        assertThat(tag.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getTag()).isNull();
    }

    @Test
    void analyzerTest() {
        Tag tag = getTagRandomSampleGenerator();
        Analyzer analyzerBack = getAnalyzerRandomSampleGenerator();

        tag.setAnalyzer(analyzerBack);
        assertThat(tag.getAnalyzer()).isEqualTo(analyzerBack);

        tag.analyzer(null);
        assertThat(tag.getAnalyzer()).isNull();
    }

    @Test
    void contentTest() {
        Tag tag = getTagRandomSampleGenerator();
        Content contentBack = getContentRandomSampleGenerator();

        tag.setContent(contentBack);
        assertThat(tag.getContent()).isEqualTo(contentBack);

        tag.content(null);
        assertThat(tag.getContent()).isNull();
    }

    @Test
    void contentFragmentTest() {
        Tag tag = getTagRandomSampleGenerator();
        ContentFragment contentFragmentBack = getContentFragmentRandomSampleGenerator();

        tag.setContentFragment(contentFragmentBack);
        assertThat(tag.getContentFragment()).isEqualTo(contentFragmentBack);

        tag.contentFragment(null);
        assertThat(tag.getContentFragment()).isNull();
    }

    @Test
    void contextTest() {
        Tag tag = getTagRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        tag.setContext(contextBack);
        assertThat(tag.getContext()).isEqualTo(contextBack);

        tag.context(null);
        assertThat(tag.getContext()).isNull();
    }

    @Test
    void organizationTest() {
        Tag tag = getTagRandomSampleGenerator();
        Organization organizationBack = getOrganizationRandomSampleGenerator();

        tag.setOrganization(organizationBack);
        assertThat(tag.getOrganization()).isEqualTo(organizationBack);

        tag.organization(null);
        assertThat(tag.getOrganization()).isNull();
    }

    @Test
    void searchConfigurationTest() {
        Tag tag = getTagRandomSampleGenerator();
        SearchConfiguration searchConfigurationBack = getSearchConfigurationRandomSampleGenerator();

        tag.setSearchConfiguration(searchConfigurationBack);
        assertThat(tag.getSearchConfiguration()).isEqualTo(searchConfigurationBack);

        tag.searchConfiguration(null);
        assertThat(tag.getSearchConfiguration()).isNull();
    }

    @Test
    void searchResultTest() {
        Tag tag = getTagRandomSampleGenerator();
        SearchResult searchResultBack = getSearchResultRandomSampleGenerator();

        tag.setSearchResult(searchResultBack);
        assertThat(tag.getSearchResult()).isEqualTo(searchResultBack);

        tag.searchResult(null);
        assertThat(tag.getSearchResult()).isNull();
    }

    @Test
    void thingTypeTest() {
        Tag tag = getTagRandomSampleGenerator();
        ThingType thingTypeBack = getThingTypeRandomSampleGenerator();

        tag.setThingType(thingTypeBack);
        assertThat(tag.getThingType()).isEqualTo(thingTypeBack);

        tag.thingType(null);
        assertThat(tag.getThingType()).isNull();
    }

    @Test
    void topicTest() {
        Tag tag = getTagRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        tag.setTopic(topicBack);
        assertThat(tag.getTopic()).isEqualTo(topicBack);

        tag.topic(null);
        assertThat(tag.getTopic()).isNull();
    }

    @Test
    void searchesTest() {
        Tag tag = getTagRandomSampleGenerator();
        Search searchBack = getSearchRandomSampleGenerator();

        tag.addSearches(searchBack);
        assertThat(tag.getSearches()).containsOnly(searchBack);
        assertThat(searchBack.getTags()).containsOnly(tag);

        tag.removeSearches(searchBack);
        assertThat(tag.getSearches()).doesNotContain(searchBack);
        assertThat(searchBack.getTags()).doesNotContain(tag);

        tag.searches(new HashSet<>(Set.of(searchBack)));
        assertThat(tag.getSearches()).containsOnly(searchBack);
        assertThat(searchBack.getTags()).containsOnly(tag);

        tag.setSearches(new HashSet<>());
        assertThat(tag.getSearches()).doesNotContain(searchBack);
        assertThat(searchBack.getTags()).doesNotContain(tag);
    }
}
