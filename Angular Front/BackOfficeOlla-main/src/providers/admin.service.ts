import { base_url } from './../app/app.module';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Admin } from '../interfaces/modele-interface';
import { Observable } from 'rxjs';
import { Offer } from 'src/app/class/Offer';
import { Operator } from 'src/app/class/Operator';

@Injectable({
    providedIn:"root"
})

export class AdminService
{
    constructor(private http:HttpClient)
    {

    }

    setHttpLogin(admin:Admin){
        if(admin.username!="" && admin.password!=""){
            let data={
                "email":admin.username,
                "password":admin.password
            }

            let headers = new HttpHeaders({
                'Access-Control-Allow-Origin':'*',
                'Content-Type': 'application/json'
            });
            let options = { headers: headers };
            return this.http.post(base_url+"/LoginAdmin",data,options);
        }else{
            throw new Error("Error:Email or password incorect");
        }
    }

   GetNoConfirmedDeposit(token : string)  : Observable<any>
    {
      let url = base_url + "/NoConfirmedDeposits";
      const httpOptions =
      {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
           Authorization: 'Bearer ' + token,
          })
      };
      return this.http.get(url, httpOptions);
    }

    ValidateNoConfirmedDeposit(token : string, idDepositV : number) : Observable<any>
    {
      let url = base_url + "/ConfirmedDeposit";
      const httpOptions =
      {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
           Authorization: 'Bearer ' + token,
          })
      };

      let data = JSON.stringify({
        idDeposit : idDepositV,
        confirmation : "true",
      });
      return this.http.post(url, data, httpOptions);
    }

    AddNewOffer(token : string, offer : Offer) : Observable<any>
    {
      let url = base_url + "/AddOffer";
      const httpOptions =
      {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
           Authorization: 'Bearer ' + token,
          })
      };
      let data = JSON.stringify(offer);
      return this.http.post(url, data, httpOptions);
    }

    GetOperatorOffer(operator : Operator) : Observable<any>
    {
        let url = base_url + "/GetOperatorOffers?idOperator=" + operator.id + "&name=" + operator.name + "&prefix=" + operator.preffix;
        const httpOptions =
        {
          headers: new HttpHeaders({
            'Content-Type':  'application/json'
            })
        };
        return this.http.get(url, httpOptions);
    }
}
