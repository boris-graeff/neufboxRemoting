package fr.graeff.boris.neufboxRemoting.utils;

/**
 * My log system for developing / debugging
 *
 */
public class Log
{
	
	// Production mode
	
	// public static void d(String message){}
	// public static void e(String message, Throwable tr){}
	

	// Developing / debugging mode

	/**
	 * Get simple class name from full class name
	 * @param fullClassName
	 * @return
	 */
	private static String getSimpleName(String fullClassName)
	{
		int index = fullClassName.lastIndexOf('.');
		return fullClassName.substring(index+1, fullClassName.length());
	}
	
	/**
	 * Display debug messages
	 * @param message
	 */
	public static void d(String message)
	{
		Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
        
        String fullClassName = trace[1].getClassName();
        String simpleName = getSimpleName(fullClassName);
        
        android.util.Log.d(simpleName+"."+trace[1].getMethodName(), "li:"+trace[1].getLineNumber()+" - "+message);
	}
	
	/**
	 * Display error messages
	 * @param message
	 * @param e
	 */
	public static void e(String message, Throwable e)
	{
		Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
       
        String fullClassName = trace[1].getClassName();
        String simpleName = getSimpleName(fullClassName);
        
        android.util.Log.e(simpleName+"."+trace[1].getMethodName(), "li:"+trace[1].getLineNumber()+" - "+message);
        android.util.Log.e(simpleName+"."+trace[1].getMethodName(), "li:"+e.getMessage());
        e.printStackTrace();
	}
	
	/**
	 * Display error messages
	 * @param message
	 */
	public static void e(String message)
	{
		Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
       
        String fullClassName = trace[1].getClassName();
        String simpleName = getSimpleName(fullClassName);
        
        android.util.Log.e(simpleName+"."+trace[1].getMethodName(), "li:"+trace[1].getLineNumber()+" - "+message);
	}
	
}
