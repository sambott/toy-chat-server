import {Component} from "angular2/core";
import {$WebSocket} from "angular2-websocket/angular2-websocket";

var ws = new $WebSocket("/rooms/123/ws");

@Component({
  selector: "chat-ui",
  template: "<h1>My First Angular 2 App</h1>"
})
export class AppComponent { }
