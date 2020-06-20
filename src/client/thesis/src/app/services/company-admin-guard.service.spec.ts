import { TestBed } from '@angular/core/testing';

import { CompanyAdminGuardService } from './company-admin-guard.service';

describe('CompanyAdminGuardService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompanyAdminGuardService = TestBed.get(CompanyAdminGuardService);
    expect(service).toBeTruthy();
  });
});
