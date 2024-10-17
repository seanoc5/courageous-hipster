package com.oconeco.courageous.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.oconeco.courageous.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.oconeco.courageous.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.oconeco.courageous.domain.User.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Authority.class.getName());
            createCache(cm, com.oconeco.courageous.domain.User.class.getName() + ".authorities");
            createCache(cm, com.oconeco.courageous.domain.Analyzer.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Analyzer.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.Comment.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Content.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Content.class.getName() + ".contentFragments");
            createCache(cm, com.oconeco.courageous.domain.Content.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.Content.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.ContentFragment.class.getName());
            createCache(cm, com.oconeco.courageous.domain.ContentFragment.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.ContentFragment.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.Context.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Context.class.getName() + ".analyzers");
            createCache(cm, com.oconeco.courageous.domain.Context.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.Context.class.getName() + ".contentFragments");
            createCache(cm, com.oconeco.courageous.domain.Context.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.Organization.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Organization.class.getName() + ".contexts");
            createCache(cm, com.oconeco.courageous.domain.Organization.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.Organization.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.Search.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Search.class.getName() + ".searchResults");
            createCache(cm, com.oconeco.courageous.domain.Search.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.Search.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.SearchConfiguration.class.getName());
            createCache(cm, com.oconeco.courageous.domain.SearchConfiguration.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.SearchConfiguration.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.SearchConfiguration.class.getName() + ".analyzers");
            createCache(cm, com.oconeco.courageous.domain.SearchResult.class.getName());
            createCache(cm, com.oconeco.courageous.domain.SearchResult.class.getName() + ".contents");
            createCache(cm, com.oconeco.courageous.domain.SearchResult.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.SearchResult.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.SearchResult.class.getName() + ".analyzers");
            createCache(cm, com.oconeco.courageous.domain.Tag.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Tag.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.ThingType.class.getName());
            createCache(cm, com.oconeco.courageous.domain.ThingType.class.getName() + ".tags");
            createCache(cm, com.oconeco.courageous.domain.ThingType.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.Topic.class.getName());
            createCache(cm, com.oconeco.courageous.domain.Topic.class.getName() + ".comments");
            createCache(cm, com.oconeco.courageous.domain.Topic.class.getName() + ".contentFragments");
            createCache(cm, com.oconeco.courageous.domain.Topic.class.getName() + ".tags");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
