import { Router } from '@angular/router';
import { LocalStorageService } from 'src/providers/localstorage.service';
import { Label } from 'ng2-charts';
import { Component, OnInit } from '@angular/core';
import { RouterLinks } from 'src/app/app.module';
import {Location} from '@angular/common';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  routerLinks:any[];
  constructor(private location:Location, private storage : LocalStorageService, private router : Router) {
    this.routerLinks=RouterLinks.filter(routerLink => routerLink);
  }

  ngOnInit(): void {

  }

  getTitle(){
      var title=this.location.prepareExternalUrl(this.location.path());
      return title.split('/')[2];
  }

  LogOut()
  {
    this.storage.clearStorage();
    this.router.navigateByUrl("login")
  }
}
