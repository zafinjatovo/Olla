import { Component, OnInit, ViewChild, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { Admin } from 'src/interfaces/modele-interface';
import { AdminService } from 'src/providers/admin.service';
import { LocalStorageService } from 'src/providers/localstorage.service';
import { ToolsService } from 'src/providers/tools.service';
import * as $ from 'jquery';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  admin:Admin;
  message="";
  constructor(
      private render: Renderer2,
      private adminService:AdminService,
      private router:Router,
      private storage:LocalStorageService,
      private tools:ToolsService
  )
  {
    this.admin={} as Admin;
  }

  ngOnInit(): void {

  }

  login(){
    const onSucces= (response : any) =>{
      if(response["status"]=="succes"){
        this.storage.setToken(response["data"]);
        this.router.navigateByUrl("/home");
      }else{
        this.message=response["status"];
      }
      this.CloseModal();
    }
    const onError= (response :any) =>{
        this.message="Server not Found";
        this.CloseModal();
    }
    try {
      this.OpenModal();
      this.adminService.setHttpLogin(this.admin).subscribe(onSucces,onError);
    } catch (error) {
      this.message=error;
    }
  }

  OpenModal()
  {
    $("#myModal").css("display", "block");
  }

  CloseModal()
  {
    $("#myModal").css("display", "none");
  }
}
