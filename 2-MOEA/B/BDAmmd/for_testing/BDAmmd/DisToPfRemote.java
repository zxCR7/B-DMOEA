/*
 * MATLAB Compiler: 6.6 (R2018a)
 * Date: Wed May 12 15:41:20 2021
 * Arguments: 
 * "-B""macro_default""-W""java:BDAmmd,DisToPf""-T""link:lib""-d""F:\\科研\\MOEA\\B\\BDAmmd\\for_testing""class{DisToPf:F:\\科研\\MOEA\\B\\BDAmmd.m}"
 */

package BDAmmd;

import com.mathworks.toolbox.javabuilder.pooling.Poolable;
import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The <code>DisToPfRemote</code> class provides a Java RMI-compliant interface to MATLAB 
 * functions. The interface is compiled from the following files:
 * <pre>
 *  F:\\科研\\MOEA\\B\\BDAmmd.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a <code>DisToPfRemote</code> 
 * instance when it is no longer needed to ensure that native resources allocated by this 
 * class are properly freed, and the server-side proxy is unexported.  (Failure to call 
 * dispose may result in server-side threads not being properly shut down, which often 
 * appears as a hang.)  
 *
 * This interface is designed to be used together with 
 * <code>com.mathworks.toolbox.javabuilder.remoting.RemoteProxy</code> to automatically 
 * generate RMI server proxy objects for instances of BDAmmd.DisToPf.
 */
public interface DisToPfRemote extends Poolable
{
    /**
     * Provides the standard interface for calling the <code>BDAmmd</code> MATLAB 
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
     * % Find the latent space of domain adaptation
     * %输入：上一环境下的一些解Fs，当前环境的一些解Fa，这两组解用来生成映射矩阵A
     * %      上一环境下的POF
     * 
     * %过程：将上一环境下的POF经过矩阵A映射为POF_deduced,再将当前环境的一些解经过矩阵W映射到latent_space中，跟POF_deduced
     * %      
     * 里的解进行比较，找到距离最近的解（好解），这些解实际上就是当前时刻的初始种群，接着用NSGAII进行迭代
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
    public Object[] BDAmmd(int nargout, Object... rhs) throws RemoteException;
  
    /** 
     * Frees native resources associated with the remote server object 
     * @throws java.rmi.RemoteException An error has occurred during the function call or in communication with the server.
     */
    void dispose() throws RemoteException;
}
