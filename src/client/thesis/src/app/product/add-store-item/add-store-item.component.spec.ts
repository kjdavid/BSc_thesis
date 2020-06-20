import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddStoreItemComponent } from './add-store-item.component';

describe('AddStoreItemComponent', () => {
  let component: AddStoreItemComponent;
  let fixture: ComponentFixture<AddStoreItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddStoreItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddStoreItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
