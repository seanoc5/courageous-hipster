import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './search.reducer';

export const SearchDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const searchEntity = useAppSelector(state => state.search.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="searchDetailsHeading">Search</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{searchEntity.id}</dd>
          <dt>
            <span id="query">Query</span>
          </dt>
          <dd>{searchEntity.query}</dd>
          <dt>
            <span id="additionalParams">Additional Params</span>
          </dt>
          <dd>{searchEntity.additionalParams}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>{searchEntity.dateCreated ? <TextFormat value={searchEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>{searchEntity.lastUpdated ? <TextFormat value={searchEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Configuration</dt>
          <dd>{searchEntity.configuration ? searchEntity.configuration.id : ''}</dd>
          <dt>Created By</dt>
          <dd>{searchEntity.createdBy ? searchEntity.createdBy.id : ''}</dd>
          <dt>Context</dt>
          <dd>{searchEntity.context ? searchEntity.context.id : ''}</dd>
          <dt>Type</dt>
          <dd>{searchEntity.type ? searchEntity.type.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/search" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/search/${searchEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SearchDetail;
