import { TestBed } from '@angular/core/testing';

import { RegCodeGuardService } from './reg-code-guard.service';

describe('RegCodeGuardService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RegCodeGuardService = TestBed.get(RegCodeGuardService);
    expect(service).toBeTruthy();
  });
});
