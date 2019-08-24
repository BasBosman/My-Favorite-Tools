
public class test {
	public static void main(String[] args) {
		String st1 = "　 ？？？ 　";
		String st2 = "　？真紅　　";
		
		byte[] b1 = st1.getBytes();
		byte[] b2 = st2.getBytes();
		
		byte len1 = (byte) (b1.length + 3);
		byte len2 = (byte) (b2.length + 3);
		
		System.out.println(len1);
		System.out.println(len2);
	}
	
}
