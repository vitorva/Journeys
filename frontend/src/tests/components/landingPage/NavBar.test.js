import NavBar from '../../../components/landingPage/NavBar.js';
import { shallow } from "enzyme";

import "../../../setupTests.js"

jest.mock('react', () => {
const ActualReact = jest.requireActual('react');
  return {
	...ActualReact,
	useContext: () => ( {session : {isUserLoggedIn : function(){} } }),
	};
});

describe('<NavBar />', () => {
  it('Component NavBar exists', () => {
	const wrapper = shallow(<NavBar/>);
	expect(wrapper.exists('.navbar')).toEqual(true);
  });
});