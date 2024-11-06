import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoTestesComponent } from './historico-testes.component';

describe('HistoricoTestesComponent', () => {
  let component: HistoricoTestesComponent;
  let fixture: ComponentFixture<HistoricoTestesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HistoricoTestesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HistoricoTestesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
