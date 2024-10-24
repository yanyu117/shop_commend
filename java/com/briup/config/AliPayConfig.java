package com.briup.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
//https://open.alipay.com
public class AliPayConfig {
    /*
     * 服务网关  ---  沙箱环境
     */

    private static String serverURL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    /*
     * 应用id,
     */

    private static String APP_ID = "9021000141645071";

    /*
     * 用户私钥,可以替换成你们的自己的私钥
     *
     */

    private static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCIGl7IR4LwXcDQoAqC3hKJvcoNupsEw+0/5pc+Qodyp6ul9HdEm5IeL8M3Nx6XFBZtub18ln99aCCrKOuWqdJewfUGEmd/FjTfVdaEOhbSyE39Zijbi2Zl4azUikvRkOsDeyiDT5XaIwKdFc9Tcv4dMc5sTSlbyw6WrsmEa1Niz2kk1o7Rwf49MmuyZg8vBn/hIqR+lt/nNoU+FEGrrTJBtIDvuk6tKdjlHro5j7YbShnSN7nkjS19I3x+2KiYAkHu3tsSxaE3HGPqsqDh8JH8z0HAqxq0CIIB3LLtJ+sM3dRL6bdhGFZpecicyT6OEXqvFPbIKyFxhbbfsbDhf4bpAgMBAAECggEADpZE8lI3xmtF7xY4Ci6i7gBZV39YffpBw0eDjjizK6aszuyUm9XQo0ubMJ/Ht4l/neL6XBAFOfufBLkuBsfMtDs1d6EhVo2avRUGGMXhDFZLhY0dc+pM3z6At4ba+Wr+xTC99tTgyrBV52AfCGKauaZM6W6bsCrjNWMmVfrqxPwt0z2C/enTyiWFXYAIXV7UyOqYC2j4KligjC41R5yUIa9S0X4DngtgeDpWVIu/ay68cOo9ujSTyYDwTeN1oi4pTAhD57J5Z1McvxQKmpibG0rLGy/lgEl6azMjUnTMTb11iAGBTgCAtfMZ+AklydYdVBAzvTVIxGcq7onI3XNZwQKBgQC+x0NoryIXOwVs/neu/FBf4wUTxNOEJYDGXPWqXbEGJw+mHovGPNlBI0JDIcfTEOaLIAphU7gRsLjp35PrtJbsTvcuS5Jbk4JyBcuGZgXxSjv6doIrTOWU9gdduwnx8Alfn2hrkdbrbfLQ9uaBHchLoM4Uak7oUAHSk47he++SQwKBgQC2ofj40Rm3oQjbmqjEl+qHgoL/Tdg4pbCu9ZMqoLlEE4UzbNnKekPRW2rOBaCr+PY7UbcEblfOT3OLqstQzR8bhZtJD9xX8UlSnbsDyoYeg7VzZAAXDMlQl+B3ZbiNI3jIcoR0pclWVlvRVnbwEDdtqhDzX0fjDkYaUmDvygA9YwKBgAqYlq3hzEruBEchexRE/1HUEhwe+oCEdLqPvzVPLt3gnYXkfG7uQkLT6oJABhE7BxMDHJB7TeXtoteai2S0Cw3k9OWlXOOpNb2PhNZaJEpajSpEGsa85qN3ZsFV+h9t7ZdaCzquRvmcibgBfpv9Q3qv28JWhTtx9ifz+iOb9ppxAoGAX+1Nr6ts/aWo2Ggw0yoUNt8XXXAO7RoiZpN4nEBVb2ttYGiBMLftnS6ON+5+yTV4aAnwvnQSzLuwriCksCk9eKBBwyuAifSjlbBhhYk/zxFRIm3ZGkJOD3XmofAc8o06KNb1uatGyIGNsrPevxNVedWFF7/pK1rY+YRhhDGu1I8CgYBQsRUxAM1hiWy78Uc42x7tmrRxd75Cgq9BcXl3YPxbcMJTpeaoDJl6Q8fJYdOfbJFxQYiXIYopbM23sHYskAFB8OXxKAOjE2sQtrT5iu9woPg2tj2v7v24Lsw4SUiWWCoDvibruM0B2awOmT/uZz+ozH93B9saEcG3PbnoXm7PlA==";

    /* 发送数据的格式,  目前为json
     *
     */

    private static String format = "json";

    /*
     * 设置字符集的编码格式 utf-8
     */

    private static String CHARSET = "utf-8";

    /*
     * 支付宝公钥,  可以替换成你们自己的
     */

    private static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnHyN5qiDRHw0+F24yxYfSDukkhBcIM6SAmyQFuVCuWFJYUH9ohEKbt571HzqBndtFxQpSFxx/GdCcxpfBUIV2zx8E3CXiVYWEcObnEX2ICdXAv+Qy7iqi4SHu8X4LivasfulDTwJdrH1EYh5UjLAwaDjDu1YEJMx6Fr4K0TV/jdx9p29iRsExWict/Hda9f974QlXgnZLNwueLjY9+PR7DxHTWZKL3eAP4x03cz+PIyX1/a+ijjGTg/DVNwDeAfvYMhvp3VnRLtpGSFpRrjxhjaZvlidjaEOpAsjdaKVyIatwjQ5mujp2DXde149JknS+ht1yGtcdiiJO6ZqHggA6QIDAQAB";

    /*
    支付宝签名  目前采用的是RSA2
     */

    private static String signType = "RSA2";
    /*
     * 页面跳转同步通知页面   http      ?12345464.外网可以访问的地址
     */



    public static AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(serverURL,APP_ID,APP_PRIVATE_KEY,format,
                CHARSET,ALIPAY_PUBLIC_KEY,signType);
    }
}
