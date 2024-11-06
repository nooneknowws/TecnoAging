import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Input() isOpen = true;
  @Output() toggleMenu = new EventEmitter<void>();

  toggle(): void {
    this.toggleMenu.emit();  
  }
}
