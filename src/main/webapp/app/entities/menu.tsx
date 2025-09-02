import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/doctor">
        <Translate contentKey="global.menu.entities.doctor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/visit">
        <Translate contentKey="global.menu.entities.visit" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/appointment">
        <Translate contentKey="global.menu.entities.appointment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/medication-category">
        <Translate contentKey="global.menu.entities.medicationCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/medication">
        <Translate contentKey="global.menu.entities.medication" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/medication-plan">
        <Translate contentKey="global.menu.entities.medicationPlan" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/medication-plan-dose">
        <Translate contentKey="global.menu.entities.medicationPlanDose" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/examination-category">
        <Translate contentKey="global.menu.entities.examinationCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/examination-record">
        <Translate contentKey="global.menu.entities.examinationRecord" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
