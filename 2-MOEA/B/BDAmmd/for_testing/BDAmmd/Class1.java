/*
 * MATLAB Compiler: 6.6 (R2018a)
 * Date: Wed May 12 15:38:51 2021
 * Arguments: 
 * "-B""macro_default""-W""java:BDAmmd,Class1""-T""link:lib""-d""F:\\科研\\MOEA\\B\\BDAmmd\\for_testing""class{Class1:F:\\科研\\MOEA\\B\\BDAmmd.m}"
 */

package BDAmmd;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;
import java.util.*;

/**
 * The <code>Class1</code> class provides a Java interface to MATLAB functions. 
 * The interface is compiled from the following files:
 * <pre>
 *  F:\\科研\\MOEA\\B\\BDAmmd.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a <code>Class1</code> instance 
 * when it is no longer needed to ensure that native resources allocated by this class 
 * are properly freed.
 * @version 0.0
 */
public class Class1 extends MWComponentInstance<Class1>
{
    /**
     * Tracks all instances of this class to ensure their dispose method is
     * called on shutdown.
     */
    private static final Set<Disposable> sInstances = new HashSet<Disposable>();

    /**
     * Maintains information used in calling the <code>BDAmmd</code> MATLAB function.
     */
    private static final MWFunctionSignature sBDAmmdSignature =
        new MWFunctionSignature(/* max outputs = */ 2,
                                /* has varargout = */ false,
                                /* function name = */ "BDAmmd",
                                /* max inputs = */ 4,
                                /* has varargin = */ false);

    /**
     * Shared initialization implementation - private
     * @throws MWException An error has occurred during the function call.
     */
    private Class1 (final MWMCR mcr) throws MWException
    {
        super(mcr);
        // add this to sInstances
        synchronized(Class1.class) {
            sInstances.add(this);
        }
    }

    /**
     * Constructs a new instance of the <code>Class1</code> class.
     * @throws MWException An error has occurred during the function call.
     */
    public Class1() throws MWException
    {
        this(BDAmmdMCRFactory.newInstance());
    }
    
    private static MWComponentOptions getPathToComponentOptions(String path)
    {
        MWComponentOptions options = new MWComponentOptions(new MWCtfExtractLocation(path),
                                                            new MWCtfDirectorySource(path));
        return options;
    }
    
    /**
     * @deprecated Please use the constructor {@link #Class1(MWComponentOptions componentOptions)}.
     * The <code>com.mathworks.toolbox.javabuilder.MWComponentOptions</code> class provides an API to set the
     * path to the component.
     * @param pathToComponent Path to component directory.
     * @throws MWException An error has occurred during the function call.
     */
    public Class1(String pathToComponent) throws MWException
    {
        this(BDAmmdMCRFactory.newInstance(getPathToComponentOptions(pathToComponent)));
    }
    
    /**
     * Constructs a new instance of the <code>Class1</code> class. Use this constructor 
     * to specify the options required to instantiate this component.  The options will 
     * be specific to the instance of this component being created.
     * @param componentOptions Options specific to the component.
     * @throws MWException An error has occurred during the function call.
     */
    public Class1(MWComponentOptions componentOptions) throws MWException
    {
        this(BDAmmdMCRFactory.newInstance(componentOptions));
    }
    
    /** Frees native resources associated with this object */
    public void dispose()
    {
        try {
            super.dispose();
        } finally {
            synchronized(Class1.class) {
                sInstances.remove(this);
            }
        }
    }
  
