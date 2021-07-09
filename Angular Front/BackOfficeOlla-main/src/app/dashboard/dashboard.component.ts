import { Label } from 'ng2-charts';
import { ToolsService } from './../../providers/tools.service';
import { StatistiqueService } from './../../providers/statistique.service';
import {  StatEnTete, dataChart } from './../../interfaces/modele-interface';
import { Component, OnInit } from '@angular/core';
import { Options } from 'highcharts';
import {Chart} from 'angular-highcharts';
import * as Highcharts from 'highcharts';
import { Operator } from '../class/Operator';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  statEntete:StatEnTete;
  idOperator:number=1;
  operators: Operator []=[];


  columChartOffer=new Chart({});
  columChartOperator=new Chart({});

  Highcharts=Highcharts;
  chartOptions:Options={};

  top:number=10;

  listUserChiffreAffaire:any[]=[];

  constructor(
    private stat:StatistiqueService,
    private tool:ToolsService
  )
  {
    this.statEntete={} as StatEnTete;
  }

  ngOnInit() {
     this.statEntete=this.stat.getStatEntete();
     this.operators=this.tool.getOpertors();
     this.updateColumDataChartOffer();
     this.updateColumDataChartOperator();
     this.updateDataUserChiffreAffaire();
     console.log(this.listUserChiffreAffaire);
  }

  updateColumDataChartOffer(){
    this.stat.getOfferStatistique(this.idOperator).subscribe((response)=>{
      if(response["status"]=="succes"){
        const labelChart:string[]=[];
        const dataChart:number[]=[];
        response["data"].forEach((data:any)=>{
          labelChart.push(data.name);
          dataChart.push(data.nombreAcheteur);
        })
        const optionsColums:Options={
          title: {
                text: 'Offer stat'
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories:labelChart
            },
            yAxis: {
              title: {
                  text: 'Number of User use'
              }

            },
            series: [{
                type: 'bar',
                colorByPoint: true,
                data:dataChart,
                showInLegend: false
            }]
         }
         this.columChartOffer=new Chart(optionsColums);
      }
    })
  }

  updateColumDataChartOperator(){
   // const tempOptions:Options={};
    const dataChart:any[]=[];
    const dataDrillDown:any[]=[];
    this.stat.getOperatorChiffreAffaire().subscribe((response)=>{
      //console.log(response);
      if(response["status"]=="succes"){
         if(response["data"]!=null){
            response["data"].forEach((data:any)=>{
                const tempDataChart:any={
                  "name":data["operator"].name,
                  "y":data["chiffreAffaire"],
                  "drilldown":data["operator"].name
                };
                const datadrill:any[]=[];
                data["detailsMonth"].forEach((dataDown:any)=>{
                    const tempdatadrill:any[]=[dataDown.month +"-"+ dataDown.year,dataDown.chiffreAffaire];
                    datadrill.push(tempdatadrill);
                });

                const tempDatadrildowm:any={
                  "name":data["operator"].name,
                  "id":data["operator"].name,
                  "data":datadrill,
                }
                dataDrillDown.push(tempDatadrildowm);
                dataChart.push(tempDataChart);
            });

          //  console.log(dataDrillDown);
          const  options:Options={
            chart: {
                type: 'column'
            },
            title: {
                text: 'Turnover operator'
            },
            subtitle: {
                text: 'On click to show by month'
            },
            accessibility: {
                announceNewData: {
                    enabled: true
                }
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Total percent turnover to purcase'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        //format: '{point.y:.1f}'
                    }
                }
            },

            series: [{
              name:"operator",
              type: 'column',
              colorByPoint: true,
              data:dataChart,
              showInLegend: false
            }],

            drilldown:{
              series:dataDrillDown
            }

          }
          this.columChartOperator=new Chart(options);
         }
      }
    });
  }

  updateDataUserChiffreAffaire(){
      this.listUserChiffreAffaire=[];
        this.stat.getUserChiffreAffaire(this.idOperator,this.top).subscribe((response)=>{
            if(response["status"]=="succes"){
              response["data"].forEach((data:any)=>{
                    const dataUser:any={
                      "chiffreAffaire":data.chiffreAffaire,
                      "user":data.user,
                    }
                    this.listUserChiffreAffaire.push(dataUser);
              });
            }
        });
  }
}
