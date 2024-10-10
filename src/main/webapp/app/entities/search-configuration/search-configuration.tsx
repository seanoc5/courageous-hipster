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

import { getEntities } from './search-configuration.reducer';

export const SearchConfiguration = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const searchConfigurationList = useAppSelector(state => state.searchConfiguration.entities);
  const loading = useAppSelector(state => state.searchConfiguration.loading);

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
      <h2 id="search-configuration-heading" data-cy="SearchConfigurationHeading">
        Search Configurations
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link
            to="/search-configuration/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Search Configuration
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {searchConfigurationList && searchConfigurationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('label')}>
                  Label <FontAwesomeIcon icon={getSortIconByFieldName('label')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('defaultConfig')}>
                  Default Config <FontAwesomeIcon icon={getSortIconByFieldName('defaultConfig')} />
                </th>
                <th className="hand" onClick={sort('url')}>
                  Url <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                </th>
                <th className="hand" onClick={sort('paramsJson')}>
                  Params Json <FontAwesomeIcon icon={getSortIconByFieldName('paramsJson')} />
                </th>
                <th className="hand" onClick={sort('headersJson')}>
                  Headers Json <FontAwesomeIcon icon={getSortIconByFieldName('headersJson')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date Created <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  Last Updated <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdated')} />
                </th>
                <th>
                  Created By <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {searchConfigurationList.map((searchConfiguration, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/search-configuration/${searchConfiguration.id}`} color="link" size="sm">
                      {searchConfiguration.id}
                    </Button>
                  </td>
                  <td>{searchConfiguration.label}</td>
                  <td>{searchConfiguration.description}</td>
                  <td>{searchConfiguration.defaultConfig ? 'true' : 'false'}</td>
                  <td>{searchConfiguration.url}</td>
                  <td>{searchConfiguration.paramsJson}</td>
                  <td>{searchConfiguration.headersJson}</td>
                  <td>
                    {searchConfiguration.dateCreated ? (
                      <TextFormat type="date" value={searchConfiguration.dateCreated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {searchConfiguration.lastUpdated ? (
                      <TextFormat type="date" value={searchConfiguration.lastUpdated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{searchConfiguration.createdBy ? searchConfiguration.createdBy.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/search-configuration/${searchConfiguration.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/search-configuration/${searchConfiguration.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/search-configuration/${searchConfiguration.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Search Configurations found</div>
        )}
      </div>
    </div>
  );
};

export default SearchConfiguration;
