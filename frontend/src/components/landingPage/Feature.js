import * as React from 'react';
import Box from '@mui/material/Box';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';

function Feature(props){
    
    if(props.left){
        return(
        <Box className = 'feature' sx={{ display: 'flex',width:'100%',bgcolor:'#ff37000e', }}>
        <CardMedia
            component="img"
            sx={{ width:{xs:'0', md:'50%'}}}
            image={props.img}
            alt={props.title}
        />

        <Box sx={{ 
                display: 'flex', 
                flexDirection: 'column' ,
                justifyContent: 'center',
                padding:'5% 10%',
                lineHeight: '32px',
                width:{xs:'100%',md:'50%'},
                
            }}
        >
            <CardContent sx={{
                    display:'flex', 
                    flex: '1 0 auto', 
                    justifyContent:'center', 
                    flexDirection:'column',
                }}
            >
                <Typography gutterBottom component="div" variant="h5" color="secondary.contrastText">
                    {props.title}
                </Typography>
                <Typography variant="subtitle1" color="secondary.contrastText" component="div">
                    {props.txt}
                </Typography>
            </CardContent>
        </Box>
        </Box>
    )}else {
        return(
            <Box className = 'feature' sx={{ display: 'flex',width:'100%', }}>
                <Box sx={{ 
                        display: 'flex', 
                        flexDirection: 'column' ,
                        justifyContent: 'center',
                        padding:'5% 10%',
                        lineHeight: '32px',
                        width:{xs:'100%',md:'50%'},
                    }}
                >
                    <CardContent sx={{
                            display:'flex', 
                            flex: '1 0 auto',
                            justifyContent:'center', 
                            flexDirection:'column',
                        }}
                    >
                        <Typography gutterBottom component="div" variant="h5" color="primary.contrastText">
                            {props.title}
                        </Typography>
                        <Typography variant="subtitle1" color="primary.contrastText" component="div">
                            {props.txt}
                        </Typography>
                    </CardContent>
                </Box>

                <CardMedia
                component="img"
                sx={{ width:{xs:'0', md:'50%'}}}
                image={props.img}
                alt={props.title}
                />
            </Box>
        )
    }
    
}

export default Feature;
