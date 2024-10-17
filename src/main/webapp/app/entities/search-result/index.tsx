import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SearchResult from './search-result';
import SearchResultDetail from './search-result-detail';
import SearchResultUpdate from './search-result-update';
import SearchResultDeleteDialog from './search-result-delete-dialog';

const SearchResultRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SearchResult />} />
    <Route path="new" element={<SearchResultUpdate />} />
    <Route path=":id">
      <Route index element={<SearchResultDetail />} />
      <Route path="edit" element={<SearchResultUpdate />} />
      <Route path="delete" element={<SearchResultDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SearchResultRoutes;
