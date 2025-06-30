import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {MenuModule} from "./components/menu/menu.module";
import {TrackModule} from './components/track/track.module';
import {HttpClientModule} from '@angular/common/http';
import {ArtistModule} from "./components/artist/artist.module";
import {AuthModule} from "./components/auth/auth.module";
import {HomeModule} from "./components/home/home.module";
import {GenreModule} from "./components/genre/genre.module";
import {PlaylistModule} from "./components/playlist/playlist.module";
//import {PlaylistListComponent} from './components/playlist/playlist-list/playlist-list.component';
//import {PlaylistFormComponent} from './components/playlist/playlist-form/playlist-form.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MenuModule,
    HttpClientModule,
    TrackModule,
    ArtistModule,
    AuthModule,
    HomeModule,
    GenreModule,
    PlaylistModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
