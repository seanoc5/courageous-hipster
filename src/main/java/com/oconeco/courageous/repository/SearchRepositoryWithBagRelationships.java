package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Search;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SearchRepositoryWithBagRelationships {
    Optional<Search> fetchBagRelationships(Optional<Search> search);

    List<Search> fetchBagRelationships(List<Search> searches);

    Page<Search> fetchBagRelationships(Page<Search> searches);
}
