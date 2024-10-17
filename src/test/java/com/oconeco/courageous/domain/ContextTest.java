package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.AnalyzerTestSamples.*;
import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContentFragmentTestSamples.*;
import static com.oconeco.courageous.domain.ContextTestSamples.*;
import static com.oconeco.courageous.domain.OrganizationTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Context.class);
        Context context1 = getContextSample1();
        Context context2 = new Context();
        assertThat(context1).isNotEqualTo(context2);

        context2.setId(context1.getId());
        assertThat(context1).isEqualTo(context2);

        context2 = getContextSample2();
        assertThat(context1).isNotEqualTo(context2);
    }

    @Test
    void analyzerTest() {
        Context context = getContextRandomSampleGenerator();
        Analyzer analyzerBack = getAnalyzerRandomSampleGenerator();

        context.addAnalyzer(analyzerBack);
        assertThat(context.getAnalyzers()).containsOnly(analyzerBack);
        assertThat(analyzerBack.getContext()).isEqualTo(context);

        context.removeAnalyzer(analyzerBack);
        assertThat(context.getAnalyzers()).doesNotContain(analyzerBack);
        assertThat(analyzerBack.getContext()).isNull();

        context.analyzers(new HashSet<>(Set.of(analyzerBack)));
        assertThat(context.getAnalyzers()).containsOnly(analyzerBack);
        assertThat(analyzerBack.getContext()).isEqualTo(context);

        context.setAnalyzers(new HashSet<>());
        assertThat(context.getAnalyzers()).doesNotContain(analyzerBack);
        assertThat(analyzerBack.getContext()).isNull();
    }

    @Test
    void tagTest() {
        Context context = getContextRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        context.addTag(tagBack);
        assertThat(context.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getContext()).isEqualTo(context);

        context.removeTag(tagBack);
        assertThat(context.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getContext()).isNull();

        context.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(context.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getContext()).isEqualTo(context);

        context.setTags(new HashSet<>());
        assertThat(context.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getContext()).isNull();
    }

    @Test
    void contentFragmentTest() {
        Context context = getContextRandomSampleGenerator();
        ContentFragment contentFragmentBack = getContentFragmentRandomSampleGenerator();

        context.addContentFragment(contentFragmentBack);
        assertThat(context.getContentFragments()).containsOnly(contentFragmentBack);
        assertThat(contentFragmentBack.getContext()).isEqualTo(context);

        context.removeContentFragment(contentFragmentBack);
        assertThat(context.getContentFragments()).doesNotContain(contentFragmentBack);
        assertThat(contentFragmentBack.getContext()).isNull();

        context.contentFragments(new HashSet<>(Set.of(contentFragmentBack)));
        assertThat(context.getContentFragments()).containsOnly(contentFragmentBack);
        assertThat(contentFragmentBack.getContext()).isEqualTo(context);

        context.setContentFragments(new HashSet<>());
        assertThat(context.getContentFragments()).doesNotContain(contentFragmentBack);
        assertThat(contentFragmentBack.getContext()).isNull();
    }

    @Test
    void commentTest() {
        Context context = getContextRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        context.addComment(commentBack);
        assertThat(context.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getContext()).isEqualTo(context);

        context.removeComment(commentBack);
        assertThat(context.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getContext()).isNull();

        context.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(context.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getContext()).isEqualTo(context);

        context.setComments(new HashSet<>());
        assertThat(context.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getContext()).isNull();
    }

    @Test
    void organizationTest() {
        Context context = getContextRandomSampleGenerator();
        Organization organizationBack = getOrganizationRandomSampleGenerator();

        context.setOrganization(organizationBack);
        assertThat(context.getOrganization()).isEqualTo(organizationBack);

        context.organization(null);
        assertThat(context.getOrganization()).isNull();
    }
}
