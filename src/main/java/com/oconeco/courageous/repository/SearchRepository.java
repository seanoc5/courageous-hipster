package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Search entity.
 * When extending this class, extend SearchRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SearchRepository extends SearchRepositoryWithBagRelationships, JpaRepository<Search, Long> {
    @Query("select search from Search search where search.createdBy.login = ?#{authentication.name}")
    List<Search> findByCreatedByIsCurrentUser();

    default Optional<Search> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Search> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Search> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<Search> findByActiveTrue();

    Page<Search> findByActiveTrue(Pageable pageable);
}
