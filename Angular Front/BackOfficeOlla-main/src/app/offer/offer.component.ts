import { Console } from 'node:console';
import { OfferComposant } from './../class/offerComposant';
import { Component, Input, OnInit } from '@angular/core';
import { OfferType} from 'src/interfaces/modele-interface';
import { LocalStorageService } from 'src/providers/localstorage.service';
import { ToolsService } from 'src/providers/tools.service';
import { Offer } from '../class/Offer';
import { type } from '../class/type';
import { AdminService } from 'src/providers/admin.service';
import { Operator } from '../class/Operator';
import * as $ from 'jquery';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.scss']
})
export class OfferComponent implements OnInit{

  public typeOffers: OfferType []=[];
  public operators: Operator []=[];
  public selectedOperator :number;

  @Input() offerName : string;
  @Input() price : number;
  @Input() duration : number;


  constructor(private service : AdminService, private toolsService : ToolsService, private storage : LocalStorageService)
  {
    this.selectedOperator=0;
    this.offerName = "";
    this.price = 500;
    this.duration = 30;
  }

  ngOnInit(): void {
    this.getDataOfferTypes();
    this.getDataOperators();
  }

  getDataOfferTypes(){
    this.typeOffers=this.toolsService.getOfferTypes();
  }

  getDataOperators(){
    this.operators=this.toolsService.getOpertors();
  }

  AddNewOffer()
  {
    let offerComposants : OfferComposant []= [];
    let index = 0;
    for(let i = 0;i<this.typeOffers.length;i++)
    {
      let offerComposant : OfferComposant;
      if(this.typeOffers[i].selected == true)
      {
        offerComposant = new OfferComposant(new type("", "", this.typeOffers[i].id), this.typeOffers[i].value, this.typeOffers[i].unite, this.typeOffers[i].uniteOther);
        offerComposants[index] = offerComposant;
        index++;
      }
    }
    let token = this.storage.getToken();
    let offer = new Offer(this.selectedOperator, this.offerName, this.price, this. duration, offerComposants);
    this.service.AddNewOffer(token, offer).subscribe(async (response) =>
    {
      var loadI = document.createElement("i");
      loadI.className = "fa fa-refresh fa-spin";
      var valid = document.getElementById("validNewOffer");

      if(valid != null)
      {
        valid.innerHTML = "";
        valid.appendChild(loadI);
      }

      if(response["status"] == "succes")
      {
        if(valid != null)
        {
          await new Promise(r => setTimeout(r, 800));
          valid.removeChild(loadI);
          valid.innerHTML = "Confirm";
          this.offerName = "";
          this.price = 0;
          this.duration = 0;
        }
      }
      else
      {
        console.log("failed");
      }
    });
  }
}
