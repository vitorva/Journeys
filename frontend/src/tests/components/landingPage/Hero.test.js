import Hero from '../../../components/landingPage/Hero.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

describe('<Hero />', () => {
  it('Component Hero exists', () => {
	const props = {
		route : {
			startCity: "Lausanne",
			destCity: "Sion"
		}
	};
    const wrapper = shallow(<Hero {...props} />);
	expect(wrapper.exists('.hero')).toEqual(true);
  });
});