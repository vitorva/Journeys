import maplibregl from 'maplibre-gl'

function itinerary(route) {
    const itinerary = [];

    for (const key in route.itinerary) {
      const element = route.itinerary[key].coordinates;
      itinerary.push(element)
    }

    itinerary.unshift(route.startCoords)
    itinerary.push(route.destCoords)

    return {
      'type': 'Feature',
      'properties': {},
      'geometry': {
        'type': 'LineString',
        'coordinates': itinerary
      }
    }
  }

function createHint(map, coords, refFlying, refHintIndex){
    refFlying.current = true
    refHintIndex.current =  refHintIndex.current + 1
    map.getSource('hint').setData({
      "type": "FeatureCollection",
      "crs": {"type": "name", "properties": {"name": "urn:ogc:def:crs:OGC:1.3:CRS84"}},
      "features": [{
        "type": "Feature",
        "properties": {},
        "geometry": {"type": "Point", "coordinates": [coords[0], coords[1]]}
        }]
     })
  }

function createMarker(position, map, el = null) {
    const marker = new maplibregl.Marker(el)
        .setLngLat(position)
        .addTo(map);
        return marker
}

function cleanClusters(isMapSet, map) {
    if (isMapSet) {
      //reset clusters
      map.getSource('clusterPOI').setData({
        "type": "FeatureCollection",
        "crs": {"type": "name", "properties": {"name": "urn:ogc:def:crs:OGC:1.3:CRS84"}},
        "features": []
      })
    }
  }

//Update start and dest markers
function checkMarker(currentCoords, ref, isMapSet, map) {
    if (JSON.stringify(currentCoords) !== JSON.stringify([])) {
        if (ref.current.marker != null && (ref.current.coordinates !== currentCoords)) {
            ref.current.marker.remove()
        }
        ref.current.coordinates = currentCoords
        ref.current.marker = createMarker([currentCoords[0], currentCoords[1]], map)
    } else {
        // When the already setted start or destination has been changed
        if (ref.current.marker != null) {
            ref.current.marker.remove()
            cleanClusters(isMapSet, map)
          }
        }
      }

async function fetchAllPOIs(fetchPOIsBetween, route, map) {
    let allPOIs = await fetchPOIsBetween(route.startCoords, route.destCoords, 30);
    let features = [];
    for (const poi of allPOIs) {
    features.push({
            "type": "Feature",
            "properties": {
                "id": poi.id,
                "name": poi.name,
                "select": false,
                "img": "/api/" + poi.url     
            },
            "geometry": {"type": "Point", "coordinates": [poi.coordinates.lng, poi.coordinates.lat]}
        })
    }
    
    map.getSource('clusterPOI').setData({
            "type": "FeatureCollection",
            "crs": {"type": "name", "properties": {"name": "urn:ogc:def:crs:OGC:1.3:CRS84"}},
            "features": features
        })
    return allPOIs
}

  export {createHint, itinerary, checkMarker, fetchAllPOIs};