import BuildingEpics from "components/building/epics"
import PlanetEpics from "components/epics";
import FleetEpics from "components/fleet/epics";
import {combineEpics} from "redux-observable";

const rootEpic = combineEpics(
    PlanetEpics[0],
    FleetEpics[0],
    BuildingEpics[0]
);

export default rootEpic;
