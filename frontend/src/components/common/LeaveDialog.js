import React from 'react'

import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';

function LeaveDialog({openDialog, leave, stay}){
    return(
        <Dialog open={openDialog} 
            sx={{ 
                color:'#687A6E',
                paddingBottom:'15px'}}
        >
                <DialogContent sx={{alignItems:'center', backgroundColor:'#DAE1DB'}}>
                    <DialogContentText textAlign='center'>
                        {"Modificatoins will be lost. Do you want to leave anyway?"}
                    </DialogContentText>
                </DialogContent>
                <DialogActions 
                    sx={{
                        justifyContent:'center', 
                        backgroundColor:'#DAE1DB',
                        paddingBottom: '15px'}}
                    >
                    <Button onClick={stay} variant="contained" color="primaryButton">
                        Stay
                    </Button>
                    <Button onClick={leave} variant="contained" color="primaryButton"
                        sx={{ marginLeft:'10px',}}
                    >
                        Leave
                    </Button>
                        
                </DialogActions>

        </Dialog>
    )
}

export default LeaveDialog;