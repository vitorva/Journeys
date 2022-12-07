import React, {useState, useContext} from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';

import LogoButton from '../common/LogoButton'
import MyjourneysButton from '../common/MyjourneysButton';
import ProfileButton from '../common/ProfileButton';
import {SessionContext} from '../userManagement/SessionHelper'
import LoginRegisterButtons from '../common/LoginRegisterButtons';


const ResponsiveAppBar = (props) => {
  const user = useContext(SessionContext);

  // MUI AppBar
  const [anchorElNav, setAnchorElNav] =useState(null);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  return (
    <AppBar className='navbar' position="fixed" sx={{bgcolor:'secondary.light', }}>
      <Container maxWidth="100%">
        <Toolbar disableGutters>
          <Box sx={{display:{xs:'none', md: 'flex'}}}>
            <LogoButton/>
          </Box>
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
            >
              <MenuItem 
                  onClick={props.onClickHome}
                  sx={{ color: 'secondary.contrastText', display: 'block' }}
              >
                  Home
              </MenuItem>
              <MenuItem 
                  onClick={props.onClickFeatures}
                  sx={{ color: 'secondary.contrastText', display: 'block'}}
              >
                  Features
              </MenuItem>
              <MenuItem 
                  onClick={props.onClickAbout}
                  sx={{ color: 'secondary.contrastText', display: 'block' }}
              >
                  About
              </MenuItem>

              {user.session.isUserLoggedIn()?(
              <>
                <MenuItem>
                  <MyjourneysButton/>
                </MenuItem>
                <MenuItem>
                  <ProfileButton/>
                </MenuItem>
              </>
              ):(
                <>
                  <MenuItem>
                    <LoginRegisterButtons/>
                  </MenuItem>
                </>
              )}
            </Menu>
          </Box>

          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <LogoButton/>
          </Box>
          
          <Box sx={{ flexGrow: 1, justifyContent:'end', alignItems:'center', display: { xs: 'none', md: 'flex' } }}>
            <Button
                onClick={props.onClickHome}
                sx={{ my: 2, color: 'secondary.contrastText', display: 'block', height:'40%' }}
              >
                Home
            </Button>
            <Button
                onClick={props.onClickFeatures}
                sx={{ my: 2, color: 'secondary.contrastText', display: 'block', height:'40%' }}
              >
                Features
            </Button>
            <Button
                onClick={props.onClickAbout}
                sx={{ my: 2, color: 'secondary.contrastText', display: 'block', height:'40%' }}
              >
                About
            </Button>

            {user.session.isUserLoggedIn()?(
              <>
                <MyjourneysButton/>
                <ProfileButton/>
              </>
            ):(
              <>
                <LoginRegisterButtons/>
              </>
            )}
            
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};
export default ResponsiveAppBar;