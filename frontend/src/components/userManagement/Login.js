import React, {useState} from "react"
import Box from "@mui/material/Box";
import Avatar from "@mui/material/Avatar";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import {Alert} from "@mui/material";
import {sendMyJourneysApiRequest} from "../common/ApiHelper";

function Login(props) {
  const LOGIN_ENDPOINT = "/auth/authenticate"

  // States used to display error messages after the response of the login request has been received
  const [hasConnectionProblem, setHasConnectionProblem] = useState(false)
  const [hasWrongCredential, setHasWrongCredential] = useState(false)

  // States used to manage username and password changes while typing
  const [userName, setUserName] = useState();
  const [password, setPassword] = useState();

  /**
   * Handles login form submission
   * @param e event that triggered the submit
   */
  const handleSubmit = async e => {
    e.preventDefault();

    await attemptLogin({
      userName,
      password
    });
  }

  /**
   * Attempts a login using our backend API
   * @param credentials username and password of the user wanting to login
   * @returns {Promise<void>}
   */
  async function attemptLogin(credentials) {
    setHasConnectionProblem(false)
    setHasWrongCredential(false)

    sendMyJourneysApiRequest(LOGIN_ENDPOINT, "POST", credentials)
      .then(result => {
        // Sets current session
        props.setLoggedUser({
          username: result.user.userName,
          firstname: result.user.firstName,
          lastname: result.user.lastName,
          email: result.user.email,
          tokenDuration: result.tokenDuration
        })
      }, error => {
        // Error with credentials
        if (error.errorCode === 401 || error.errorCode === 404 || error.errorCode === 403) {
          setHasWrongCredential(true)
        } // Generic error
        else {
          console.log("Error occurred :", error.errorCode, error.response)
          setHasConnectionProblem(true)
        }
      })
  }

  return (
    <Box
      sx={{
        margin: 2,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >
      <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
        <LockOutlinedIcon/>
      </Avatar>
      <Typography component="h1" variant="h5">
        Login
      </Typography>
      <Box component="form" onSubmit={handleSubmit} noValidate sx={{mt: 1}}>
        <TextField
          margin="normal"
          required
          fullWidth
          id="username"
          label="Username"
          name="username"
          autoComplete="username"
          autoFocus
          onChange={e => setUserName(e.target.value)}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          name="password"
          label="Password"
          type="password"
          id="password"
          autoComplete="current-password"
          onChange={e => setPassword(e.target.value)}
        />
        <Button
          id="loginBtn"
          type="submit"
          fullWidth
          variant="contained"
          sx={{mt: 3, mb: 2}}
        >
          Login
        </Button>

        {hasConnectionProblem ? (<Alert severity="error">
          The connection to the server has had a problem
        </Alert>) : (<></>)}
        {hasWrongCredential ? (<Alert severity="warning">
          Wrong username and/or password
        </Alert>) : (<></>)}
      </Box>
    </Box>
  );
}

export default Login
