import {Action, handleActions} from 'redux-actions';
import {
    SET_AVAILABLE_TEMPLATES_RESPONSE,
    SET_LIST_TEMPLATES_MODAL,
    SET_MISSION_PLANET_COORDS,
    SET_MISSION_PLANET_TO,
    SET_MISSION_TYPE,
    SET_NEW_TEMPLATE_MODAL,
    SET_SELECTED_FLEET,
    SET_TEMPLATE_OPTIONS_RESPONSE,
    TOGGLE_LIST_TEMPLATES_MODAL,
    TOGGLE_NEW_TEMPLATE_MODAL
} from "./actions";
import FleetState from "./FleetState";
import {MissionType} from "./MissionType";

const initialState: FleetState = {
    selectedFleet: {},
    missionType: MissionType.NONE,
    planetIdTo: '',
    planetCoordinates: {x: "", y: ""},
    newTemplateModalVisible: false,
    templateOptions: {
        hullOptions: {
            weapons: []
        }
    },
    availableTemplates: {},
    listTemplatesModalVisible: false
};

export default handleActions<FleetState, any>({
    [SET_SELECTED_FLEET]: (state: FleetState, action: Action<{ templateName: string, amount: number }>): FleetState => {
        const newState = {...state};
        const {templateName, amount} = action.payload;
        if (amount === 0) {
            delete newState.selectedFleet[templateName];
        } else {
            newState.selectedFleet[templateName] = amount;
        }
        return newState;
    },
    [SET_MISSION_TYPE]: (state: FleetState, action: Action<MissionType>): FleetState => {
        return {...state, missionType: action.payload};
    },
    [SET_MISSION_PLANET_TO]: (state: FleetState, action: Action<string>): FleetState => {
        return {...state, planetIdTo: action.payload};
    },
    [SET_MISSION_PLANET_COORDS]: (state: FleetState, action: Action<{ x: string, y: string }>): FleetState => {
        return {...state, planetCoordinates: action.payload};
    },
    [TOGGLE_NEW_TEMPLATE_MODAL]: (state: FleetState): FleetState => {
        return {...state, newTemplateModalVisible: !state.newTemplateModalVisible};
    },
    [SET_NEW_TEMPLATE_MODAL]: (state: FleetState, action: Action<boolean>): FleetState => {
        return {...state, newTemplateModalVisible: action.payload};
    },
    [SET_TEMPLATE_OPTIONS_RESPONSE]: (state: FleetState, action: Action<FleetState>): FleetState => {
        return {...state, templateOptions: action.payload.templateOptions};
    },
    [SET_AVAILABLE_TEMPLATES_RESPONSE]: (state: FleetState, action: Action<FleetState>): FleetState => {
        return {...state, availableTemplates: action.payload.availableTemplates};
    },
    [TOGGLE_LIST_TEMPLATES_MODAL]: (state: FleetState): FleetState => {
        return {...state, listTemplatesModalVisible: !state.listTemplatesModalVisible};
    },
    [SET_LIST_TEMPLATES_MODAL]: (state: FleetState, action: Action<boolean>): FleetState => {
        return {...state, listTemplatesModalVisible: action.payload};
    },

}, initialState);