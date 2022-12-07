import React, {useEffect, useRef, useState} from 'react'

import maplibregl from 'maplibre-gl'
import 'maplibre-gl/dist/maplibre-gl.css'
import {sendMyJourneysApiRequest} from "../common/ApiHelper";

const JOURNEY_ENDPOINT = "/journey/"

function MapSavedItinerary(props) {
  const id = props.id

  const mapContainerRef = useRef(null)
  const [map, setMap] = useState(null)
  const [isMapSet, setIsMapSet] = useState(false)
  const [isFetchDone, setIsFetchDone] = useState(false)
  const refFetch = useRef(null)

  useEffect(() => {
    fetchJourney()
  }, [])

  async function fetchJourney() {
    const fetchJourney = await sendMyJourneysApiRequest(JOURNEY_ENDPOINT, "GET", {}, {}, id, null)
      .then(result => {
          return result
        },
        error => {
          console.log(error)
        })

    refFetch.current = fetchJourney
    setIsFetchDone(true)

    return fetchJourney
  }

  useEffect(() => {
    if (isFetchDone === false) {
      return
    }

    const map = new maplibregl.Map({
      container: mapContainerRef.current,
      style: `https://api.maptiler.com/maps/streets/style.json?key=H2cC8SlE6frfaVx5G3b0`,
      center: [refFetch.current.start.lng, refFetch.current.start.lat],
      zoom: 8,
    })

    map.addControl(new maplibregl.NavigationControl())
    const scale = new maplibregl.ScaleControl({
      maxWidth: 80,
      unit: 'metric'
    })

    map.addControl(scale)

    setMap(map)

    map.on('load', function () {

      map.addSource('route', {
        'type': 'geojson',
        'data': {
          'type': 'Feature',
          'properties': {},
          'geometry': {
            'type': 'LineString',
            'coordinates': []
          }
        }
      });

      map.addLayer({
        'id': 'route',
        'type': 'line',
        'source': 'route',
        'layout': {
          'line-join': 'round',
          'line-cap': 'round'
        },
        'paint': {
          'line-dasharray': [1, 2],
          'line-color': '#df8e73',
          'line-width': 5
        }
      });

      map.addSource('POI', {
        type: 'geojson',
        data: {
          "type": "FeatureCollection",
          "features": []
        }
      });

      map.addLayer({
        id: 'unclustered-point',
        type: 'circle',
        source: 'POI',
        paint: {
          'circle-color': '#11b4da',
          'circle-radius': 8,
          'circle-stroke-width': 1,
          'circle-stroke-color': '#fff'
        }
      });
    })

    // Ensures that the elements have been loaded before observing/manipulating them in the useEffect
    function sourceCallback() {
      if (map.getSource('route') && map.isSourceLoaded('route') && map.getSource('POI') && map.isSourceLoaded('POI')) {
        setIsMapSet(true)
      }
    }

    map.on('sourcedata', sourceCallback);

  }, [isFetchDone])

  useEffect(() => {
    if (isFetchDone === false) {
      return
    }

    const fetchJourney = refFetch.current

    // Ordered experiences
    const orderedExperiences = fetchJourney.experiences.sort((function (a, b) {
      return a.order - b.order;
    }))

    const itinerary = orderedExperiences.map(exp => ([exp.pointOfInterest.coordinates.lng, exp.pointOfInterest.coordinates.lat]))

    const start = [fetchJourney.start.lng, fetchJourney.start.lat]

    const dest = [fetchJourney.end.lng, fetchJourney.end.lat]

    let features = itinerary.map(feature => ({
      "type": "Feature",
      "geometry": {"type": "Point", "coordinates": [feature[0], feature[1]]}
    }));

    itinerary.unshift(start)
    itinerary.push(dest)

    map.getSource('route').setData({
      'type': 'Feature',
      'properties': {},
      'geometry': {
        'type': 'LineString',
        'coordinates': itinerary
      }
    });

    map.getSource('POI').setData({
      "type": "FeatureCollection",
      "crs": {"type": "name", "properties": {"name": "urn:ogc:def:crs:OGC:1.3:CRS84"}},
      "features": features
    })

    //Create markerStart
    new maplibregl.Marker()
      .setLngLat(start)
      .addTo(map);

    //Create markerDest
    new maplibregl.Marker()
      .setLngLat(dest)
      .addTo(map);
  }, [isMapSet]);


  return (
    <div className="map-container-savedItinerary">
      <div ref={mapContainerRef} className="map-savedItinerary"/>
    </div>
  );
}

export default MapSavedItinerary
