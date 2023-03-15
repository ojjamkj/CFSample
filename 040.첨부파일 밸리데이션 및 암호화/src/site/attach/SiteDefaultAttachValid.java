package site.attach;
 
import java.io.File;
import java.io.InputStream;

import jspeed.base.http.AttachFileDataSource;
 
public class SiteDefaultAttachValid extends DefaultAttachValid {
	/**
	 * 첨부파일 밸리데이션을 수행합니다.
	 * @param attachFileDSs : 첨부파일 내용
	 * @param uploadPath    : 첨부파일 경로
	 */
	@Override
	public void checkAttach(AttachFileDataSource[] attachFileDSs, String uploadPath) throws Exception
	{
		//첨부파일 저장 공통입니다. uploadPath값으로 첨부파일의 종류를 구분하세요. 
		//BBS가 있는 경우 게시판 첨부파일입니다.
		int size = attachFileDSs.length;
 
		for(int i=0; i<size; i++){
			byte[] fileByte = attachFileDSs[i].getSource();
			/**
			 * TODO 첨부파일 검증, 검증 실패시 throw error
			 */
		}
	}

	/**
	 * 첨부파일을 암호화합니다.
	 * @param savedPath : 첨부파일이 저장된 경로
	 * @param savedName : 첨부파일명
	 */
	@Override
	public void encryptAttach(String savedPath, String savedName) throws Exception
	{
		
	}
	/**
	 * 첨부파일을 복호화합니다.
	 * @param file
	 * @return InputStream
	 */
	@Override
	public InputStream decryptAttach(File file) throws Exception
	{
		return null;
	}
}