import {Artist} from "./artist";
import {Genre} from "./genre";

export interface Track {
    id?: number;
    title: String;
    durationSeconds: number;
    yearReleased: number;
    beatsPerMinute: number;
    genre: Genre;
    artists: Artist[];
}
