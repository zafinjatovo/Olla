import { type } from './../class/type';
import { OfferComposant } from './../class/offerComposant';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/providers/admin.service';
import { LocalStorageService } from 'src/providers/localstorage.service';
import { ToolsService } from 'src/providers/tools.service';
import { Offer } from '../class/Offer';
import { Operator } from '../class/Operator';
import * as $ from 'jquery';

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.scss']
})
export class TableListComponent implements OnInit {

  operators : Operator [] = [];
  selectedOperator : number = 1;
  offers : Offer [] = [];
  constructor( private toolsService : ToolsService, private storage : LocalStorageService, private service : AdminService)
  {
  }

  ngOnInit(): void {
    this.InitCall();
  }

  getDataOperators (){
    this.operators=this.toolsService.getOpertors();
    return "";
  }

  async InitCall()
  {
    this.getDataOperators()
    await new Promise(r => setTimeout(r, 750));
    this.GetOffersOperator();
  }

  GetOffersOperator()
  {
    this.offers = [];
    this.service.GetOperatorOffer(this.operators[this.selectedOperator - 1]).subscribe((response)=>{
      if(response["status"] == "succes")
      {
        let i = 0;
        response["data"].forEach((offer : any) =>
        {
          let composants : OfferComposant [] = [];
          let j = 0;
          offer.offerComposants.forEach((composant : any) =>
          {
            composants[j] = new OfferComposant(new type(composant.type.name, composant.type.unit, composant.type.id), composant.value, composant.consumptionUnit, composant.otherConsumptionUnit);
            j++;
          });
          this.offers[i] = new Offer(offer.operator.id, offer.name, offer.price, offer.duration, composants);
          i++;
        });
        console.log(this.offers);
      }
      else
      {
        console.log("failed");
      }
    });
  }

  show_hide_row(row : any)
  {
    $("#"+row).toggle();
  }
}
