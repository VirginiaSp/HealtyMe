import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './medication-plan.reducer';

export const MedicationPlan = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const medicationPlanList = useAppSelector(state => state.medicationPlan.entities);
  const loading = useAppSelector(state => state.medicationPlan.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="medication-plan-heading" data-cy="MedicationPlanHeading">
        <Translate contentKey="healthyMeApp.medicationPlan.home.title">Medication Plans</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="healthyMeApp.medicationPlan.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/medication-plan/new" className="btn btn-primary create-entity-btn" id="create-entity-btn" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="healthyMeApp.medicationPlan.home.createLabel">Create new Medication Plan</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {medicationPlanList && medicationPlanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="healthyMeApp.medicationPlan.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('planName')}>
                  <Translate contentKey="healthyMeApp.medicationPlan.planName">Plan Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('planName')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="healthyMeApp.medicationPlan.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="healthyMeApp.medicationPlan.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('medicationName')}>
                  <Translate contentKey="healthyMeApp.medicationPlan.medicationName">Medication Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('medicationName')} />
                </th>
                <th className="hand" onClick={sort('colorHex')}>
                  <Translate contentKey="healthyMeApp.medicationPlan.colorHex">Color Hex</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('colorHex')} />
                </th>
                <th>
                  <Translate contentKey="healthyMeApp.medicationPlan.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {medicationPlanList.map((medicationPlan, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/medication-plan/${medicationPlan.id}`} color="link" size="sm">
                      {medicationPlan.id}
                    </Button>
                  </td>
                  <td>{medicationPlan.planName}</td>
                  <td>
                    {medicationPlan.startDate ? (
                      <TextFormat type="date" value={medicationPlan.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {medicationPlan.endDate ? (
                      <TextFormat type="date" value={medicationPlan.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{medicationPlan.medicationName}</td>
                  <td>{medicationPlan.colorHex}</td>
                  <td>{medicationPlan.owner ? medicationPlan.owner.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/medication-plan/${medicationPlan.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/medication-plan/${medicationPlan.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/medication-plan/${medicationPlan.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="healthyMeApp.medicationPlan.home.notFound">No Medication Plans found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MedicationPlan;
