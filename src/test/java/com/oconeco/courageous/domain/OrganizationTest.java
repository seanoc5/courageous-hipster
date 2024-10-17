package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContextTestSamples.*;
import static com.oconeco.courageous.domain.OrganizationTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = getOrganizationSample1();
        Organization organization2 = new Organization();
        assertThat(organization1).isNotEqualTo(organization2);

        organization2.setId(organization1.getId());
        assertThat(organization1).isEqualTo(organization2);

        organization2 = getOrganizationSample2();
        assertThat(organization1).isNotEqualTo(organization2);
    }

    @Test
    void contextTest() {
        Organization organization = getOrganizationRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        organization.addContext(contextBack);
        assertThat(organization.getContexts()).containsOnly(contextBack);
        assertThat(contextBack.getOrganization()).isEqualTo(organization);

        organization.removeContext(contextBack);
        assertThat(organization.getContexts()).doesNotContain(contextBack);
        assertThat(contextBack.getOrganization()).isNull();

        organization.contexts(new HashSet<>(Set.of(contextBack)));
        assertThat(organization.getContexts()).containsOnly(contextBack);
        assertThat(contextBack.getOrganization()).isEqualTo(organization);

        organization.setContexts(new HashSet<>());
        assertThat(organization.getContexts()).doesNotContain(contextBack);
        assertThat(contextBack.getOrganization()).isNull();
    }

    @Test
    void tagTest() {
        Organization organization = getOrganizationRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        organization.addTag(tagBack);
        assertThat(organization.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getOrganization()).isEqualTo(organization);

        organization.removeTag(tagBack);
        assertThat(organization.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getOrganization()).isNull();

        organization.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(organization.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getOrganization()).isEqualTo(organization);

        organization.setTags(new HashSet<>());
        assertThat(organization.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getOrganization()).isNull();
    }

    @Test
    void commentTest() {
        Organization organization = getOrganizationRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        organization.addComment(commentBack);
        assertThat(organization.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getOrganization()).isEqualTo(organization);

        organization.removeComment(commentBack);
        assertThat(organization.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getOrganization()).isNull();

        organization.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(organization.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getOrganization()).isEqualTo(organization);

        organization.setComments(new HashSet<>());
        assertThat(organization.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getOrganization()).isNull();
    }
}
