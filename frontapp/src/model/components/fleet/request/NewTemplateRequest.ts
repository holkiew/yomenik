export default interface NewTemplateRequest {
    templateName: string,
    shipClassType: string,
    hullType: string,
    weaponSlots: {
        [order: number]: string
    },
    fireMode: string
}

