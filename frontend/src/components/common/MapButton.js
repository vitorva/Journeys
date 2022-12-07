import React from 'react'

import Button from '@mui/material/Button';
import MapIcon from '@mui/icons-material/Map';

function MapButton(props) {
  
  return (
    <Button
        color='secondaryButton'
        variant="contained"
        href="/map"
        endIcon={<MapIcon/>}
    >
        Map
    </Button>
  );
}

export default MapButton;