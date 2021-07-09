export interface RouterInfo{
    path:string,
    title:string,
    icon:String,
    class:String
}
export interface Admin{
    username:string,
    password:string,
}
export interface DetailOffer{
    id:string,
    name:string,
    value:number,
    unite:number,
    uniteOther:number,
}

export interface OfferType{
    id:number,
    name:string,
    value:number,
    unite:number,
    uniteOther:number,
    selected:boolean
}

export interface Usernombre{
    id:number;
    nameOperator:string;
    nombre:number;
}

export interface StatEnTete{
    lastTraffic:number,
    nowTrafic: number,
    lastUser: number,
    nowUser: number,
    chiffreAffaireTotal:number,
}

export interface columChartDataLabel{
    label:any,
    data:any,
}

export interface dataChart{
    name:string;
    y:number;
    drilldown: string;
}

export interface drilldownData{

}
