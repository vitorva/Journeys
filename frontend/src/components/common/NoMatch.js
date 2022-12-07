import {Link} from "react-router-dom";

function NoMatch() {
  return (
    <div>
      <h2>Missed a turn ? 🧭 There's nothing to see here ! 👀</h2>
      <p>
        <Link to="/">Go back to the landing page and start your trip over</Link>
      </p>
    </div>
  );
}

export default NoMatch;
