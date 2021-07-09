import { User } from './../class/User';
import { Deposit } from './../class/Deposit';
import { LocalStorageService } from './../../providers/localstorage.service';
import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/providers/admin.service';

@Component({
  selector: 'app-validate',
  templateUrl: './validate.component.html',
  styleUrls: ['./validate.component.scss']
})
export class ValidateComponent implements OnInit {

  deposits : Deposit [];

  constructor(private service : AdminService, private storage : LocalStorageService)
  {
    this.deposits = [];
    this.GetAllNonConfirmedDeposits();
  }

  ngOnInit(): void
  {
  }

  GetAllNonConfirmedDeposits()
  {
    let token = this.storage.getToken();
    this.service.GetNoConfirmedDeposit(token).subscribe((response) =>
    {
      if(response["status"] == "succes")
      {
        let i = 0;
        response["data"].forEach((deposit : any) =>
        {
          let user = new User(deposit.user.idOperator, deposit.user.name, deposit.user.firstname, "", "", "", deposit.user.ncin, deposit.user.id);
          this.deposits[i] = new Deposit(user, parseFloat(deposit.cash), deposit.transaction,  deposit.date, deposit.id);
          i++;
        });
      }
    });
  }

  ValidateDeposit(idDeposit : number, btnIndex : number)
  {
    var loadI = document.createElement("i");
    loadI.className = "fa fa-refresh fa-spin";
    var acceptB = document.getElementById(btnIndex.toString());

    if(acceptB != null)
    {
      acceptB.innerHTML = "";
      acceptB.appendChild(loadI);
    }
    let token = this.storage.getToken();
    this.service.ValidateNoConfirmedDeposit(token, idDeposit).subscribe(async (response) =>
    {
      if(response["status"] == "succes")
      {
        if(acceptB != null)
        {
          acceptB.removeChild(loadI);
          var loadIC = document.createElement("i");
          loadIC.className = "fa fa-check-circle-o";
          acceptB.className = "buttonC button3";
          acceptB.appendChild(loadIC);
          acceptB.setAttribute("disabled", "");
        }
      }
      else
      {
        console.log("validation failed");
      }
    });
  }
}
