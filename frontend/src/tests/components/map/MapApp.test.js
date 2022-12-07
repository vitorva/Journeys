import MapApp from '../../../components/map/MapApp.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

describe('<MapApp />', () => {
  const wrapper = shallow(<MapApp />);

  it('Component MapApp exists', () => {
	  expect(wrapper.exists('.map-app-root')).toEqual(true);
  });

  it('Component MapApp contains 5 elements', () => {
	  expect(wrapper.find('.map-app-root').children().length).toEqual(5)
  });
});



