import React from 'react'
import { useNavigate } from 'react-router-dom';

import Button from '@mui/material/Button';
import MenuBookIcon from '@mui/icons-material/MenuBook';

function MyjourneysButton(props) {

    const navigate = useNavigate();

    function goToLogbook(event) {
    navigate(`/logbook/`);
  }

  return (
    <Button sx={{marginLeft: '10px',}}
            id="myjourneysBtn"
        variant="contained"
        onClick={goToLogbook}
        endIcon={<MenuBookIcon/>}
        color='secondaryButton'
      >
        Journeys
    </Button>
  );
}

export default MyjourneysButton;
