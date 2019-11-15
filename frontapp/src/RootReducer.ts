import {combineReducers} from 'redux';
import exemplaryReducer from "reduxExampleTemplate/reducer";
import planets from "components/reducer";
import updatePlanetsEpic from "components/epics";
import {combineEpics} from "redux-observable";

export const rootEpic = combineEpics(
    updatePlanetsEpic[0]
);

export const rootReducer = combineReducers({
    exemplaryReducer,
    planets
});
