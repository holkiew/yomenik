export default interface BuildingState {
    selectedBuildingSlot: {
        slotKey: number,
        specification: {
            type: string,
            label: string,
            level: number,
            slot: number
            excluded: string[],
            included: string[]
        }
    }
    selectedBuildingOption: {
        slot: number,
        type: string
    }
}