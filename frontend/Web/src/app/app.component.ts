import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isAuthRoute =  true;
  isOpen = false;
  static API_URL: string = "http://localhost:3000/api";

  constructor(){}
}
