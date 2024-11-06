import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormIvcf20Component } from './form-ivcf20.component';

describe('FormIvcf20Component', () => {
  let component: FormIvcf20Component;
  let fixture: ComponentFixture<FormIvcf20Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormIvcf20Component]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormIvcf20Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
