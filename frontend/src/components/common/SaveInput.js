import React from 'react'

import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SaveAsIcon from '@mui/icons-material/SaveAs';

function SaveInput(props){
    
    return(
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
            value={props.value} 
            placeholder={props.placeholder}
            name={props.nameInput}
            onChange={props.onChange}/>
        <IconButton 
            name={props.nameBtn}
            onClick={props.onClick}>
            <SaveAsIcon sx={{pointerEvents:'none'}}/>
        </IconButton>
    </Paper>
    )
}

export default SaveInput