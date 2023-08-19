package com.douzone.prosync.redis;

public interface TokenStorageService {

     String getRefreshToken(String key);


     void setRefreshToken(String key,String value);

     void removeRefreshToken(String key);
     void setEmailCertificationNumber(String key, String certificationNumber);

     String getEmailCertificationNumber(String key);

     void removeEmailCertificationNumber(String key);
}
