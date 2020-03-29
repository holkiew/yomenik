export default interface NewTemplateRequest {
    templateName: string,
    hullType: string,
    weaponSlots: {
        [order: number]: string
    },
    fireMode: string
}

