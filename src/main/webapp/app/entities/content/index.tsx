import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Content from './content';
import ContentDetail from './content-detail';
import ContentUpdate from './content-update';
import ContentDeleteDialog from './content-delete-dialog';

const ContentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Content />} />
    <Route path="new" element={<ContentUpdate />} />
    <Route path=":id">
      <Route index element={<ContentDetail />} />
      <Route path="edit" element={<ContentUpdate />} />
      <Route path="delete" element={<ContentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContentRoutes;
