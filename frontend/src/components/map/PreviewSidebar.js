import React, {useState, useEffect} from "react";

import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Pagination from '@mui/material/Pagination';
import Collapse from '@mui/material/Collapse';

import useWindowDimensions from '../common/WindowDimensions';
import PreviewCard from './PreviewCard';
import placeholder from '../../assets/placeholder.png';
 

function PreviewSidebar({previews, setPreviews}) {

  // window size
  const { height, width } = useWindowDimensions(); 

  const [currentPage, setCurrentPage] = useState(1);  // page number
  const [loading, setLoading] = useState(true);
  const [poisPerPage, setPoisPerPage] = useState(Math.floor((height-36)/88));
  const [nbPages, setNbPages] = useState(previews.list.length/poisPerPage);
  const [spacingBetweenCards, setSpacing] = useState(1+((height-36-(88*poisPerPage))/(poisPerPage*8)))

  // pois to display
  const [poiCardList,setPoiCardList] = useState([])




  useEffect(()=>{
    setPoiCardList(previews.list.map(poi => {
      // Get a preview image (for CustomCard of a poi) from the first experience of a journey
      var imgPreview = placeholder;
      const randomJourney = poi.journey;
      if(!(randomJourney
        && Object.keys(randomJourney).length === 0
        && Object.getPrototypeOf(randomJourney) === Object.prototype)){
          const keyJourneyId = Object.keys(randomJourney)[0];
          const valueFilename = randomJourney[`${keyJourneyId}`];
          imgPreview ="/api/journey/"+keyJourneyId+"/image/"+valueFilename
      }

      return <PreviewCard
        onClick = {() => {
          previews.flyTo([poi.coordinates.lng,poi.coordinates.lat]);
          setPreviews({...previews, clickedPoiId: poi.id})}
        }
        sx={{width: '95%',}}
        txt={poi.name}
        img={imgPreview}
        key={poi.id}/>
      }
    ))
  },[previews])

  useEffect(() => {
    setNbPages(Math.ceil(previews.list.length/poisPerPage))
  }, [previews.list.length, poisPerPage])

  useEffect(() => {
    const newPoisPerPage = Math.floor((height-36)/88)
    setPoisPerPage(Math.floor((height-36)/88))

    // 88 = 80px + 8px = height of a card + space between 2 cards
    // 36 = 10 + 26 = padding of previewSidebar + pagination height
    setSpacing(1+((height-36-(88*newPoisPerPage))/(newPoisPerPage*8)))
  }, [height])

  useEffect(() => {
    if(poiCardList.length === 0){
      setLoading(true)
      setCurrentPage(1)
    } else {
      setLoading(false)
    }
  }, [poiCardList]);

  const indexOfLastPoi = currentPage * poisPerPage;
  const indexOfFirstPoi = indexOfLastPoi - poisPerPage;
  const currentPois = poiCardList.slice(indexOfFirstPoi, indexOfLastPoi)

  const handleChange = (e, pageNbr) => {
    setCurrentPage(pageNbr)
  };

  // responsive pagination
  if(!loading && currentPage > nbPages){
    setCurrentPage(nbPages)
  } 

  return (
      <Collapse orientation="horizontal" in={!loading} sx={{zIndex:4}}>
        <Box className='previewSidebar'>
            <Stack spacing={spacingBetweenCards} sx={{width: '100%',}}>
              {currentPois}
              <div className='paginationContainer'>
                <Pagination count={nbPages} size="small" siblingCount={0} page={currentPage} onChange={handleChange}/>
              </div>
            </Stack>
          </Box>
      </Collapse>
  );
}
  
  export default PreviewSidebar;
  
