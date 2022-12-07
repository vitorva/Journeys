import React from 'react'
import Card from '@mui/material/Card'
import CardActionArea from '@mui/material/CardActionArea'
import CardMedia from '@mui/material/CardMedia'
import CardContent from '@mui/material/CardContent'
import { Typography } from '@mui/material'

function PreviewCard(props){
    
    return(
      <Card>
        <CardActionArea onClick={props.onClick} name={props.nameId} // name is used for "onClick
          sx={{
            display: 'flex',
            justifyContent: 'flex-start',
            height: 80,
            '&:hover': {
              color: 'black',
              backgroundColor: 'white',
          }}}
        >
          <CardMedia c
            sx={{
              height: 100,
              width: 100,
            }}
              component="img"
              image={props.img}
              alt={props.txt}
          />
          <CardContent>
            <Typography gutterBottom component="h4" color="#787878"
              sx={{fontSize: '0.875rem',}}>
              {props.txt}
            </Typography>
          </CardContent>
          
        </CardActionArea>
      </Card>
    )
}

export default PreviewCard
