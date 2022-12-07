import React, {useEffect, useState} from 'react'

import placeholder from '../../assets/placeholder.png';

import Typography from '@mui/material/Typography';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import CardActionArea from '@mui/material/CardActionArea';

import AddIcon from '@mui/icons-material/Add';

import ExperiencePopover from './ExperiencePopover';

// Represents an "experience" and its onClick pop up
function ExperienceItem({tripId, poiId, title, description, images, refresh, setRefresh}) {
    // get first image or placeholder
    const urlFirstImage = (images !== null && images.length !== 0) ?
    "/api/journey/"+tripId+"/image/"+ images[0] : placeholder

    const isEmptyImage = description === null && (images === null || images.length === 0) 

    // Mui basic popover
    const [anchorEl, setAnchorEl] = useState(null);

    function onClickExp(e){
        setAnchorEl(e.currentTarget);
    }

    return (
        <Card className="experienceCard" sx={{
            width:'100%',
            height: '100%',
            borderRadius: '10px',
            backgroundColor: 'card.light',
            }}>
            <CardActionArea onClick={onClickExp}
                sx={{
                height: '100%',
                width: '100%',
                '&:hover': {
                    color: 'black',
                    backgroundColor: 'card.main',},
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'start',
                }}>
                <CardMedia
                    component="img"
                    height="50%"
                    image={urlFirstImage}
                    alt="image preview"
                />
                <CardContent height="50%" width='100%' sx={{
                    boxSizing:'border-box',
                    maxHeight: '50%',
                    textAlign: 'center',
                    color: '#687A6E',
                }}
                >
                    <Typography gutterBottom variant="h6" component="div" sx={{inlineSize: '100%', overflow:'hidden'}}>
                        {title}
                        {/*approx. max 50 characters*/}
                    </Typography>
                    {isEmptyImage?
                    <><Typography gutterBottom variant="subtitle1"  component="div" sx={{inlineSize: '100%', overflow:'hidden'}}>
                        Share your experience
                    </Typography>
                    <AddIcon className="addExperienceIcon" fontSize='large'/></>
                    :<></>
                    }
                </CardContent>
            </CardActionArea>
            <ExperiencePopover
                anchorEl={anchorEl}
                setAnchorEl={setAnchorEl}
                tripId={tripId}
                poiId={poiId}
                imgFilenames={images}
                title={title}
                description={description}
                editMode={isEmptyImage}
                refresh={refresh}
                setRefresh={setRefresh}
                />
        </Card>
    );
}

export default ExperienceItem;
