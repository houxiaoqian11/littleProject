package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.codec.binary.Base64;


import com.sumavision.tvpay.commons.ftp.FtpUtils;
import com.sumavision.tvpay.connector.util.CacheService;
import com.sumavision.tvpay.connector.util.CommonUtil;
import com.sumavision.tvpay.connector.util.Constant;
import com.sumavision.tvpay.connector.util.FastPayCryptionUtil;

public class Test {
	
	
	public static void main2(String[] args) throws NoSuchAlgorithmException {
		System.out.println( getMd5("江纪辉_d656"));
	}

	public static String getMd5( String src) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(src.getBytes());
		byte [] dg = md.digest();
		return toHex(dg);
	}
	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}
	
	public static void main1(String[] args) throws HttpException, IOException {
		String localFilePath = "tranCode=IFP001&signData=i8Tg2aVHcybBCYyth0okARYdmXw6pOMo/f9lVTe+yE4y3psLsuWdUQqKTPCizaU/FnHLKlYpCJgU8S5BdD5eCur1cunCz66eFLfpaSvyG4mpHEQmyiGmP/YVUPpYlr3/7/mNxlY9S98Jqab9gBWdm9o2l9Tnk65kUoN73uLa/Q2G/kEZwG4tcDrqjKuho/ubFK3VMJdbn4wKwwrM+A+NyqquOKMRC6/0Ee0oG4kDRPIhy91dCR/YOQM3OWsTzTnNAX+OLwUOYCS2P49rqVMEB6MrzcTwmTHyi8Jcp+FCfa8zCKVurCZbnTWXXfNmbNytWiIJN8L9zLgYnPiH2xa+2A==&encryptKey=ho0Mq4DBUrB+nE1Zjyhr9YZzhvXbjUGHI5Eh01xV6nda8UcqG39SvxMIOFtVSsGRP8SLe6EZ/RFtVgp44nzLvzC/PMTXxWGuLnIHv3H1zGupYfomWxnNKxxLCQDy+rsNSE8BH4UnusCyPAh+Fp/HGINycmJql+t+PCL01HSxKyrFI8yx+yD3ZSYpDrw7vhETC/m9Cq9rXX/ogm6a1yp7TLf2uvJenOedaT4N0YJCYUC9lnUAmv7JqOgv/7UzSIt+2sYrvCOGZdASwXRJc7STMzqWdSdZfzBzJOPsOj6lDOqhkMWJrFeTcAlFQGQX2phEXjve/JxJGB136ceCcltu+w==&encryptData=binTUL68Bv1VL4Td5vLa6q4vrDzhnJpnBn0IOwORuRZI8s/zF2Txbcj5cGvkc6Fm0gwmp8AQEYBZ3tmhK2NuatoImZQR5H1uwXe+j4nxF4099QvDCZZgLJly85fDGISR2zftg/TxU3aIv/52TzTVamo/t1EkVAlslNtSY/lxWFy3i1QjMFRRILdx/Tqxr+RRFFL3MCobyrsi7wDriL4KEbJgtv0TNiyq5r13JV8T68nhcJcABb/r/c2bgrjK03Rl0ZPPfytBywIPs627SMOaKx5obs+2fs0WW7MkdhDOkDFp8hggPvsecPylQEhUMCKYggiExZd9uWVSGjY7GZZ77vTRm6hctH9xnti1L8u8eWDx9U+Vev6sbgJte7S+w3UCKSILkpZKZEQppuctKSNr6NoQWIAUQiQsVVA4dcOxWLDvbM/N22g+5adjzRZguDvWYFehGKM7VjocOmE5ux7q3KmRD/ZnFHG3I2jvHWH/LpuWrPhRGqYQ4vRn5mWlWJABuqS7sAzDsOTcYiEWqt94C3Pjb0xAFyxrAC0n7bYNSW5CuohMsmJZ0YwK6EjLC8TcfzDF1UZTLvcWW7SRZurAyExLVIBNV0HABsJ7tWV05gc5f6qVnBlak0T0eIbkzVHjC2j/Kv6qm73fglyPeJkMjGOoYZEkUWdFR0dR93ZBPYvKmI5jL6TL5mblM5ozH05gyQWTtaZCYUjIF1+wt1zMT4PFhoE3U/GXofxZw/ZSg+sq97ZUoWTo/OrDM8X6xhv3";
		String[] ags = localFilePath.split("&");
		HttpClient http = new HttpClient();
		String url = "http://172.16.6.21:8080/ebankfastpay/webBankNotice4FastPay";
		System.out.println("请求地址url=" + url);
		PostMethod post = new PostMethod(url);
		 
		for(String s : ags){
//			sb.append("&").append(key).append("=").append(request.getParameter((String)key));
			post.addParameter(s.split("=")[0].trim(), s.split("=")[1]);
			System.out.println("转发给银行参数：" + s.split("=")[0] + "= " + s.split("=")[1]);
			
		}
		int stat = http.executeMethod(post);
		System.out.println(stat);
		post.releaseConnection();
	}
	
	public static void main8(String[] args) throws Exception {
		for(int i =0 ; i< 1 ; i++){
		
			String encryptData = "xfD6ItGvh+Nf54s91ll4yrO64GveHIbsigjmC/ZpWHSrzWUhpqf/EMLQfTwMbgGKTY2thp/CPVN8psJr5qkCYz7iS4Bw+UfawicitVTqidbBtCFDO7n5rkMec7F32rrH2jbtxsx7tfVrnaplKIBg4kTCyhv0H+/laGhbZut7sbsyXhbGh52xfjwR7tcGMXClQYyDoX72i5z34dbqCAVuHbhZV2f4hoZSk4kyq5htJMfV+tVHbx/9izrDeTAu4MuOYpEh+kGaW+wW21nfd2PIfpBypamq73X47nzIHasRWcHLwJrWx9a4fYELyAARZHAJ6byJi4GwDEl0/LLDYXNneWo637dAXzIsp2D0htZYhj709J0IJ8JvjZmqYO32kRNUpPET7syk1muOOThI1dDYndm4b++pvwJxAbl7AfMZelGuhfZAThzJGhxXgPFUd5gEiuZYmjlaKuKN+hEG19FkcaN0Oto6Bm46g4GCcwN9jPKD/KXGcHtGoLgQ2J71zyTrXpCrVXkzgTlaBaupxG+DfWrdNbiDiKVZZuQLWawx1lm0IZzged1Yy9uBpdzZBKn9v/nQ+9/ILswDvuhp0+40CntMlibgEKFc8IiKUy5uYQEQeOp+E1SklTFbukv4WSFW5CAAMm0AzK0jYkIcc0urQd7/0+B9JyP61n2P1VEikjxCrDbh8xoRph+bU9yNWFwLnuh4+HKPxjYiUN7GQnVtQKbSVnhhMxTMMNP+jAl9oda3SGHATJLL7l/6l2P1Mc7gHEiI+zfkuUEZD9DYeXlOlPKiagq1ht3NmiYHUcuF9U2PcCgzD7xvxwdMjNEtqcj4";
	//		String encryptKey = request.getParameter(Constant.ENCRYPTKEY);
			String encryptKey = "oKYOZ3kW07zD45bUv+Dw42bj2SfEcE7OUszJFnTtMcrDR+rhNSI835V4bfTOMtHCLWcncIUlOZjoAq14LSLbfAbepquzRNU3k0CxFarXuRxZkh5+CyuNkbSluICNAeT3nl337DhEKM7jlVnhxGkyaNBBDCbojU3/LSh6AXvQcVBhtJx0iRFCo/1MVQPhcCs1ArEdl/u3NCvKPKHmirXhQNRHUKauc9SIisco8BgPMbzIA0v85u8mvagZxGzgdXwIsUbce9uRrDv4heb1MoJmnGEqjxN1MGvnFDhdHLVpI9/ufb0lnVydIZSfFULtZ8RYL7BcSYlkZYHcKQENR2vufw==";
			String signData = "auNqVIa9DNeSSIpMqHS0GRY2basV9hwM/Di6Et/Rz7YZdJs6eB/lIqIllmW+K8sbDWFTzOQ6adF4cd18jN0/rxgROURas1f3HF13aR9L1NgbGvHiFruJDNHpLwk4Opl50lvoevqb3vj3mLJIB0qI1/C3sm/jjhsehuTfaBl8UBzu+MOTs71MBB/2TVBXnXIz4grvjuhpEbsibdpKJydAWFKiGaQ7vJOQaxHnoRHa5VIrcdVB+GdVi4f5hwDmEJxewPgaOWJf6BdyNQaAan8PzuLxz7iqTAv/OemirfV+LNNibY8Mah3CVYZepNQamhmBkONnGeDDGNpxWz1nlvhZWw==";
			PrivateKey privateKey = FastPayCryptionUtil.getPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDXRhPfcQ6HIUfAs8Ms/psS8ocAstViKC4KzOnpg4K+Jg1GCf96p2XfFFue4hDDCzs3AdqjHaYXHIqVQ6q5JDz0JnS9hg+Fk/kYSduFWlY1ikeeFZtD8xQUXlp/bem6tJsf8uo5E8gjm3PBqFgxwbVIbpbFo0yAxIWrrRbtYyoZWRIBiIYj0I1GGPrNeBrKcwD68Cw4Q+kq46QQTys/CYFt7YX8q0wyCHEmz4icTJaCl28bhGFsuJWXsB2dZ8S5TxI2MBj1MdtJuZ6EUlYkcw0I59FqJVFjU+DnQS+fJH8XdB+BBfFDI06MZ6yi1dMFZh4vyv+TZ9UaZtWvAnqjMdsFAgMBAAECggEAd9dnUMibkdaRH1HzoaTLp6ykQ1tLNy2vFAwnW/v+WOTOFuOR+jIeoCWxK64mLq6d9G1vzca/o7Yb4CSfchoeZLzT2H49Pdu2nDLhpkc4Hu4wGG1m1lkIIJ62OqJxr6LDiPGuOsAsdsYflvukvW5dAUmvXa8e88BzubLORLEjyp+p5YnQuCuxBfYGAXDhuLMxgKM2w1cjgGg5UhVNKQHfzJoYjdJwxgudJN46DxON+kzMJVAoaD4j2ZLhkkfrXrYduHaKjNAc4eG/PyM8Ty/KhPbdVNne00nk3fFnoDrUpYdXvBt/90nsepUw9nAJ/eLlsKKFLBHSq7nGZP7cgRgjMQKBgQDtcCoArB2xF5t71sB4UnRc+aPCJGFQBF1kHp6mSnJAGhdurs8/vdWmv+NHxEE9+MPtme1j8WY/qHbFQoGVy7SMSln59EtJzPFl1i45PvGtHX+/V0hhd2Mt80EQUVlE0Mv48ELyr8dOKvKbxJANSb3KKROqXj+5KEFUe0LgP40CjwKBgQDoGlaz7sjUB9IIxuYXcPiz+ffL2HQpjxOHBZlI/OfNVjycrffmrBPIPMxux9aZsnnXqErN0iviE1PDJGkToOLDyoTYgOVgqi0dXV/mUSuIlV8EUqMX2l18a36GhfBasOQQpiBJ+OvDRmq9iiX/V14X/naDY05Ya2vaoe5aMzlDKwKBgEbW/SEgOOeaNIB5LQl5CFCpBouo0QPPmZGCdTEF6xpm7nDOZPUOjUzn2UaTLifwLXyZPTNhVXFOsMjfs8veb+qfUEL7+O5239rTUQ0xU2ej8nZC80xpE0UlW1cj/i9C4GhQPpkripouxB1nQtdEFgTXxo9A+WAju4IaGHTRYR/PAoGAQBsOOXJJS67BRyVQZccRGfx/gXDOXLAkz5UvSiZ4C5G2+ftezfpXGGYafLp7PkY6OuNDjedvsWhvKSXEU+SK3VLxMC8hq3o1LbgdhaqDoJzI+pOscikgyv2Rm6wgv5u90g9gOa4jx/KKBsHCTu0dkvj4WL+z4t4AkwlIoOcm+LMCgYEAzZAxO82oIL1pfjlOz7iXedvWoj/QVZy4VpsTkA6lOa9PchGFu/+mDEj5SkpVuSsyxJ8uoN2eXXYHYJvfHTUdxaQpp03ib3ry8AZKQAu9aUvKVXVQ8nySQDRenR63E1JqG9TQtKs4cG3lfUK/5dzi6iJR+epmqU34ZmsQ+bGKkbw=", null, "RSA");
			PublicKey publicKey = FastPayCryptionUtil.getPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA398nZFU4ddJ7lnPIBw+RqJ5Z9+zZ1D/IO/yVy6IY3qu2MQRVWEMvjzbWCk02Id6D7JoltPnywk9GocGTECefu3jvUwF2aigCT2k0yctdC61Vcxo0wMBE+TrIrkosJDWbM+ztM+yhRPyTJJuZpl8IdRg+TvQYUimwVTuu+h+tmL7ky3EcnKLxUf3hPY2sRLMapOyaVrlV0/Cl68NjzyjrNu/Lh6UF0wZyva+1DUK4l2YBmqpq6r3rF60BT2YxqIf0eSJSBEw5W/AdhiWzJ4/smdd1BNWBcXaC+zQsPkmbmnllkv1nB4BElwJ26NRb3HoDeCl1JpmAttBc0OlsinKMCQIDAQAB","RSA");
			
			byte [] aesKey = FastPayCryptionUtil.RSADecrypt(Base64.decodeBase64(encryptKey.getBytes()),privateKey);
			System.out.println(new String(aesKey));
			byte[] xmlBytes = FastPayCryptionUtil.AESDecrypt(Base64.decodeBase64(encryptData.getBytes("UTF-8")), aesKey);
			String xml = new String(xmlBytes,"UTF-8");
			byte[] signBytes = Base64.decodeBase64(signData.getBytes(Constant.FAST_PAY_ENCODING));
			boolean signFlag = FastPayCryptionUtil.verifyDigitalSign(xmlBytes, signBytes, publicKey);
			System.out.println(signFlag);
			System.out.println(xml);
		}
	}
	
	public static void main(String[] args) {
		String s = "I00025|IFP005|160331200014969735|20160412183103|2016041213938998|S|000000|交易成功|20160412182931|5|156|200|0|丰付退款";
		String [] ss = s.split("\\|");
		System.out.println(ss.length);
	}

}
