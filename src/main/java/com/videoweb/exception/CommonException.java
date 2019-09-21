
package com.videoweb.exception;

public class CommonException extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3863962889046261350L;
	private String exceptionCode;
    private String errorMsg;
    
    
    
    /**
     * @return Returns the exceptionCode.
     */
    public String getExceptionCode()
    {
        return exceptionCode;
    }



    /**
     * @param exceptionCode The exceptionCode to set.
     */
    public void setExceptionCode(String exceptionCode)
    {
        this.exceptionCode = exceptionCode;
    }

    /**
     * @return Returns the errorMsg.
     */
    public String getErrorMsg()
    {
        return errorMsg;
    }



    /**
     * @param errorMsg The errorMsg to set.
     */
    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }



    public CommonException(String exceptionCode, String errorMsg)
    {
        super(errorMsg);
        this.exceptionCode = exceptionCode;
    }
    
    public CommonException(String errorMsg, Throwable t){
        super(errorMsg, t);
    }
    
    public CommonException(String errorMsg){
        super(errorMsg);
    }
    
    public CommonException(Throwable t){
        super(t);
    }

}
