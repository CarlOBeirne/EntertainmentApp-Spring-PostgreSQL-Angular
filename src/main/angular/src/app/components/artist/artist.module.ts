import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ArtistFormComponent} from "./artist-form/artist-form.component";
import {ReactiveFormsModule} from "@angular/forms";
import {ArtistListComponent} from "./artist-list/artist-list.component";
import {RouterLink, RouterLinkActive} from "@angular/router";


@NgModule({
  declarations: [
    ArtistFormComponent,
    ArtistListComponent
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink,
    RouterLinkActive
  ]
})
export class ArtistModule { }
