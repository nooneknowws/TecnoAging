import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultarAvaliacaoComponent } from './consultar-avaliacao.component';

describe('ConsultarAvaliacaoComponent', () => {
  let component: ConsultarAvaliacaoComponent;
  let fixture: ComponentFixture<ConsultarAvaliacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConsultarAvaliacaoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultarAvaliacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
