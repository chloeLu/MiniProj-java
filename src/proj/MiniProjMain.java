package proj;

public class MiniProjMain {
	public static void main(String[] args) {
		try {
			TradeFileReader reader = new TradeFileReader();
//			reader.preProcessRawTradeFile("E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			System.out.println(System.nanoTime());
			reader.preProcessRawNbboFile( "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			System.out.println(System.nanoTime());
		} catch (Exception e) {
			// do nothing
		}
	}
}
