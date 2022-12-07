import About from '../../../components/landingPage/About.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

describe('<About />', () => {
  it('Component About exists', () => {
    const wrapper = shallow(<About />);
	  expect(wrapper.exists('.about')).toEqual(true);
  });
});