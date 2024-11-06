import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarPerfilPacienteComponent as EditarPerfilPacienteComponent } from './editar-perfil.component';

describe('EditarPerfilComponent', () => {
  let component: EditarPerfilPacienteComponent;
  let fixture: ComponentFixture<EditarPerfilPacienteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditarPerfilPacienteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditarPerfilPacienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
