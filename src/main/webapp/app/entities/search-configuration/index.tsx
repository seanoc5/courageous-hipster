import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SearchConfiguration from './search-configuration';
import SearchConfigurationDetail from './search-configuration-detail';
import SearchConfigurationUpdate from './search-configuration-update';
import SearchConfigurationDeleteDialog from './search-configuration-delete-dialog';

const SearchConfigurationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SearchConfiguration />} />
    <Route path="new" element={<SearchConfigurationUpdate />} />
    <Route path=":id">
      <Route index element={<SearchConfigurationDetail />} />
      <Route path="edit" element={<SearchConfigurationUpdate />} />
      <Route path="delete" element={<SearchConfigurationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SearchConfigurationRoutes;
