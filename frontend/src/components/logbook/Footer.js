import { useNavigate } from 'react-router-dom';

import '../../styles/Logbook.css';

import Button from '@mui/material/Button';
import AddIcon from '@mui/icons-material/Add';

function Footer() {

    const onClickCreateTrip = (e) => {
        navigate(`/map`);
    };

    const navigate = useNavigate();

    function onClickCreatePOI(){
        
    }

    return (
      <div className="footerLogbook">
        <Button color="primary"
            variant="contained"
            onClick={onClickCreatePOI}
            startIcon={<AddIcon/>}
            > 
            Add a new POI
        </Button>
        <Button sx={{
                backgroundColor: '#699B86', 
                '&:hover': {
                    backgroundColor: '#699B86',},
                marginLeft: '10px',
                height: '95%'
                }}
            variant="contained"
            onClick={onClickCreateTrip}
            startIcon={<AddIcon/>}
            > 
            Create a trip
        </Button>
      </div>
    );
  }
  
  export default Footer;