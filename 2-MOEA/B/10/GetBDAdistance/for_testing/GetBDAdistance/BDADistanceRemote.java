/*
 * MATLAB Compiler: 6.6 (R2018a)
 * Date: Fri Aug  6 13:21:27 2021
 * Arguments: 
 * "-B""macro_default""-W""java:GetBDAdistance,BDADistance""-T""link:lib""-d""F:\\科研\\MOEA\\B\\10\\GetBDAdistance\\for_testing""class{BDADistance:F:\\科研\\MOEA\\B\\GetBDAdistance.m}"
 */

package GetBDAdistance;

import com.mathworks.toolbox.javabuilder.pooling.Poolable;
import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The <code>BDADistanceRemote</code> class provides a Java RMI-compliant interface to 
 * MATLAB functions. The interface is compiled from the following files:
 * <pre>
 *  F:\\科研\\MOEA\\B\\GetBDAdistance.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a <code>BDADistanceRemote</code> 
 * instance when it is no longer needed to ensure that native resources allocated by this 
 * class are properly freed, and the server-side proxy is unexported.  (Failure to call 
 * dispose may result in server-side threads not being properly shut down, which often 
 * appears as a hang.)  
 *
 * This interface is designed to be used together with 
 * <code>com.mathworks.toolbox.javabuilder.remoting.RemoteProxy</code> to automatically 
 * generate RMI server proxy objects for instances of GetBDAdistance.BDADistance.
 */
public interface BDADistanceRemote extends Poolable
{
    /**
     * Provides the standard interface for calling the <code>GetBDAdistance</code> MATLAB 
     * function with 4 input arguments.  
     *
     * Input arguments to standard interface methods may be passed as sub-classes of 
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of any 
     * supported Java type (i.e. scalars and multidimensional arrays of any numeric, 
     * boolean, or character type, or String). Arguments passed as Java types are 
     * converted to MATLAB arrays according to default conversion rules.
     *
     * All inputs to this method must implement either Serializable (pass-by-value) or 
     * Remote (pass-by-reference) as per the RMI specification.
     *
     * Documentation as provided by the author of the MATLAB function:
     * <pre>
     * %% 将传过来的源域数据和目标域数据做成矩阵形式的
     *         %将之前时刻的一些可行解转换为矩阵
     * </pre>
     *
     * @param nargout Number of outputs to return.
     * @param rhs The inputs to the MATLAB function.
     *
     * @return Array of length nargout containing the function outputs. Outputs are 
     * returned as sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>. 
     * Each output array should be freed by calling its <code>dispose()</code> method.
     *
     * @throws java.rmi.RemoteException An error has occurred during the function call or 
     * in communication with the server.
     */
    public Object[] GetBDAdistance(int nargout, Object... rhs) throws RemoteException;
  
    /** 
     * Frees native resources associated with the remote server object 
     * @throws java.rmi.RemoteException An error has occurred during the function call or in communication with the server.
     */
    void dispose() throws RemoteException;
}
