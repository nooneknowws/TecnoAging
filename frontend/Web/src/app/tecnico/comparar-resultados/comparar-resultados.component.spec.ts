import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompararResultadosComponent } from './comparar-resultados.component';

describe('CompararResultadosComponent', () => {
  let component: CompararResultadosComponent;
  let fixture: ComponentFixture<CompararResultadosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CompararResultadosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CompararResultadosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
