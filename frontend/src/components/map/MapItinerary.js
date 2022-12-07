import React, {useEffect, useRef, useState} from 'react'
import ReactDOM from 'react-dom';

import maplibregl from 'maplibre-gl'
import 'maplibre-gl/dist/maplibre-gl.css'

import Popup from './Popup';
import {createHint, itinerary, checkMarker, fetchAllPOIs} from './utilsMap.js'

function MapItinerary({route, setRoute, fetchPOIsBetween, centerLongitude, centerLatitude, zoom, pois, setPois,previews,setPreviews}) {

  const mapContainerRef = useRef(null)
  const [map, setMap] = useState(null)

  // Allows us to know if you we can start applying actions on the map
  const [isMapSet, setIsMapSet] = useState(false)

  function setPreviewCollapse(){
    if(route.startCity === "" || route.endCity === ""){
      setPreviews({list:[], clickedPoiId: null})
      return;
    }
    var llb = map.getBounds()
    var prev = []
    for(const poi of pois.list){
      if(llb.contains(new maplibregl.LngLat(poi.coordinates.lng,poi.coordinates.lat)))
      {
          prev.push(poi)
      }
    }
    // Update if necessary
    if(previews.list.length === prev.length){
      return;
    }
    setPreviews({list:prev, clickedPoiId: null, flyTo : (coords) => {
      if(refFlying.current === true){
        return
      }
      createFlyToAction(coords, map)}
    })
  }
  
  const refPreviews = useRef(null)
  useEffect(()=>{
    if(refPreviews.current != null){
      map.off('moveend', refPreviews.current)
    }

    if(pois.list.length<=0){
      setPreviews({list:[], clickedPoiId: null})
      return;
    }

    if (isMapSet) {
      refPreviews.current = setPreviewCollapse
      setPreviewCollapse();
      map.on('moveend', refPreviews.current)
    }
  }, [pois, route])
  
  // On every Map component update
  useEffect(() => {
    setRoute({
      ...route,
      itinerary: {},
      index: {value: 0},
      selectedPois: new Set(),
      poiToIndex: new Map(), // Indicates the index associated to the poi from the popup
    })

    // Center the map on starting point if it exists
    const center = JSON.stringify(route.startCoords) !== "[]" ? [route.startCoords[0], route.startCoords[1]]: [centerLongitude, centerLatitude]

    const map = new maplibregl.Map({
      container: mapContainerRef.current,
      style: `https://api.maptiler.com/maps/streets/style.json?key=H2cC8SlE6frfaVx5G3b0`,
      center: center,
      zoom: zoom,
    })

    map.addControl(new maplibregl.NavigationControl())
    const scale = new maplibregl.ScaleControl({
      maxWidth: 80,
      unit: 'metric'
    })

    map.addControl(scale)

    setMap(map)

    map.on('load', function () {

      // Itinerary source
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

      // Hint source
      map.addSource('hint', {
        'type': 'geojson',
        'data': {
          'type': 'Feature',
          'properties': {},
          'geometry': {
            'type': 'Point',
            'coordinates': []
          }
        }
      });

      // POIs source
      map.addSource('clusterPOI', {
        type: 'geojson',
        data: {
          "type": "FeatureCollection",
          "features": []
        },
        cluster: true,
        clusterMaxZoom: 14, // Max zoom to cluster points on
        clusterRadius: 50 // Radius of each cluster when clustering points (defaults to 50)
      });

      
      // Clusters layer
      map.addLayer({
        id: 'clusters',
        type: 'circle',
        source: 'clusterPOI',
        filter: ['has', 'point_count'],
        paint: {
          // Use step expressions (https://maplibre.org/maplibre-gl-js-docs/style-spec/#expressions-step)
          // with three steps to implement three types of circles:
          //   * Blue, 20px circles when point count is less than 10
          //   * Orange, 30px circles when point count is between 10 and 50
          //   * Green, 40px circles when point count is greater than or equal to 50
          'circle-color': [
            'step',
            ['get', 'point_count'],
            '#df8e73',
            10,
            '#b1bcb4',
            50,
            '#68796e'
          ],
          'circle-radius': [
            'step',
            ['get', 'point_count'],
            20,
            10,
            30,
            50,
            40
          ],
          "circle-opacity": 0.8
        }
      });

      map.addLayer({
        id: 'cluster-count',
        type: 'symbol',
        source: 'clusterPOI',
        filter: ['has', 'point_count'],
        layout: {
          'text-field': '{point_count_abbreviated}',
          'text-font': ['DIN Offc Pro Medium', 'Arial Unicode MS Bold'],
          'text-size': 12
        }
      });

      // Itinerary layer
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

      // POIs layer
      map.addLayer({
        id: 'unclustered-point',
        type: 'circle',
        source: 'clusterPOI',
        filter: ['!', ['has', 'point_count']],
        paint: {
          'circle-color': '#11b4da',
          'circle-radius': 8,
          'circle-stroke-width': 1,
          'circle-stroke-color': '#fff'
        }
      });

      // Hint layer
      map.addLayer({
        id: 'unclustered-point-hint',
        type: 'circle',
        source: 'hint',
        paint: {
          'circle-color': '#df8e73',
          'circle-radius': 8,
          'circle-stroke-width': 1,
          'circle-stroke-color': '#fff'
        }
      });

      // Inspect a cluster on click
      map.on('click', 'clusters', function (e) {
        let features = map.queryRenderedFeatures(e.point, {
          layers: ['clusters']
        });
        let clusterId = features[0].properties.cluster_id;
        map.getSource('clusterPOI').getClusterExpansionZoom(
          clusterId,
          function (err, zoom) {
            if (err) return;

            map.easeTo({
              center: features[0].geometry.coordinates,
              zoom: zoom
            });
          }
        );
      });

      map.on('mouseenter', 'clusters', function () {
        map.getCanvas().style.cursor = 'pointer';
      });
      map.on('mouseleave', 'clusters', function () {
        map.getCanvas().style.cursor = '';
      });
    })

    // Ensures that the elements have been loaded before observing/manipulating them in the useEffect
    function sourceCallback() {
      if (map.getSource('route') && map.isSourceLoaded('route') && map.getSource('clusterPOI') && map.isSourceLoaded('clusterPOI')
        &&  map.getSource('hint') && map.isSourceLoaded('hint')) {
        setIsMapSet(true)
      }
    }
    map.on('sourcedata', sourceCallback);
  }, [])

  // Draw start and dest marker
  const refMarkerStart = useRef({
    marker: null,
    coordinates: "",
  });

  const refMarkerDest = useRef({
    marker: null,
    coordinates: "",
  });
  useEffect(() => {
    if (isMapSet) {
      checkMarker(route.startCoords, refMarkerStart, isMapSet, map)
    }
  }, [route.startCoords, isMapSet]);

  useEffect(() => {
    if (isMapSet) {
      checkMarker(route.destCoords, refMarkerDest, isMapSet, map)
    }
  }, [route.destCoords, isMapSet]);

  // Update the links between the route and a popup (necessary otherwise a popup keeps old links in reference)
  const refCreatePOI = useRef({currentFunc: null})
  useEffect(() => {
    if (isMapSet) {
      if (refCreatePOI.current.currentFunc != null) {
        map.off('click', 'unclustered-point', refCreatePOI.current.currentFunc)
      }
      refCreatePOI.current.currentFunc = createPOI
      map.on('click', 'unclustered-point', refCreatePOI.current.currentFunc)
    }
  }, [isMapSet, route.startCoords, route.destCoords])

  useEffect(() => {
    if (isMapSet) {
      //Draw the itinerary
      map.getSource('route').setData(itinerary(route));
    }
  }, [isMapSet, route])

  const refHintIndex = useRef(0)
  const refFlying = useRef(false)
  useEffect(() => {
    if(isMapSet){
      // Load nearby POIs
      if (JSON.stringify(route.startCoords) !== JSON.stringify([]) && JSON.stringify(route.destCoords) !== JSON.stringify([])) {
      // Create flyto action for each poi
      let allPOIs = fetchAllPOIs(fetchPOIsBetween, route, map)
      allPOIs.then(value => setPois({...pois, list: value, clickedPoiId: null, flyTo : (coords) => {
        if(refFlying.current === true){
          return
        }
        createFlyToAction(coords, map)}}))
     }
    }
  }, [isMapSet, route.startCoords, route.destCoords])

  function createFlyToAction(coords, map){
    // Ensures that the marker is drawn before moving to the point
    function onSourceData() {
      if (map.isSourceLoaded('hint')) {
        map.flyTo({
        center: [coords[0] - 0.005, coords[1]],
        zoom: 15 // This animation is considered essential with respect to prefers-reduced-motion
        });

      map.off('sourcedata', onSourceData);
      }
    }
    map.on('sourcedata', onSourceData);

    createHint(map,coords, refFlying, refHintIndex)

    const removeHint = () => {
      refFlying.current = false
      const hintIndex =  refHintIndex.current
      setTimeout(() => {
        if(hintIndex === refHintIndex.current){
          map.getSource('hint').setData({
            "type": "FeatureCollection",
            "crs": {"type": "name", "properties": {"name": "urn:ogc:def:crs:OGC:1.3:CRS84"}},
              "features": [{
              "type": "Feature",
              "properties": {},
              "geometry": {"type": "Point", "coordinates": []}
              }]
          })
        }
      }, 5000);
      map.off('moveend', hintIndex)
    }

    map.on('moveend', removeHint)
  }

  const createPOI = (e) => {
    let coordinates = e.features[0].geometry.coordinates.slice();
    let poiName = e.features[0].properties.name
    let poiID = e.features[0].properties.id

    // Ensure that if the map is zoomed out such that
    // multiple copies of the feature are visible, the
    // popup appears over the copy being pointed to.
    while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
      coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
    }

    const placeholder = document.createElement('div');
    placeholder.className = "popup-container";

    ReactDOM.render(<Popup isAdd={route.selectedPois.has(poiID) === false} coordinates={coordinates} poiName={poiName}
                           poiID={poiID} route={route} setRoute={setRoute}/>, placeholder);

    new maplibregl.Popup()
      .setLngLat(coordinates)
      .setDOMContent(placeholder)
      .addTo(map);
  }

  return (
    <div className="map-container">
      <div ref={mapContainerRef} className="map"/>
    </div>
  );
}

export default MapItinerary
