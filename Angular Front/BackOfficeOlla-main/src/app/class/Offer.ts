import { OfferComposant } from './offerComposant';
export class Offer
{
    public idOperator : number;
    public name : string;
    public price : number;
    public duration : number;
    public offerComposant : OfferComposant [];

    constructor(idOperator : number, name : string, price : number, duration : number, offerComposants : OfferComposant [])
    {
      this.idOperator = idOperator;
      this.name = name;
      this.price = price;
      this.duration = duration;
      this.offerComposant = offerComposants;
    }
}
