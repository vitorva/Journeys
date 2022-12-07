import Typography from '@mui/material/Typography'
import CardContent from '@mui/material/CardContent';
import CardActionArea from '@mui/material/CardActionArea';
import CardMedia from '@mui/material/CardMedia';
import placeholder from '../../../assets/placeholder.png';


function LogbookCardActionArea(props) {
  return (
    <CardActionArea id={props.id} onClick={props.onClick} name={props.id}
                    sx={{
                      height: '90%',
                      width: '100%',
                      '&:hover': {
                        color: 'black',
                        bgcolor: 'card.main',
                      },
                      display: 'flex',
                      flexDirection: 'column',
                      justifyContent: 'start',
                      bgcolor: 'card.light',
                    }}>
      {
        props.img === null ? (
          <CardMedia
          component="img"
          height="70%"
          image={placeholder}
          alt="preview image"
        />
        ) : (
          <CardMedia
          component="img"
          height="70%"
          image={"/api/journey/"+props.id+"/image/"+props.img}
          alt="preview image"
        />
        )
      }

      <CardContent height="50%" width='100%' sx={{
        boxSizing: 'border-box',
        maxHeight: '50%',
        display: 'flex',
        justifyContent: 'center',
      }}>
        <Typography gutterBottom variant="h6" color='primary.contrastText' component="div" sx={{inlineSize: '100%', overflow: 'hidden'}}>
          {props.title}
          {/*approx. max 50 characters*/}
        </Typography>
      </CardContent>
    </CardActionArea>
  );
}

export default LogbookCardActionArea;
