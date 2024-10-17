import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './search-configuration.reducer';

export const SearchConfigurationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const searchConfigurationEntity = useAppSelector(state => state.searchConfiguration.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="searchConfigurationDetailsHeading">Search Configuration</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{searchConfigurationEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{searchConfigurationEntity.label}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{searchConfigurationEntity.description}</dd>
          <dt>
            <span id="defaultConfig">Default Config</span>
          </dt>
          <dd>{searchConfigurationEntity.defaultConfig ? 'true' : 'false'}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{searchConfigurationEntity.url}</dd>
          <dt>
            <span id="paramsJson">Params Json</span>
          </dt>
          <dd>{searchConfigurationEntity.paramsJson}</dd>
          <dt>
            <span id="headersJson">Headers Json</span>
          </dt>
          <dd>{searchConfigurationEntity.headersJson}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {searchConfigurationEntity.dateCreated ? (
              <TextFormat value={searchConfigurationEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>
            {searchConfigurationEntity.lastUpdated ? (
              <TextFormat value={searchConfigurationEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Created By</dt>
          <dd>{searchConfigurationEntity.createdBy ? searchConfigurationEntity.createdBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/search-configuration" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/search-configuration/${searchConfigurationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SearchConfigurationDetail;
