import BuildingEpics from "components/building/epics"
import PlanetEpics from "components/epics";
import FleetEpics from "components/fleet/epics";
import {combineEpics} from "redux-observable";

const rootEpic = combineEpics(
    ...PlanetEpics,
    ...FleetEpics,
    ...BuildingEpics
);

export default rootEpic;
