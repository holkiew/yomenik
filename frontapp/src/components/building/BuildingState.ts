export default interface BuildingState {
    selectedBuildingSlot: {
        slotKey: string,
        rules: {
            excluded: string[],
            included: string[]
        }
    }
    selectedBuildingOption: string
}