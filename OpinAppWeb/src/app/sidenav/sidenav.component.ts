import { Component, Output, EventEmitter, OnInit, HostListener } from '@angular/core';
import { navbarData } from './nav-data';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {} from '@fortawesome/angular-fontawesome'
import { Auth } from '@angular/fire/auth';

interface SideNavToggle{
  screenWidth:number;
  collapsed:boolean;
}
@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports: [NgFor,NgIf,NgClass,RouterLink,RouterLinkActive],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.css'
})
export class SidenavComponent implements OnInit{

  @Output() onToggleSideNav: EventEmitter<SideNavToggle>=new EventEmitter();
  screenWidth=0;
  collapsed=false;
  navData=navbarData;

  constructor(private afAuth: Auth) {}

  @HostListener('window:resize', ['$event'])
  onResize(event:any){
    this.screenWidth=window.innerWidth;
    if(this.screenWidth<=768){
      this.collapsed=false;
      this.onToggleSideNav.emit({collapsed:this.collapsed,screenWidth:this.screenWidth});
    }
  }

  ngOnInit(): void {
      this.screenWidth=window.innerWidth;
      console.log('screen',window.innerWidth);
  }

  toogleCollapse():void{
    this.collapsed=!this.collapsed
    this.onToggleSideNav.emit({collapsed:this.collapsed,screenWidth:this.screenWidth});
  }
  closeSidenav():void{
    this.collapsed=false;
    this.onToggleSideNav.emit({collapsed:this.collapsed,screenWidth:this.screenWidth});
  }
  logout(): void {
    this.afAuth.signOut().then(() => {
      // Aquí puedes agregar lógica adicional después de cerrar sesión, como redireccionar al usuario a la página de inicio.
    }).catch((error:any) => {
      console.error('Error al cerrar sesión:', error);
    });
  }
}
