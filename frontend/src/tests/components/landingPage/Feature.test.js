import Feature from '../../../components/landingPage/Feature.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

describe('<Feature />', () => {
  it('Component Feature exists', () => {
    const wrapper = shallow(<Feature />);
	  expect(wrapper.exists('.feature')).toEqual(true);
  });
});