export interface RegistrationCode {
    code: String;
    isUsed: Boolean;
    userId?: number;
    companyId: number;
    createdAccountType: String;
}