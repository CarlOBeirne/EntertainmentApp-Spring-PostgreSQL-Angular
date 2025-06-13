import {ArtistType} from "./artist-type";

export interface Artist {
  id?: number,
  name: string,
  artistType: ArtistType,
  biography: string,
  nationality: string,
  yearFounded: number
}
