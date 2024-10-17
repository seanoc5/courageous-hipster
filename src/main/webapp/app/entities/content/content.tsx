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

import { getEntities } from './content.reducer';

export const Content = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const contentList = useAppSelector(state => state.content.entities);
  const loading = useAppSelector(state => state.content.loading);

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
      <h2 id="content-heading" data-cy="ContentHeading">
        Contents
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/content/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Content
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contentList && contentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  Title <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('uri')}>
                  Uri <FontAwesomeIcon icon={getSortIconByFieldName('uri')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('path')}>
                  Path <FontAwesomeIcon icon={getSortIconByFieldName('path')} />
                </th>
                <th className="hand" onClick={sort('source')}>
                  Source <FontAwesomeIcon icon={getSortIconByFieldName('source')} />
                </th>
                <th className="hand" onClick={sort('params')}>
                  Params <FontAwesomeIcon icon={getSortIconByFieldName('params')} />
                </th>
                <th className="hand" onClick={sort('bodyText')}>
                  Body Text <FontAwesomeIcon icon={getSortIconByFieldName('bodyText')} />
                </th>
                <th className="hand" onClick={sort('textSize')}>
                  Text Size <FontAwesomeIcon icon={getSortIconByFieldName('textSize')} />
                </th>
                <th className="hand" onClick={sort('structuredContent')}>
                  Structured Content <FontAwesomeIcon icon={getSortIconByFieldName('structuredContent')} />
                </th>
                <th className="hand" onClick={sort('structureSize')}>
                  Structure Size <FontAwesomeIcon icon={getSortIconByFieldName('structureSize')} />
                </th>
                <th className="hand" onClick={sort('author')}>
                  Author <FontAwesomeIcon icon={getSortIconByFieldName('author')} />
                </th>
                <th className="hand" onClick={sort('language')}>
                  Language <FontAwesomeIcon icon={getSortIconByFieldName('language')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  Type <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('subtype')}>
                  Subtype <FontAwesomeIcon icon={getSortIconByFieldName('subtype')} />
                </th>
                <th className="hand" onClick={sort('mineType')}>
                  Mine Type <FontAwesomeIcon icon={getSortIconByFieldName('mineType')} />
                </th>
                <th className="hand" onClick={sort('publishDate')}>
                  Publish Date <FontAwesomeIcon icon={getSortIconByFieldName('publishDate')} />
                </th>
                <th className="hand" onClick={sort('offensiveFlag')}>
                  Offensive Flag <FontAwesomeIcon icon={getSortIconByFieldName('offensiveFlag')} />
                </th>
                <th className="hand" onClick={sort('favicon')}>
                  Favicon <FontAwesomeIcon icon={getSortIconByFieldName('favicon')} />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date Created <FontAwesomeIcon icon={getSortIconByFieldName('dateCreated')} />
                </th>
                <th className="hand" onClick={sort('lastUpdate')}>
                  Last Update <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdate')} />
                </th>
                <th>
                  Search Result <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contentList.map((content, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/content/${content.id}`} color="link" size="sm">
                      {content.id}
                    </Button>
                  </td>
                  <td>{content.title}</td>
                  <td>{content.uri}</td>
                  <td>{content.description}</td>
                  <td>{content.path}</td>
                  <td>{content.source}</td>
                  <td>{content.params}</td>
                  <td>{content.bodyText}</td>
                  <td>{content.textSize}</td>
                  <td>{content.structuredContent}</td>
                  <td>{content.structureSize}</td>
                  <td>{content.author}</td>
                  <td>{content.language}</td>
                  <td>{content.type}</td>
                  <td>{content.subtype}</td>
                  <td>{content.mineType}</td>
                  <td>{content.publishDate ? <TextFormat type="date" value={content.publishDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{content.offensiveFlag}</td>
                  <td>{content.favicon}</td>
                  <td>{content.dateCreated ? <TextFormat type="date" value={content.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{content.lastUpdate ? <TextFormat type="date" value={content.lastUpdate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {content.searchResult ? <Link to={`/search-result/${content.searchResult.id}`}>{content.searchResult.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/content/${content.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/content/${content.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/content/${content.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Contents found</div>
        )}
      </div>
    </div>
  );
};

export default Content;
