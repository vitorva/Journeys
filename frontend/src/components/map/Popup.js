import React, {useState, useEffect} from 'react'
import {sendMyJourneysApiRequest} from "../common/ApiHelper";

import placeholder from '../../assets/placeholder.png';

import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";

import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import NavigateBeforeIcon from '@mui/icons-material/NavigateBefore';
import AddLocationAltIcon from '@mui/icons-material/AddLocationAlt';
import DeleteIcon from '@mui/icons-material/Delete';

const POI_ENDPOINT = "/poi/"

function Popup({isAdd, coordinates, poiName, poiID, route, setRoute}) {
  const [state, setState] = useState({
    experiences: [{}], // Default value required otherwise error on first rendering
    index : 0,
    isAddValue : isAdd,
  })

  function addToItinerary(coordinates, poiName, poiID) {
    const newItinerary = route.itinerary
    newItinerary[route.index.value] = {coordinates, poiName, poiID}

    const newPoiToIndex = route.poiToIndex
    newPoiToIndex.set(poiID, route.index.value)

    const newSelectedPois = route.selectedPois
    newSelectedPois.add(poiID)

    const newIndex = route.index
    newIndex.value = newIndex.value + 1

    setRoute(
      {...route}
    )
  }

  var experiencesFetch = []
  // Recover the experiences of a POI
  async function fetchExperiences() {
    experiencesFetch = await sendMyJourneysApiRequest(POI_ENDPOINT, "GET", {}, {}, poiID, "/experience")
      .then(result => {
          return result
        },
        error => {
          console.log(error)
        })

    const validExperiences = experiencesFetch.filter(experience => experience.description !== null || experience.images !== null)

    if (validExperiences.length > 0) {
      setState({...state, experiences: validExperiences})
    }
  }

  useEffect(() => {
    fetchExperiences();
  }, []);


  const addOrDeleteBtn = state.isAddValue === true ? 
    <Button 
      endIcon={<AddLocationAltIcon/>}
      sx={{
        color:'#fff',
        fontWeight: 'bolder',
        backgroundColor: '#699B86', 
        '&:hover': {
            backgroundColor: '#527A6A',},
        height: {md:'60%', sx:'90%'},
      }}
      onClick = {() => {
        addToItinerary(coordinates, poiName, poiID);
        setState({...state, isAddValue : false})       
      }}
    >
      ADD
    </Button> 
      :  
    <Button 
      endIcon={<DeleteIcon/>}
      sx={{
        color:'#fff',
        fontWeight: 'bolder',
        backgroundColor: '#699B86', 
        '&:hover': {
            backgroundColor: '#527A6A',},
        height: {md:'60%', sx:'90%'},
      }}
      onClick = {() => {
        const newSelectedPois = route.selectedPois
        newSelectedPois.delete(poiID)
    
        const newItinerary = route.itinerary
        const newPoiToIndex = route.poiToIndex

        delete newItinerary[newPoiToIndex.get(poiID)]
        newPoiToIndex.delete(poiID)
        
        setRoute({...route})
        setState({...state, isAddValue : true})
      }}
    >
      delete
    </Button>

  return (
    <Card sx={{ 
        width: '400px', 
        height:'100%',
        bgcolor:'#F9F5F0',
        borderRadius:'10px',
        display:'flex',
        flexDirection:'column'
      }}
    >
      <CardHeader sx={{boxSizing:'border-box', height: '20%',color:'#687A6E', bgcolor:'#A6CABD', }}
        avatar={<AccountCircleIcon sx={{color:'#fff'}}/> }
        titleTypographyProps={{variant:'subtitle1', color:'#fff', fontWeight: 'bolder' }}
        title={poiName}
        subheader=
          
          {state.experiences[state.index]["creator"]}
      />
      
      { 
        state.experiences[state.index] === undefined 
        || state.experiences[state.index] === null
        || state.experiences[state.index].images === undefined
        || state.experiences[state.index].images.length === 0 ? 
          <CardMedia 
            component="img" 
            height="200" 
            image={placeholder} 
            alt="placeholder"
          />
          : 
          <CustomCarousel
              key={state.experiences[state.index].journeyId}
              items={state.experiences[state.index].images.map(img =>{
              return <CardMedia 
                component="img" 
                height="200px" 
                image={"api/journey/"+state.experiences[state.index].journeyId+"/image/"+img} 
                alt={"api/journey/"+state.experiences[state.index].journeyId+"/image/"+img}/>
              })
            }
          />
      }

      <CardContent 
        sx={{
          boxSizing:'border-box',
          maxHeight:'100px',
          overflowY:'auto', 
          scrollbarWidth:'thin', 
          scrollbarColor:'#F0E4D8 #F9F5F0',
        }}
      >
        <Typography variant="body2" color='#86635B'>
          {state.experiences[state.index]["description"]}
        </Typography>
      </CardContent>

      <Box sx={{display:'flex', justifyContent:'center', alignItems:'center',height: '15%', mt:'10px'}}>
        <CardActions sx={{
            boxSizing:'border-box',
            height: '100%', 
            width: '100%', 
            display:'flex', 
            justifyContent:'space-between', 
            padding: 0,
          }} 
          disableSpacing 
          color='#687A6E'
        >
          <IconButton aria-label="navigate before" 
            onClick={() => {
              if (state.index === 0) {
                  const newIndex = state.experiences.length - 1 
                  setState({...state, index : newIndex })
              } else {
                  const newIndex = state.index - 1 
                  setState({...state, index : newIndex})
              }
            }}
          >
            <NavigateBeforeIcon sx={{color:'#699B86'}}/>
          </IconButton>
          {addOrDeleteBtn}
          <IconButton aria-label="navigate next"
            onClick={() => {   
              if (state.index === state.experiences.length - 1) {
                  setState({...state, index : 0})
              }else {
                  const newIndex = state.index + 1
                  setState({...state, index :newIndex})
              }
            }}
          >
            <NavigateNextIcon sx={{color:'#699B86'}}/>
          </IconButton>
        </CardActions>
      </Box>
    </Card>
  )
}

const responsive = {
  desktop: {
    breakpoint: {max: 3000, min: 1024},
    items: 1,
    slidesToSlide: 1 // optional, default to 1.
  },
  tablet: {
    breakpoint: {max: 1024, min: 464},
    items: 1,
    slidesToSlide: 1 // optional, default to 1.
  },
  mobile: {
    breakpoint: {max: 464, min: 0},
    items: 1,
    slidesToSlide: 1 // optional, default to 1.
  }
};


  // Used to display the experience's images
  function CustomCarousel(props) {
    return (
      <Carousel 
        key={props.key} 
        activeIndex={0}
        swipeable={false}
        draggable={false}
        responsive={responsive}
        ssr={true} // means to render carousel on server-side.
        infinite={false}
        keyBoardControl={true}
        transitionDuration={500}
        containerClass="carousel-container-expPopover"
        deviceType={props.deviceType}
      >
        {props.items}
      </Carousel>
    )
  }

export default Popup;