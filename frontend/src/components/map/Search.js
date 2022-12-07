import useMediaQuery from '@mui/material/useMediaQuery';

import Autocomplete from '@mui/material/Autocomplete';
import ListItemText from '@mui/material/ListItemText';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';

function Search({pois,setPois}){
    const matches = useMediaQuery('(min-width:1000px)');

    return (
        <Autocomplete
        className='searchPoi'
        id="combo-box-demo"
        options={pois.list}
        sx={{ 
          width: '300px',
          zIndex:10,
          position: 'relative',
          marginLeft: '10px',
          marginTop:'10px',
          visibility: matches?'visible':'hidden',
          pointerEvents:'none'
          }}
        getOptionLabel={(option) => ( option.name)}
        onChange = {(props,poi) => {
            if(poi != null){
                pois.flyTo([poi.coordinates.lng,poi.coordinates.lat]); 
                setPois({...pois, clickedPoiId: poi.name})}
            }
          } 
        renderOption={(props,poi) => (
        <Box component="li" sx={{ mr: 2, flexShrink: 0 }}  {...props} key={poi.id} >
        {poi.name}
          </Box>  
        )}
        renderInput={(params) => (
          <TextField className='listItemPoi'
            {...params}
            label="Find a poi"
            sx={{ 
              pointerEvents:'all'
              }}

                        inputProps={{
            ...params.inputProps,
            autoComplete: 'new-password', // disable autocomplete and autofill
          }}
          />
        )}
      />
    )
}

export default Search
