import React from 'react'

import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

import Dialog from '@mui/material/Dialog';
import DialogTitle  from '@mui/material/DialogTitle';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';

import MyjourneysButton from './MyjourneysButton';

import * as SAVE from "../../constants/saveJourney";

function SaveJourneyDialog({
    openDialog, closeSaveJourneyDialog, 
    state, 
    save, 
    handleChange, nameInput,
    saveState}){
    
    return(
        <form onSubmit={e => { e.preventDefault(); }}>
            <Dialog open={openDialog} 
                sx={{ 
                    color:'#687A6E',
                    paddingBottom:'15px'}}
            >
                {saveState === SAVE.STATUS.IDLE && 
                    <>
                    <DialogContent sx={{alignItems:'center', bgcolor:'primary.light'}}>
                        <DialogContentText textAlign='center'>
                            {SAVE.DIALOG_MSG.ASK_TITLE}
                        </DialogContentText>
                        <Paper component="form"
                            sx={{
                                p: '2px 4px', 
                                display: 'flex', 
                                alignItems: 'center',
                                width: '80%',
                                margin: '10px',
                                paddingLeft: '10px',
                            }}>
                                
                            <InputBase sx={{ml: 1, flex: 1,}} 
                                type='text' 
                                placeholder={'Title'}
                                name={nameInput}
                                onChange={handleChange}
                                />
                        </Paper>
                    </DialogContent>
                    <DialogActions 
                        sx={{
                            justifyContent:'center', 
                            bgcolor:'primary.light',
                            paddingBottom: '15px'}}
                        >
                        <Button 
                            onClick={closeSaveJourneyDialog} 
                            variant="contained"
                            color='primaryButton'
                        >
                            Cancel
                        </Button>
                        <Button 
                            onClick={save} 
                            variant="contained"
                            color='secondaryButton'
                            sx={{marginLeft:'10px',}}
                        >
                            Save
                        </Button>
                            
                    </DialogActions>
                    </>
                }

                {saveState === SAVE.STATUS.SAVING &&
                    <>
                    <DialogContent sx={{bgcolor:'primary.light'}}>
                        <DialogContentText textAlign='center'>
                            {SAVE.DIALOG_MSG.SAVING}
                        </DialogContentText>
                    </DialogContent>
                    </>
                }
                
                {saveState === SAVE.STATUS.SUCCESS &&
                    <>
                    <DialogTitle textAlign='right'
                        sx={{
                            bgcolor:'primary.light',
                            height:'20px'}}>
                        <IconButton
                            aria-label="close"
                            onClick={closeSaveJourneyDialog}
                            sx={{
                                position: 'absolute',
                                right: 8,
                                top: 8,
                                color: (theme) => theme.palette.grey[500],
                            }}
                            >
                            <CloseIcon />
                        </IconButton>
                    </DialogTitle>
                    <DialogContent sx={{alignItems:'center', bgcolor:'primary.light'}}>
                        <DialogContentText textAlign='center'>
                            {SAVE.DIALOG_MSG.SUCCESS}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions 
                        sx={{
                            justifyContent:'center', 
                            bgcolor:'primary.light',
                            paddingBottom: '15px'}}>
                        <MyjourneysButton/>
                    </DialogActions>
                    </>
                }

                {saveState === SAVE.STATUS.ERROR &&
                    <>
                    <DialogContent sx={{bgcolor:'primary.light'}}>
                        <DialogTitle textAlign='right'>
                            <IconButton
                                aria-label="close"
                                onClick={closeSaveJourneyDialog}
                                sx={{
                                    position: 'absolute',
                                    right: 8,
                                    top: 8,
                                    color: (theme) => theme.palette.grey[500],
                                }}
                                >
                                <CloseIcon />
                            </IconButton>
                        </DialogTitle>
                        <DialogContentText textAlign='center'>
                            {SAVE.DIALOG_MSG.ERROR}
                        </DialogContentText>
                    </DialogContent>
                    </>
                }

            </Dialog>
        </form>
    )
}

export default SaveJourneyDialog