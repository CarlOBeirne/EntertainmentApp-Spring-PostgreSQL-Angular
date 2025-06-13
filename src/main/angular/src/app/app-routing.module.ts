import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TrackListComponent} from "./components/track/track-list/track-list.component";
import {ArtistFormComponent} from "./components/artist/artist-form/artist-form.component";

const routes: Routes = [
  {path: 'artist/new', component: ArtistFormComponent},
  {path: 'track/all', component: TrackListComponent},
  {path: '', redirectTo: '/', pathMatch: 'full'},
  {path: '**', redirectTo: '/'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
