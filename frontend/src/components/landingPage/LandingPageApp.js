import Hero from './Hero';
import Feature from './Feature';
import NavBar from './NavBar';
import About from './About';

import featureImg1 from '../../assets/map_carte.png';
import featureImg2 from '../../assets/featureImg2.png';
import featureImg3 from '../../assets/featureImg3.png';

import '../../styles/LandingPage.css';
import '../../styles/App.css';

function LandingPageApp({setRoute, route, fetchCityCoords}) {

  const goToHome = (event) => {
    const anchor = (
      (event.target).ownerDocument || document
    ).querySelector('#back-to-top-anchor');

    if (anchor) {
      anchor.scrollIntoView({
        behavior: 'smooth',
        block: 'center',
      });
    }
  };

  const goToFeatures = (event) => {
    const anchor = (
      (event.target).ownerDocument || document
    ).querySelector('#features-anchor');

    if (anchor) {
      anchor.scrollIntoView({
        behavior: 'smooth',
      });
    }
  };

  const goToAbout = (event) => {
    const anchor = (
      (event.target).ownerDocument || document
    ).querySelector('#about-anchor');

    if (anchor) {
      anchor.scrollIntoView({
        behavior: 'smooth',
      });
    }
  };

  return (
    <div className="App">
      <NavBar onClickHome={goToHome} onClickFeatures={goToFeatures} onClickAbout={goToAbout}/>

      <Hero route={route} setRoute={setRoute} fetchCityCoords={fetchCityCoords}/>
      <div id="features-anchor">
      <Feature 
        title="Find your next adventure"
        txt="Plan places you want to visit be it local, cantonal or national, plan as you go and visualize your trip.
          Choose from many of our Points Of Interest, shared by other members
          Save your journey and come back to it anytime to edit your story
          Inspire Others by sharing your experiences within the community"
        img={featureImg1}
        left={false}>
      </Feature>
      <Feature
        title="Share your story"
        txt="Have you been to a place that no one else has seen ?
          Share them on our site and show the world your experiences.
          Create an account to add your new points of interest
          Look for the place you want to add on our interactive map
          Upload your pictures, and write your experience"
        img={featureImg2}
        left={true}>
      </Feature>
      <Feature
        title="Journal your trip"
        txt="Complete your dashboard with various adventures, save
          your memories to keep them forever.
          Thanks to our user friendly dashboard you will be able to edit your
          cards on the go or after you have completed your trip."
        img={featureImg3}
        left={false}>
      </Feature>
      </div>
      <About/>
    </div>
  );
}

export default LandingPageApp;
