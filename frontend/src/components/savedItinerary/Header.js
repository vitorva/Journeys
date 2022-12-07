import React from 'react'

import '../../styles/SavedItinerary.css';

import SnackbarContent from '@mui/material/SnackbarContent';
import ProfileButton from '../common/ProfileButton';
import MyjourneysButton from '../common/MyjourneysButton'
import LogoButton from '../common/LogoButton';
import MapButton from '../common/MapButton';

function Header(props) {

  return (
    <div className="savedItineraryHeader">
        <div style={{position:'absolute', left:'0'}}>
            <LogoButton/>
        </div>
        <div>
            <SnackbarContent sx={{
                    maxWidth: '70%',
                    backgroundColor: '#E2B4A5',
                    justifyContent: 'center',
                    alignItems: 'center',
                    fontSize: 'large',
                    padding: '0 8px',

                }}
                message={props.tripTitle}
            />
        </div>
        <div className="headerButtons">
            <MapButton/>
            <MyjourneysButton/>
            <ProfileButton username={'John'}/>
        </div>
    </div>
  );
}

export default Header;