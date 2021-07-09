import { Observable } from 'rxjs/internal/Observable';
import { LocalStorageService } from 'src/providers/localstorage.service';
import { StatEnTete } from './../interfaces/modele-interface';
import { ToolsService } from 'src/providers/tools.service';
import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { base_url } from 'src/app/app.module';

@Injectable({
    providedIn:"root"
})

export class StatistiqueService{
    constructor(private tools:ToolsService,private http:HttpClient,private storage:LocalStorageService){

    }

    getStatEntete():StatEnTete{
        const statEnTete:StatEnTete={} as StatEnTete;
        const onSucces=(response : any) =>{
            if(response["status"]="succes"){
                if(response["data"]!=null){
                    statEnTete.lastTraffic=response["data"].lastTraffic;
                    statEnTete.nowTrafic=response["data"].nowTrafic;
                    statEnTete.lastUser=response["data"].lastUser;
                    statEnTete.nowUser=response["data"].nowUser;
                }
            }
        }
        const onError=(response : any) =>{
            
        }

        const onSucces1=(response : any) =>{
            if(response["status"]="succes"){
                if(response["data"]!=null){
                   statEnTete.chiffreAffaireTotal=response["data"];
                }
            }
        }
        const onError1=(response : any) =>{
            
        }

        this.tools.setHttpGet("/UserTrafficNombre").subscribe(onSucces,onError);
        this.tools.setHttpGet("/ChiffreAffaireTotal").subscribe(onSucces1,onError1);
        return statEnTete;
    }


    /// statistiqueOffre

    getOfferStatistique(operator:number):Observable<any>{
        
        let headers = new HttpHeaders({
            'Access-Control-Allow-Origin':'*',
            'Content-Type': 'application/json',
            'authorization':'Bearear ' + this.storage.getToken()
        });
        let options = { headers: headers };
        return this.http.get(base_url+"/StatOffer?operator="+ operator,options);
    }

    // statistique Operator chiffre affaire

    getOperatorChiffreAffaire():Observable<any>{
        let headers = new HttpHeaders({
            'Access-Control-Allow-Origin':'*',
            'Content-Type': 'application/json',
            'authorization':'Bearear ' + this.storage.getToken()
        });
        let options = { headers: headers };
        return this.http.get(base_url+"/OperatorChiffreAffaire",options);
    }


    getUserChiffreAffaire(operator:number,top:number):Observable<any>{
        let headers = new HttpHeaders({
            'Access-Control-Allow-Origin':'*',
            'Content-Type': 'application/json',
            'authorization':'Bearear ' + this.storage.getToken()
        });
        let options = { headers: headers };
        return this.http.get(base_url+"/UserChiffreAffaire?operator="+operator+"&top="+top,options);
    }

}