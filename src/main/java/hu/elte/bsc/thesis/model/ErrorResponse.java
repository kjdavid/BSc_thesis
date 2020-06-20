/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.model;

/**
 *
 * @author kjdavid <kjdavid96 at gmail.com>
 */
public enum ErrorResponse {
    //Registration errors
    REGISTRATION_CODE_HAS_ALREADY_USED(1),
    REGISTRATION_CODE_NOT_VALID(2),
    NAME_TAKEN(3),
    EMAIL_TAKEN(4),
    EMAIL_NOT_VALID(5),
    //Login errors
    USER_NOT_EXISTS(6),
    WRONG_PASSWORD(7),
    USER_SUSPENDED(8),
    USER_NOT_ACTIVATED(9),
    //Other errors
    PERMISSION_DENIED(10),
    CHANGE_PASSWORD_ERROR(11), // It can happen when the user is logged out due to session expiring, or the user tries to change an other user's password
    UNAUTHORIZED(12),
    COMPANY_NOT_EXISTS(13),
    ITEM_ALREADY_ADDED(14),
    NO_ENTITY_WITH_THE_GIVEN_ID(15),
    NOT_ENOUGH_ITEM(16);
    private final int errorCode;
    ErrorResponse(int value){
        this.errorCode=value;
    }
    @Override
    public String toString(){
        String s = Integer.toString(this.errorCode);
        return s;
    }
}
