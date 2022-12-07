import FormDialog from "../userManagement/FormDialog";
import Login from "../userManagement/Login";
import {SessionContext} from "../userManagement/SessionHelper";
import React, {useContext} from "react";
import Box from "@mui/material/Box";
import Register from "../userManagement/Register";

function LoginRegisterButtons() {
  const user = useContext(SessionContext);

  return (
    <Box sx={{display:'flex'}}>
        <FormDialog buttonText="Login"
                    dialogContent={<Login setLoggedUser={loginInfo => {
                        user.session.login(loginInfo)
                    }}/>
                    }/>
        <FormDialog buttonText="Register"
                    dialogContent={<Register setLoggedUser={loginInfo => {
                        user.session.login(loginInfo)
                    }}/>
                    }/>
    </Box>
  )
}

export default LoginRegisterButtons;
