import { Routes } from '@angular/router';
import { MainComponent } from './main/main.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ReviewsComponent } from './reviews/reviews.component';
import { CuestionariesComponent } from './cuestionaries/cuestionaries.component';
import { TicketsComponent } from './tickets/tickets.component';
import { RewardsComponent } from './rewards/rewards.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  {path:'main',component:MainComponent,canActivate: [AuthGuard],
    children:[
      {path:'dashboard', component:DashboardComponent},
      {path:'reviews', component:ReviewsComponent},
      {path:'rewards', component:RewardsComponent},
      {path:'tickets', component:TicketsComponent},
      {path:'cuestionaries', component:CuestionariesComponent}
    ]
  },
  {path:'login',component:LoginComponent},
  {path:'**',pathMatch: 'full',redirectTo:'login'}
];
