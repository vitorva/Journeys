import {Link} from "react-router-dom";
import '@geoapify/geocoder-autocomplete/styles/round-borders.css'
import '../../styles/LocationInput.css'
import {GeoapifyGeocoderAutocomplete, GeoapifyContext} from '@geoapify/react-geocoder-autocomplete'

import Button from '@mui/material/Button';
import Box from '@mui/material/Box';

function Hero({setRoute, route}) {
  function isEmpty(str) {
    return !str.trim().length;
  }

  // Handle press on "start" button by asking a forward geocoding API the coordinates of the given start and destination cities
  // and adding them to the global route state
  async function handleSubmit(e) {
    let inputStartCity = document.getElementsByClassName("geoapify-autocomplete-input")[0].value
    let inputDestCity = document.getElementsByClassName("geoapify-autocomplete-input")[1].value

    if (isEmpty(route.startCity) || isEmpty(route.destCity) || route.startCity !== inputStartCity || route.destCity !== inputDestCity) {
      e.preventDefault();
      alert("Check your input for the starting point or destination (you must provide both locations and select a suggestion from the dropdown)");
    }
  }

  function onStartSelect(value) {
    if (value !== null) {
      setRoute({
        ...route,
        startCity: document.getElementById('auto1').children[0].firstChild.value,
        startCoords: [value.properties.lon, value.properties.lat]
      })
    } else {
      setRoute({
        ...route,
        startCity: "",
        startCoords: []
      })
    }
  }

  function onDestSelect(value) {
    if (value !== null) {
      setRoute({
        ...route,
        destCity: document.getElementById('auto2').children[0].firstChild.value,
        destCoords: [value.properties.lon, value.properties.lat]
      })
    } else {
      setRoute({
        ...route,
        destCity: "",
        destCoords: []
      })
    }
  }

  return (
    <Box className='hero' id="back-to-top-anchor">
      <Box className='inputs-and-button'>
      <Box className='inputs'>
        <GeoapifyContext apiKey="40e91d4ae8d84f60914c5c9bf724e6b0">
          <div id ='auto1'className="locationInput ellipsis">
          <GeoapifyGeocoderAutocomplete
            name='startCity'
            placeholder="Starting point"
            filterByCountryCode={['ch']}
            placeSelect={onStartSelect}
          />
          </div>
          <div id ='auto2' className="locationInput ellipsis">
          <GeoapifyGeocoderAutocomplete
            name='destCity'
            placeholder="Destination"
            filterByCountryCode={['ch']}
            placeSelect={onDestSelect}
          />
          </div>
        </GeoapifyContext>
      </Box>

      <Link style={{textDecoration: "none", marginLeft : "20px"}} to={{
          pathname: '/map'
        }}>
          <Button sx={{
              height:'53px',
              width: '100px',
              borderRadius:'10px',
              ml:{md:'10px'},
              mt:{xs:'20px',md:'0'}
            }}
            onClick={e => handleSubmit(e)}
            variant="contained"
            color='primaryButton'
          >
            START
          </Button>
        </Link>
        </Box>
    </Box>
  )
}

export default Hero;
