import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Ivcf20Component } from './ivcf-20.component';

describe('Ivcf20Component', () => {
  let component: Ivcf20Component;
  let fixture: ComponentFixture<Ivcf20Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Ivcf20Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Ivcf20Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
