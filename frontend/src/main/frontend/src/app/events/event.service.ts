import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Evento} from "./event";
import {Observable} from "rxjs/Observable";

@Injectable()
export class EventService {

  constructor(private http: HttpClient) { }

  getEvent(eventId : number, folderId : number) :  Observable<Evento>{
    let headers = new HttpHeaders()
      .set("Authorization", `Bearer ${JSON.parse(localStorage.currentUser).token}`);

    return this.http
      .get<any>('api/profile/' +
        JSON.parse(localStorage.currentUser).id + '/folders/' + folderId + '/events/' + eventId, {headers: headers});

  }

}
