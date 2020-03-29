import {MissionType} from "./MissionType";

// create sub interfaces grouping up regarding fields
export default interface FleetState {
    // mission part
    selectedFleet: { [key: string]: number; }
    missionType: MissionType
    planetIdTo: string
    planetCoordinates: { x: string, y: string }
    // new template part
    newTemplateModalVisible: boolean
    templateOptions: { [key: string]: { weapons: [] } }
    availableTemplates: { [key: string]: {} }
}