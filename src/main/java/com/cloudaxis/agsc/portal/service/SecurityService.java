package com.cloudaxis.agsc.portal.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
