import React from 'react'
import { useNavigate } from 'react-router-dom';

import Icon from '@mui/material/Icon';
import Button from '@mui/material/Button';

import logo from '../../assets/logo_transparent.png';

function LogoButton(props){

    const logoIcon = (
        <Icon sx={{width:'100%', height:'100%'}}>
        <img alt="myjourneys" src={logo} style={{width: "100%", height:'100%'}}/>
        </Icon>
    );
    
    const navigate = useNavigate();

    function onClick(){
        navigate(`/`);
    }

    return(
        <Button sx={{
            height:'40px',
            padding: '0',
            backgroundColor:'#DF8E7300',
            boxShadow: 'none',
            '&:hover': {
                backgroundColor: '#DF8E7300',boxShadow: 'none',},
            }}
        variant="contained"
        onClick={onClick}
        > 
        {logoIcon}
        </Button>
    )
}

export default LogoButton