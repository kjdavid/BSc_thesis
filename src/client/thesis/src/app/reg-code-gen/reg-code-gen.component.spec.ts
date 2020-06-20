import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegCodeGenComponent } from './reg-code-gen.component';

describe('RegCodeGenComponent', () => {
  let component: RegCodeGenComponent;
  let fixture: ComponentFixture<RegCodeGenComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegCodeGenComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegCodeGenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
