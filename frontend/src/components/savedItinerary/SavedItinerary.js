import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router-dom';

import '../../styles/SavedItinerary.css';

import MapSavedItinerary from './MapSavedItinerary';
import Header from './Header';
import ExperiencesList from './ExperiencesList';

import {sendMyJourneysApiRequest} from "../common/ApiHelper";

const JOURNEY_ENDPOINT = "/journey/"

function SavedItinerary(props) {
  const {id} = useParams(); // get the id of the url, so the id of the trip

  const [state, setState] = useState({tripId:"", tripTitle:"", experiences: []})

  async function fetchExperiences() {
    sendMyJourneysApiRequest(JOURNEY_ENDPOINT, "GET", {}, {}, id, "/experience")
      .then(result => {
          const orderExperiences = result[0].experiences.sort((function (a, b) {
            return a.order-b.order;
          }))
          setState({tripId:result[0].id, tripTitle: result[0].title, experiences: orderExperiences})
        },
        error => {
          console.log(error)
        })
  }

  const [refresh, setRefresh] = useState(0)
  useEffect(() => {
    fetchExperiences();
  }, [refresh]);


  let allTripExp = []
  allTripExp = state.experiences.map(experience => ({title: experience.pointOfInterest.id}))

  return (
    <div className="savedItinerary">
      <Header tripTitle={state.tripTitle}/>
      <MapSavedItinerary experiences={allTripExp} id={id}/>
      <ExperiencesList tripId={state.tripId} experiences={state.experiences} refresh={refresh} setRefresh={setRefresh}/>
    </div>
  );
}

export default SavedItinerary;
