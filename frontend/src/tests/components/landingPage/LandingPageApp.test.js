import LandingPageApp from '../../../components/landingPage/LandingPageApp.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

describe('<LandingPageApp />', () => {
  it('Component LandingPageApp exists', () => {
    const wrapper = shallow(<LandingPageApp />);
	  expect(wrapper.exists('.App')).toEqual(true);
  });
});