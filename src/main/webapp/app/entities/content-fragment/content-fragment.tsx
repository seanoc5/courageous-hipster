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

import { getEntities } from './content-fragment.reducer';

export const ContentFragment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const contentFragmentList = useAppSelector(state => state.contentFragment.entities);
  const loading = useAppSelector(state => state.contentFragment.loading);

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
      <h2 id="content-fragment-heading" data-cy="ContentFragmentHeading">
        Content Fragments
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/content-fragment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Content Fragment
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contentFragmentList && contentFragmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('label')}>
                  Label <FontAwesomeIcon icon={getSortIconByFieldName('label')} />
                </th>
                <th className="hand" onClick={sort('text')}>
                  Text <FontAwesomeIcon icon={getSortIconByFieldName('text')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('structuredContent')}>
                  Structured Content <FontAwesomeIcon icon={getSortIconByFieldName('structuredContent')} />
                </th>
                <th className="hand" onClick={sort('startPos')}>
                  Start Pos <FontAwesomeIcon icon={getSortIconByFieldName('startPos')} />
                </th>
                <th className="hand" onClick={sort('endPos')}>
                  End Pos <FontAwesomeIcon icon={getSortIconByFieldName('endPos')} />
                </th>
                <th className="hand" onClick={sort('startTermNum')}>
                  Start Term Num <FontAwesomeIcon icon={getSortIconByFieldName('startTermNum')} />
                </th>
                <th className="hand" onClick={sort('endTermNum')}>
                  End Term Num <FontAwesomeIcon icon={getSortIconByFieldName('endTermNum')} />
                </th>
                <th className="hand" onClick={sort('subtype')}>
                  Subtype <FontAwesomeIcon icon={getSortIconByFieldName('subtype')} />
                </th>
                <th className="hand" onClick={sort('language')}>
                  Language <FontAwesomeIcon icon={getSortIconByFieldName('language')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date Created <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  Last Updated <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdated')} />
                </th>
                <th>
                  Type <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Content <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Context <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Topic <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contentFragmentList.map((contentFragment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/content-fragment/${contentFragment.id}`} color="link" size="sm">
                      {contentFragment.id}
                    </Button>
                  </td>
                  <td>{contentFragment.label}</td>
                  <td>{contentFragment.text}</td>
                  <td>{contentFragment.description}</td>
                  <td>{contentFragment.structuredContent}</td>
                  <td>{contentFragment.startPos}</td>
                  <td>{contentFragment.endPos}</td>
                  <td>{contentFragment.startTermNum}</td>
                  <td>{contentFragment.endTermNum}</td>
                  <td>{contentFragment.subtype}</td>
                  <td>{contentFragment.language}</td>
                  <td>
                    {contentFragment.dateCreated ? (
                      <TextFormat type="date" value={contentFragment.dateCreated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {contentFragment.lastUpdated ? (
                      <TextFormat type="date" value={contentFragment.lastUpdated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {contentFragment.type ? <Link to={`/thing-type/${contentFragment.type.id}`}>{contentFragment.type.id}</Link> : ''}
                  </td>
                  <td>
                    {contentFragment.content ? <Link to={`/content/${contentFragment.content.id}`}>{contentFragment.content.id}</Link> : ''}
                  </td>
                  <td>
                    {contentFragment.context ? <Link to={`/context/${contentFragment.context.id}`}>{contentFragment.context.id}</Link> : ''}
                  </td>
                  <td>{contentFragment.topic ? <Link to={`/topic/${contentFragment.topic.id}`}>{contentFragment.topic.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/content-fragment/${contentFragment.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/content-fragment/${contentFragment.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/content-fragment/${contentFragment.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Content Fragments found</div>
        )}
      </div>
    </div>
  );
};

export default ContentFragment;
