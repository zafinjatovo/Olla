import { HighchartsChartModule } from 'highcharts-angular';
import { StatistiqueService } from './../providers/statistique.service';
import { StorageServiceModule } from 'ngx-webstorage-service';
import { FormsModule } from '@angular/forms';
import { ToolsService } from './../providers/tools.service';
import { RouterInfo } from './../interfaces/modele-interface';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatIconModule,MatIconRegistry} from '@angular/material/icon';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { SidemenuComponent } from './components/sidemenu/sidemenu.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { AdminService } from './../providers/admin.service';
import { HttpClientModule } from '@angular/common/http';
import { NgxPullToRefreshModule } from 'ngx-pull-to-refresh';
import * as drilldown from 'highcharts/modules/drilldown.src';
import { HIGHCHARTS_MODULES } from 'angular-highcharts';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    SidemenuComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatIconModule,
    HttpClientModule,
    FormsModule,
    StorageServiceModule,
    NgxPullToRefreshModule,
    HighchartsChartModule,
  ],
  exports:[
    MatIconModule
  ],
  providers: [MatIconRegistry,AdminService,ToolsService,StatistiqueService, { provide: HIGHCHARTS_MODULES, useFactory: () => [drilldown] } ],
  bootstrap: [AppComponent]
})
export class AppModule { }
export const base_url="https://appeltelephonique.herokuapp.com";
// export const base_url="http://localhost:8081";
export const RouterLinks:RouterInfo[]=[
  {path:'/home/Dashboard',title:'Dashboard',icon:'fa fa-th-large c-blue',class:''},
  {path:'/home/Validate',title:"Validate user's deposit",icon:'fa fa-check-square-o c-orange',class:''},
  {path:'/home/Offer',title:'Add new offer',icon:'fa fa-tag c-yellow',class:''},
  {path:'/home/Table-List',title:'Offer list',icon:'fa fa-list-alt c-red',class:''}
];
