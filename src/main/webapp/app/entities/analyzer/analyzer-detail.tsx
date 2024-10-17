import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './analyzer.reducer';

export const AnalyzerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const analyzerEntity = useAppSelector(state => state.analyzer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="analyzerDetailsHeading">Analyzer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{analyzerEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{analyzerEntity.label}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{analyzerEntity.description}</dd>
          <dt>
            <span id="code">Code</span>
            <UncontrolledTooltip target="code">closure type groovy code to be run)</UncontrolledTooltip>
          </dt>
          <dd>{analyzerEntity.code}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {analyzerEntity.dateCreated ? <TextFormat value={analyzerEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>
            {analyzerEntity.lastUpdated ? <TextFormat value={analyzerEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Created By</dt>
          <dd>{analyzerEntity.createdBy ? analyzerEntity.createdBy.id : ''}</dd>
          <dt>Context</dt>
          <dd>{analyzerEntity.context ? analyzerEntity.context.id : ''}</dd>
          <dt>Search Configuration</dt>
          <dd>{analyzerEntity.searchConfiguration ? analyzerEntity.searchConfiguration.id : ''}</dd>
          <dt>Search Result</dt>
          <dd>{analyzerEntity.searchResult ? analyzerEntity.searchResult.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/analyzer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/analyzer/${analyzerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AnalyzerDetail;
