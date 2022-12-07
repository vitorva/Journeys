import React, {useEffect, useState} from 'react'
import Carousel from 'react-multi-carousel';

import placeholder from '../../assets/placeholder.png';

import { styled } from '@mui/material/styles';
import Popover from '@mui/material/Popover';
import Typography from '@mui/material/Typography';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import CardHeader from '@mui/material/CardHeader';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';

import SaveAsIcon from '@mui/icons-material/SaveAs';
import EditIcon from '@mui/icons-material/Edit';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';

import OnSuccessSaveExp from '../common/OnSuccessSaveExp';

import '../../styles/SavedItinerary.css';
import '../../styles/PopoverCarousel.css';

const Input = styled('input')({
    display: 'none',
  });

// Used to display the experience's images
function CustomCarousel(props) {

    const responsive = {
      desktop: {
        breakpoint: {max: 3000, min: 1024},
        items: 1,
        slidesToSlide: 1 // optional, default to 1.
      },
      tablet: {
        breakpoint: {max: 1024, min: 464},
        items: 1,
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
        infinite={false}
        keyBoardControl={true}
        transitionDuration={500}
        containerClass="carousel-container-expPopover"
        deviceType={props.deviceType}
      >
        {props.items}
      </Carousel>
    )
  }

function ExperiencePopover({anchorEl, setAnchorEl,tripId, poiId, imgFilenames, title, description, editMode, refresh, setRefresh}) {
    
    // MUI Popover
    const open = Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    // Get images 
    var urlImages = [];
    if(imgFilenames !== null && imgFilenames.length !== 0){
        for(const imgFilename of imgFilenames){
            urlImages.push("/api/journey/"+tripId+"/image/"+imgFilename)
        }
    }

    // Handle edit mode (edit experience)
    const [editing, setEditing] = useState(editMode);

    function switchEditMode(){
        setEditing(!editing)
    }

    // Cancel button action
    function cancelModifications(){
        switchEditMode();
        setExp({description:description, images:urlImages})
    }

    // Mui snackbar alert success (after user has saved experience)
    const [openSuccessAlert, setOpenSuccessAlert] = useState(false);

    // Experience state, used to make a "preview" of what user is editing
    const [exp, setExp] = useState({description:description, images:urlImages}) // images:allImages

    function onDescriptionChange(e){
        const userText = e.target.value;
        setExp({...exp, description:userText})
    }

    function onImagesChange(e){
        const userFiles = e.target.files;
        var userUrlImages = []
        for(const file of userFiles){
            userUrlImages.push(URL.createObjectURL(file))
        }
        setExp({...exp, images:exp.images.concat(userUrlImages)})
    }

    function saveExperience() {        
        // FormData to PUT to API
        let formData = new FormData();
        var selectedFiles = document.querySelector('#exp-images-input').files;

        var textInput = exp.description;
        formData.append('journeyId', tripId);  
        formData.append('poiId', poiId);

        if(textInput !== null){
            // return user input 
            formData.append('description', textInput);
        }
            
        // send new filename uploaded by user
        if (selectedFiles.length !== 0){
            for(const filename of selectedFiles){
                formData.append('images', filename);
            }
        }
        
        try {            
            return fetch("/api/journey", {
                method: 'PUT',
                body: formData,
            }).then(response => {
                if (response.status >= 200 && response.status <= 299) {
                    setRefresh(++refresh)
                    setOpenSuccessAlert(true);
                    switchEditMode();
                  return response.json()
                } else {
                  console.log("error")
                }
              })
          } catch (err) {
            console.log(err);
        }
    }


    // MUI Dialog (to prevent user modifications will be lost if he doesn't save)
    const [openDialog, setOpenDialog] = useState(false);
    const [left, setLeft] = useState(false);

    const openLeaveDialog = () => {
        setLeft(false)
        setOpenDialog(true);
    };

    function leave(){
        setOpenDialog(false)
        setLeft(true)
    } 

    function stay(){
        setOpenDialog(false)
        setLeft(false)
    }

    const handleClose = () => {
        setAnchorEl(null);
    };

    useEffect(()=>{
        if(left){
            setAnchorEl(null)
        }
    },[left])

    return (
        <Popover PaperProps={{style: { width: '25%', height:'60%', borderRadius: '20px', },}}
            id={id}
            open={open}
            anchorEl={anchorEl}
            onClose={handleClose}
            anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'center',
            }}
            transformOrigin={{
                vertical: 'bottom',
                horizontal: 'center',
            }}
        >
            <OnSuccessSaveExp open={openSuccessAlert} setOpen={setOpenSuccessAlert}/>
            <Card sx={{
                width:'100%', 
                height: '100%', 
                overflowY: 'auto',
                scrollbarWidth: 'thin',
                scrollbarColor: 'card.main card.light',
                backgroundColor: 'card.light',
            }}> 

                <CardHeader
                    titleTypographyProps={{variant:'subtitle1', color:'primary.contrastText', fontWeight: 'bolder', textAlign: 'center', }}
                    subheaderTypographyProps={{variant:'subtitle2', color:'primary.contrastText', textAlign: 'center', }}
                    title={title}
                    action={editing?
                            <></>
                          :
                          <IconButton aria-label="see images" onClick={switchEditMode}>
                            <EditIcon/>
                          </IconButton>
                        }
                />
                { 
                    exp.images.length === 0 ? 

                        <CardMedia 
                            component="img" 
                            height="200" 
                            image={placeholder} 
                            alt="placeholder"
                        />
                    : 
                        <CustomCarousel
                            items={exp.images.map((img) =>{
                                    return <CardMedia 
                                        component="img" 
                                        height="200" 
                                        image={img} 
                                        alt={img}/>
                                })
                            }
                        />
                }
                <CardContent >
                    
                    {editing? 
                    <>
                    <label htmlFor="exp-images-input">
                        <Input accept="image/*" id="exp-images-input" multiple type="file" onChange={onImagesChange} />            
                        <Button 
                            aria-label="upload photos" 
                            variant="contained"
                            endIcon={<AddPhotoAlternateIcon/>}
                            sx={{width:'100%', marginBottom:'10px'}}
                            component="span"
                        >
                            Add photos
                        </Button>
                        
                    </label>
                    <TextField sx={{width:'100%'}}
                        onChange={onDescriptionChange}
                        id="exp-description-input"
                        multiline
                        defaultValue={exp.description===null?"Ton expérience...":exp.description}
                    />
                    </> 
                    : 
                    <>
                    <Typography variant='body2' color='primary.contrastText'>
                        {exp.description === null ? "" : exp.description}
                    </Typography></>
                    }
                </CardContent>
                {editing? 
                    <><CardActions sx={{display:'flex', justifyContent:'center', marginTop:0}}>
                        <Button
                            onClick={cancelModifications}
                            variant="contained" 
                            color='secondaryButton'
                        >
                            CANCEL
                        </Button>
                        <Button
                            onClick={saveExperience}
                            variant="contained" 
                            endIcon={<SaveAsIcon/>} 
                            color='secondaryButton'
                            sx={{marginLeft:'10px'}}
                        >
                            SAVE
                        </Button>
                    </CardActions>
                    </> 
                    : <></>
                    }
            </Card>
        </Popover>
    );
}

export default ExperiencePopover;