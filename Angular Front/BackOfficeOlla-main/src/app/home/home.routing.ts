import { TableListComponent } from './../table-list/table-list.component';
import { OfferComponent } from './../offer/offer.component';
import { ValidateComponent } from './../validate/validate.component';
import { Routes } from '@angular/router';
import { DashboardComponent } from '../dashboard/dashboard.component';

export const HomeRouting: Routes =[
    {path:'Dashboard',component:DashboardComponent},
    {path:'Validate',component:ValidateComponent},
    {path:'Offer',component:OfferComponent},
    {path:'Table-List',component:TableListComponent},
    {
        path:'',
        redirectTo:'Dashboard',
        pathMatch:'full'
    }
]