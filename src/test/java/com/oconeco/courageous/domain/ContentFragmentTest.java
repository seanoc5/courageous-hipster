package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContentFragmentTestSamples.*;
import static com.oconeco.courageous.domain.ContentTestSamples.*;
import static com.oconeco.courageous.domain.ContextTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static com.oconeco.courageous.domain.ThingTypeTestSamples.*;
import static com.oconeco.courageous.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContentFragmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentFragment.class);
        ContentFragment contentFragment1 = getContentFragmentSample1();
        ContentFragment contentFragment2 = new ContentFragment();
        assertThat(contentFragment1).isNotEqualTo(contentFragment2);

        contentFragment2.setId(contentFragment1.getId());
        assertThat(contentFragment1).isEqualTo(contentFragment2);

        contentFragment2 = getContentFragmentSample2();
        assertThat(contentFragment1).isNotEqualTo(contentFragment2);
    }

    @Test
    void tagTest() {
        ContentFragment contentFragment = getContentFragmentRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        contentFragment.addTag(tagBack);
        assertThat(contentFragment.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getContentFragment()).isEqualTo(contentFragment);

        contentFragment.removeTag(tagBack);
        assertThat(contentFragment.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getContentFragment()).isNull();

        contentFragment.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(contentFragment.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getContentFragment()).isEqualTo(contentFragment);

        contentFragment.setTags(new HashSet<>());
        assertThat(contentFragment.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getContentFragment()).isNull();
    }

    @Test
    void commentTest() {
        ContentFragment contentFragment = getContentFragmentRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        contentFragment.addComment(commentBack);
        assertThat(contentFragment.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getContentFragment()).isEqualTo(contentFragment);

        contentFragment.removeComment(commentBack);
        assertThat(contentFragment.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getContentFragment()).isNull();

        contentFragment.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(contentFragment.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getContentFragment()).isEqualTo(contentFragment);

        contentFragment.setComments(new HashSet<>());
        assertThat(contentFragment.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getContentFragment()).isNull();
    }

    @Test
    void typeTest() {
        ContentFragment contentFragment = getContentFragmentRandomSampleGenerator();
        ThingType thingTypeBack = getThingTypeRandomSampleGenerator();

        contentFragment.setType(thingTypeBack);
        assertThat(contentFragment.getType()).isEqualTo(thingTypeBack);

        contentFragment.type(null);
        assertThat(contentFragment.getType()).isNull();
    }

    @Test
    void contentTest() {
        ContentFragment contentFragment = getContentFragmentRandomSampleGenerator();
        Content contentBack = getContentRandomSampleGenerator();

        contentFragment.setContent(contentBack);
        assertThat(contentFragment.getContent()).isEqualTo(contentBack);

        contentFragment.content(null);
        assertThat(contentFragment.getContent()).isNull();
    }

    @Test
    void contextTest() {
        ContentFragment contentFragment = getContentFragmentRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        contentFragment.setContext(contextBack);
        assertThat(contentFragment.getContext()).isEqualTo(contextBack);

        contentFragment.context(null);
        assertThat(contentFragment.getContext()).isNull();
    }

    @Test
    void topicTest() {
        ContentFragment contentFragment = getContentFragmentRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        contentFragment.setTopic(topicBack);
        assertThat(contentFragment.getTopic()).isEqualTo(topicBack);

        contentFragment.topic(null);
        assertThat(contentFragment.getTopic()).isNull();
    }
}
