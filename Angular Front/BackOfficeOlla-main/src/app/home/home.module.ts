import { ChartsModule } from 'ng2-charts';
import { TableListComponent } from './../table-list/table-list.component';
import { OfferComponent } from './../offer/offer.component';
import { ValidateComponent } from './../validate/validate.component';
import { DashboardComponent } from './../dashboard/dashboard.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeRouting } from './home.routing';
import { ChartModule, HIGHCHARTS_MODULES } from 'angular-highcharts';
import { HighchartsChartModule } from 'highcharts-angular';
import * as drilldown from 'highcharts/modules/drilldown.src';
@NgModule({
    imports:[
        CommonModule,
        RouterModule.forChild(HomeRouting),
        FormsModule,
        ReactiveFormsModule,
        ChartModule,
        HighchartsChartModule,
    ],
    declarations:[
        DashboardComponent,
        ValidateComponent,
        OfferComponent,
        TableListComponent
    ],
    providers:[ { provide: HIGHCHARTS_MODULES, useFactory: () => [drilldown] } ]
})

export class HomePageModule{}