    /**
     * Invokes the first MATLAB function specified to MCC, with any arguments given on
     * the command line, and prints the result.
     *
     * @param args arguments to the function
     */
    public static void main (String[] args)
    {
        try {
            MWMCR mcr = BDAmmdMCRFactory.newInstance();
            mcr.runMain( sBDAmmdSignature, args);
            mcr.dispose();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Calls dispose method for each outstanding instance of this class.
     */
    public static void disposeAllInstances()
    {
        synchronized(Class1.class) {
            for (Disposable i : sInstances) i.dispose();
            sInstances.clear();
        }
    }

    /**
     * Provides the interface for calling the <code>BDAmmd</code> MATLAB function 
     * where the first argument, an instance of List, receives the output of the MATLAB function and
     * the second argument, also an instance of List, provides the input to the MATLAB function.
     * <p>
     * Description as provided by the author of the MATLAB function:
     * </p>
     * <pre>
     * % Find the latent space of domain adaptation
     * %输入：上一环境下的一些解Fs，当前环境的一些解Fa，这两组解用来生成映射矩阵A
     * %      上一环境下的POF
     * 
     * %过程：将上一环境下的POF经过矩阵A映射为POF_deduced,再将当前环境的一些解经过矩阵W映射到latent_space中，跟POF_deduced
     * %      
     * 里的解进行比较，找到距离最近的解（好解），这些解实际上就是当前时刻的初始种群，接着用NSGAII进行迭代
     * </pre>
     * @param lhs List in which to return outputs. Number of outputs (nargout) is
     * determined by allocated size of this List. Outputs are returned as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>.
     * Each output array should be freed by calling its <code>dispose()</code>
     * method.
     *
     * @param rhs List containing inputs. Number of inputs (nargin) is determined
     * by the allocated size of this List. Input arguments may be passed as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or
     * as arrays of any supported Java type. Arguments passed as Java types are
     * converted to MATLAB arrays according to default conversion rules.
     * @throws MWException An error has occurred during the function call.
     */
    public void BDAmmd(List lhs, List rhs) throws MWException
    {
        fMCR.invoke(lhs, rhs, sBDAmmdSignature);
    }

    /**
     * Provides the interface for calling the <code>BDAmmd</code> MATLAB function 
     * where the first argument, an Object array, receives the output of the MATLAB function and
     * the second argument, also an Object array, provides the input to the MATLAB function.
     * <p>
     * Description as provided by the author of the MATLAB function:
     * </p>
     * <pre>
     * % Find the latent space of domain adaptation
     * %输入：上一环境下的一些解Fs，当前环境的一些解Fa，这两组解用来生成映射矩阵A
     * %      上一环境下的POF
     * 
     * %过程：将上一环境下的POF经过矩阵A映射为POF_deduced,再将当前环境的一些解经过矩阵W映射到latent_space中，跟POF_deduced
     * %      
     * 里的解进行比较，找到距离最近的解（好解），这些解实际上就是当前时刻的初始种群，接着用NSGAII进行迭代
     * </pre>
     * @param lhs array in which to return outputs. Number of outputs (nargout)
     * is determined by allocated size of this array. Outputs are returned as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>.
     * Each output array should be freed by calling its <code>dispose()</code>
     * method.
     *
     * @param rhs array containing inputs. Number of inputs (nargin) is
     * determined by the allocated size of this array. Input arguments may be
     * passed as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of
     * any supported Java type. Arguments passed as Java types are converted to
     * MATLAB arrays according to default conversion rules.
     * @throws MWException An error has occurred during the function call.
     */
    public void BDAmmd(Object[] lhs, Object[] rhs) throws MWException
    {
        fMCR.invoke(Arrays.asList(lhs), Arrays.asList(rhs), sBDAmmdSignature);
    }

    /**
     * Provides the standard interface for calling the <code>BDAmmd</code> MATLAB function with 
     * 4 comma-separated input arguments.
     * Input arguments may be passed as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of
     * any supported Java type. Arguments passed as Java types are converted to
     * MATLAB arrays according to default conversion rules.
     *
     * <p>
     * Description as provided by the author of the MATLAB function:
     * </p>
     * <pre>
     * % Find the latent space of domain adaptation
     * %输入：上一环境下的一些解Fs，当前环境的一些解Fa，这两组解用来生成映射矩阵A
     * %      上一环境下的POF
     * 
     * %过程：将上一环境下的POF经过矩阵A映射为POF_deduced,再将当前环境的一些解经过矩阵W映射到latent_space中，跟POF_deduced
     * %      
     * 里的解进行比较，找到距离最近的解（好解），这些解实际上就是当前时刻的初始种群，接着用NSGAII进行迭代
     * </pre>
     * @param nargout Number of outputs to return.
     * @param rhs The inputs to the MATLAB function.
     * @return Array of length nargout containing the function outputs. Outputs
     * are returned as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>. Each output array
     * should be freed by calling its <code>dispose()</code> method.
     * @throws MWException An error has occurred during the function call.
     */
    public Object[] BDAmmd(int nargout, Object... rhs) throws MWException
    {
        Object[] lhs = new Object[nargout];
        fMCR.invoke(Arrays.asList(lhs), 
                    MWMCR.getRhsCompat(rhs, sBDAmmdSignature), 
                    sBDAmmdSignature);
        return lhs;
    }
}
