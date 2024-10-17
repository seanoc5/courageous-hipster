import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Search from './search';
import SearchDetail from './search-detail';
import SearchUpdate from './search-update';
import SearchDeleteDialog from './search-delete-dialog';

const SearchRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Search />} />
    <Route path="new" element={<SearchUpdate />} />
    <Route path=":id">
      <Route index element={<SearchDetail />} />
      <Route path="edit" element={<SearchUpdate />} />
      <Route path="delete" element={<SearchDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SearchRoutes;
