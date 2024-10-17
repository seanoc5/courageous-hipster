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

import { getEntities } from './comment.reducer';

export const Comment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const commentList = useAppSelector(state => state.comment.entities);
  const loading = useAppSelector(state => state.comment.loading);

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
      <h2 id="comment-heading" data-cy="CommentHeading">
        Comments
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/comment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Comment
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {commentList && commentList.length > 0 ? (
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
                <th className="hand" onClick={sort('url')}>
                  Url <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date Created <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('lastUpdate')}>
                  Last Update <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdate')} />
                </th>
                <th>
                  User <FontAwesomeIcon icon="sort" />
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
                  Tag <FontAwesomeIcon icon="sort" />
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
              {commentList.map((comment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/comment/${comment.id}`} color="link" size="sm">
                      {comment.id}
                    </Button>
                  </td>
                  <td>{comment.label}</td>
                  <td>{comment.description}</td>
                  <td>{comment.url}</td>
                  <td>{comment.dateCreated ? <TextFormat type="date" value={comment.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{comment.lastUpdate ? <TextFormat type="date" value={comment.lastUpdate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{comment.user ? comment.user.id : ''}</td>
                  <td>{comment.content ? <Link to={`/content/${comment.content.id}`}>{comment.content.id}</Link> : ''}</td>
                  <td>
                    {comment.contentFragment ? (
                      <Link to={`/content-fragment/${comment.contentFragment.id}`}>{comment.contentFragment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{comment.context ? <Link to={`/context/${comment.context.id}`}>{comment.context.id}</Link> : ''}</td>
                  <td>
                    {comment.organization ? <Link to={`/organization/${comment.organization.id}`}>{comment.organization.id}</Link> : ''}
                  </td>
                  <td>{comment.search ? <Link to={`/search/${comment.search.id}`}>{comment.search.id}</Link> : ''}</td>
                  <td>
                    {comment.searchConfiguration ? (
                      <Link to={`/search-configuration/${comment.searchConfiguration.id}`}>{comment.searchConfiguration.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {comment.searchResult ? <Link to={`/search-result/${comment.searchResult.id}`}>{comment.searchResult.id}</Link> : ''}
                  </td>
                  <td>{comment.tag ? <Link to={`/tag/${comment.tag.id}`}>{comment.tag.id}</Link> : ''}</td>
                  <td>{comment.thingType ? <Link to={`/thing-type/${comment.thingType.id}`}>{comment.thingType.id}</Link> : ''}</td>
                  <td>{comment.topic ? <Link to={`/topic/${comment.topic.id}`}>{comment.topic.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/comment/${comment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/comment/${comment.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/comment/${comment.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Comments found</div>
        )}
      </div>
    </div>
  );
};

export default Comment;
