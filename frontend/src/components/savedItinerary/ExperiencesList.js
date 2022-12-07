import React from 'react'
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';

import ExperienceItem from '../common/ExperienceItem';

import '../../styles/Logbook.css';
import '../../styles/SavedItineraryCarousel.css';

function CustomCarousel(props) {

  const responsive = {
    desktop: {
      breakpoint: {max: 3000, min: 1024},
      items: 4,
      slidesToSlide: 1 // optional, default to 1.
    },
    tablet: {
      breakpoint: {max: 1024, min: 464},
      items: 2,
      slidesToSlide: 1 // optional, default to 1.
    },
    mobile: {
      breakpoint: {max: 464, min: 0},
      items: 1,
      slidesToSlide: 1 // optional, default to 1.
    }
  };

  return (
    <Carousel
      swipeable={false}
      draggable={false}
      responsive={responsive}
      ssr={true} // means to render carousel on server-side.
      infinite={true}
      keyBoardControl={true}
      transitionDuration={500}
      containerClass="carousel-container-expList"
      deviceType={props.deviceType}
    >

      {props.items}

    </Carousel>
  )
}

function ExperiencesList(props) {

  const experiences = props.experiences.map((exp) => {
      const poiID = exp.pointOfInterest.id

      return <ExperienceItem 
        refresh={props.refresh}
        setRefresh={props.setRefresh}
        tripId={props.tripId}
        poiId={poiID}
        title={exp.pointOfInterest.name} 
        description={exp.description} 
        images={exp.images}/>
  });

  return (
    <div className="experiencesList">
      <CustomCarousel
        items={experiences}
      />
    </div>
  );
}

export default ExperiencesList;
