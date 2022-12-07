import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {

      primary: {
        light: '#DAE1DB',
        main: '#A6CABD',
        dark: '#699B86',
        contrastText: '#687A6E',
      },

      secondary:{
        light: '#F0E4D8',
        main: '#E2B4A5',
        dark: '#DF8E73',
        contrastText: '#664E4A',
      },

      
      primaryButton:{
        light: '#A6CABD',
        main: '#699B86',
        dark:'#527A6A',
        contrastText: '#fff',
      },

      secondaryButton:{
        light:'#E2B4A5',
        main: '#DF8E73',
        dark:'#D56B48',
        contrastText: '#fff',
      },

      card:{
        light: '#F9F5F0',
        main: '#F0E4D8',
      },

      greyEmpty:{
        light: '#E0E5E8',
        main: '#989898',
      }

    },
  });