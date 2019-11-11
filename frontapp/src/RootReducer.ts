import {combineReducers} from 'redux';
import exemplaryReducer from "reduxExampleTemplate/reducer";
import planets from "components/reducer";

const rootReducer = combineReducers({
    exemplaryReducer,
    planets
});

export default rootReducer;
