import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/analyzer">
        Analyzer
      </MenuItem>
      <MenuItem icon="asterisk" to="/comment">
        Comment
      </MenuItem>
      <MenuItem icon="asterisk" to="/content">
        Content
      </MenuItem>
      <MenuItem icon="asterisk" to="/content-fragment">
        Content Fragment
      </MenuItem>
      <MenuItem icon="asterisk" to="/context">
        Context
      </MenuItem>
      <MenuItem icon="asterisk" to="/organization">
        Organization
      </MenuItem>
      <MenuItem icon="asterisk" to="/search">
        Search
      </MenuItem>
      <MenuItem icon="asterisk" to="/search-configuration">
        Search Configuration
      </MenuItem>
      <MenuItem icon="asterisk" to="/search-result">
        Search Result
      </MenuItem>
      <MenuItem icon="asterisk" to="/tag">
        Tag
      </MenuItem>
      <MenuItem icon="asterisk" to="/thing-type">
        Thing Type
      </MenuItem>
      <MenuItem icon="asterisk" to="/topic">
        Topic
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
