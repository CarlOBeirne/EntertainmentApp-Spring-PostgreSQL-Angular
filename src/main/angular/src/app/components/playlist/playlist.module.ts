import {NgModule } from '@angular/core';
import {CommonModule } from '@angular/common';
import {PlaylistFormComponent } from './playlist-form/playlist-form.component';
import {PlaylistListComponent } from './playlist-list/playlist-list.component';
import {ReactiveFormsModule, FormsModule } from '@angular/forms';
import {RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    PlaylistFormComponent,
    PlaylistListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule
  ],
  exports: [
    PlaylistFormComponent,
    PlaylistListComponent
  ]
})
export class PlaylistModule {}
