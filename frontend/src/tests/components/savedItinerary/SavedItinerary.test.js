
import SavedItinerary from '../../../components/savedItinerary/SavedItinerary.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

describe('<SavedItinerary />', () => {
  const wrapper = shallow(<SavedItinerary />);

  it('Component SavedItinerary exists', () => {
	  expect(wrapper.exists('.savedItinerary')).toEqual(true);
  });

  it('Component SavedItinerary contains 3 elements', () => {
	  expect(wrapper.find('.savedItinerary').children().length).toEqual(3)
  });
});



