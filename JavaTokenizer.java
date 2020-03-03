package HW;


import java.io.Reader;

public interface JavaTokenizer {
	public void RegexTokenizer( Reader inStream );
	public void WeissTokenizer( Reader inStream );
	public String getNextID();
	public Integer getLineNumber();
}
