import { element } from 'protractor';
import { Component, OnInit } from '@angular/core';
import { interval } from 'rxjs';
import { LocalStorageService } from 'src/providers/localstorage.service';
import { ToolsService } from 'src/providers/tools.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  icons="fa-chevron-right";
  isMEnuSow=false;
  typesOffer:any=[];
  constructor(private storage:LocalStorageService,private toolsService:ToolsService, private router : Router) {
    this.isMEnuSow=false;
    console.log(this.storage.getToken());
   }

  ngOnInit(): void {
    if(this.storage.getToken() == null)
    {
      this.router.navigateByUrl("login");
    }
  }

  showMenu(){
    console.log("ok");
    this.isMEnuSow=true;
    const main=document.getElementsByClassName('main-body');
    const sidebar=document.getElementsByClassName('sidebar');
    sidebar[0].setAttribute('style','left:0!important');
  }

  closeMenu(){
    this.isMEnuSow=false;
    const main=document.getElementsByClassName('main-body');
    const sidebar=document.getElementsByClassName('sidebar');
    sidebar[0].setAttribute('style','left:-260px!important');
  }


}
