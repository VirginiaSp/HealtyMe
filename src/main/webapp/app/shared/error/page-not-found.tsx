import React from 'react';
import { Translate } from 'app/shared/component';
import { Alert } from 'reactstrap';

const PageNotFound = () => {
  return (
    <div>
      <Alert color="danger">
        <Translate contentKey="error.http.404">The page does not exist.</Translate>
      </Alert>
    </div>
  );
};

export default PageNotFound;
