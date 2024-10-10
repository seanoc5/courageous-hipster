import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Analyzer from './analyzer';
import Comment from './comment';
import Content from './content';
import ContentFragment from './content-fragment';
import Context from './context';
import Organization from './organization';
import Search from './search';
import SearchConfiguration from './search-configuration';
import SearchResult from './search-result';
import Tag from './tag';
import ThingType from './thing-type';
import Topic from './topic';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="analyzer/*" element={<Analyzer />} />
        <Route path="comment/*" element={<Comment />} />
        <Route path="content/*" element={<Content />} />
        <Route path="content-fragment/*" element={<ContentFragment />} />
        <Route path="context/*" element={<Context />} />
        <Route path="organization/*" element={<Organization />} />
        <Route path="search/*" element={<Search />} />
        <Route path="search-configuration/*" element={<SearchConfiguration />} />
        <Route path="search-result/*" element={<SearchResult />} />
        <Route path="tag/*" element={<Tag />} />
        <Route path="thing-type/*" element={<ThingType />} />
        <Route path="topic/*" element={<Topic />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
