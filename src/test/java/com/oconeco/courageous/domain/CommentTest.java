package com.oconeco.courageous.domain;

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
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = getCommentSample1();
        Comment comment2 = new Comment();
        assertThat(comment1).isNotEqualTo(comment2);

        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);

        comment2 = getCommentSample2();
        assertThat(comment1).isNotEqualTo(comment2);
    }

    @Test
    void contentTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Content contentBack = getContentRandomSampleGenerator();

        comment.setContent(contentBack);
        assertThat(comment.getContent()).isEqualTo(contentBack);

        comment.content(null);
        assertThat(comment.getContent()).isNull();
    }

    @Test
    void contentFragmentTest() {
        Comment comment = getCommentRandomSampleGenerator();
        ContentFragment contentFragmentBack = getContentFragmentRandomSampleGenerator();

        comment.setContentFragment(contentFragmentBack);
        assertThat(comment.getContentFragment()).isEqualTo(contentFragmentBack);

        comment.contentFragment(null);
        assertThat(comment.getContentFragment()).isNull();
    }

    @Test
    void contextTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        comment.setContext(contextBack);
        assertThat(comment.getContext()).isEqualTo(contextBack);

        comment.context(null);
        assertThat(comment.getContext()).isNull();
    }

    @Test
    void organizationTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Organization organizationBack = getOrganizationRandomSampleGenerator();

        comment.setOrganization(organizationBack);
        assertThat(comment.getOrganization()).isEqualTo(organizationBack);

        comment.organization(null);
        assertThat(comment.getOrganization()).isNull();
    }

    @Test
    void searchTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Search searchBack = getSearchRandomSampleGenerator();

        comment.setSearch(searchBack);
        assertThat(comment.getSearch()).isEqualTo(searchBack);

        comment.search(null);
        assertThat(comment.getSearch()).isNull();
    }

    @Test
    void searchConfigurationTest() {
        Comment comment = getCommentRandomSampleGenerator();
        SearchConfiguration searchConfigurationBack = getSearchConfigurationRandomSampleGenerator();

        comment.setSearchConfiguration(searchConfigurationBack);
        assertThat(comment.getSearchConfiguration()).isEqualTo(searchConfigurationBack);

        comment.searchConfiguration(null);
        assertThat(comment.getSearchConfiguration()).isNull();
    }

    @Test
    void searchResultTest() {
        Comment comment = getCommentRandomSampleGenerator();
        SearchResult searchResultBack = getSearchResultRandomSampleGenerator();

        comment.setSearchResult(searchResultBack);
        assertThat(comment.getSearchResult()).isEqualTo(searchResultBack);

        comment.searchResult(null);
        assertThat(comment.getSearchResult()).isNull();
    }

    @Test
    void tagTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        comment.setTag(tagBack);
        assertThat(comment.getTag()).isEqualTo(tagBack);

        comment.tag(null);
        assertThat(comment.getTag()).isNull();
    }

    @Test
    void thingTypeTest() {
        Comment comment = getCommentRandomSampleGenerator();
        ThingType thingTypeBack = getThingTypeRandomSampleGenerator();

        comment.setThingType(thingTypeBack);
        assertThat(comment.getThingType()).isEqualTo(thingTypeBack);

        comment.thingType(null);
        assertThat(comment.getThingType()).isNull();
    }

    @Test
    void topicTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        comment.setTopic(topicBack);
        assertThat(comment.getTopic()).isEqualTo(topicBack);

        comment.topic(null);
        assertThat(comment.getTopic()).isNull();
    }
}
