import { base_url } from './../app/app.module';
import { OfferType, Usernombre } from './../interfaces/modele-interface';
import { LocalStorageService } from './localstorage.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Operator } from 'src/app/class/Operator';

@Injectable({
    providedIn:"root"
})

export class ToolsService{
    constructor(private http:HttpClient,private storage:LocalStorageService){

    }

    setHttpGet(url:string){
        let headers = new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': "Bearer " +this.storage.getToken()
        });
        let options = { headers: headers };
        return this.http.get(base_url+url,options);
    }


    /// get touts les operator
    getOpertors():any{
        const operators:Operator[]=[];
        const onSucces=(response : any) =>{
            if(response["status"]=="succes"){
                for(var i=0;i<response["data"].length;i++){
                    const operator={} as Operator;
                    operator.id=response["data"][i].id;
                    operator.name=response["data"][i].name;
                    operator.preffix=response["data"][i].preffix;
                    operators.push(operator);
                }
            }
        }
        const onError=(response : any) =>{

        }
        this.setHttpGet("/Operators").subscribe(onSucces,onError);
        return operators;
    }

    /// get tous les type Offre
    getOfferTypes():any{
        const typeOffers:OfferType[] = [];
        const onSucces=(response : any) => {
          if(response["status"]=="succes"){
            for(var i=0;i<response["data"].length;i++){
             const tempOffer= {} as OfferType;
             tempOffer.id=response["data"][i].id;
             tempOffer.name=response["data"][i].name;
             tempOffer.selected=false;
             typeOffers.push(tempOffer);
            }
          }
        }
        const onError=(response : any) => {

        }
        this.setHttpGet("/OfferTypes").subscribe(onSucces,onError);
        return typeOffers;
    }


    /// get tous les nombre user par operateur
    getUserNombre(){
        return this.setHttpGet("/Usernombre");
    }
}
