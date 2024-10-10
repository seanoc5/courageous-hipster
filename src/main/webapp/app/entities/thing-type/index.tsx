import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ThingType from './thing-type';
import ThingTypeDetail from './thing-type-detail';
import ThingTypeUpdate from './thing-type-update';
import ThingTypeDeleteDialog from './thing-type-delete-dialog';

const ThingTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ThingType />} />
    <Route path="new" element={<ThingTypeUpdate />} />
    <Route path=":id">
      <Route index element={<ThingTypeDetail />} />
      <Route path="edit" element={<ThingTypeUpdate />} />
      <Route path="delete" element={<ThingTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ThingTypeRoutes;
