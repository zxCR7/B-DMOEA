/*
 * MATLAB Compiler: 6.6 (R2018a)
 * Date: Wed May 12 15:41:20 2021
 * Arguments: 
 * "-B""macro_default""-W""java:BDAmmd,DisToPf""-T""link:lib""-d""F:\\科研\\MOEA\\B\\BDAmmd\\for_testing""class{DisToPf:F:\\科研\\MOEA\\B\\BDAmmd.m}"
 */

package BDAmmd;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class BDAmmdMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "BDAmmd_048CBB3435D02162EEE60EAA6B4A3156";
    
    /** Component name */
    private static final String sComponentName = "BDAmmd";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(BDAmmdMCRFactory.class)
        );
    
    
    private BDAmmdMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            BDAmmdMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{9,4,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
