import Box from "@mui/material/Box";
import Avatar from "@mui/material/Avatar";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import React, {useState} from "react";
import {sendMyJourneysApiRequest} from "../common/ApiHelper";
import {Alert} from "@mui/material";

function Register(props) {
  const REGISTER_ENDPOINT = "/auth/signup"

  // States used to display generic errors on form submit
  const [hasRegistrationProblem, setHasRegistrationProblem] = useState(false)
  const [hasWrongCredential, setHasWrongCredential] = useState(false)

  // States used to display precise errors on every input
  const [isFirstNameInvalid, setIsFirstNameInvalid] = useState({state: false, helperText: ""})
  const [isLastNameInvalid, setIsLastNameInvalid] = useState({state: false, helperText: ""})
  const [isUserNameInvalid, setIsUserNameInvalid] = useState({state: false, helperText: ""})
  const [isEmailInvalid, setIsEmailInvalid] = useState({state: false, helperText: ""})
  const [isPasswordInvalid, setIsPasswordInvalid] = useState({state: false, helperText: ""})

  /**
   * Handles register form submission
   * @param e event that triggered the submit
   */
  const handleSubmit = async e => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);

    // Reset states to hide previous error messages
    setIsFirstNameInvalid({...isFirstNameInvalid, state: false, helperText: ""})
    setIsLastNameInvalid({...isLastNameInvalid, state: false, helperText: ""})
    setIsUserNameInvalid({...isUserNameInvalid, state: false, helperText: ""})
    setIsEmailInvalid({...isEmailInvalid, state: false, helperText: ""})
    setIsPasswordInvalid({...isPasswordInvalid, state: false, helperText: ""})

    if (validate(data)) {
      await attemptRegister({
        firstName: data.get('firstName'),
        lastName: data.get('lastName'),
        userName: data.get('userName'),
        email: data.get('email'),
        password: data.get('password')
      });
    }
  }

  /**
   * Attempts a user registration using our backend API
   * @param credentials user's registration credentials
   * @returns {Promise<any>}
   */
  async function attemptRegister(credentials) {
    sendMyJourneysApiRequest(REGISTER_ENDPOINT, "POST", credentials)
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
        if (error.errorCode === 404) {
          setHasWrongCredential(true)
        } // Generic error
        else {
          console.log("Error occurred :", error.errorCode, error.response)
          setHasRegistrationProblem(true)
        }
      })
  }

  /**
   * Function to validate every form inputs
   * @param data form data
   * @returns {boolean} true if the form is valid, false otherwise
   */
  function validate(data) {
    let isValid = true; // Used instead of using states because setState is asynchronous

    if (!data.get("firstName")) {
      setIsFirstNameInvalid({...isFirstNameInvalid, state: true, helperText: "Missing first name"})
      isValid = false;
    }
    if (!data.get("lastName")) {
      setIsLastNameInvalid({...isLastNameInvalid, state: true, helperText: "Missing last name"})
      isValid = false;
    }
    if (!data.get("userName")) {
      setIsUserNameInvalid({...isUserNameInvalid, state: true, helperText: "Missing username"})
      isValid = false;
    }
    if (!emailValidation(data.get("email"))) {
      setIsEmailInvalid({...isEmailInvalid, state: true, helperText: "Email address is misformated"})
      isValid = false;
    }
    if (!data.get("password")) {
      setIsPasswordInvalid({...isPasswordInvalid, state: true, helperText: "Missing password"})
      isValid = false;
    }

    //return !(isFirstNameInvalid.state || isLastNameInvalid.state || isUserNameInvalid.state || isEmailInvalid.state || isPasswordInvalid.state)
    return isValid;
  }

  /**
   * Function to check email address formatting against a regex
   * @param email to verify
   * @returns {boolean} true if the email is valid, false otherwise
   */
  function emailValidation(email) {
    const regex = /[^@ \t\r\n]+@[^@ \t\r\n]+\.[^@ \t\r\n]+/;
    return !(!email || regex.test(email) === false);
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
        <AccountCircleIcon/>
      </Avatar>
      <Typography component="h1" variant="h5">
        Register
      </Typography>
      <Box component="form" noValidate onSubmit={handleSubmit} sx={{mt: 3}}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <TextField
              id="firstName"
              label="First Name"
              name="firstName"
              fullWidth
              autoFocus
              autoComplete="given-name"
              required
              error={isFirstNameInvalid.state}
              helperText={isFirstNameInvalid.state ? isFirstNameInvalid.helperText : ""}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              id="lastName"
              label="Last Name"
              name="lastName"
              fullWidth
              autoComplete="family-name"
              required
              error={isLastNameInvalid.state}
              helperText={isLastNameInvalid.state ? isLastNameInvalid.helperText : ""}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              id="username"
              label="Username"
              name="userName"
              autoComplete="username"
              error={isUserNameInvalid.state}
              helperText={isUserNameInvalid.state ? isUserNameInvalid.helperText : ""}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              error={isEmailInvalid.state}
              helperText={isEmailInvalid.state ? isEmailInvalid.helperText : ""}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="new-password"
              error={isPasswordInvalid.state}
              helperText={isPasswordInvalid.state ? isPasswordInvalid.helperText : ""}
            />
          </Grid>
        </Grid>
        <Button
          id="registerBtn"
          type="submit"
          fullWidth
          variant="contained"
          sx={{mt: 3, mb: 2}}
        >
          Register
        </Button>

        {hasWrongCredential ? (<Alert severity="warning">
          Problem with username and/or password
        </Alert>) : (<></>)}
        {hasRegistrationProblem ? (<Alert severity="error">
          There has been an error during the registration process
        </Alert>) : (<></>)}
      </Box>
    </Box>
  );
}

export default Register;
