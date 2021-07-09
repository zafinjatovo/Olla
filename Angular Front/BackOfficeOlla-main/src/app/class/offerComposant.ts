import { type } from './type';
export class OfferComposant
{
    public type : type;
    public value : number;
    public consumptionUnit :number;
    public otherConsumptionUnit : number;

    constructor(type : type, value : number, consumptionUnit : number, otherConsumptionUnit : number)
    {
      this.type = type;
      this.value = value;
      this.consumptionUnit = consumptionUnit;
      this.otherConsumptionUnit = otherConsumptionUnit;
    }
}
