import { base_url } from './../app/app.module';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn :'root'
})

export class Service {
    constructor(private http:HttpClient){

    }
}