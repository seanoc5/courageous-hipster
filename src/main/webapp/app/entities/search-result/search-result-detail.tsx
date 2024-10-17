import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './search-result.reducer';

export const SearchResultDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const searchResultEntity = useAppSelector(state => state.searchResult.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="searchResultDetailsHeading">Search Result</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{searchResultEntity.id}</dd>
          <dt>
            <span id="query">Query</span>
          </dt>
          <dd>{searchResultEntity.query}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{searchResultEntity.type}</dd>
          <dt>
            <span id="responseBody">Response Body</span>
          </dt>
          <dd>{searchResultEntity.responseBody}</dd>
          <dt>
            <span id="statusCode">Status Code</span>
          </dt>
          <dd>{searchResultEntity.statusCode}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {searchResultEntity.dateCreated ? (
              <TextFormat value={searchResultEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>
            {searchResultEntity.lastUpdated ? (
              <TextFormat value={searchResultEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Config</dt>
          <dd>{searchResultEntity.config ? searchResultEntity.config.id : ''}</dd>
          <dt>Search</dt>
          <dd>{searchResultEntity.search ? searchResultEntity.search.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/search-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/search-result/${searchResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SearchResultDetail;
