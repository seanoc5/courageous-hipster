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

import { getEntities } from './analyzer.reducer';

export const Analyzer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const analyzerList = useAppSelector(state => state.analyzer.entities);
  const loading = useAppSelector(state => state.analyzer.loading);

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
      <h2 id="analyzer-heading" data-cy="AnalyzerHeading">
        Analyzers
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/analyzer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Analyzer
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {analyzerList && analyzerList.length > 0 ? (
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
                <th className="hand" onClick={sort('code')}>
                  Code <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
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
                <th>
                  Context <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Search Configuration <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Search Result <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {analyzerList.map((analyzer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/analyzer/${analyzer.id}`} color="link" size="sm">
                      {analyzer.id}
                    </Button>
                  </td>
                  <td>{analyzer.label}</td>
                  <td>{analyzer.description}</td>
                  <td>{analyzer.code}</td>
                  <td>{analyzer.dateCreated ? <TextFormat type="date" value={analyzer.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{analyzer.lastUpdated ? <TextFormat type="date" value={analyzer.lastUpdated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{analyzer.createdBy ? analyzer.createdBy.id : ''}</td>
                  <td>{analyzer.context ? <Link to={`/context/${analyzer.context.id}`}>{analyzer.context.id}</Link> : ''}</td>
                  <td>
                    {analyzer.searchConfiguration ? (
                      <Link to={`/search-configuration/${analyzer.searchConfiguration.id}`}>{analyzer.searchConfiguration.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {analyzer.searchResult ? <Link to={`/search-result/${analyzer.searchResult.id}`}>{analyzer.searchResult.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/analyzer/${analyzer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/analyzer/${analyzer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/analyzer/${analyzer.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Analyzers found</div>
        )}
      </div>
    </div>
  );
};

export default Analyzer;
