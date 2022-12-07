import React, {useState, useContext} from 'react'
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import Drawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Typography from '@mui/material/Typography'
import {SessionContext} from "../userManagement/SessionHelper";

function ProfileButton(props){

    const user = useContext(SessionContext);
    const navigate = useNavigate();

    // MUI drawer
    const [state, setState] = useState(false)

    const toggleDrawer =
    (rightAnchor, open) =>
    (event) => {
    if (
        event &&
        event.type === 'keydown' &&
        event.key === 'Tab' ||
        event.key === 'Shift')
    {
        return;
    }
    setState({ ...state, [rightAnchor]: open });
    };

    // list displayed when clicking on the profile
    const list = (rightAnchor) => (
        <Box sx={{ 
          width: '250px', 
          height: '100%', 
          boxSizing:'border-box', 
          padding: '20px',
          bgcolor:'card.light',
          color:'secondary.contrastText'
        }}
          role="presentation"
          onClick={toggleDrawer(rightAnchor, false)}
          onKeyDown={toggleDrawer(rightAnchor, false)}
        >
            <Typography variant='subtitle2'>
              Username 
            </Typography>
            <Typography variant='h6' fontWeight='bold' gutterBottom>
              { user.session.getUserName()}
            </Typography>

            <Typography variant='subtitle2'>
              E-mail 
            </Typography>
            <Typography variant='h6' fontWeight='bold' gutterBottom>
              { user.session.getUserEmail()}
            </Typography>

            <Typography variant='subtitle2'>
              Lastname 
            </Typography>
            <Typography variant='h6' fontWeight='bold' gutterBottom>
              { user.session.getUserLastName()}
            </Typography>

            <Typography variant='subtitle2'>
              Firstname 
            </Typography>
            <Typography variant='h6' fontWeight='bold' gutterBottom>
              {user.session.getUserFirstName()}
            </Typography>
            <Box sx={{display:'flex', justifyContent:'center', marginTop: 3}}>
              <Button 
                color='secondaryButton'
                variant="contained" 
                onClick={() => {
                  navigate(`/map`);
                  user.session.logout();
                }}>
                    Logout
              </Button>
            </Box>
        </Box>
      );
    
    return(
        <div>
        <React.Fragment key={'right'}>
            <Button 
                color='secondaryButton'
                variant="contained"
                onClick={toggleDrawer('right', true)}
                endIcon={<AccountCircleIcon/>}
                sx={{marginLeft: '10px',}}
            >
                { user.session.getUserName()}
            </Button>
            
            <Drawer
              anchor={'right'}
              open={state['right']}
              onClose={toggleDrawer('right', false)}
            >
              {list('right')}
            </Drawer>
        </React.Fragment>
      </div>

    )
}

export default ProfileButton