import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path:'login',component:LoginComponent
  },
  {
    path:'home',
    component:HomeComponent,
    children:[
      {
      path:'',
      loadChildren: () => import('../app/home/home.module').then(m => m.HomePageModule)
      }
    ]
  },
  {
    path:'',
    redirectTo:'login',
    pathMatch:'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
