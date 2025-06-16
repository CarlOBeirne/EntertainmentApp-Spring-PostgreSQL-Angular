import {Artist} from "./artist";

export interface Track {
    id?: number;
    title: String;
    durationSeconds: number;
    yearReleased: number;
    beatsPerMinute: number;
    // genre: Genre;
    artists: Artist[];
}
