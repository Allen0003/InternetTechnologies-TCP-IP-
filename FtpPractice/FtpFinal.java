package ftp;

/**
 * final version of FTP
 * 
 */
public class FtpFinal {
	public static void main(String[] arg) throws Exception {
		FtpUtil ftpUtil = new FtpUtil();
		ftpUtil.openConnection("localhost");
		ftpUtil.getMsgs();
		ftpUtil.main_proc();
		ftpUtil.closeConnection();
	}
}
