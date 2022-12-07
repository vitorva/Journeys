import '@geoapify/geocoder-autocomplete/styles/round-borders.css'
import '../../styles/LocationInput.css'
import {GeoapifyGeocoderAutocomplete, GeoapifyContext} from '@geoapify/react-geocoder-autocomplete'

import {useContext, useState, useEffect, useRef} from 'react';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';

import {Typography} from '@mui/material';

import Tooltip from '@mui/material/Tooltip';

import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import SaveAsIcon from '@mui/icons-material/SaveAs';
import CloseIcon from '@mui/icons-material/Close';

import {SessionContext} from "../userManagement/SessionHelper";
import {sendMyJourneysApiRequest} from "../common/ApiHelper";
import SaveJourneyDialog from '../common/SaveJourneyDialog';


import * as SAVE from "../../constants/saveJourney";

// Scroll to bottom quand ça exécde la div référencée
const AlwaysScrollToBottom = () => {
  const elementRef = useRef();
  useEffect(() => elementRef.current.scrollIntoView());
  return <div ref={elementRef} />;
};

function ItinerarySidebar({route, setRoute, setPois, pois}) {
  const user = useContext(SessionContext);
  const SAVE_ITINERARY_ENDPOINT = "/journey";

  function clear() {
    setRoute({
      startCity: "",
      destCity: "",
      startCoords: [],
      destCoords: [],
      itinerary: {},
      index: {value: 0},
      selectedPois: new Set(),
      poiToIndex: new Map(),
    })
    setPois({...pois, list: []})
    
    removePopup()
    setState({startCity: "", destCity: "", journeyName: ""})
  }

  function removePopup() {
    const popup = document.getElementsByClassName('mapboxgl-popup');
    if (popup.length) {
      popup[0].remove();
    }
  }

  const [state, setState] = useState({startCity: route.startCity, destCity: route.destCity, journeyName: ""})

  function handleChange(e) {
    const name = e.target.name;
    const value = e.target.value;

    setState({
      ...state, [name]: value
    });
  }

  function onStartSelect(value) {
    if(document.getElementById('auto3').children[0].firstChild.value === route.startCity) {
      return
    }

    removePopup()
    if (value !== null) {
      setRoute({
        ...route,
        startCity: document.getElementById('auto3').children[0].firstChild.value,
        startCoords: [value.properties.lon, value.properties.lat],
        itinerary: {},
        selectedPois: new Set(),
        poiToIndex: new Map()
      })
      setState({
        ...state, startCity: document.getElementById('auto3').children[0].firstChild.value
      });
    } else {
      removePopup()
      setPois({...pois, list: []});
      setState({...state, startCity: ""})
      setRoute({
        ...route,
        startCity: "",
        startCoords: [],
        itinerary: {},
        selectedPois: new Set(),
        poiToIndex: new Map()
      });
    }
  }

  function onDestSelect(value) {
    if(document.getElementById('auto4').children[0].firstChild.value === route.startCity) {
      return
    }

    removePopup()
    if (value !== null) {
      setRoute({
        ...route,
        destCity: document.getElementById('auto4').children[0].firstChild.value,
        destCoords: [value.properties.lon, value.properties.lat],
        itinerary: {},
        selectedPois: new Set(),
        poiToIndex: new Map()

      })
      setState({
        ...state, destCity: document.getElementById('auto4').children[0].firstChild.value
      });
    } else {
      removePopup()
      setPois({...pois, list: []});
      setState({...state, destCity: ""})
      setRoute({
        ...route,
        destCity: "",
        destCoords: [],
        itinerary: {},
        selectedPois: new Set(),
        poiToIndex: new Map()
      });
    }
  }

  function deletePOI(index) {
    const tmpSelectedPois = route.selectedPois
    tmpSelectedPois.delete(route.itinerary[index].poiID)

    const tmpItinerary = route.itinerary
    delete tmpItinerary[index]

    setRoute({...route, itinerary: tmpItinerary, selectedPois: tmpSelectedPois})

    removePopup()
  }

  function saveJourney(journeyName) {
    const orderedPOIsArray = Object.keys(route.itinerary).map(index => {
      return route.itinerary[index].poiID
    })

    if (orderedPOIsArray.length === 0) {
      alert("You need to add at least one POI to save the itinerary")
      return
    }

    let body = {
      "title": journeyName,
      "start": {
        "lat": route.startCoords[1],
        "lng": route.startCoords[0]
      },
      "end": {
        "lat": route.destCoords[1],
        "lng": route.destCoords[0]
      },
      "orderedPOIsArray": orderedPOIsArray
    }

    setSaveState(SAVE.STATUS.SAVING)

    sendMyJourneysApiRequest(SAVE_ITINERARY_ENDPOINT, "POST", body).then(() => {
      setSaveState(SAVE.STATUS.SUCCESS)
      clear()
    }, error => {
      setSaveState(SAVE.STATUS.ERROR)
    });
  }

  // MUI FormDialog (pour save a journey)
  const [openDialog, setOpenDialog] = useState(false);
  const [saveState, setSaveState] = useState(SAVE.STATUS.IDLE);

  const openSaveJourneyDialog = () => {
    const orderedPOIsArray = Object.keys(route.itinerary).map(index => {
      return route.itinerary[index].poiID
    })

    if (orderedPOIsArray.length === 0) {
      alert("You need to add at least one POI to save the itinerary")
      return
    }
    setOpenDialog(true);
  };

  const closeSaveJourneyDialog = () => {
    setSaveState(SAVE.STATUS.IDLE)
    setOpenDialog(false);
  };

  return (
    <Box className='itinerarySidebar' sx={{bgcolor:'primary.light'}}>
      <Typography variant="h5" gutterBottom sx={{paddingTop: '10px', color:'primary.contrastText'}}>
        Your Itinerary
      </Typography>

    <Box
        sx={{
          width:'100%',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          flexGrow:1,
          overflowY:'auto',
          scrollbarWidth:'thin',
        }}
      >
      <GeoapifyContext apiKey="40e91d4ae8d84f60914c5c9bf724e6b0">
        <div id = 'auto3' className='locationInput ellipsis'>
          <GeoapifyGeocoderAutocomplete
            name='startCity'
            placeholder="Starting point"
            value={state.startCity}
            filterByCountryCode={['ch']}
            placeSelect={onStartSelect}
          />
        </div>

        {
          Object.keys(route.itinerary).map(index => {
            return (
              <Box
                sx={{
                  minHeight:'40px',
                  bgcolor:'secondary.light',
                  color:'secondary.contrastText',
                  borderRadius:'5px',
                  width:'80%',
                  boxSizing:'borderBox',
                  paddingLeft:'10px',
                  margin:'5px 0px',
                  display:'flex',
                  justifyContent:'space-between',
                  alignItems:'center'}}>
                  <Typography variant='subtitle2' noWrap={true}>
                  {route.itinerary[index].poiName}
                  </Typography>
                  <IconButton aria-label="delete">
                    <CloseIcon fontSize='small' onClick={() => deletePOI(index)}/>
                  </IconButton>
              </Box>
            )
          })
        }
        <div id = 'auto4' className='locationInput ellipsis'>
          <GeoapifyGeocoderAutocomplete
            name='destCity'
            placeholder="Destination"
            value={state.destCity}
            filterByCountryCode={['ch']}
            placeSelect={onDestSelect}
          />
        </div>
        </GeoapifyContext>
        <AlwaysScrollToBottom />
      </Box>

      <Box
        sx={{
          display:'flex',
          margin:'20px',
          height:'5%'
        }}
      >
        <Button
          onClick={() => clear()}
          variant="contained"
          endIcon={<DeleteIcon/>}
          color='primaryButton'
        >
          CLEAR
        </Button>
        {user.session.isUserLoggedIn() ? (
          <>
          <Button

            onClick={openSaveJourneyDialog}
            variant="contained"
            endIcon={<SaveAsIcon/>}
            color='secondaryButton'
            sx={{marginLeft:'10px'}}
          >
            SAVE
          </Button>

          <SaveJourneyDialog
            openDialog={openDialog}
            closeSaveJourneyDialog={closeSaveJourneyDialog}
            save={() => saveJourney(state.journeyName)}
            nameInput="journeyName"
            handleChange={handleChange}
            saveState={saveState}
            />
          </>) : (
          <>
            <Tooltip title="login or register" placement="top">
              <Box sx={{height:"100%"}}>
              <Button
                  id="saveBtn"
                  disabled
                  variant="contained"
                  endIcon={<SaveAsIcon/>}
                  sx={{marginLeft:'10px', height:'100%'}}
                  color='secondaryButton'
                >
                  SAVE
                </Button>
              </Box>
            </Tooltip>
          </>)
        }
      </Box>
    </Box>
  )
}

export default ItinerarySidebar;
