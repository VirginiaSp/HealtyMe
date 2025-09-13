import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './medication-plan-dose.reducer';

export const MedicationPlanDose = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const medicationPlanDoseList = useAppSelector(state => state.medicationPlanDose.entities);
  const loading = useAppSelector(state => state.medicationPlanDose.loading);

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
      <h2 id="medication-plan-dose-heading" data-cy="MedicationPlanDoseHeading">
        <Translate contentKey="healthyMeApp.medicationPlanDose.home.title">Medication Plan Doses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="healthyMeApp.medicationPlanDose.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/medication-plan-dose/new"
            className="btn btn-primary create-entity-btn"
            id="create-entity-btn"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="healthyMeApp.medicationPlanDose.home.createLabel">Create new Medication Plan Dose</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {medicationPlanDoseList && medicationPlanDoseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="healthyMeApp.medicationPlanDose.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('timeOfDay')}>
                  <Translate contentKey="healthyMeApp.medicationPlanDose.timeOfDay">Time Of Day</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('timeOfDay')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="healthyMeApp.medicationPlanDose.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="healthyMeApp.medicationPlanDose.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="healthyMeApp.medicationPlanDose.plan">Plan</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {medicationPlanDoseList.map((medicationPlanDose, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/medication-plan-dose/${medicationPlanDose.id}`} color="link" size="sm">
                      {medicationPlanDose.id}
                    </Button>
                  </td>
                  <td>{medicationPlanDose.timeOfDay}</td>
                  <td>{medicationPlanDose.notes}</td>
                  <td>{medicationPlanDose.owner ? medicationPlanDose.owner.login : ''}</td>
                  <td>
                    {medicationPlanDose.plan ? (
                      <Link to={`/medication-plan/${medicationPlanDose.plan.id}`}>{medicationPlanDose.plan.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/medication-plan-dose/${medicationPlanDose.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/medication-plan-dose/${medicationPlanDose.id}/edit`}
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
                        onClick={() => (window.location.href = `/medication-plan-dose/${medicationPlanDose.id}/delete`)}
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
              <Translate contentKey="healthyMeApp.medicationPlanDose.home.notFound">No Medication Plan Doses found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MedicationPlanDose;
