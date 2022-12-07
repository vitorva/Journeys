import { Box, Typography, Link, Icon } from '@mui/material';

import gitlabLogo from '../../assets/gitlab-icon.jpg';

function About(){

    const logoIcon = (
        <Icon sx={{width:'30px', height:'30px'}}>
        <img alt="myjourneys" src={gitlabLogo} style={{width: "100%", height:'100%'}}/>
        </Icon>
    );

    return(
        <Box className='about' id="about-anchor">
            <Box sx={{
                    display:'flex',
                    flexDirection:{xs:'column', md:'row'},
                    justifyContent:'center',
                    width:{xs:'100%',md:'50%'},
                    alignItems:'center'
                }} 
            >
                <Box color='secondary.contrastText'
                    sx={{
                        width:{xs:'60%', md:'50%'},
                        padding:'5%',
                        lineHeight:'30px'
                    }}
                >
                    <Typography variant='h6' fontWeight='bold' gutterBottom>
                        About us
                    </Typography>
                    <Typography  variant='body1'>
                        We are a friendly and dynamic team from HEIG-VD school. Join us on GitLab!
                    </Typography>
                    <Box sx={{display: 'flex', alignItems:'center'}}>
                    {logoIcon}
                    <Link href="https://gitlab.com/pdg-journeys/journeys" underline="none" color='primary.contrastText'>
                        {'Journeys Project'}
                    </Link>
                    </Box>
                </Box>
                <Box color='secondary.contrastText'
                    sx={{
                        width:{xs:'60%', md:'50%'},
                        padding:'3%',
                        lineHeight:'30px'
                    }}>
                    <Typography variant='h6' fontWeight='bold' gutterBottom>
                        Our Team
                    </Typography>
                    <ul>
                        <li>Ludovic Bonzon</li>
                        <li>Rosalie Chhen</li>
                        <li>Emmanuel Janssens</li>
                        <li>Dalia Maillefer</li>
                        <li>Vitor Vaz Afonso</li>
                    </ul>
                </Box>
            </Box>
        </Box>
    )
}

export default About;
