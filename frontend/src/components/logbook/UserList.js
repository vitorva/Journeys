import React from 'react'
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';

import { Typography, Box } from '@mui/material';

import '../../styles/Logbook.css';
import '../../styles/LogbookCarousel.css';

function CustomCarousel(props){

    const responsive = {
        desktop: {
          breakpoint: { max: 3000, min: 1024 },
          items: 4,
          slidesToSlide: 3 // optional, default to 1.
        },
        tablet: {
          breakpoint: { max: 1024, min: 464 },
          items: 2,
          slidesToSlide: 1 // optional, default to 1.
        },
        mobile: {
          breakpoint: { max:520, min: 0 },
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
            keyBoardControl={false}
            transitionDuration={500}
            containerClass="carousel-container"
            deviceType={props.deviceType}
            >

            {props.items}

        </Carousel>    
    )
}

function UserList(props) {

  return (
    <div className="userListLogbook">
        <Typography variant="h5" className='title1' gutterBottom fontWeight='bolder'
          sx={{padding: '1.5vw',}}>
            {props.title}
        </Typography>

        <Box sx={{
            flex: '1 0 auto',
            backgroundColor: '#F0E4D8',
            borderRadius: '25px',
            height:'80%',
            width:'100%',
            boxSizing:'border-box',
            padding:'20px 0',
        }}>
          <CustomCarousel 
            items={props.list} 
            />
        </Box>
    </div>
  );
}

export default UserList;