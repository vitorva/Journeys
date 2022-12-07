import * as React from 'react';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import Fade from '@mui/material/Fade';

function OnSuccessSaveExp({open, setOpen}) {
  
  const handleClose = (reason) => {
    // prevent snackbar alert to close when clicking away
    if (reason === 'clickaway') {
        return;
      }
    setOpen(false);
  };    

  return (
      <Snackbar 
        open={open} 
        autoHideDuration={3000} 
        onClose={handleClose}
        transitionDuration={{ enter: 500, exit: 1000 }}
        TransitionComponent={Fade}
        anchorOrigin={{ vertical: 'center', horizontal:'center' }}
        sx={{position:'absolute'}}
        >
        <MuiAlert onClose={handleClose} severity="success" sx={{ width: '100%', bgcolor:'primary.main', color:'#fff'}}>
          Experience saved!
        </MuiAlert>
      </Snackbar>
  );
}

export default OnSuccessSaveExp;
