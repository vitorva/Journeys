import React from 'react'

import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import DeleteIcon from '@mui/icons-material/Delete';

function SearchInput(props){
    
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
            <SearchIcon sx={{pointerEvents:'none'}}/>
        </IconButton>
        <IconButton 
            name={props.nameBtn}
            onClick={props.onClickDelete}>
            <DeleteIcon sx={{pointerEvents:'none'}}/>
        </IconButton>
    </Paper>
    )
}

export default SearchInput