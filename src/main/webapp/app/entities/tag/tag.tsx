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

import { getEntities } from './tag.reducer';

export const Tag = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const tagList = useAppSelector(state => state.tag.entities);
  const loading = useAppSelector(state => state.tag.loading);

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
      <h2 id="tag-heading" data-cy="TagHeading">
        Tags
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/tag/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Tag
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tagList && tagList.length > 0 ? (
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
                <th className="hand" onClick={sort('defaultTag')}>
                  Default Tag <FontAwesomeIcon icon={getSortIconByFieldName('defaultTag')} />
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
                  Analyzer <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Content <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Content Fragment <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Context <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Organization <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Search <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Search Configuration <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Search Result <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Thing Type <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Topic <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tagList.map((tag, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/tag/${tag.id}`} color="link" size="sm">
                      {tag.id}
                    </Button>
                  </td>
                  <td>{tag.label}</td>
                  <td>{tag.description}</td>
                  <td>{tag.defaultTag ? 'true' : 'false'}</td>
                  <td>{tag.dateCreated ? <TextFormat type="date" value={tag.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{tag.lastUpdated ? <TextFormat type="date" value={tag.lastUpdated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{tag.createdBy ? tag.createdBy.id : ''}</td>
                  <td>{tag.analyzer ? <Link to={`/analyzer/${tag.analyzer.id}`}>{tag.analyzer.id}</Link> : ''}</td>
                  <td>{tag.content ? <Link to={`/content/${tag.content.id}`}>{tag.content.id}</Link> : ''}</td>
                  <td>
                    {tag.contentFragment ? <Link to={`/content-fragment/${tag.contentFragment.id}`}>{tag.contentFragment.id}</Link> : ''}
                  </td>
                  <td>{tag.context ? <Link to={`/context/${tag.context.id}`}>{tag.context.id}</Link> : ''}</td>
                  <td>{tag.organization ? <Link to={`/organization/${tag.organization.id}`}>{tag.organization.id}</Link> : ''}</td>
                  <td>{tag.search ? <Link to={`/search/${tag.search.id}`}>{tag.search.id}</Link> : ''}</td>
                  <td>
                    {tag.searchConfiguration ? (
                      <Link to={`/search-configuration/${tag.searchConfiguration.id}`}>{tag.searchConfiguration.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{tag.searchResult ? <Link to={`/search-result/${tag.searchResult.id}`}>{tag.searchResult.id}</Link> : ''}</td>
                  <td>{tag.thingType ? <Link to={`/thing-type/${tag.thingType.id}`}>{tag.thingType.id}</Link> : ''}</td>
                  <td>{tag.topic ? <Link to={`/topic/${tag.topic.id}`}>{tag.topic.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/tag/${tag.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/tag/${tag.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/tag/${tag.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Tags found</div>
        )}
      </div>
    </div>
  );
};

export default Tag;
