import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './search-result.reducer';

export const SearchResult = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const searchResultList = useAppSelector(state => state.searchResult.entities);
  const loading = useAppSelector(state => state.searchResult.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="search-result-heading" data-cy="SearchResultHeading">
        Search Results
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/search-result/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Search Result
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {searchResultList && searchResultList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('query')}>
                  Query <FontAwesomeIcon icon={getSortIconByFieldName('query')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  Type <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('responseBody')}>
                  Response Body <FontAwesomeIcon icon={getSortIconByFieldName('responseBody')} />
                </th>
                <th className="hand" onClick={sort('statusCode')}>
                  Status Code <FontAwesomeIcon icon={getSortIconByFieldName('statusCode')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date Created <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  Last Updated <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdated')} />
                </th>
                <th>
                  Config <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Search <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {searchResultList.map((searchResult, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/search-result/${searchResult.id}`} color="link" size="sm">
                      {searchResult.id}
                    </Button>
                  </td>
                  <td>{searchResult.query}</td>
                  <td>{searchResult.type}</td>
                  <td>{searchResult.responseBody}</td>
                  <td>{searchResult.statusCode}</td>
                  <td>
                    {searchResult.dateCreated ? <TextFormat type="date" value={searchResult.dateCreated} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {searchResult.lastUpdated ? <TextFormat type="date" value={searchResult.lastUpdated} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {searchResult.config ? (
                      <Link to={`/search-configuration/${searchResult.config.id}`}>{searchResult.config.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{searchResult.search ? <Link to={`/search/${searchResult.search.id}`}>{searchResult.search.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/search-result/${searchResult.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/search-result/${searchResult.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/search-result/${searchResult.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Search Results found</div>
        )}
      </div>
    </div>
  );
};

export default SearchResult;
