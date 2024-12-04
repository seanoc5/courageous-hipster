import './search.scss';

import React, { useEffect, useState } from 'react';

import { Button, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Box, Typography } from '@mui/material';
import { getEntities as getSearchConfigurations } from 'app/entities/search-configuration/search-configuration.reducer';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import EnhancedAutocomplete from 'app/shared/components/enhanced-autocomplete';
import { ValidatedField } from 'react-jhipster';
import { ISearch } from 'app/shared/model/search.model';
import { cleanEntity } from 'app/shared/util/entity-utils';
import WebserchResults from '../administration/search-result/webserchresult';
import axios from 'axios';
import { toast } from 'react-toastify';

export const SearchComponent = () => {
  const dispatch = useAppDispatch();
  const [selectedConfig, setSelectedConfig] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState({});
  const [loader, setLoader] = useState(false);

  const searchConfigurations = useAppSelector(state => state.searchConfiguration.entities);
  const tags = useAppSelector(state => state.tag.entities);

  useEffect(() => {
    dispatch(getSearchConfigurations({}));
    dispatch(getTags({}));
  }, []);

  const handleConfigChange = (newValue, id) => {
    switch (id) {
      case 'configuration':
        setSelectedConfig(newValue);
        break;
      case 'tags':
        setSelectedTags(newValue);
        break;
      default:
        break;
    }
  };

  const handleSearch = async () => {
    setLoader(true);
    try {
      const results = await getSearchResults();
      setSearchResults(results.data);
    } catch (error) {
      toast.error(error?.messsage);
      const errorMessage = JSON.parse(JSON.stringify(error)).message;
      toast.error(errorMessage);
      setLoader(false);
    }

    setLoader(false);
  };
  const getSearchResults = async () => {
    const searchApiUrl = 'api/searches/search';
    const searchParams = {
      query: searchQuery,
      configurations: selectedConfig,
    };
    return axios.post<ISearch>(searchApiUrl, cleanEntity(searchParams));
  };
  return (
    <div>
      <Row className="px-5">
        <Box>
          <Typography variant="h6" gutterBottom>
            SEARCH
          </Typography>
        </Box>
        <Col md="6">
          <EnhancedAutocomplete
            options={searchConfigurations}
            name="configuration"
            id="search-configuration"
            dataCy="configuration"
            isMulti={true}
            className="custom-autocomplete"
            value={selectedConfig}
            onChange={(e, newValue) => handleConfigChange(newValue, 'configuration')}
            label="Configuration"
          />
        </Col>
        <Col md="6">
          <EnhancedAutocomplete
            options={tags}
            name="tags"
            id="search-tags"
            dataCy="tags"
            isMulti={true}
            className="custom-autocomplete"
            value={selectedTags}
            onChange={(e, newValue) => handleConfigChange(newValue, 'tags')}
            label="Tags"
          />
        </Col>
      </Row>
      <Row className="px-5">
        <Col md="9">
          <ValidatedField type="text" name="search" onChange={e => setSearchQuery(e.target.value)}></ValidatedField>
        </Col>
        <Col md="3">
          <Button color="primary" id="search" data-cy="searchbutton" onClick={handleSearch} disabled={searchQuery.length < 1}>
            <FontAwesomeIcon icon="search" />
            &nbsp; Search
          </Button>
        </Col>
      </Row>
      <hr />
      <WebserchResults results={searchResults || []} isLoading={loader} />
    </div>
  );
};

export default SearchComponent;
