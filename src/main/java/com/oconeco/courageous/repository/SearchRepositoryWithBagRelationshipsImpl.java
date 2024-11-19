package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Search;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SearchRepositoryWithBagRelationshipsImpl implements SearchRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SEARCHES_PARAMETER = "searches";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Search> fetchBagRelationships(Optional<Search> search) {
        return search.map(this::fetchConfigurations);
    }

    @Override
    public Page<Search> fetchBagRelationships(Page<Search> searches) {
        return new PageImpl<>(fetchBagRelationships(searches.getContent()), searches.getPageable(), searches.getTotalElements());
    }

    @Override
    public List<Search> fetchBagRelationships(List<Search> searches) {
        return Optional.of(searches).map(this::fetchConfigurations).orElse(Collections.emptyList());
    }

    Search fetchConfigurations(Search result) {
        return entityManager
            .createQuery("select search from Search search left join fetch search.configurations where search.id = :id", Search.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Search> fetchConfigurations(List<Search> searches) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, searches.size()).forEach(index -> order.put(searches.get(index).getId(), index));
        List<Search> result = entityManager
            .createQuery("select search from Search search left join fetch search.configurations where search in :searches", Search.class)
            .setParameter(SEARCHES_PARAMETER, searches)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
