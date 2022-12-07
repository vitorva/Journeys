import React from 'react'

import '../../styles/Logbook.css';

import Box from '@mui/material/Box';

import ProfileButton from '../common/ProfileButton';
import LogoButton from '../common/LogoButton';
import MapButton from '../common/MapButton';

function Header(props) {
  return (
    <div className="logbookHeader">
      <LogoButton/>

      <Box sx={{display:'flex'}}>
        <div>
          <MapButton/>
        </div>
        <ProfileButton/>
      </Box>
    </div>
  );
}

export default Header;
