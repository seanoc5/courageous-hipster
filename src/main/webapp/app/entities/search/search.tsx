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

import { getEntities } from './search.reducer';

export const Search = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const searchList = useAppSelector(state => state.search.entities);
  const loading = useAppSelector(state => state.search.loading);

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
      <h2 id="search-heading" data-cy="SearchHeading">
        Searches
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/search/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Search
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {searchList && searchList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('query')}>
                  Query <FontAwesomeIcon icon={getSortIconByFieldName('query')} />
                </th>
                <th className="hand" onClick={sort('additionalParams')}>
                  Additional Params <FontAwesomeIcon icon={getSortIconByFieldName('additionalParams')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date Created <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  Last Updated <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdated')} />
                </th>
                <th>
                  Configuration <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Created By <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Context <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Type <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {searchList.map((search, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/search/${search.id}`} color="link" size="sm">
                      {search.id}
                    </Button>
                  </td>
                  <td>{search.query}</td>
                  <td>{search.additionalParams}</td>
                  <td>{search.dateCreated ? <TextFormat type="date" value={search.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{search.lastUpdated ? <TextFormat type="date" value={search.lastUpdated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {search.configuration ? (
                      <Link to={`/search-configuration/${search.configuration.id}`}>{search.configuration.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{search.createdBy ? search.createdBy.id : ''}</td>
                  <td>{search.context ? <Link to={`/context/${search.context.id}`}>{search.context.id}</Link> : ''}</td>
                  <td>{search.type ? <Link to={`/thing-type/${search.type.id}`}>{search.type.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/search/${search.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/search/${search.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/search/${search.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Searches found</div>
        )}
      </div>
    </div>
  );
};

export default Search;
