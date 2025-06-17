import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TrackListComponent} from "./components/track/track-list/track-list.component";
import {ArtistFormComponent} from "./components/artist/artist-form/artist-form.component";
import {TrackFormComponent} from "./components/track/track-form/track-form.component";
import {ArtistListComponent} from "./components/artist/artist-list/artist-list.component";

const routes: Routes = [
  {path: 'artist/new', component: ArtistFormComponent},
  {path: 'artist/all', component: ArtistListComponent},
  {path: 'artist/update/:id', component: ArtistFormComponent},
  {path: 'artist/:artistId/tracks', component: TrackListComponent},
  {path: 'track/update/:id', component: TrackFormComponent},
  {path: 'track/all', component: TrackListComponent},
  {path: '', redirectTo: '/', pathMatch: 'full'},
  {path: '**', redirectTo: '/'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
