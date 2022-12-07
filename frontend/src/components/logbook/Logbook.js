import React, {useContext, useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';

import Header from "./Header";
import UserList from "./UserList";
import CarouselItemTrip from './journeyItem/CarouselItemTrip';
import ExperienceItem from '../common/ExperienceItem';

import '../../styles/Logbook.css';
import {SessionContext} from "../userManagement/SessionHelper";
import {sendMyJourneysApiRequest} from "../common/ApiHelper";

const USER_ENDPOINT = "/user/"
const JOURNEY_ENDPOINT = "/journey/"

function Logbook() {
  const user = useContext(SessionContext);

  // Retrieves the journeys of a user
  const [state, setState] = useState({journeys: []})

  // Fetch experiences of a poi
  async function fetchJourneys() {
    sendMyJourneysApiRequest(USER_ENDPOINT, "GET", {}, {}, user.session.getUserName(), "/journeys")
      .then(result => {
          setState({journeys: result.journeys})
        },
        error => {
          console.log(error)
        })
  }

  useEffect(() => {
    fetchJourneys();
  }, []);

  async function handleJourneyDelete(journeyID) {
    sendMyJourneysApiRequest(JOURNEY_ENDPOINT, "DELETE", {}, {}, journeyID, null)
      .then(() => {
          // Deletes journey in local state (avoids page reload or API call)
          const updatedJourneys = state.journeys.filter(journey => journey.id !== journeyID)
          setState({journeys: updatedJourneys})
        },
        error =>
          console.log("Error occurred :", error.errorCode, error.response))
  }

  let allUserTrips = state.journeys

  const navigate = useNavigate();

  function onClickTrip(e) {
    navigate(`/logbook/trip/${e.currentTarget.name}`);
  }

  function lookForFirstImage(trip){
    for(const exp of trip){
      if(exp.images !== null && exp.images.length !== 0){
        return exp.images[0];
      }
    }
    return null
  }

  const trips =
    allUserTrips !== [] ?
      allUserTrips.map((trip) => {
        const firstImgFilename = lookForFirstImage(trip.experiences)
        return <CarouselItemTrip 
          id={trip.id} 
          title={trip.title} 
          exp={trip.experiences} 
          img={firstImgFilename}
          onClick={onClickTrip}
          handleJourneyDelete={handleJourneyDelete}/>
        }
      ) : null

  let experienceCards = []

  for (const trip of allUserTrips) {
    for (const exp of trip["experiences"]) {
      experienceCards.push(
        <ExperienceItem
          tripId={trip.id}
          poiId={exp.pointOfInterest.id}
          title={exp.pointOfInterest.name}
          description={exp.description}
          images={exp.images}
        />
      )
    }
  }

  return (
    <div className="logbook">
      <Header user={user}/>
      <UserList title="My journeys" list={trips}/>
      <UserList title="My experiences" list={experienceCards}/>
    </div>
  );
}

export default Logbook;
