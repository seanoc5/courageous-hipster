import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content.reducer';

export const ContentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentEntity = useAppSelector(state => state.content.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentDetailsHeading">Content</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{contentEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{contentEntity.title}</dd>
          <dt>
            <span id="uri">Uri</span>
          </dt>
          <dd>{contentEntity.uri}</dd>
          <dt>
            <span id="description">Description</span>
            <UncontrolledTooltip target="description">
              snippets back from search engine, or summary from ML, or actual &#39;description&#39; from author/publisher
            </UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.description}</dd>
          <dt>
            <span id="path">Path</span>
            <UncontrolledTooltip target="path">url or file path</UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.path}</dd>
          <dt>
            <span id="source">Source</span>
            <UncontrolledTooltip target="source">site or machine name</UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.source}</dd>
          <dt>
            <span id="params">Params</span>
            <UncontrolledTooltip target="params">
              any additional parameters, such as url query params, where relevant (optional)
            </UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.params}</dd>
          <dt>
            <span id="bodyText">Body Text</span>
            <UncontrolledTooltip target="bodyText">
              extracted text from source document (typically fetched by ContentService.fetchContent(contentDoc)
            </UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.bodyText}</dd>
          <dt>
            <span id="textSize">Text Size</span>
          </dt>
          <dd>{contentEntity.textSize}</dd>
          <dt>
            <span id="structuredContent">Structured Content</span>
            <UncontrolledTooltip target="structuredContent">
              html/xml version typically, but perhaps other (markdown, json,...)
            </UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.structuredContent}</dd>
          <dt>
            <span id="structureSize">Structure Size</span>
          </dt>
          <dd>{contentEntity.structureSize}</dd>
          <dt>
            <span id="author">Author</span>
          </dt>
          <dd>{contentEntity.author}</dd>
          <dt>
            <span id="language">Language</span>
          </dt>
          <dd>{contentEntity.language}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{contentEntity.type}</dd>
          <dt>
            <span id="subtype">Subtype</span>
          </dt>
          <dd>{contentEntity.subtype}</dd>
          <dt>
            <span id="mineType">Mine Type</span>
          </dt>
          <dd>{contentEntity.mineType}</dd>
          <dt>
            <span id="publishDate">Publish Date</span>
          </dt>
          <dd>
            {contentEntity.publishDate ? <TextFormat value={contentEntity.publishDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="offensiveFlag">Offensive Flag</span>
            <UncontrolledTooltip target="offensiveFlag">
              originally added to capture Brave api &#39;family_friendly&#39; flag info...
            </UncontrolledTooltip>
          </dt>
          <dd>{contentEntity.offensiveFlag}</dd>
          <dt>
            <span id="favicon">Favicon</span>
          </dt>
          <dd>{contentEntity.favicon}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {contentEntity.dateCreated ? <TextFormat value={contentEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastUpdate">Last Update</span>
          </dt>
          <dd>{contentEntity.lastUpdate ? <TextFormat value={contentEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Search Result</dt>
          <dd>{contentEntity.searchResult ? contentEntity.searchResult.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content/${contentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentDetail;
