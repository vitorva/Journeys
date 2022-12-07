import React, {useState} from 'react';
import LandingPageApp from './components/landingPage/LandingPageApp';
import MapApp from './components/map/MapApp';
import NoMatch from "./components/common/NoMatch";
import Logbook from './components/logbook/Logbook';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import {SESSION_DURATION, SessionHelper, SessionContext} from "./components/userManagement/SessionHelper";
import SavedItinerary from './components/savedItinerary/SavedItinerary';
import {sendMyJourneysApiRequest} from "./components/common/ApiHelper";
import moment from "moment";
import Login from "./components/userManagement/Login";
import Register from "./components/userManagement/Register";

// For color and font styling
import {ThemeProvider} from '@mui/material/styles';
import {theme} from './styles/StyleTheme';


function App() {
  const SESSION_REFRESH_ENDPOINT = "/auth/authrefresh"

  const userStorage = localStorage.getItem('user')
  const userObject = userStorage === null ? {} : JSON.parse(userStorage)
  const [userSession, setUserSession] = useState(userObject)

  const session = new SessionHelper(userSession, setUserSession)

  const user = {
    userInfo: userSession,
    session: session,
  }

  // If the user is logged in, we set a timeout to send a request to refresh the token
  if (session.isUserLoggedIn()) {

    const expirationMoment = moment(session.getExpirationDate()).subtract(3, "minutes")
    const diff = moment.duration(expirationMoment.diff(moment()));

    // Check that the diff is less than
    const duration = diff.asMilliseconds() < 0 ? 3000 : diff.asMilliseconds();

    // If the session hasn't yet expired we add a timeout to refresh the token right before expiration
    setTimeout(() => {
      const refreshTokenRequest = () => sendMyJourneysApiRequest(SESSION_REFRESH_ENDPOINT, 'GET').then(result => {
        session.login({
          username: result.user.userName,
          firstname: result.user.firstName,
          lastname: result.user.lastName,
          email: result.user.email,
          tokenDuration: result.tokenDuration
        })
        setTimeout(refreshTokenRequest, SESSION_DURATION)
      }, error => {
        console.log("Refresh token error: ", error)
      })

      refreshTokenRequest()

    }, duration)
  }

  const [route, setRoute] = useState({
    startCity: "",
    destCity: "",
    startCoords: [],
    destCoords: [],
    itinerary: {}
  })

  function fetchCityCoordinates(cityName) {
    try {
      // Switzerland bbox (bounding box) : min latitude: 45.6755, max latitude: 47.9163, min longitude: 5.7349, max longitude: 10.6677.
      // MapTiler API query param : ?bbox=5.7349,45.6755,10.6677,47.9163
      if (cityName === "") {
        return []
      }
      return fetch("https://api.maptiler.com/geocoding/" + cityName + ".json?key=H2cC8SlE6frfaVx5G3b0&lang=fr&bbox=5.7349,45.6755,10.6677,47.9163").then(value => value.json().then(r => r.features[0] ? r.features[0].center : []));
    } catch (err) {
      console.log(err);
    }
  }

  function fetchPOIsBetween(startCoords, destCoords, count, radius = 0.2) {
    const POIS_BETWEEN_ENDPOINT = "/poi/map";

    return sendMyJourneysApiRequest(POIS_BETWEEN_ENDPOINT, "GET", {}, {
      startLat: `${encodeURIComponent(startCoords[1])}`,
      startLng: `${encodeURIComponent(startCoords[0])}`,
      destLat: `${encodeURIComponent(destCoords[1])}`,
      destLng: `${encodeURIComponent(destCoords[0])}`,
      radius: radius
    }).then(response => {
        return response
      },
      error => {
        console.log(error);
      })
  }

  return (
    <React.StrictMode>
      <SessionContext.Provider value={user}>
        <BrowserRouter>
          <ThemeProvider theme={theme}>
            <Routes>
              <Route path="/"
                     element={<LandingPageApp route={route} setRoute={setRoute}
                                              fetchCityCoords={fetchCityCoordinates}/>}/>
              <Route path="map"
                     element={<MapApp route={route} setRoute={setRoute} fetchCityCoords={fetchCityCoordinates}
                                      fetchPOIsBetween={fetchPOIsBetween}/>}/>
              <Route path="*" element={<NoMatch/>}/>
              <Route path="/error" element={<NoMatch/>}/>
              <Route path="logbook" element=
                {user.session.isUserLoggedIn() ? (
                  <Logbook/>
                ) : (
                  <Login setLoggedUser={loginInfo => {
                    user.session.login(loginInfo)
                  }}/>
                )}
              />
              <Route path="logbook/trip/:id"
                     element={user.session.isUserLoggedIn() ? (
                       <SavedItinerary/>
                     ) : (
                       <Login setLoggedUser={loginInfo => {
                         user.session.login(loginInfo)
                       }}/>
                     )}/>
            </Routes>
          </ThemeProvider>
        </BrowserRouter>
      </SessionContext.Provider>
    </React.StrictMode>
  )
}

export default App;
