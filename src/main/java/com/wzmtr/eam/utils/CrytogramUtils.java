package com.wzmtr.eam.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密方式
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/11/19 15:00
 */
public class CrytogramUtils
{

/*  public static void main(String[] args){
    //String pwd = CrytogramUtils.encrypt("WZxxzx@123456mtr","MD5");
    //System.out.println(pwd);
    //oGcN2dlXlSSeon+HcRaQsg==
    //UkGW01e3If73/o+9yF/6Zg==
    //00001202 bEcejJbH4B2SDeISenhS6w==
    //oGcN2dlXlSSeon+HcRaQsg==
  }*/

  public static String encrypt(String paramString1, String paramString2)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance(paramString2);
      localMessageDigest.reset();
      byte[] arrayOfByte1 = paramString1.getBytes();
      byte[] arrayOfByte2 = localMessageDigest.digest(arrayOfByte1);
      return Base64.encodeBase64String(arrayOfByte2);
    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return paramString1;
  }
}
