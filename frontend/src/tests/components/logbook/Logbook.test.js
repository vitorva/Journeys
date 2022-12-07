import Logbook from '../../../components/logbook/Logbook.js';
import { shallow } from 'enzyme';
import "../../../setupTests.js"

const mockedUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedUsedNavigate,
}));

describe('<Logbook />', () => {
  const wrapper = shallow(<Logbook />);
  it('Component Logbook exists', () => {
	  expect(wrapper.exists('.logbook')).toEqual(true);
  });
  
  it('Component Logbook contains 3 elements', () => {
	  expect(wrapper.find('.logbook').children().length).toEqual(3)
  });
});


