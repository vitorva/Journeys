import React from 'react'
import {useNavigate} from 'react-router-dom';

import Card from '@mui/material/Card';

import OptionsListCard from './OptionsListCard';
import LogbookCardActionArea from './LogbookCardActionArea';

// Represents the card preview in the "my trips" carousel
function CarouselItemTrip(props) {

  const navigate = useNavigate();

  function onClickTrip(e) {
    navigate(`/logbook/trip/${e.currentTarget.name}`);
  }

  return (
    <Card sx={{
      width: '100%',
      height: '100%',
      borderRadius: '10px',
    }}>
      <LogbookCardActionArea
        onClick={onClickTrip}
        id={props.id}
        title={props.title}
        img={props.img}
      />
      <OptionsListCard id={props.id} handleJourneyDelete={props.handleJourneyDelete}/>
    </Card>
  );
}

export default CarouselItemTrip;
