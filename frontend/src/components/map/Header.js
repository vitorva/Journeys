import React, {useContext} from 'react'
import Box from '@mui/material/Box'

import LogoButton from '../common/LogoButton'
import ProfileButton from '../common/ProfileButton';
import LoginRegisterButtons from '../common/LoginRegisterButtons';
import MyjourneysButton from '../common/MyjourneysButton';
import {SessionContext} from '../userManagement/SessionHelper'


function Header(props){
    const user = useContext(SessionContext);

    return(
      <Box className='headerMap'>
          <Box className='headerMap-right'>
                <LogoButton/>
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
      </Box >
    )
}

export default Header
