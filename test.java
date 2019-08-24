import java.io.UnsupportedEncodingException;

public class test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		//String st1 = "~■ルート蓄積フラグ0～2■";
		//String st2 = "~■ルート蓄積フラグ0～2■";
		
		String st1 = "誰かの心を連れ去った――神様の羽根。";
		String st2 = "誰かの心を連れ去った――神様の羽根。";
		
		
		
		byte[] b1 = st1.getBytes("SJIS");
		byte[] b2 = st2.getBytes("SJIS");
		
		byte len1 = (byte) (b1.length + 3);
		byte len2 = (byte) (b2.length + 3);
		
		System.out.println(len1);
		System.out.println(len2);
	}
	
}
