import React, {useState} from 'react';
import MapItinerary from "./MapItinerary";
import ItinerarySidebar from "./ItinerarySidebar";

import '../../styles/MapApp.css'
import PreviewSidebar from './PreviewSidebar';
import Header from "./Header";
import Search from './Search';

function MapApp({route, setRoute, fetchCityCoords, fetchPOIsBetween}) {
  const [pois, setPois] = useState({
    list: [], // list of pois loaded on the map and to be displayed in the sidebar preview
    clickedPoiId: null, // the poidId that has been clicked from the sidebar preview
  })

  
  const [previews, setPreviews] = useState( {
    list: [], // liste de pois chargés sur la map et à afficher dans la sidebar preview
    clickedPoiId: null, // le poid id qui a été cliqué depuis la sidebar preview
  })

  const [preview,showPreview] = useState(false)
  return (
    <div className="map-app-root">
      <Header/>
      <ItinerarySidebar
        route={route}
        setRoute={setRoute}
        fetchCityCoords={fetchCityCoords}
        setPois={setPois}
      />

      <PreviewSidebar previews={previews} setPreviews={setPreviews}/>

      <Search pois={pois} setPois={setPois}/>

      <MapItinerary route={route}
           setRoute={setRoute}
           fetchPOIsBetween={fetchPOIsBetween}
           centerLongitude={6.63333}
           centerLatitude={46.53333}
           zoom={8}
           pois={pois}
           setPois={setPois}
           previews={previews}
           setPreviews={setPreviews}
      />
    </div>
  )
}

export default MapApp;
