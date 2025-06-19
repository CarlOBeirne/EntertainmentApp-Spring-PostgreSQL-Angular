import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MenuNavComponent} from "./menu-nav/menu-nav.component";
import {RouterLink, RouterLinkActive} from "@angular/router";



@NgModule({
  declarations: [
    MenuNavComponent
  ],
  exports: [
    MenuNavComponent
  ],
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive
  ]
})
export class MenuModule { }
